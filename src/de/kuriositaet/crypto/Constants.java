package de.kuriositaet.crypto;

import java.math.BigInteger;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * Created by a2800276 on 2015-11-02.
 */
public class Constants {
    /**
     * Default RSA Keysize if no specifically mentioned (2048).
     */

    public static final int RSA_DEFAULT_KEYSIZE = 2048;

    /**
     * Default Exponent to use for RSA (65537)
     */
    public static final BigInteger RSA_DEFAULT_EXPONENT = RSAKeyGenParameterSpec.F4;

    /**
     * Default DSA Keysize if no specifically mentioned (1024).
     */
    public static final int DSA_DEFAULT_KEYSIZE = 1024;

    ////////////////////////////////////////////////////////////////////////
    // JCA Constants
    // http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html
    ////////////////////////////////////////////////////////////////////////

    /**
     * Certificate Type of X.509 certs.
     */
    public static final String CERTIFICATE_TYPE_X509 = "X.509";
    public static final String FORMAT_PKCS8 = "PKCS#8";
    public static final String FORMAT_X509 = "X.509";
}
