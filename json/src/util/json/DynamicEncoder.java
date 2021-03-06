package util.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicEncoder extends Encoder {
    boolean canEncode(Object o) {
        // return true if o has a toJSON function returning String
        Class c = o.getClass();
        try {
            Method m = c.getDeclaredMethod("toJSON");
        } catch (NoSuchMethodException nsme) {
            return false;
        }
        return true;
    }

    void encodeCustom(Object o) {
        Class c = o.getClass();
        Method m;
        try {
            m = c.getDeclaredMethod("toJSON");
            buf.append(m.invoke(o));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            error(e);
        }
    }
}
