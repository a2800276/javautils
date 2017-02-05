package util.json;

import java.util.*;

/**
 * Created by a2800276 on 2017-02-05.
 */
public class RandomObject {
	final Random rnd;

	public RandomObject() {
		this(0);
	}
	public RandomObject (long seed) {
		this.rnd = new Random(seed);
	}
	int between (int min, int max) {
		int n = max - min;
		return rnd.nextInt( n ) + min;
	}
	Object randomObject (int depth) {

		// equals prob map, list, string, numbe, float
		// unless depth is 0, in that case no map or list
		int objType = rnd.nextInt( depth <=0 ? 3 : 5  );
		switch (objType) {
			case 0:
				return randomString (between(10, 200));
			case 1: // int
				return Integer.toString(rnd.nextInt(  ), (10));
			case 2: // float
				return Double.toString( rnd.nextDouble() );
			case 3:
				return randomMap( between(4,10), depth );
			case 4:
				return randomList( between(5,12), depth );
			default: throw new RuntimeException( "cannot happen." );
		}
	}

	Map randomMap (int numKeys, int depth) {

		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i!= numKeys; ++i) {
			String key = randomString(8);
			Object val = randomObject( depth -1 );
			map.put(key, val);
		}
		return map;
	}
	List randomList (int n, int depth) {
		List<Object> list = new LinkedList<>();
		for (int i=0; i!=n; ++i) {
			list.add( randomObject(depth -1) );
		}
		return list;
	}

	char [] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0', ' '};

	char randomChar() {
		int i = rnd.nextInt( chars.length );
		return chars[i];
	}

	String randomString(int n) {
		StringBuilder builder = new StringBuilder(  );
		for (int i = 0; i!=  n; ++i) {
			builder.append( randomChar() );
		}
		return builder.toString();
	}

	public static void main (String [] args) {
		RandomObject obj = new RandomObject(  );
		Map map = obj.randomMap( 10, 5 );
		p(map);
		p(JSON.jsonify( map ).length());
	}

	static void p (Object o) {
		System.out.println(o);
	}
}
