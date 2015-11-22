package util.flags;


import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.*;

public class CmdLineTest {

    @Test
    public void testBasics() {
        String[] args = {"-a", "testa", "-b", "testb"};
        String[] flags = {"-a", "-b"};
        CmdLine cl = new CmdLine(args);
        assertEquals("testa", cl.get("-a"));
        assertEquals("testb", cl.get("-b"));
        assertEquals(args, cl.getArgs());

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


        String[] args4 = {"-a", "true", "run", "build.xml", "src/javacd/CProtocol.java", "src/javacd/JavaCDaemon.java", "src/javacd/Protocol.java", "src/javacd/Server.java"};
        String[] res4 = {"-a", "true"};
        String[] rest4 = {"run", "build.xml", "src/javacd/CProtocol.java", "src/javacd/JavaCDaemon.java", "src/javacd/Protocol.java", "src/javacd/Server.java"};

        cl = new CmdLine(args4);
        assertEquals(res4, cl.getArgs());
        assertEquals(rest4, cl.getRest());

    }

    @Test
    public void testGetRest() {
        String[] f = {"-fn", "bla.text", "-func", "read_all", "one", "two", "three"};
        CmdLine cl = new CmdLine(f);
        String[] rest = cl.getRest();
        String[] expectedRest = {"one", "two", "three"};
        assertEquals(rest, expectedRest);

    }

    @Test
    public void testGetArgs() {
        String[] f = {"-fn", "bla.text", "-func", "read_all", "one", "two", "three"};
        CmdLine cl = new CmdLine(f);
        String[] args = cl.getArgs();
        String[] expected = {"-fn", "bla.text", "-func", "read_all"};
        assertEquals(args, expected);
    }

    @Test
    public void testEnsure() {
        String[] f = {"-fn", "bla.text", "-func", "read_all", "one", "two", "three"};
        CmdLine cl = new CmdLine(f);
        assertTrue(cl.ensure("-fn"));
        assertTrue(cl.ensure("-fn", "-func"));
        assertFalse(cl.ensure("-fn", "-func", "-dingdong"));
    }

    @Test
    public void testExists() {
        String[] f = {"-fn", "bla.text", "-func", "read_all", "one", "two", "three"};
        CmdLine cl = new CmdLine(f);
        assertTrue(cl.exists("-fn"));
        assertFalse(cl.exists("-dingdong"));
    }

    @Test
    public void testGetFlags() {
        String[] f = {"-fn", "bla.text", "-func", "read_all", "one", "two", "three"};
        CmdLine cl = new CmdLine(f);
        Set s = cl.getFlags();
        assertTrue(s.contains("-fn"));
        assertTrue(s.contains("-func"));
        assertFalse(s.contains("-dingdong"));
    }

    @Test
    public void testGetInt() {
        String[] args3 = {"-a", "testa", "-b", "testb", "-c", "23"};
        CmdLine cl = new CmdLine(args3);
        assertEquals(23, cl.get("-c", -1));
        assertEquals(2, cl.get("-d", 2));
    }

    @Test
    public void testNullBehaviour() {
        CmdLine cl = new CmdLine(null);
        String[] args = {"-a", "testa", "-b", "testb", "-c", "23"};
        cl = new CmdLine(args);
        cl.get(null);
        cl.get("");
        args = new String[0];
        cl = new CmdLine(args);
    }


}
