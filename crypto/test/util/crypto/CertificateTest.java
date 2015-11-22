package util.crypto;

import org.testng.annotations.Test;
import util.io.IO;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by a2800276 on 2015-11-03.
 */
public class CertificateTest {

    final static String PEM_FN = "/keys/DigiCertGlobalRootG3.pem";
    final static String PEM_ENCODED_AS_DER_FN = "/keys/DigiCertGlobalRootG3.crt";
    final static String DER_FN = "/keys/altNameEdiCert39.der";

    static byte[] PEM_BYTES;
    static byte[] PEM_ENCODED_AS_DER_BYTES;
    static byte[] DER_BYTES;

    static {
        InputStream inputStream = ClassLoader.class.getResourceAsStream(PEM_FN);
        PEM_BYTES = IO.readAllClose(inputStream, 1024);
        inputStream = ClassLoader.class.getResourceAsStream(DER_FN);
        DER_BYTES = IO.readAllClose(inputStream);
        inputStream = ClassLoader.class.getResourceAsStream(PEM_ENCODED_AS_DER_FN);
        PEM_ENCODED_AS_DER_BYTES = IO.readAllClose(inputStream);

    }

    @Test
    public void testLoadCertificateInputStream() throws Exception {
        InputStream stream = ClassLoader.class.getResourceAsStream(PEM_FN);
        java.security.cert.Certificate cert = Certificate.loadCertificate(stream);
        basicTests(cert, "7089244469030293291760083333884364146");
        stream.close();

        stream = ClassLoader.class.getResourceAsStream(DER_FN);
        java.security.cert.Certificate cert2 = Certificate.loadCertificate(stream);
        basicTests(cert2, "44");
        stream.close();

    }


    @Test
    public void testLoadCertificateBytes() throws Exception {
        java.security.cert.Certificate cert = Certificate.loadCertificate(PEM_BYTES);
        basicTests(cert, "7089244469030293291760083333884364146");
        cert = Certificate.loadCertificate(DER_BYTES);
        basicTests(cert, "44");

        boolean caught = false;
        try {
            Certificate.loadCertificate(PEM_BYTES, 1, PEM_BYTES.length - 1);
        } catch (WrappedException ce) {
            caught = true;
            assertEquals(ce.getCause().getClass(), CertificateException.class);
        }
        assertTrue(caught);
    }

    private void basicTests(java.security.cert.Certificate cert, String serial) {
        assertTrue(cert instanceof X509Certificate);
        X509Certificate x509 = (X509Certificate) cert;
        assertEquals(x509.getSerialNumber(), new BigInteger(serial, 10));
    }

    @Test
    public void testGetX509Bytes() throws Exception {
        java.security.cert.Certificate cert = Certificate.loadCertificate(PEM_BYTES);
        assertEquals(Certificate.getX509Bytes(cert), PEM_ENCODED_AS_DER_BYTES);
        java.security.cert.Certificate cert2 = Certificate.loadCertificate(DER_BYTES);
        assertEquals(Certificate.getX509Bytes(cert2), DER_BYTES);

        boolean caught = false;
        try {
            Certificate.getX509Bytes(null);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        assertTrue(caught);
    }
}