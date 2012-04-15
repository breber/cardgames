package cs309.a1.shared.wifi;


/**
 * A class of constants that should be used when interacting
 * with the Bluetooth module.
 */
public class WifiConstants {
	/**
	 * Generic Bluetooth Logcat tag
	 */
	public static final String TAG = WifiConstants.class.getName();

	/**
	 *  Name for the SDP record when creating server socket
	 */
	public static final String SOCKET_NAME = "CardGames";

	/**
	 * The port number to use
	 */
	public static final int PORT_NUMBER = 1234;

	/**
	 * The constant used internally from the BluetoothConnectionService
	 * to the BluetoothClient/BluetoothServer indicating that we read a message
	 * from the Bluetooth port.
	 */
	protected static final int READ_MESSAGE  = 0;

	/**
	 * THe constant used internally from the BluetoothConnectionService
	 * to the BluetoothClient/BluetoothServer indicating that the state of the
	 * Bluetooth connection has changed.
	 */
	protected static final int STATE_MESSAGE = 1;

}
