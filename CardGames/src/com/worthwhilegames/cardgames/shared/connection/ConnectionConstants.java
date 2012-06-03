package com.worthwhilegames.cardgames.shared.connection;

public class ConnectionConstants {

	/**
	 * The intent that is sent out when a device has been connected will use
	 * this string as its action.
	 */
	public static final String DEVICE_CONNECTED_INTENT = "cs309.a1.shared.connection.DEVICE_CONNECTED_INTENT";

	/**
	 * The intent that is sent out when a device has been connected will use
	 * this string as its action.
	 */
	public static final String MESSAGE_RX_INTENT = "cs309.a1.shared.connection.MESSAGE_RX_INTENT";

	/**
	 * The intent that is sent out when the state of a Bluetooth connection has been changed
	 * will use this string as its action.
	 */
	public static final String STATE_CHANGE_INTENT = "cs309.a1.shared.connection.STATE_CHANGE_INTENT";


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
	public static final String KEY_MSG_DATA = "DATA";

	/**
	 * The messageType that indicates that the game is ready to begin
	 */
	public static final int MSG_TYPE_INIT = Integer.MAX_VALUE;

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
