package de.kuriositaet.util.crypto;

import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by a2800276 on 2015-11-03.
 */
public class CertificateTest {

    final static String PEM_FN = "test/keys/DigiCertGlobalRootG3.pem";
    final static String PEM_ENCODED_AS_DER_FN = "test/keys/DigiCertGlobalRootG3.crt";
    final static String DER_FN = "test/keys/altNameEdiCert39.der";

    static byte[] PEM_BYTES;
    static byte[] PEM_ENCODED_AS_DER_BYTES;
    static byte[] DER_BYTES;

    static {
        try {
            FileInputStream fis = new FileInputStream(PEM_FN);
            PEM_BYTES = Util.readAllClose(fis, 100);
            fis = new FileInputStream(DER_FN);
            DER_BYTES = Util.readAllClose(fis);
            fis = new FileInputStream(PEM_ENCODED_AS_DER_FN);
            PEM_ENCODED_AS_DER_BYTES = Util.readAllClose(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testLoadCertificateInputStream() throws Exception {
        FileInputStream fis = new FileInputStream(PEM_FN);
        java.security.cert.Certificate cert = Certificate.loadCertificate(fis);
        basicTests(cert, "7089244469030293291760083333884364146");
        fis.close();

        fis = new FileInputStream(DER_FN);
        java.security.cert.Certificate cert2 = Certificate.loadCertificate(fis);
        basicTests(cert2, "44");
        fis.close();

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