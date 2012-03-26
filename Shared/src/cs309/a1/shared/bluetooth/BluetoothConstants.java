package cs309.a1.shared.bluetooth;

import java.util.UUID;

public class BluetoothConstants {

	public static final String TAG = BluetoothConstants.class.getName();

	public static final String DEVICE_CONNECTED_INTENT = "cs309.a1.shared.bluetooth.DEVICE_CONNECTED_CHANGE";
	public static final String MESSAGE_RX_INTENT = "cs309.a1.shared.bluetooth.MESSAGE_RX_CHANGE";
	public static final String STATE_CHANGE_INTENT = "cs309.a1.shared.bluetooth.STATE_CHANGE";
	public static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

	// Name for the SDP record when creating server socket
	public static final String SOCKET_NAME = "BluetoothChat";


	public static final int READ_MESSAGE  = 0;
	public static final int WRITE_MESSAGE = 1;
	public static final int STATE_MESSAGE = 2;

	// The message
	public static final int MSG_TYPE_INIT = Integer.MAX_VALUE;


	// The keys for the "map" that is Intent messages/Handler messages
	public static final String KEY_DEVICE_ID = "DEVICE_ID";
	public static final String KEY_TOAST_MESSAGE = "TOAST_MESSAGE";
	public static final String KEY_STATE_MESSAGE = "STATE";
	public static final String KEY_MESSAGE_RX = "MSG";
	public static final String KEY_MESSAGE_TYPE = "MSG_TYPE";

	// The data key for message send/receive - protected
	// because only Bluetooth needs to know about it
	protected static final String KEY_MSG_DATA = "DATA";

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device
}
