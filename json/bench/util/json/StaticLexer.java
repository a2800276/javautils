package util.json;


/**
 * One-to-One copy, except for "everything static"
 */
public class StaticLexer {
	/**
	 * You shouldn't ever need more than a single instance of Lexer,
	 * because the Lexer state is maintained in your implementation of CB.
	 * <p>
	 * So here's a prepared instance to avoid having to create garbage.
	 * <p>
	 * I'm not enforcing the use of a single instance because:
	 * <p>
	 * * who am I to judge whether you need more instances?
	 * * the way java handles static is fairly retarded for non-trivial cases
	 * * I think singletons are overrated.
	 *
	 * Visiting this again after some time leaves me unconvinced that
	 * that the `lex` methods shouldn't be static. So this may change in the
	 * future. Perhaps I will benchmark.
	 */

	//public static Lexer lexer = new Lexer();

	public static void lex(byte[] arr, Lexer.CB cb) {
		lex( arr, 0, arr.length, cb );
	}

	public static void lex(byte[] arr, int off, int len, Lexer.CB cb) {
		for (int i = off, end = off + len; i != end; ++i, ++cb.pos) {
			byte c = arr[i];
			//p((char)c + ":" + cb.state+" i:"+i+":"+len);
			switch (cb.state) {
				case VALUE:
					if (isWS(c)) {
						continue;
					}
					switch (c) {
						// String
						case '"':
							cb.state = Lexer.State.STRING_START;
							cb.cache = new StringBuilder();
							continue;

							// Number
						case '-':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
						case '0':
							cb.state = Lexer.State.NUMBER_START;
							cb.cache = new StringBuilder();
							cb.cache.append((char) c);
							continue;

							// Object
						case '{':
							cb.state = Lexer.State.VALUE;
							cb.tok( Lexer.Token.LCURLY);
							continue;

						case '}':
							cb.state = Lexer.State.AFTER_VALUE;
							cb.tok( Lexer.Token.RCURLY);
							continue;

							// Array
						case '[':
							cb.state = Lexer.State.VALUE;
							cb.tok( Lexer.Token.LSQUARE);
							continue;

						case ']':
							cb.state = Lexer.State.AFTER_VALUE;
							cb.tok( Lexer.Token.RSQUARE);
							continue;

							// true
						case 't':
							cb.state = Lexer.State.T;
							continue;

							// false
						case 'f':
							cb.state = Lexer.State.F;
							continue;

							// null
						case 'n':
							cb.state = Lexer.State.N;
							continue;

						default:
							error(cb, c);
					}

				case T:
					if ('r' == c) {
						cb.state = Lexer.State.TR;
						continue;
					}
					error(cb, c);
				case TR:
					if ('u' == c) {
						cb.state = Lexer.State.TRU;
						continue;
					}
					error(cb, c);
				case TRU:
					if ('e' == c) {
						cb.tok( Lexer.Token.TRUE);
						cb.state = Lexer.State.AFTER_VALUE;
						continue;
					}
					error(cb, c);

				case F:
					if ('a' == c) {
						cb.state = Lexer.State.FA;
						continue;
					}
					error(cb, c);
				case FA:
					if ('l' == c) {
						cb.state = Lexer.State.FAL;
						continue;
					}
					error(cb, c);
				case FAL:
					if ('s' == c) {
						cb.state = Lexer.State.FALS;
						continue;
					}
					error(cb, c);
				case FALS:
					if ('e' == c) {
						cb.tok( Lexer.Token.FALSE);
						cb.state = Lexer.State.AFTER_VALUE;
						continue;
					}
					error(cb, c);

				case N:
					if ('u' == c) {
						cb.state = Lexer.State.NU;
						continue;
					}
					error(cb, c);
				case NU:
					if ('l' == c) {
						cb.state = Lexer.State.NUL;
						continue;
					}
					error(cb, c);
				case NUL:
					if ('l' == c) {
						cb.tok( Lexer.Token.NULL);
						cb.state = Lexer.State.AFTER_VALUE;
						continue;
					}
					error(cb, c);

				case AFTER_VALUE:
					if (isWS(c)) {
						continue;
					}
					switch (c) {
						case '}':
							cb.tok( Lexer.Token.RCURLY);
							continue;
						case ']':
							cb.tok( Lexer.Token.RSQUARE);
							continue;
						case ',':
							cb.tok( Lexer.Token.COMMA);
							continue;
						case ':':
							cb.tok( Lexer.Token.COLON);
							continue;
						default:
							--i;
							--cb.pos;
							cb.state = Lexer.State.VALUE;
							continue;
					}

				case NUMBER_START:
					switch (c) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
						case 'e':
						case 'E':
						case '+':
						case '-':
						case '.':
							cb.cache.append((char) c);
							continue;
						default:
							cb.numberToken( cb.cache );
							--i;
							--cb.pos;
							cb.state = Lexer.State.AFTER_VALUE;
							continue;
					}

				case STRING_START:
					switch (c) {
						case '"':
							cb.tok(cb.cache.toString());
							cb.state = Lexer.State.AFTER_VALUE;
							continue;
						case '\\':
							cb.state = Lexer.State.STR_ESC;
							continue;
						default:
							if (Character.isISOControl(c)) {
								error(cb, c);
							}
							cb.cache.append((char) c);
							continue;
					}

				case STR_ESC:
					switch (c) {
						case '"':
						case '/':
						case '\\':
							cb.cache.append((char) c);
							break;
						case 'b':
							cb.cache.append('\b');
							break;
						case 'f':
							cb.cache.append('\f');
							break;
						case 'n':
							cb.cache.append('\r');
							break;
						case 'r':
							cb.cache.append('\r');
							break;
						case 't':
							cb.cache.append('\t');
							break;
						case 'u':
							cb.state = Lexer.State.HEX1;
							continue;
						default:
							error(cb, c);
					}
					cb.state = Lexer.State.STRING_START;
					continue;

				case HEX1:
					if (!isHex(c)) {
						error(cb, c);
					}
					cb.hexCache = new StringBuilder();
					cb.hexCache.append((char) c);
					cb.state = Lexer.State.HEX2;
					continue;
				case HEX2:
					if (!isHex(c)) {
						error(cb, c);
					}
					cb.hexCache.append((char) c);
					cb.state = Lexer.State.HEX3;
					continue;
				case HEX3:
					if (!isHex(c)) {
						error(cb, c);
					}
					cb.hexCache.append((char) c);
					cb.state = Lexer.State.HEX4;
					continue;
				case HEX4:
					if (!isHex(c)) {
						error(cb, c);
					}
					cb.hexCache.append((char) c);
					char u = toChar(cb.hexCache);
					cb.cache.append(u);
					cb.state = Lexer.State.STRING_START;
					continue;

				default:
					error(cb, c);
			} // state switch
		}// for
	}



	static boolean isWS(byte c) {
		return Character.isWhitespace(c);
	}

	static char toChar(CharSequence buf) {
		// this can't happen b/c of the way it's getting called.
		// therefore, no runtime check.
		assert buf.length() == 4;
		return (char) Integer.parseInt(buf.toString(), 16);
	}


	static boolean isHex(byte c) {
		switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
				return true;
			default:
				return false;
		}
	}

	static void error(Lexer.CB cb) {
		error("??? " + cb.state + " at pos: " + cb.pos);
	}

	static void error(Lexer.CB cb, byte c) {
		error("unexpected char: " + (char) c + "(" + c + ") in state: " + cb.state + " at pos:" + cb.pos);
	}

	static void error(String mes) {
		throw new JSONException(mes);
	}



}
