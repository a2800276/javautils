package util.json;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CustomEncoderTest {
	static final String somethingJson = "{'something' : 'yeah, baby!'}";

	static void p(Object o) {
		System.out.println( o );
	}

	@Test
	public void testCustom() {
		CustomEncoder enc = new CustomEncoder();
		enc.addEncoder( Something.class, new SomethingEncoder() );


		String res = JSON.jsonifyCustom( new Something(), enc );
		assertEquals( res, somethingJson );
	}

	@Test
	public void testCustomConstructed() {
		Object[] obj = {"one", "two", new Something()};
		CustomEncoder enc = new CustomEncoder();
		enc.addEncoder( Something.class, new SomethingEncoder() );

		String res = JSON.jsonifyCustom( obj, enc );
		assertEquals( res, "[\"one\",\"two\",{'something' : 'yeah, baby!'}]" );
	}

	@Test
	public void testCustomConstructedMultEnc() {
		Object[] obj = {"one", "two", new Something(), new SomethingElse()};
		CustomEncoder enc = new CustomEncoder();
		enc.addEncoder( Something.class, new SomethingEncoder() );
		enc.addEncoder( SomethingElse.class, new SmThngElseEnc() );

		String res = JSON.jsonifyCustom( obj, enc );
		assertEquals( res, "[\"one\",\"two\",{'something' : 'yeah, baby!'},'somethingElse']" );

	}

	static class Something {

	}

	static class SomethingElse {
	}

	static class SomethingEncoder implements CustomEncoder.Encoder {
		public void encode(StringBuilder b, Object s) {
			b.append( somethingJson );
		}
	}

	static class SmThngElseEnc implements CustomEncoder.Encoder {
		public void encode(StringBuilder b, Object o) {
			b.append( "'somethingElse'" );
		}
	}
}
