package cs309.a1.shared.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cs309.a1.shared.Util;

public class BluetoothServer extends BluetoothCommon {
	private static final String TAG = BluetoothServer.class.getName();
	private static BluetoothServer instance = null;

	private HashMap<String, BluetoothConnectionService> services;
	private BluetoothAdapter mAdapter;
	private List<BluetoothConnectionService> mList;
	private AcceptThread mAcceptThread;
	private Context mContext;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "handleMessage: " + msg.what);
				Toast.makeText(mContext, "handleMessage: " + msg.what, Toast.LENGTH_LONG).show();
			}
			Bundle data = msg.getData();
			switch (msg.what) {
			case BluetoothConstants.STATE_MESSAGE:
				// When the state of the Bluetooth connection has changed
				// let all the listeners know that this has happened
				Intent i = new Intent(BluetoothConstants.STATE_CHANGE_INTENT);
				i.putExtra(BluetoothConstants.STATE_MESSAGE_KEY, data.getString(BluetoothConstants.STATE_MESSAGE_KEY));
				i.putExtra(BluetoothConstants.DEVICE_ID_KEY, data.getString(BluetoothConstants.DEVICE_ID_KEY));
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

	private BluetoothServer(Context ctx) {
		mContext = ctx;
		services = new HashMap<String, BluetoothConnectionService>();
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mList = new ArrayList<BluetoothConnectionService>();
		mAcceptThread = new AcceptThread(mContext, mAdapter, mHandler, mList);
	}

	public static BluetoothServer getInstance(Context ctx) {
		if (instance == null) {
			instance = new BluetoothServer(ctx);
		}

		return instance;
	}

	/**
	 * Start listening for Bluetooth Connections
	 */
	public void startListening() {
		if (!mAcceptThread.isAlive()) {
			mAcceptThread.start();
		}
	}

	/**
	 * Gets the number of devices listed as "connected"
	 * 
	 * @return the number of connected devices
	 */
	public int getConnectedDeviceCount() {
		if (services.size() == 0) {
			return 0;
		}

		int countConnected = 0;

		for (BluetoothConnectionService service : services.values()) {
			if (service.getState() == BluetoothConstants.STATE_CONNECTED) {
				countConnected++;
			}
		}

		return countConnected;
	}

	/**
	 * Gets a list of Devices ids
	 * 
	 * @return a list of connected devices' ids
	 */
	public List<String> getConnectedDevices() {
		return new ArrayList<String>(services.keySet());
	}

	@Override
	public boolean write(Object obj, String ... address) {
		boolean retVal = true;

		if (address.length == 0) {
			for (BluetoothConnectionService service : services.values()) {
				if (!performWrite(service, obj)) {
					retVal = false;
				}
			}
		} else {
			for (String addr : address) {
				BluetoothConnectionService service = services.get(addr);

				if (service != null) {
					if (!performWrite(service, obj)) {
						retVal = false;
					}
				}
			}
		}

		return retVal;
	}

}
