package team.xxds.flinkse.core.util;

import javax.annotation.Nullable;

public final class Preconditions {

    // 判断对象是否为空，不为空则返回对象，为空则抛出异常
    // @Nullable表示对象参数可为null
    public static <T> T checkNotNull(@Nullable T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    // 自定义返回错误信息
    public static <T> T checkNotNull(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean condition, @Nullable Object errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }




}
