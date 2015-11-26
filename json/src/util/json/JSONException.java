package util.json;

/**
 * Created by a2800276 on 2015-11-23.
 */
public class JSONException extends RuntimeException {
	public JSONException(String message) {
		super(message);
	}

	public JSONException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSONException(Throwable cause) {
		super(cause);
	}
}
