package de.kuriositaet.utils.csv;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Trivial implementation of a CSV parser.
 * <p/>
 * Default field delimiter is ';', default escape character is '\'. These
 * settings may be changed using `setDelimiter()` and `setEscape()`
 * respectively. The escape character will pass the following character through,
 * unless it is used at the end of a line or the end of the file, in which
 * case an error is reported. This means, no newlines in csv fields.
 * <p/>
 * usage:
 * <pre>
 * String csv = "one; two; three"
 * CSVParser parser = new CSVParser(csv);
 * while ( (list = parser.getNextLine()) != null) {
 * 	// do something with the results.
 * }
 *
 * if (parser.hasErrors()) {
 * 	System.err.println(parser.getErrorMessage());
 * }
 * </pre>
 *
 * @author Tim Becker
 */
public class CSVParser {

    private static final char ESC_CHARACTER = '\\';
    private static final char DELIMITER = ';';
    private static final String COMMENT = "#";

    private LineNumberReader reader;
    private char esc = ESC_CHARACTER;
    private char delim = DELIMITER;
    private String comment = COMMENT;
    private StringBuilder errorMessage = new StringBuilder();
    private boolean hasErrors;
    private int numFields = -1;
    private Pattern[] patterns;
    private String currentLine; // for error reporting


    /**
     * Construct a CSVParser form the specified Reader
     */
    public CSVParser(Reader reader) {
        this.reader = new LineNumberReader(reader);
    }


    /**
     * Construct a CSVParser form the specified Reader, providing
     * the number of fields which should be in each record, parsing
     * a line with an incorrect number of fields results in
     * an error.
     */

    public CSVParser(Reader reader, int numFields) {
        this(reader);
        setNumFields(numFields);
    }

    /**
     * Construct a CSVParser form the specified Reader
     */

    public CSVParser(InputStream is) {
        this(new InputStreamReader(is));
    }


    /**
     * Construct a CSVParser form the specified Stream, providing
     * the number of fields which should be in each record, parsing
     * a line with an incorrect number of fields results in
     * an error.
     */

    public CSVParser(InputStream is, int numFields) {
        this(is);
        setNumFields(numFields);
    }


    /**
     * Construct a CSVParser form a String containing the
     * csv data.
     */

    public CSVParser(String string) {
        this(new StringReader(string));
    }

    public CSVParser(String string, int numFields) {
        this(string);
        setNumFields(numFields);
    }


    /**
     * Pass a set of regular expressions that the fields in
     * the CSV are expected to match. One pattern per field.
     *
     * @param patterns
     */

    public void setChecks(String[] patterns) {
        this.patterns = new Pattern[patterns.length];
        for (int i = 0; i != patterns.length; ++i) {
            this.patterns[i] = Pattern.compile(patterns[i]);
        }
        setNumFields(patterns.length);
    }


    /**
     * Pass a set of regular expressions that the fields in
     * the CSV are expected to match. One pattern per field.
     *
     * @param patterns
     */

    public void setChecks(Pattern[] patterns) {
        this.patterns = patterns;
        setNumFields(patterns.length);
    }

    /**
     * Retrieve the next non-comment line from the csv in
     * form of a list of Strings, returning null if the end of
     * input was reached, in case an error was encountered, an
     * empty list is returned, errors can be examined using
     * `getErrorMessages`
     *
     * @return null if the end of input was reached.
     */
    public List<String> getNextLine() {
        List<String> list = null;
        try {

            String line;
            while ((line = reader.readLine()) != null) {
                // skip comments and blank lines.
                if (line.startsWith(this.comment) || line.trim().equals("")) {
                    // keep searching until we find a non-comment
                    continue;
                }
                break;
            }
            if (line == null) {
                // end of stream reached,
                return null;
            }
            // remember current line to include in error report
            this.currentLine = line;
            list = handleLine(line);


        } catch (Throwable t) {
            error("Technical Error: " + t.getMessage());
        }

        // if list is still null at this point, handleLine()
        // ran into an exception. Exceptions are reported through
        // getErrors, just return an empty line.

        return list == null ? new LinkedList<String>() : list;
    }

    /**
     * `getNextLine` calls this after reading a line from the
     * CSV to cut the line apart into tokens.
     *
     * @param line
     * @return a list of Strings, one for each CSV value
     * @throws IOException
     */
    private List<String> handleLine(String line) throws IOException {
        Reader r = new StringReader(line);
        StringBuilder buf = new StringBuilder(); // the current CSV value is collected in this list
        List<String> list = new LinkedList<String>(); // CSV values are collected in this list
        int i = -1;
        char ch;

        while ((i = r.read()) != -1) {
            ch = (char) i;
            if (ch == esc) {
                // escape character, grab the _next_ char
                i = r.read();
                if (i == -1) {
                    error("csv line ends in delimeter!");
                    list.add(buf.toString());
                    return list;
                } else {
                    buf.append((char) i);
                }
            } else if (ch == delim) {
                // reached the next delimiter, add the characters
                // collected up to now to the return list and reset the
                // StringBuilder
                list.add(buf.toString());
                buf = new StringBuilder();
            } else {
                buf.append(ch);
            }
        }
        list.add(buf.toString());

        if (this.patterns != null) {
            // if regular expressions were passed using `setChecks`
            // check that the tokens from the CSV match the regexp.
            check(list);
        }

        if (this.numFields != -1 && list.size() != this.numFields) {
            // if we're supposed to check that the proper number of
            // fields are available in the CSV, check that now.
            String err = String.format("incorrect number of fields, expected %d, was %d",
                    numFields,
                    list.size());
            error(err);
        }
        return list;
    }

    private void check(List<String> list) {
        if (this.patterns == null) {
            throw new RuntimeException("[CSVParser] `check(...)` called incorrectly!");
        }

        // make sure we have the same number of patterns/checks as we
        // have CSV fields.
        if (list == null || list.size() != this.patterns.length) {
            error("can't check patterns!");
        }
        for (int i = 0; i != list.size(); ++i) {
            if (!patterns[i].matcher(list.get(i)).matches()) {
                String err = String.format("incorrect format for field %d, value \"%s\" expected to match /%s/",
                        i + 1,
                        list.get(i),
                        this.patterns[i].toString());
                error(err);
            }
        }
    }

    /**
     * This method collects the errors encountered while parsing the csv.
     * The collected errors can be viewed as a report using the method:
     * `getErrorMessage()`
     *
     * @param string
     */
    private void error(String string) {

        hasErrors = true;
        String errorMsg = String.format("line %3d : %s\n\t%s\n",
                this.reader.getLineNumber(),
                string,
                this.currentLine);
        this.errorMessage.append(errorMsg);

    }

    /**
     * The character which seperates the fields of this csv, default is
     * the character ';'
     *
     * @param delim
     */
    public void setDelimeter(char delim) {
        this.delim = delim;
    }

    /**
     * Default escape character, any character following this one will
     * be passed through unmodified, except EOL/EOF. Default '\'
     */
    public void setEscape(char esc) {
        this.esc = esc;
    }

    /**
     * Retrieve the collected human readable error messages for this csv
     * The report contains one entry per error, each entry is formatted as
     * follows:
     * <p/>
     * '<lineno> : <error message>
     * '	the;offending;line;from;csv
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage.toString();
    }

    /**
     * Indicates whether errors were encountered processing this csv
     *
     * @return
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * set the number of fields this csv should have. If a line in the csv
     * has a different number of fields, it is reported as an error. Default
     * is -1, indicating it's irrelevant.
     *
     * @param numFields
     */
    public void setNumFields(int numFields) {
        this.numFields = numFields;
    }

    /**
     * Set a string as a comment delimiter, any line in the csv beginning
     * with the comment string will be skipped, default is "#"
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


}
