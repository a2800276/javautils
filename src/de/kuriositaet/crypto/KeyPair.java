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
 * Utility to create, serialize and load KeyPair's
 *
 * A KeyPairs generated by these utilities will be generated by default JCA providers. KeyPairs generated
 * otherwise (specifically in Android hardware ...) can be wrapped to use with these utility functions.
 *
 * To err on the side of caution, for EC Keypairs only curves P224, P256, P384, P521
 * are supported. These are likely the most commonly used curves and the most widely
 * available one. The alternatively would be to provide all curves provided in default JCA JDK ver. X
 * in JDK 7 that's already a laundry list ~40 curves.
 *
 * The only other one would be Curve25519, which is not likely to be available in JCA any time soon.
 *
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

    /**
     * @param pkcs8_private
     * @param x509_public
     * @throws WrappedException if provided invalid key bytes.
     */
    public KeyPair(byte[] pkcs8_private, byte[] x509_public) {
        this(PrivateKey.loadPKCS8(pkcs8_private), PublicKey.loadX509(x509_public));
    }

    private KeyPair(java.security.KeyPair kp) {
        this(
                new PrivateKey(kp.getPrivate()),
                new PublicKey(kp.getPublic())
        );
    }

    /**
     * Generates a key pair for the indicated algorithm. RSA and DSA keys will be generated
     * with the bitsize (@see Constants.RSA_DEFAULT_KEYSIZE and @see Constants.DSA_DEFAULT_KEYSIZE).
     * <p/>
     * To influence parameters of RSA and DSA use:
     *
     * @param algorithm
     * @return
     * @throws WrappedException wrapping InvalidAlgorithmParameterException, NoSuchAlgorithmException
     *                          if underlying JCA does not provide the Algorithm or curve.
     * @see #generateRSAKeyPair
     * @see #generateDSAKeyPair
     */
    public static KeyPair generateKeyPair(Algorithm algorithm) {
        java.security.KeyPair kp = generateJCAKeyPair(algorithm);
        return new KeyPair(
                new PrivateKey(kp.getPrivate()),
                new PublicKey(kp.getPublic())
        );
    }

    /**
     * Generate an RSA key pair of the indicated size with exponent @see Constants.RSA_DEFAULT_EXPONENT
     *
     * @param keysizeBits
     * @return
     * @throws WrappedException wrapping NoSuchAlgorithmException only if the JCA does not provide RSA or wrapping
     *                          InvalidAlgorithmParameterException if provided an invalid keysize.
     */
    public static KeyPair generateRSAKeyPair(int keysizeBits) {
        return generateRSAKeyPair(keysizeBits, RSA_DEFAULT_EXPONENT);
    }

    /**
     * Generate an RSA key pair of the indicated size and exponent
     * @param keysizeBits
     * @return
     * @throws WrappedException wrapping NoSuchAlgorithmException only if the JCA does not provide RSA or wrapping
     *         InvalidAlgorithmParameterException if provided an invalid key size or exponent.
     */
    public static KeyPair generateRSAKeyPair(int keysizeBits, BigInteger exponent) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keysizeBits, exponent);
            kpg.initialize(spec);
            java.security.KeyPair kp = kpg.generateKeyPair();
            return new KeyPair(kp);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
            throw new WrappedException(e);
        }
    }

    public static KeyPair generateDSAKeyPair(int keysizeBites) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(DSA_DEFAULT_KEYSIZE);
            return new KeyPair(kpg.generateKeyPair());
        } catch (NoSuchAlgorithmException e) {
            throw new WrappedException(e);
        }
    }

    static java.security.KeyPair generateJCAKeyPair(Algorithm algorithm) {
        KeyPairGenerator kpg;
        AlgorithmParameterSpec spec;
        String KPG_INSTANCE_ID;
        switch (algorithm) {
            case P224:
            case P256:
            case P384:
            case P521:
                try {
                    kpg = KeyPairGenerator.getInstance("EC");
                    spec = new ECGenParameterSpec(algorithm.getJcaName());
                    kpg.initialize(spec);
                } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                    throw new WrappedException(e);
                }
                break;
            case DSA:
                try {
                    kpg = KeyPairGenerator.getInstance("DSA");
                    kpg.initialize(DSA_DEFAULT_KEYSIZE);
                } catch (NoSuchAlgorithmException e) {
                    throw new WrappedException(e);
                }
                break;
            case RSA:
                try {
                    kpg = KeyPairGenerator.getInstance("RSA");
                    spec = new RSAKeyGenParameterSpec(RSA_DEFAULT_KEYSIZE, RSA_DEFAULT_EXPONENT);
                    kpg.initialize(spec);
                } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                    throw new WrappedException(e);
                }
                break;
            default:
                throw new IllegalArgumentException("not possible");
        }
        return kpg.generateKeyPair();
    }

    /**
     * because we can. May come in useful some day :)
     *
     * @param privateKeyCrt
     * @return
     */
    public static java.security.PublicKey getPublicKeyFromRSACRTPrivateKey(RSAPrivateCrtKey privateKeyCrt) {
        try {
            RSAPublicKeySpec s = new RSAPublicKeySpec(privateKeyCrt.getModulus(), privateKeyCrt.getPublicExponent());
            KeyFactory f = KeyFactory.getInstance("RSA");
            return f.generatePublic(s);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new WrappedException("Could not extract public RSA key");
        }
    }


    //public KeyPair (InputStream pkcs8_private, InputStream x509_public) throws IOException{
    //    this(PrivateKey.loadPKCS8(pkcs8_private), PublicKey.loadX509(x509_public));
    //}

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
        /**
         * NIST P224
         * JCA: secp224r1
         * OID: 1.3.132.0.33
         */
        P224,
        /**
         * NIST P256
         * JCA: secp256r1
         * OID: 1.2.840.10045.3.1.7
         */
        P256,
        /**
         * NIST 384
         * JCA: secp384r1
         * OID: 1.3.132.0.34
         */
        P384,
        /**
         * NIST 521
         * JCA: secp521r1
         * OID: 1.3.132.0.35
         */
        P521;

        String getJcaName() {
            switch (this) {
                case DSA:
                case RSA:
                    return this.toString();
                case P224:
                    return "secp224r1";
                case P256:
                    return "secp256r1";
                case P384:
                    return "secp384r1";
                case P521:
                    return "secp521r1";
                default:
                    throw new IllegalArgumentException("unknown algorithm.");
            }
        }
    }

    public static class Key {
        private java.security.Key key;

        public Key(java.security.Key k) {
            this.key = k;
        }

        java.security.Key getKey() {
            return this.key;
        }

        Signature.Algorithm getSignatureAlgorithm(Hash.Algorithm algorithm) {
            Signature.Algorithm sa;
            if ("DSA".equals(getKey().getAlgorithm())) {
                switch (algorithm) {
                    case SHA1:
                        sa = Signature.Algorithm.SHA1withDSA;
                        break;
                    default:
                        throw new WrappedException("DSA signature only supported with SHA1");
                }
            } else if ("RSA".equals(getKey().getAlgorithm())) {
                switch (algorithm) {
                    case SHA1:
                        sa = Signature.Algorithm.SHA1withRSA;
                        break;
                    case SHA256:
                        sa = Signature.Algorithm.SHA256withRSA;
                        break;
                    default:
                        throw new WrappedException("RSA signature only supported with SHA1 or SHA256");
                }
            } else if ("EC".equals(getKey().getAlgorithm())) {
                switch (algorithm) {
                    case SHA1:
                        sa = Signature.Algorithm.SHA1withECDSA;
                        break;
                    case SHA256:
                        sa = Signature.Algorithm.SHA256withECDSA;
                        break;
                    case SHA384:
                        sa = Signature.Algorithm.SHA384withECDSA;
                        break;
                    case SHA512:
                        sa = Signature.Algorithm.SHA512withECDSA;
                        break;
                    default:
                        throw new WrappedException("unsupported hash for EC signature");
                }
            } else {
                throw new WrappedException("unknown getType: " + getKey().getAlgorithm());
            }
            return sa;
        }
    }

    public static class PrivateKey extends Key {

        public PrivateKey(java.security.PrivateKey pk) {
            super(pk);
        }

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
            throw new WrappedException("invalid key");
        }

        java.security.PrivateKey getPrivateKey() {
            return (java.security.PrivateKey) super.getKey();
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
            if (!FORMAT_PKCS8.equals(getPrivateKey().getFormat())) {
                throw new WrappedException("Unable to encode Private Key to PKCS8");
            }
            return getPrivateKey().getEncoded();
        }

        public byte[] sign(Hash.Algorithm algorithm, byte[]... data) {
            return Signature.sign(getSignatureAlgorithm(algorithm), this, data);
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

            return getPrivateKey().equals(that.getPrivateKey());

        }

        @Override
        public int hashCode() {
            return getPrivateKey().hashCode();
        }
    }

    public static class PublicKey extends Key {

        public PublicKey(java.security.PublicKey publicKey) {
            super(publicKey);
            if (!FORMAT_X509.equals(getPublicKey().getFormat())) {
                // to the best of my knowledge no other `type` exists.
                throw new IllegalArgumentException("Unable to encode Public Key to x.509");
            }
        }

        public static PublicKey loadX509(InputStream is) throws IOException {
            byte[] x509Bytes = readAllClose(is);
            return loadX509(x509Bytes);
        }

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
            throw new IllegalArgumentException("invalid key");
        }

        java.security.PublicKey getPublicKey() {
            return (java.security.PublicKey) super.getKey();
        }

        public byte[] toX509() {
            if (!FORMAT_X509.equals(getPublicKey().getFormat())) {
                // to the best of my knowledge no other `type` exists.
                // so this really shouldn't happen (not the least because
                // the constructor should have died)
                throw new RuntimeException("Unable to encode Public Key to x.509");
            }
            return getPublicKey().getEncoded();
        }

        public boolean verify(Hash.Algorithm algorithm, byte[] signature, byte[]... data) {
            return Signature.verify(getSignatureAlgorithm(algorithm), this, signature, data);
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

            return getPublicKey().equals(that.getPublicKey());
        }

        @Override
        public int hashCode() {
            return getPublicKey().hashCode();
        }

    }

    /**
     * Subclass of PublicKey that can handle PK specific encodings, presently
     * Uncompressed Curve Point representation.
     */
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
                throw new IllegalArgumentException("invalid length of uncompressed key data");
            }
            if (keyData[0] != 0x04) {
                throw new IllegalArgumentException("invalid format of uncompressed key data");
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
                throw new WrappedException(e);
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
         * @throws IllegalArgumentException if the curve point byte length doesn't
         *         correspond to the expected size based on the named curve. This should
         *         not be able to happen unless one somehow managed to stuff a completely
         *         wrong custom implementation of a JCA public key into this object.
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
                throw new IllegalArgumentException("incorrect length");
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
                throw new IllegalArgumentException("coordinateBytes to long");
            }
            System.arraycopy(coordinateBytes, srcStart, bytes, offset, numBytes - skip);
        }
    }
}
