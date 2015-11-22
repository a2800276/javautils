package de.kuriositaet.utils.ioutils;

import org.junit.Test;

import static org.junit.Assert.*;

public class IOUtilsTest  {
  
  static void p (Object o) {
    System.out.println(o);
  }

  @Test public void readFileTest () {
    final String FILENAME = "test/specimen/bla";
    final String CONTENTS = "This is a Test\n";
    IOUtils.Res result = IOUtils.readFile(FILENAME);
    if (result.error) p(result.message);
    assertFalse("error",result.error);
    assertEquals("wrong stuff read", CONTENTS, result.contents);
    assertNull("error messgae", result.message);
  }
  @Test public void readNonExistantFileTest () {
    final String FILENAME = "test/specimens/bla.doesnt.live.here";
    IOUtils.Res result = IOUtils.readFile(FILENAME);
    assertTrue(result.error);
    assertNull(result.contents);
    assertEquals("file not found: test/specimens/bla.doesnt.live.here", result.message);
  }
  @Test public void relativeFNTest () {
    assertEquals("../test", IOUtils.filenameRelativeTo("test", "../bbbbb"));
    assertEquals("/test", IOUtils.filenameRelativeTo("/test", "../bbbbb"));

    assertEquals("/bla/bla/test", IOUtils.filenameRelativeTo("test", "/bla/bla/bbbbb"));
    assertEquals("/bla/bla/../test", IOUtils.filenameRelativeTo("../test", "/bla/bla/bbbbb"));
  }
}
