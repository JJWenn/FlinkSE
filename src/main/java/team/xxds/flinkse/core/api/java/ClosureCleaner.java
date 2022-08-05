package team.xxds.flinkse.core.api.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.xxds.flinkse.core.api.common.ExecutionConfig;
import team.xxds.flinkse.core.util.InstantiationUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class ClosureCleaner {

    private static final Logger LOG = LoggerFactory.getLogger(ClosureCleaner.class);

    public static void clean(
            Object func, ExecutionConfig.ClosureCleanerLevel level, boolean checkSerializable) {
        clean(func, level, checkSerializable, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    private static void clean(
            Object func,
            ExecutionConfig.ClosureCleanerLevel level,
            boolean checkSerializable,
            Set<Object> visited) {
        if (func == null) {
            return;
        }

        if (!visited.add(func)) {
            return;
        }

        final Class<?> cls = func.getClass();

        boolean closureAccessed = false;

        for (Field f : cls.getDeclaredFields()) {
            // scala
            //if (f.getName().startsWith("this$")) {
            //    // found a closure referencing field - now try to clean
            //    closureAccessed |= cleanThis0(func, cls, f.getName());
            //} else {

            // java
            // 查找闭包引用的成员变量
            Object fieldObject;
            try {
                f.setAccessible(true);
                fieldObject = f.get(func);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        String.format(
                                "Can not access to the %s field in Class %s",
                                f.getName(), func.getClass()));
            }

            /*
             * 深度清除除了成员变量的类。
             * There are five kinds of classes (or interfaces):
             * a) Top level classes
             * b) Nested classes (static member classes)
             * c) Inner classes (non-static member classes)
             * d) Local classes (named classes declared within a method)
             * e) Anonymous classes
             */
            if (level == ExecutionConfig.ClosureCleanerLevel.RECURSIVE && needsRecursion(f, fieldObject)){
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Dig to clean the {}", fieldObject.getClass().getName());
                }

                // 因为是对象，所以递归调用clean去闭包清除
                clean(
                        fieldObject,
                        ExecutionConfig.ClosureCleanerLevel.RECURSIVE,
                        true,
                        visited);
            }
        }

        // 真正检查闭包的逻辑:直接给你来上一发序列化。不成功就报错
        if (checkSerializable) {
            try {
                InstantiationUtil.serializeObject(func);
            } catch (Exception e) {
                String functionType = getSuperClassOrInterfaceName(func.getClass());

                String msg =
                        functionType == null
                                ? (func + " is not serializable.")
                                : ("The implementation of the "
                                + functionType
                                + " is not serializable.");

                if (closureAccessed) {
                    msg +=
                            " The implementation accesses fields of its enclosing class, which is "
                                    + "a common reason for non-serializability. "
                                    + "A common solution is to make the function a proper (non-inner) class, or "
                                    + "a static inner class.";
                } else {
                    msg += " The object probably contains or references non serializable fields.";
                }

                throw new RuntimeException(msg, e);
            }
        }
    }

    private static boolean needsRecursion(Field f, Object fo) {
        return (fo != null
                && !Modifier.isStatic(f.getModifiers())
                && !Modifier.isTransient(f.getModifiers()));
    }

    private static String getSuperClassOrInterfaceName(Class<?> cls) {
        Class<?> superclass = cls.getSuperclass();
        if (superclass == null) {
            return null;
        }
        if (superclass.getName().startsWith("team.xxds.flinkse")) {
            return superclass.getSimpleName();
        } else {
            for (Class<?> inFace : cls.getInterfaces()) {
                if (inFace.getName().startsWith("team.xxds.flinkse")) {
                    return inFace.getSimpleName();
                }
            }
            return null;
        }
    }

    public static void ensureSerializable(Object obj) {
        try {
            InstantiationUtil.serializeObject(obj);
        } catch (Exception e) {
            throw new RuntimeException("Object " + obj + " is not serializable", e);
        }
    }

}

