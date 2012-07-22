package android.util;

/**
 * Stub class so that tests still work in Debug Build
 * where we need a Logger class.
 */
public class Log {

	public static int d(String tag, String message) {
		// Do nothing
		return 0;
	}

}
