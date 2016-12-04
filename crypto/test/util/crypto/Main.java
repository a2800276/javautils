package util.crypto;

import util.bytes.hexy.Hexy;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.security.Key;
import java.security.Provider;

import static util.bytes.Bytes.b2h;
import static util.bytes.Bytes.h2b;

public class Main {

	public static void main(String[] args) throws Throwable {
		//for (Provider p : Security.getProviders()) {
		//dumpProvider(p);
		//}

		//Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		byte[] data = h2b("85C99C14 0884AE40" +
				"EC97333A 57FF3AF5" +
				"9116B5AA FCE9FC18" +
				"BEA41A4B 4262DCDA" +
				"A014DA9E A0337C62" +
				"235A06C8 A51101D6" +
				"CACB237B F44FBD86" +
				"47AA6E9A 27794C51" +
				"401657B2 84DFFB55" +
				"DBD18DFE FE0E5AEB" +
				"578BCDA9 AEAAD8B6" +
				"6696AC73 75BA430B" +
				"8FB2454E 732A2879" +
				"2B51055A C9A795F5" +
				"8816BFCE 59CAA00A" +
				"015BDAB9 60BB882E" +
				"C3CBD6D4 A7CA9A1D" +
				"F4E12301 2E53E652" +
				"EE7FF2D7 4F6C5A4E" +
				"436FEBF5 AD040D6B" +
				"BEB41372 4C41ACE5" +
				"15EA4193 917295F4");

		byte[] modulus = h2b("A1F5E1C9BD8650BD43AB6EE56B891EF7459C0A24FA84F9127D1A6C79D4930F6DB1852E2510F18B61CD354DB83A356BD190B88AB8DF04284D02A4204A7B6CB7C5551977A9B36379CA3DE1A08E69F301C95CC1C20506959275F41723DD5D2925290579E5A95B0DF6323FC8E9273D6F849198C4996209166D9BFC973C361CC826E1");

		//modulus = h2b("B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597");
		p("size modulus: " + modulus.length);


		byte[] exponent = {0x03};

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		Key key = KeyPair.getRSAPublicKeyFromModulusAndExponent(modulus, exponent);
		p(data.length);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(data);
		p(b2h(result));
		p(Hexy.toString(result));
		p(cipher);
	}

	private static void dumpProvider(Provider provider) {
		p("------------------------------------------------------------------------");
		p("Provider: " + provider.getName());
		p("Services: ");
		for (Provider.Service service : provider.getServices()) {
			p(service);
//            if (service.getType().equals("AlgorithmParameters") && null != service.getAttribute("SupportedCurves")) {
//                for (String curve : service.getAttribute("SupportedCurves").split("\\|")){
//                  p(curve.split(",")[0]);
//                }
//                System.exit(0);
//
//            }
		}
		p("------------------------------------------------------------------------");
	}

	static void p(Object s) {
		System.out.println(s);
	}

	static void p(Object[] os) {
		for (Object o : os) {
			p(o);
		}
	}

	static void p(byte[] bs) {
		for (byte b : bs) {
			p(b);
		}
	}

	public void test(byte[] testdata) {
		CipherInputStream is = null;

	}

	enum TestEnum {
		bla_2000
	}

}
