package team.xxds.flinkse.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class InstantiationUtil {

    public static byte[] serializeObject(Object o) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            oos.flush();
            return baos.toByteArray();
        }
    }
}
