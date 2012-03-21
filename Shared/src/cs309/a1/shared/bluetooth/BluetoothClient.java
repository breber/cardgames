package cs309.a1.shared.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cs309.a1.shared.Util;

/**
 * This Singleton class acts as a Client for a Bluetooth connection.
 * 
 * It connects to another Bluetooth device (running the server), and then
 * communicates with it (by sending messages and receiving messages).
 */
public class BluetoothClient extends BluetoothCommon {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = BluetoothClient.class.getName();

	/**
	 * The Singleton instance of this class
	 */
	private static BluetoothClient instance = null;

	/**
	 * The BluetoothConnectionService associated with this client
	 */
	private BluetoothConnectionService mService;

	/**
	 * The BluetoothAdapter used to query Bluetooth information
	 */
	private BluetoothAdapter mBluetoothAdapter;

	/**
	 * The context of this thread
	 */
	private Context mContext;

	/**
	 * The MAC address of the device to connect to
	 */
	private String mMacAddress;

	/**
	 * The Handler to handle all messages coming from the BluetoothConnectionService
	 * 
	 * This should pretty much just pass the message on to the UI/GameLayer
	 */
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
				Intent i1 = new Intent(BluetoothConstants.MESSAGE_RX_INTENT);
				i1.putExtra(BluetoothConstants.MESSAGE_RX_KEY, data.getString(BluetoothConstants.MESSAGE_RX_KEY));
				i1.putExtra(BluetoothConstants.DEVICE_ID_KEY, data.getString(BluetoothConstants.DEVICE_ID_KEY));
				mContext.sendBroadcast(i1);
				break;
			}
		}
	};

	/**
	 * Create a new BluetoothClient
	 * 
	 * @param ctx
	 */
	private BluetoothClient(Context ctx) {
		mContext = ctx;
		mService = new BluetoothConnectionService(mContext, mHandler);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Gets the instance of the BluetoothClient
	 * 
	 * @param ctx the Context of this BluetoothClient
	 * @return the BluetoothClient
	 */
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
		mService.connect(device);
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
		return performWrite(mService, obj);
	}
}
