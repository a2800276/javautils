package util.crypto;

import java.security.SecureRandom;

/**
 * Created by a2800276 on 2015-10-30.
 */
public class Random {
    static SecureRandom rnd = new SecureRandom();

    public static byte[] random(int num) {
        num = num < 0 ? 0 : num;
        byte[] bytes = new byte[num];
        rnd.nextBytes(bytes);
        return bytes;
    }
}
