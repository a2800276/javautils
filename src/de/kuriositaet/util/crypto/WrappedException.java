package de.kuriositaet.util.crypto;

/**
 * This class provides a RuntimeException (tagged to indicate exceptions stem from use of this library).
 * It's intended to provide a wrapper around the checked exception prevelant to the JCA Architecture.
 * I.e. The judicious NoSuchAlgorithm typically shouldn't be occurring outside of testing, because you
 * should have tested that the algorithms your code is using are actually available on the targeted platform.
 * <p/>
 * Also, the selection of provided algorithms is conservative enough that they should be available anywhere.
 * <p/>
 * Use of wrapped exceptions will be noted in the Javadoc of any function potentially throwing them.
 * <p/>
 * <p/>
 * Created by a2800276 on 2015-11-15.
 */
public class WrappedException extends RuntimeException {
    public WrappedException(String message) {
        super(message);
    }

//    public WrappedException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public WrappedException(Throwable cause) {
        super(cause);
    }
}
