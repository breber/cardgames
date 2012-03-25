package cs309.a1.shared.bluetooth;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Common methods for Bluetooth Client and Server
 */
public abstract class BluetoothCommon {

	/**
	 * Write an object to the given addresses
	 * 
	 * @param messageType the type of message this is
	 * @param obj the object to write
	 * @param address the addresses to write it to
	 * @return whether the message was written or not
	 */
	abstract boolean write(int messageType, Object obj, String ... address);

	/**
	 * Perform a write operation
	 * 
	 * @param service the BluetoothConnectionService to write to
	 * @param obj the object to write
	 * @return whether the message was written or not
	 */
	protected boolean performWrite(BluetoothConnectionService service, int messageType, Object obj) {
		if (service.getState() != BluetoothConstants.STATE_CONNECTED) {
			// TODO: we probably want to try and reconnect before just saying
			// that we aren't connected...
			return false;
		}

		try {
			// Wrap the data into a JSON object with a messageType
			//
			// {
			// 		"MSG_TYPE"	: messageType,
			//		"DATA"		: obj.toString()
			// }

			JSONObject json = new JSONObject();
			json.put(BluetoothConstants.KEY_MSG_DATA, obj.toString());
			json.put(BluetoothConstants.KEY_MESSAGE_TYPE, messageType);

			service.write(json.toString().getBytes());
		} catch (JSONException e) {
			// If we encounter a JSON error, just send the message without the
			// message type parameter
			e.printStackTrace();

			service.write(obj.toString().getBytes());
		}

		return true;
	}

}
