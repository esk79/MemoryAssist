package utils;

import java.nio.charset.Charset;

/**
 * Created by EvanKing on 7/31/17.
 */
public class ByteUtil {
    public static byte[] hexStringToByteArray(String s) {
        return s.getBytes(Charset.forName("UTF-8"));
    }
}
