package cs309.a1.shared.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cs309.a1.shared.Util;

public class BluetoothClient extends BluetoothCommon {
	private static final String TAG = BluetoothClient.class.getName();

	private static BluetoothClient instance = null;

	private BluetoothConnectionService service;
	private BluetoothAdapter mBluetoothAdapter;
	private Context mContext;
	private String mMacAddress;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "handleMessage: " + msg.what);
			}
			Bundle data = msg.getData();
			switch (msg.what) {
			case BluetoothConstants.STATE_MESSAGE:
				// When the state of the Bluetooth connection has changed
				// let all the listeners know that this has happened
				Intent i = new Intent(BluetoothConstants.STATE_CHANGE_INTENT);
				i.putExtra(BluetoothConstants.STATE_MESSAGE_KEY, data.getInt(BluetoothConstants.STATE_MESSAGE_KEY));
				mContext.sendBroadcast(i);
				break;
			case BluetoothConstants.READ_MESSAGE:
				// TODO: send a broadcast
				break;
			case BluetoothConstants.TOAST_MESSAGE:
				Toast.makeText(mContext, msg.getData().getString(BluetoothConstants.TOAST_MESSAGE_KEY), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private BluetoothClient(Context ctx) {
		mContext = ctx;
		service = new BluetoothConnectionService(mContext, mHandler);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public static BluetoothClient getInstance(Context ctx) {
		if (instance == null) {
			instance = new BluetoothClient(ctx);
		} else {
			instance.mContext = ctx;
		}

		return instance;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return mMacAddress;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.mMacAddress = macAddress;
	}

	/**
	 * Connect to the device given by the macAddress
	 * 
	 * This method only starts trying to connect. It would be beneficial
	 * to create a BroadcastReceiver that listens on the BluetoothConstants.STATE_CHANGE_INTENT
	 * in order to figure out whether the connection was actually made.
	 * 
	 * @param macAddress
	 */
	public void connect(String macAddress) {
		setMacAddress(macAddress);

		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
		service.connect(device);
	}

	/**
	 * If this Bluetooth device is connected, send the given object to the server
	 * 
	 * @param obj - Object to send
	 * @param address - doesn't matter for the client
	 * @return whether the message was sent or not
	 */
	@Override
	public boolean write(Object obj, String ... address) {
		return performWrite(service, obj);
	}

}
