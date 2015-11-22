package util.crypto;

import javax.crypto.CipherInputStream;
import java.security.Provider;
import java.security.Security;

public class Main {
    public static void main(String[] args) throws Throwable {
        for (Provider p : Security.getProviders()) {
            dumpProvider(p);
        }
        //Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
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

    public void test(byte[] testdata) {
        CipherInputStream is = null;

    }

    enum TestEnum {
        bla_2000
    }

}
