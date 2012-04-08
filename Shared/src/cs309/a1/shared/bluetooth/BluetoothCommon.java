package cs309.a1.shared.bluetooth;

import org.json.JSONException;
import org.json.JSONObject;

import cs309.a1.shared.connection.ConnectionConstants;

/**
 * Common methods for Bluetooth Client and Server
 */
public abstract class BluetoothCommon {

	/**
	 * Perform a write operation
	 * 
	 * @param service the BluetoothConnectionService to write to
	 * @param obj the object to write
	 * @return whether the message was written or not
	 */
	protected boolean performWrite(BluetoothConnectionService service, int messageType, Object obj) {
		// If we aren't connected, return false
		if (service.getState() != BluetoothConstants.STATE_CONNECTED) {
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
			if (obj != null) {
				json.put(ConnectionConstants.KEY_MSG_DATA, obj.toString());
			}
			json.put(ConnectionConstants.KEY_MESSAGE_TYPE, messageType);

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
