package de.kuriositaet.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.*;

import static de.kuriositaet.crypto.Constants.*;
import static de.kuriositaet.crypto.Util.readAllClose;

/**
 * Utility to create, serialize and load KeyPair
 * Created by a2800276 on 2015-11-02.
 */
public class KeyPair {


    private static String[] types = {"RSA", "DSA", "EC"};
    private PrivateKey privKey;
    private PublicKey pubKey;

    public KeyPair(PrivateKey priv, PublicKey pub) {
        this.privKey = priv;
        if (pub.getPublicKey().getAlgorithm() == "EC" && !(pub instanceof KeyPair.ECPublicKey)) {
            pub = new ECPublicKey((java.security.interfaces.ECPublicKey) pub.getPublicKey());
        }
        this.pubKey = pub;
    }

    public KeyPair(byte[] pkcs8_private, byte[] x509_public) {
        this(PrivateKey.loadPKCS8(pkcs8_private), PublicKey.loadX509(x509_public));
    }

    private KeyPair(java.security.KeyPair kp) {
        this(
                new PrivateKey(kp.getPrivate()),
                new PublicKey(kp.getPublic())
        );
    }

    public static KeyPair generateKeyPair(Algorithm algorithm) {
        java.security.KeyPair kp = generateJCAKeyPair(algorithm);
        return new KeyPair(
                new PrivateKey(kp.getPrivate()),
                new PublicKey(kp.getPublic())
        );
    }

    public static KeyPair generateRSAKeyPair(int keysizeBits) {
        return generateRSAKeyPair(keysizeBits, RSA_DEFAULT_EXPONENT);
    }

    public static KeyPair generateRSAKeyPair(int keysizeBits, BigInteger exponent) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keysizeBits, exponent);
            kpg.initialize(spec);
            java.security.KeyPair kp = kpg.generateKeyPair();
            return new KeyPair(kp);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateDSAKeyPair(int keysizeBites) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(DSA_DEFAULT_KEYSIZE);
            return new KeyPair(kpg.generateKeyPair());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    static java.security.KeyPair generateJCAKeyPair(Algorithm algorithm) {
        KeyPairGenerator kpg;
        AlgorithmParameterSpec spec;
        String KPG_INSTANCE_ID;
        switch (algorithm) {
            case secp112r1:
            case secp112r2:
            case secp128r1:
            case secp128r2:
            case secp160k1:
            case secp160r1:
            case secp160r2:
            case secp192k1:
            case secp192r1:
            case secp224k1:
            case secp224r1:
            case secp256k1:
            case secp256r1:
            case secp384r1:
            case secp521r1:
            case sect113r1:
            case sect113r2:
            case sect131r1:
            case sect131r2:
            case sect163k1:
            case sect163r1:
            case sect163r2:
            case sect193r1:
            case sect193r2:
            case sect233k1:
            case sect233r1:
            case sect239k1:
            case sect283k1:
            case sect283r1:
            case sect409k1:
            case sect409r1:
            case sect571k1:
            case sect571r1:
            case X9_62_prime192v2:
            case X9_62_prime192v3:
            case X9_62_prime239v1:
            case X9_62_prime239v2:
            case X9_62_prime239v3:
            case X9_62_c2tnb191v1:
            case X9_62_c2tnb191v2:
            case X9_62_c2tnb191v3:
            case X9_62_c2tnb239v1:
            case X9_62_c2tnb239v2:
            case X9_62_c2tnb239v3:
            case X9_62_c2tnb359v1:
            case X9_62_c2tnb431r1:
                try {
                    kpg = KeyPairGenerator.getInstance("EC");
                    spec = new ECGenParameterSpec(getJCAName(algorithm));
                    kpg.initialize(spec);
                } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                }
                break;
            case DSA:
                try {
                    kpg = KeyPairGenerator.getInstance("DSA");
                    kpg.initialize(DSA_DEFAULT_KEYSIZE);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                break;
            case RSA:
                try {
                    kpg = KeyPairGenerator.getInstance("RSA");
                    spec = new RSAKeyGenParameterSpec(RSA_DEFAULT_KEYSIZE, RSA_DEFAULT_EXPONENT);
                    kpg.initialize(spec);
                } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new RuntimeException("not possible");
        }
        return kpg.generateKeyPair();
    }


    //public KeyPair (InputStream pkcs8_private, InputStream x509_public) throws IOException{
    //    this(PrivateKey.loadPKCS8(pkcs8_private), PublicKey.loadX509(x509_public));
    //}

    private static String getJCAName(Algorithm algorithm) {
        switch (algorithm) {
            case secp112r1:
            case secp112r2:
            case secp128r1:
            case secp128r2:
            case secp160k1:
            case secp160r1:
            case secp160r2:
            case secp192k1:
            case secp192r1:
            case secp224k1:
            case secp224r1:
            case secp256k1:
            case secp256r1:
            case secp384r1:
            case secp521r1:
            case sect113r1:
            case sect113r2:
            case sect131r1:
            case sect131r2:
            case sect163k1:
            case sect163r1:
            case sect163r2:
            case sect193r1:
            case sect193r2:
            case sect233k1:
            case sect233r1:
            case sect239k1:
            case sect283k1:
            case sect283r1:
            case sect409k1:
            case sect409r1:
            case sect571k1:
            case sect571r1:
                return algorithm.toString();
            case X9_62_prime192v2:
            case X9_62_prime192v3:
            case X9_62_prime239v1:
            case X9_62_prime239v2:
            case X9_62_prime239v3:
            case X9_62_c2tnb191v1:
            case X9_62_c2tnb191v2:
            case X9_62_c2tnb191v3:
            case X9_62_c2tnb239v1:
            case X9_62_c2tnb239v2:
            case X9_62_c2tnb239v3:
            case X9_62_c2tnb359v1:
            case X9_62_c2tnb431r1:
                StringBuilder builder = new StringBuilder(algorithm.toString());
                builder.replace(2, 3, ".");
                builder.replace(5, 6, " ");
                return builder.toString();
            default:
                throw new RuntimeException("impossible");
        }

    }

    public static java.security.PublicKey getPublicKeyFromRSACRTPrivateKey(java.security.PrivateKey privateKey) {
        try {
            RSAPrivateCrtKey crt = (RSAPrivateCrtKey) privateKey;
            RSAPublicKeySpec s = new RSAPublicKeySpec(crt.getModulus(), crt.getPublicExponent());
            KeyFactory f = KeyFactory.getInstance("RSA");
            return f.generatePublic(s);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not extract public RSA key");
        }
    }

    public PublicKey getPublicKey() {
        return pubKey;
    }

    public PrivateKey getPrivateKey() {
        return privKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyPair that = (KeyPair) o;

        assert this.privKey != null;

        return this.privKey.equals(that.privKey) && this.pubKey.equals(that.pubKey);

    }

    @Override
    public int hashCode() {
        int result = privKey.hashCode();
        result = 31 * result + pubKey.hashCode();
        return result;
    }

    public enum Algorithm {
        DSA,
        RSA,
        secp112r1,
        secp112r2,
        secp128r1,
        secp128r2,
        secp160k1,
        secp160r1,
        secp160r2,
        secp192k1,
        secp192r1,
        secp224k1,
        secp224r1,
        secp256k1,
        secp256r1,
        secp384r1,
        secp521r1,
        sect113r1,
        sect113r2,
        sect131r1,
        sect131r2,
        sect163k1,
        sect163r1,
        sect163r2,
        sect193r1,
        sect193r2,
        sect233k1,
        sect233r1,
        sect239k1,
        sect283k1,
        sect283r1,
        sect409k1,
        sect409r1,
        sect571k1,
        sect571r1,
        X9_62_prime192v2,
        X9_62_prime192v3,
        X9_62_prime239v1,
        X9_62_prime239v2,
        X9_62_prime239v3,
        X9_62_c2tnb191v1,
        X9_62_c2tnb191v2,
        X9_62_c2tnb191v3,
        X9_62_c2tnb239v1,
        X9_62_c2tnb239v2,
        X9_62_c2tnb239v3,
        X9_62_c2tnb359v1,
        X9_62_c2tnb431r1
    }

    public static class PrivateKey {
        java.security.PrivateKey pk;

        public PrivateKey(java.security.PrivateKey pk) {
            this.pk = pk;
        }

//        public PublicKey getPublicKey () {
//            switch(this.pk.getAlgorithm()) {
//                case "RSA":
//                    return new PublicKey(getPublicKeyFromRSACRTPrivateKey(this.pk));
//                case "DSA":
//                case "EC":
//                    // no possible with std JCA provider ...
//
//            }
//            throw new RuntimeException("could not get public key.");
//        }

        public static PrivateKey loadPKCS8(InputStream is) throws IOException {
            byte[] pkcs8Bytes = readAllClose(is);
            return loadPKCS8(pkcs8Bytes);
        }

        public static PrivateKey loadPKCS8(byte[] bytes) {
            for (String type : types) {
                try {
                    KeyFactory factory = KeyFactory.getInstance(type);
                    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
                    return new PrivateKey(factory.generatePrivate(spec));
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    continue;
                }
            }
            throw new RuntimeException("invalid key");
        }

        /**
         * Encodes the private key according to pkcs8. PKCS#8 specifies that encoding the public
         * key half along with the private key is optional, so unfortunately, we're at the
         * liberty of the JCA implementation concerning whether the public part is also encoded
         * At any rate, the JDK won't reliably allow extracting the public half anyway(1).
         * <p/>
         * (1) RSA Private Keys are (in JDK 7) handled internally as CRT, which would allow this,
         * see commented out code below.
         */
        public byte[] toPKCS8() {
            if (!"PKCS#8".equals(pk.getFormat())) {
                throw new RuntimeException("Unable to encode Private Key to PKCS8");
            }
            return pk.getEncoded();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PrivateKey that = (PrivateKey) o;

            return pk.equals(that.pk);

        }

        @Override
        public int hashCode() {
            return pk.hashCode();
        }
    }

    public static class PublicKey {
        private java.security.PublicKey pub;

        public PublicKey(java.security.PublicKey pub) {
            if (!"X.509".equals(pub.getFormat())) {
                // to the best of my knowledge no other `type` exists.
                throw new RuntimeException("Unable to encode Public Key to x.509");
            }
            this.pub = pub;
        }

        public static PublicKey loadX509(InputStream is) throws IOException {
            byte[] x509Bytes = readAllClose(is);
            return loadX509(x509Bytes);
        }
//        public void storeX509(OutputStream out) throws IOException {
//            out.write(toX509());
//        }

        public static PublicKey loadX509(byte[] bytes) {
            for (String type : types) {
                try {
                    KeyFactory factory = KeyFactory.getInstance(type);
                    X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
                    return new PublicKey(factory.generatePublic(spec));
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    continue;
                }
            }
            throw new RuntimeException("invalid key");
        }

        public java.security.PublicKey getPublicKey() {
            return pub;
        }

        public byte[] toX509() {
            if (!"X.509".equals(pub.getFormat())) {
                // to the best of my knowledge no other `type` exists.
                // so this really shouldn't happen (not the least because
                // the constructor should have died)
                throw new RuntimeException("Unable to encode Public Key to x.509");
            }
            return pub.getEncoded();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PublicKey that = (PublicKey) o;

            return pub.equals(that.pub);
        }

        @Override
        public int hashCode() {
            return pub.hashCode();
        }
    }

    public static class ECPublicKey extends KeyPair.PublicKey {
        public ECPublicKey(java.security.interfaces.ECPublicKey pub) {
            super(pub);
        }

        private static int getByteSize(ECParameterSpec params) {
            int bitLength = params.getCurve().getField().getFieldSize();
            return (bitLength + 7) >>> 3; // add 7 in case bitLength % 8 != 0
        }

        public static ECPublicKey loadUncompressedCurvePoints(Algorithm algorithm, byte[] keyData) {
            switch (algorithm) {
                case RSA:
                case DSA:
                    throw new RuntimeException("not an EC algorithm");

            }

            // There has to be a less moronic way to get the params for a named
            // curve in the JCA apart from actually generating a key and stealing
            // those keys params. Look!
            KeyPair kp = generateKeyPair(algorithm);
            ECPublicKey ecPublicKey = (ECPublicKey) kp.getPublicKey();
            java.security.interfaces.ECPublicKey pub = ecPublicKey.getPublicKey();
            ECParameterSpec params = pub.getParams();

            int byteLength = getByteSize(params);

            if (keyData.length != (byteLength * 2 + 1)) {
                throw new RuntimeException("invalid length of uncompressed key data");
            }
            if (keyData[0] != 0x04) {
                throw new RuntimeException("invalid format of uncompressed key data");
            }

            byte[] bytes = new byte[byteLength];
            System.arraycopy(keyData, 1, bytes, 0, byteLength);
            BigInteger x = new BigInteger(bytes);
            System.arraycopy(keyData, byteLength + 1, bytes, 0, byteLength);
            BigInteger y = new BigInteger(bytes);

            java.security.spec.ECPoint point = new ECPoint(x, y);
            java.security.spec.ECPublicKeySpec spec = new ECPublicKeySpec(point, params);

            try {
                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                ecPublicKey = new ECPublicKey((java.security.interfaces.ECPublicKey) keyFactory.generatePublic(spec));
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            return ecPublicKey;
        }

        public static ECPublicKey loadUncompressedCurvePoints(Algorithm a, InputStream is) {
            byte[] points = readAllClose(is);
            return loadUncompressedCurvePoints(a, points);
        }

        @Override
        public java.security.interfaces.ECPublicKey getPublicKey() {
            return (java.security.interfaces.ECPublicKey) super.getPublicKey();
        }

        /**
         * 4.3.6 of ANSI X9.62. , 2.3.3 SEC1
         *
         * @return the uncompressed point representation of this piblic key.
         */
        public byte[] toUncompressedCurvePoint() {
            int byteLength = getByteSize(this.getPublicKey().getParams());

            int size = 1 + (2 * (byteLength));
            byte[] bytes = new byte[size];
            bytes[0] = 0x04;

            BigInteger x = this.getPublicKey().getW().getAffineX();
            BigInteger y = this.getPublicKey().getW().getAffineY();
            byte[] x_bytes = x.toByteArray();
            byte[] y_bytes = y.toByteArray();

            copyDespiteJavaBigInteger(x_bytes, bytes, 1, byteLength);
            copyDespiteJavaBigInteger(y_bytes, bytes, 1 + byteLength, byteLength);
            return bytes;
        }

        // java's BigInteger trims leading zeros and adds them in to make sure byte array
        // represenations are signed ... This is to compensate for wonky lengths.
        private void copyDespiteJavaBigInteger(byte[] coordinateBytes, byte[] bytes, int offset, int numBytes) {
            if (coordinateBytes == null || coordinateBytes.length > numBytes + 1) {
                throw new RuntimeException("incorrect length");
            }
            int srcStart = 0;
            int skip = 0;
            if (coordinateBytes.length <= numBytes) {
                // we're fine
                skip = numBytes - coordinateBytes.length;
                offset += skip;
            } else if (coordinateBytes.length == numBytes + 1 && (coordinateBytes[0] == 0x00)) {
                // java added and additional sign byte...
                srcStart += 1;
            } else {
                throw new RuntimeException("param to long");
            }
            System.arraycopy(coordinateBytes, srcStart, bytes, offset, numBytes - skip);
        }
    }
}
