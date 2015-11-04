package de.kuriositaet.crypto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import static de.kuriositaet.crypto.Constants.CERTIFICATE_TYPE_X509;

/**
 * Created by a2800276 on 2015-11-03.
 */
public class Certificate {

    public static java.security.cert.Certificate loadCertificate(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return loadCertificate(bis);
    }

    public static java.security.cert.Certificate loadCertificate(InputStream is) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(is);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return null in case this certificate can't be expressed as an x509
     * certificate.
     */
    public static byte[] getX509Bytes(java.security.cert.Certificate certificate) {
        if (!(certificate.getType().equals(CERTIFICATE_TYPE_X509))) {
            return null;
        }
        try {
            return certificate.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
