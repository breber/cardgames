package com.worthwhilegames.cardgames.shared.bluetooth;

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
	 * The UUID of the Bluetooth port that the server will be listening on and the client
	 * will be connecting from.
	 */
	public static final UUID MY_UUID = UUID.fromString("9d6b7fe4-d2cd-37f9-950b-0aad096c2d57");

	/**
	 *  Name for the SDP record when creating server socket
	 */
	public static final String SOCKET_NAME = "CardGames";

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
