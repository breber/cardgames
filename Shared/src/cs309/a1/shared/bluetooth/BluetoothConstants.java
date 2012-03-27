package cs309.a1.shared.bluetooth;

import java.util.UUID;

/**
 * A class of constants that should be used when interacting
 * with the Bluetooth module.
 */
public class BluetoothConstants {
	/**
	 * Generic Bluetooth Logcat tag
	 */
	public static final String TAG = BluetoothConstants.class.getName();

	/**
	 * The intent that is sent out when a device has been connected will use
	 * this string as its action.
	 */
	public static final String DEVICE_CONNECTED_INTENT = "cs309.a1.shared.bluetooth.DEVICE_CONNECTED_INTENT";

	/**
	 * The intent that is sent out when a device has been connected will use
	 * this string as its action.
	 */
	public static final String MESSAGE_RX_INTENT = "cs309.a1.shared.bluetooth.MESSAGE_RX_INTENT";

	/**
	 * The intent that is sent out when the state of a Bluetooth connection has been changed
	 * will use this string as its action.
	 */
	public static final String STATE_CHANGE_INTENT = "cs309.a1.shared.bluetooth.STATE_CHANGE_INTENT";

	/**
	 * The UUID of the Bluetooth port that the server will be listening on and the client
	 * will be connecting from.
	 */
	public static final UUID MY_UUID = UUID.fromString("9d6b7fe4-d2cd-37f9-950b-0aad096c2d57");

	/**
	 *  Name for the SDP record when creating server socket
	 */
	public static final String SOCKET_NAME = "CrazyEights";

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

	/**
	 * The messageType that indicates that the game is ready to begin
	 */
	public static final int MSG_TYPE_INIT = Integer.MAX_VALUE;


	/**
	 * The key in Handler messages/Intent data that indicates
	 * which device the corresponding message is from/to.
	 */
	public static final String KEY_DEVICE_ID = "DEVICE_ID";

	/**
	 * The key in Handler messages/Intent data that indicates
	 * what the current state of the Bluetooth connection is.
	 */
	public static final String KEY_STATE_MESSAGE = "STATE";

	/**
	 * The key in Handler messages/Intent data that corresponds
	 * to the message that was received/sent.
	 */
	public static final String KEY_MESSAGE_RX = "MSG";

	/**
	 * The key in Handler messages/Intent data that indicates
	 * what type of message this is.
	 */
	public static final String KEY_MESSAGE_TYPE = "MSG_TYPE";

	/**
	 * The data key for message send/receive - protected
	 * because only Bluetooth needs to know about it
	 */
	protected static final String KEY_MSG_DATA = "DATA";

	/**
	 * Indicates that the BluetoothConnectionService is inactive
	 */
	public static final int STATE_NONE = 0;

	/**
	 * Indicates that the BluetoothConnectionService is listening for connections
	 */
	public static final int STATE_LISTEN = 1;

	/**
	 * Indicates that the BluetoothConnectionService is trying to connect to a device
	 */
	public static final int STATE_CONNECTING = 2;

	/**
	 * Indicates that the BluetoothConnectionService is currently connected to a device
	 */
	public static final int STATE_CONNECTED = 3;
}
