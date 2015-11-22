package de.kuriositaet.util.crypto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Utilities to deal with Certificates.
 * This class only provides static helper classes to pack/unpack Certifiates
 * into x.509/java.security.cert.Certificate objects and does not provide a
 * Wrapper as many of the other classes in this library do.
 * Created by a2800276 on 2015-11-03.
 */
public class Certificate {

    /**
     * Convert x.509 bytes into a java.security.cert.Certificate
     *
     * @param bytes
     * @return java.security.cert.Certificate
     * @throws WrappedException on invalid x.509 bytes
     */
    public static java.security.cert.Certificate loadCertificate(byte[] bytes) {
        return loadCertificate(bytes, 0, bytes.length);
    }

    /**
     * Convert x.509 bytes into a java.security.cert.Certificate
     *
     * @param bytes
     * @return java.security.cert.Certificate
     * @throws WrappedException on invalid x.509 bytes
     */
    public static java.security.cert.Certificate loadCertificate(byte[] bytes, int offset, int length) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes, offset, length);
        return loadCertificate(bis);
    }

    /**
     * Convert x.509 bytes from an InputStream into a java.security.cert.Certificate
     *
     * @param is
     * @return java.security.cert.Certificate
     * @throws WrappedException on invalid x.509 bytes
     */
    public static java.security.cert.Certificate loadCertificate(InputStream is) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance(Constants.CERTIFICATE_TYPE_X509);
            return cf.generateCertificate(is);
        } catch (CertificateException e) {
            throw new WrappedException(e);
        }
    }

    /**
     * Generate the X509 representation of the certificate.
     * @return x.509 bytes of certificate
     * @throws IllegalArgumentException if certificate type is not x.509 (no other types exist in
     *         default JCA) or null is provided.
     *         CryptoException wrapping an underlying CertificateEncodingException if "encoding errors occur"
     */
    public static byte[] getX509Bytes(java.security.cert.Certificate certificate) {
        if (certificate == null || !(certificate.getType().equals(Constants.CERTIFICATE_TYPE_X509))) {
            throw new IllegalArgumentException("can not convert to x509");
        }
        try {
            return certificate.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new WrappedException(e);
        }
    }
}
