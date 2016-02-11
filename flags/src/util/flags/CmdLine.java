package util.flags;


/**
 * Quick utility class to facilitate working with commandline
 * arguments passed to the main method of an application. This is a
 * rather rudementary implementation that won't parse any really
 * fancy cmd line flag type things only the following<p>
 * <pre>
 * -flagName flagValue
 * </pre>
 * <p/>
 * In other words, a flag preceded by a dash ('-') is necessary as
 * well as an option name. Standalone flags return an empty string.
 *
 * Usage is as follows:
 * <pre>
 *     ... in main ...
 *     CmdLine cl = new CmdLine(args);
 *     // make sure all mandatory flags are passed
 *     if (!cl.ensure("-filename", "-out")) {
 *         usage();
 *     }
 *
 *     int defaultPort = cl.get("-port", 8080);
 *     String fn = cl.get("-filename");
 * </pre>
 *
 * @author tim@kuriositaet.de
 */

import java.util.Hashtable;
import java.util.Set;

public class CmdLine {

    private static final String[] CAST = new String[0];
    private Hashtable<String, String> hash = new Hashtable<String, String>();
    private String[] args;
    private String[] argsArr;
    private String[] rest;

    /**
     * Initialise these CmdLine Parameters with the given commandline. This
     * should be the commandline arguments provided to the main method, i.e.
     * the `args` in <code>public static void main (String [] args)</code>
     */
    public CmdLine(String[] args) {
        this.args = args;
        if (null != this.args) {
            parse();
        }
    }

    /**
     * The method that handles parsing the cmd line argument array.
     */
    private void parse() {
        String option = null;
        int rest_i = args.length;
        for (int i = 0; i < args.length; ++i) {
            if (!args[i].startsWith("-")) {
                // always have to have a flag
                rest_i = i;
                break;
            }
            option = args[i];

            ++i;
            if (i >= args.length) {
                // we're done
                hash.put(option, "");
                break;
            }
            if (args[i].startsWith("-")) {
                // this is another option
                hash.put(option, "");
                --i;
                continue;
            }
            hash.put(option, args[i]);
        }
        int len = args.length - rest_i;
        this.rest = new String[len];

        System.arraycopy(args, rest_i, this.rest, 0, len);

        this.argsArr = new String[args.length - len];
        System.arraycopy(args, 0, this.argsArr, 0, args.length - len);

    }

    /**
     * Retrieve the option with the provided name, returns null if the option
     * was not set.
     *
     * <code>String fn = flags.get("-filename")</code>
     */
    public String get(String option) {
        if (null == option) {
            // if you get here you're an imbecile.
            return null;
        }
        return hash.get(option);
    }

    /**
     * Retrieve the option with the provided name, if the value is not set in
     * the commandline arguments, return the stated default value.
     */
    public String get(String option, String defaultValue) {
        return null == get(option) ? defaultValue : get(option);
    }

    /**
     * Retrieve the trailing args with no option flags, e.g. for the
     * command line:
     * <pre>
     *     -fn bla.text -func read_all one two three
     * </pre>
     * <p/>
     * returns <code>{"one", "two", "three"}</code>
     *
     * @return
     */
    public String[] getRest() {
        return this.rest;
    }

    /*
     * This returns the args (-flag value ...) portion of the
     * args:
     *
     * <pre>
     *     -fn bla.text -func read_all one two three
     * </pre>
     *
     * returns <code>{"-fn", "bla.text", "-func", "read_all"} </code>
     *
     * I have no ideas what this is supposed to be good for, and
     * will likely to remove next time I come across it.
     *
     * @deprecated
     */
    public String[] getArgs() {
        return this.argsArr;
    }

    /**
     * Make sure all the provided options are set and contains a value.
     *
     * @param options
     * @return
     */
    public boolean ensure(String... options) {
        for (String s : options) {
            if (get(s) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether a parameter of the given name was set at all.
     *
     * @param option
     * @return
     */
    public boolean exists(String option) {
        return hash.containsKey(option);
    }

    /**
     * @return
     */
    public Set<String> getFlags() {
        return hash.keySet();
    }

    /**
     * Retrieve an `int` parameter named `option`.
     *
     * @param option the name of the parameter to retrieve
     * @param def    the default value in case the parameter wasn't set.
     * @return
     */
    public int get(String option, int def) {
        if (get(option) == null)
            return def;
        int ret = def;
        try {
            ret = Integer.parseInt(get(option));
        } catch (NumberFormatException nfe) {

        }
        return ret;
    }


}


