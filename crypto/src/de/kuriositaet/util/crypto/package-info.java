/**
 * These are a series of utilities to deal with some of the annoying
 * behaviour of Java JCA cryptography. In no particular order:
 * <ul>
 * <li> Overcomplicated architecture</li>
 * <li> Overuse of Exceptions</li>
 * <li> Overreliance on (undocumented) magic String constants</li>
 * <li> Undocumented formats of results</li>
 * </ul>
 * <p/>
 * <h2> Overcomplicated Architecture </h2>
 * <p/>
 * The JCA is very powerful and lends itself to complicated use cases concerning
 * cryptography, e.g. use of hardware modules. Unfortunately, this gets in the
 * way of more common use cases. As an example, to calculate a SHA256 HMAC value:
 * <p/>
 * <pre>
 *    try {
 *      byte [] data = ...;
 *      byte [] actualkey = ...;
 *      Key key = new SecretKeySpec(actualkey, "HmacSHA256");
 *      Mac mac = Mac.getInstance("HmacSHA256");
 *      mac.init(key); /* InvalidKeyException is thrown here, not creating the SecretKeySpec
 *      for (byte[] bytes : data) {
 *          mac.update(bytes);
 *      }
 *      byte[] hmac = mac.doFinal();
 *    } catch (NoSuchAlgorithmException e) {
 *      /* how is this not a RuntimeException!?
 *    } catch (InvalidKeyException e) {
 *      /* no such thing as an InvalidKey for HMAC ...
 *    }
 * </pre>
 * <p/>
 * This leaves you wondering how to deal with two exceptions that really can't occur (or at
 * best should only occur during development/testing) as well as a ton of boilerplate and digging
 * around arcane documentations to find out why the algorithm String constant is "HmacSHA256" or
 * "HMAC-sha256"
 * <p/>
 * The static methods in HMAC encapsulates this behaviour into:
 * <p/>
 * <pre>
 *     byte [] data = ...;
 *     byte [] actualkey = ...;
 *     byte hmac = HMAC.hmacSHA256(actualkey, data);
 * </pre>
 * <p/>
 * <h2>Overuse of Exceptions</h2>
 * <p/>
 * In the extremely hypothetical case you are using JCA providers that don't provide HMAC based on
 * SHA256, the otherwise useless <code>NoSuchAlgorithmException</code> will be thrown within a
 * <code>WrappedException</code> which is derived from <code>RuntimeException</code> and does not
 * need to be handled. In consequence this means you should test your crypto code on the plattform you
 * wish to deploy on to catch these problems. (I'm not aware of any scenario with a reasonable modern
 * JDK [say 6+] that doesn't support all algorithms hardcoded in this library).
 * <p/>
 * <h2> Overreliance on (undocumented) magic String constants</h2>
 * <p/>
 * Use of String constants in the JCA is replaced by either named methods (as in the HMAC example above)
 * or Java Enums. Enums are defined as inner classes  in the relevant classes, namely:
 * <p/>
 * <ul>
 * <li>{@link de.kuriositaet.util.crypto.Hash.Algorithm Hash.Algorithm}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.Algorithm}</li>
 * <li>{@link de.kuriositaet.util.crypto.Signature.Algorithm}</li>
 * </ul>
 * The named constants are particularly troublesame identifying named curves. Currently NIST curves
 * P224, P256, P384, P521 are supported assuming widespread availability.
 * <p/>
 * <h2> Undocumented formats of results</h2>
 * <p/>
 * The JCA/JDK documentation is fairly unclear as to the precise format of binary return values. As
 * an example, <code>Certificate.getEncoded()</code> returns the x.509 representation of a certificate.
 * Note: this only applies if <code>Certificate.getType</code> returns the String "X.509" [as of 2015 no other
 * type of Certificate is generally in use or at least implemented.]
 * <p/>
 * While the binary representation of a Certificate may be obvious, what the format of the bytes returned
 * by <code>PrivateKey.getEncoded</code> is, is not. Specifically, these utilities provide:
 * <ul>
 * <li>{@link de.kuriositaet.util.crypto.Certificate#getX509Bytes}</li>
 * <li>{@link de.kuriositaet.util.crypto.Certificate#getX509Bytes}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.PrivateKey#toPKCS8}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.PrivateKey#loadPKCS8}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.PublicKey#toX509}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.PublicKey#loadX509}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.ECPublicKey#toUncompressedCurvePoint}</li>
 * <li>{@link de.kuriositaet.util.crypto.KeyPair.ECPublicKey#loadUncompressedCurvePoints}</li>
 * </ul>
 * <p/>
 * <h2>Caveat Emptor</h2>
 * <p/>
 * This is not meant as a full fledged set of primitives to enable arbitrary crypto, but instead a
 * number of routines I find myself using regularly.
 */

package de.kuriositaet.util.crypto;