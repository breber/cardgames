package cs309.a1.shared.bluetooth;

public abstract class BluetoothCommon {

	abstract boolean write(Object obj, String ... address);

	protected boolean performWrite(BluetoothConnectionService service, Object obj) {
		if (service.getState() != BluetoothConstants.STATE_CONNECTED) {
			// TODO: we probably want to try and reconnect before just saying
			// that we aren't connected...
			return false;
		}

		// TODO: make this actually useful
		service.write(obj.toString().getBytes());

		return true;
	}

}
