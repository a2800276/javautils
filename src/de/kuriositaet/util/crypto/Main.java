package de.kuriositaet.util.crypto;

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
        Util.p("------------------------------------------------------------------------");
        Util.p("Provider: " + provider.getName());
        Util.p("Services: ");
        for (Provider.Service service : provider.getServices()) {
            Util.p(service);
//            if (service.getType().equals("AlgorithmParameters") && null != service.getAttribute("SupportedCurves")) {
//                for (String curve : service.getAttribute("SupportedCurves").split("\\|")){
//                  p(curve.split(",")[0]);
//                }
//                System.exit(0);
//
//            }
        }
        Util.p("------------------------------------------------------------------------");
    }

    public void test(byte[] testdata) {
        CipherInputStream is = null;

    }

    enum TestEnum {
        bla_2000
    }

}
