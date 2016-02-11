package util.crypto;

import java.security.SecureRandom;

/**
 * Utilites to deal with random numbers.
 */
public class Random {
	static SecureRandom rnd = new SecureRandom();

	/**
	 * return a byte array of size `num` containing random bytes.
	 *
	 * @param num
	 * @return
	 */
	public static byte[] random(int num) {
		num = num < 0 ? 0 : num;
		byte[] bytes = new byte[num];
		rnd.nextBytes(bytes);
		return bytes;
	}
}
