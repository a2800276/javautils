package cmdline;


import org.testng.annotations.Test;

import static java.util.Arrays.sort;
import static org.testng.Assert.*;

public class CmdLineTest {

    @Test
    public void testBasics() {
        String[] args = {"-a", "testa", "-b", "testb"};
        String[] flags = {"-a", "-b"};
        cmdline.CmdLine cl = new CmdLine(args);
        assertEquals("testa", cl.get("-a"));
        assertEquals("testb", cl.get("-b"));
        assertEquals(args, cl.getArgs());
        sort(flags);
        String[] ff = cl.getFlags();
        sort(ff);
        assertEquals(flags, ff);

        String[] args2 = {"-a", "testa", "-b", "testb", "bla"};
        String[] args2Arr = {"-a", "testa", "-b", "testb"};
        String[] rest = {"bla"};
        String[] ensure = {"-a", "-b"};
        String[] ensure2 = {"-a", "-b", "-c"};
        assertEquals(args2Arr, cl.getArgs());

        cl = new CmdLine(args2);
        assertEquals("testa", cl.get("-a"));
        assertEquals("testb", cl.get("-b"));
        assertEquals("testc", cl.get("-c", "testc"));
        assertTrue(cl.exists("-a"));
        assertTrue(cl.exists("-b"));
        assertFalse(cl.exists("-c"));
        assertTrue(cl.ensure(ensure));
        assertFalse(cl.ensure(ensure2));
        assertEquals(rest, cl.getRest());

        String[] args3 = {"-a", "testa", "-b", "testb", "-c", "23"};
        cl = new CmdLine(args3);
        assertEquals(23, cl.get("-c", -1));
        assertEquals(2, cl.get("-d", 2));

        String[] args4 = {"-a", "true", "run", "build.xml", "src/javacd/CProtocol.java", "src/javacd/JavaCDaemon.java", "src/javacd/Protocol.java", "src/javacd/Server.java"};
        String[] res4 = {"-a", "true"};
        String[] rest4 = {"run", "build.xml", "src/javacd/CProtocol.java", "src/javacd/JavaCDaemon.java", "src/javacd/Protocol.java", "src/javacd/Server.java"};

        cl = new CmdLine(args4);
        assertEquals(res4, cl.getArgs());
        assertEquals(rest4, cl.getRest());

    }


}
