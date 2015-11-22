package de.kuriositaet.utils.ioutils;

import java.io.File;
import java.io.FileInputStream;

public class IOUtils {

    /**
     * Do irrational stuff like caring whether closing a
     * stream causes an IOException.
     */
    public static boolean PEDANTIC;

    static void p(Object o) {
        System.out.println(o);
    }

    public static Res readFile(String filename) {
        Res r = new Res();
        r.error = true;
        r.message = "<not initialized>";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (java.io.FileNotFoundException fno) {
            r.error = true;
            r.message = "file not found: " + filename;
            r.exception = fno;
            return r;
        }

        StringBuilder builder = new StringBuilder();
        int b;

        try {
            while (-1 != (b = fis.read())) {
                builder.append((char) b);
            }
        } catch (java.io.IOException ioe) {
            r.error = true;
            r.message = ioe.getMessage();
            r.exception = ioe;
            return r;
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (Throwable t) {
                    if (PEDANTIC) throw new RuntimeException(t);
                }
            }
        }
        r.contents = builder.toString();
        r.error = false;
        r.message = null;

        return r;
    }

    /**
     * Try to retrieve a filename relative to another filename. Useful for includes, e.g.
     * say you are scanning through "../somefile" which contains the following:
     * "./someinclude", from the point of view of the scanning code, the file that is
     * meant is "../someinclude". This methods rectifies this, unless `filename` is an
     * absolute filename, in which case it is returned ...
     */
    public static String filenameRelativeTo(String filename, String relativeTo) {
        File f = new File(filename);
        if (f.isAbsolute()) {
            return filename;
        }
        return getPath(relativeTo) + File.separator + filename;
    }

    /**
     * Java's getPath does whatever (?), this tries to return the system
     * dependant path component of the filename
     */
    public static String getPath(String fn) {
        int i = fn.lastIndexOf(File.separator);
        return (i > -1) ? fn.substring(0, i) : fn;
    }

    public static class Res {
        public boolean error;
        public String contents;
        public String message;
        public Throwable exception;
    }
}
