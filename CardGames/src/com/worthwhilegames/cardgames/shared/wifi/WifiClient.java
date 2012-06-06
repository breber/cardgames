package com.worthwhilegames.cardgames.shared.wifi;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;
import com.worthwhilegames.cardgames.shared.connection.ConnectionCommon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This Singleton class acts as a Client for a Bluetooth connection.
 * 
 * It connects to another Bluetooth device (running the server), and then
 * communicates with it (by sending messages and receiving messages).
 */
public class WifiClient extends ConnectionCommon implements ConnectionClient {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = WifiClient.class.getName();

	/**
	 * The Singleton instance of this class
	 */
	private static WifiClient instance = null;

	/**
	 * The BluetoothConnectionService associated with this client
	 */
	private WifiConnectionService mService;

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
			case WifiConstants.STATE_MESSAGE:
				// When the state of the Bluetooth connection has changed
				// let all the listeners know that this has happened
				Intent i = new Intent(ConnectionConstants.STATE_CHANGE_INTENT);
				i.putExtra(ConnectionConstants.KEY_STATE_MESSAGE, data.getInt(ConnectionConstants.KEY_STATE_MESSAGE));
				mContext.sendBroadcast(i);
				break;
			case WifiConstants.READ_MESSAGE:
				Intent i1 = new Intent(ConnectionConstants.MESSAGE_RX_INTENT);
				i1.putExtra(ConnectionConstants.KEY_MESSAGE_TYPE, data.getInt(ConnectionConstants.KEY_MESSAGE_TYPE));
				i1.putExtra(ConnectionConstants.KEY_MESSAGE_RX, data.getString(ConnectionConstants.KEY_MESSAGE_RX));
				i1.putExtra(ConnectionConstants.KEY_DEVICE_ID, data.getString(ConnectionConstants.KEY_DEVICE_ID));
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
	private WifiClient(Context ctx) {
		mContext = ctx;
		mService = new WifiConnectionService(mContext, mHandler);
	}

	/**
	 * Gets the instance of the BluetoothClient
	 * 
	 * @param ctx the Context of this BluetoothClient
	 * @return the BluetoothClient
	 */
	public static WifiClient getInstance(Context ctx) {
		if (instance == null) {
			instance = new WifiClient(ctx);
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

	/* (non-Javadoc)
	 * @see cs309.a1.shared.connection.ConnectionClient#connect(java.lang.String)
	 */
	@Override
	public void connect(final String macAddress) {
		setMacAddress(macAddress);

		mService.connect(macAddress);
	}

	/* (non-Javadoc)
	 * @see cs309.a1.shared.connection.ConnectionClient#disconnect()
	 */
	@Override
	public void disconnect() {
		mService.stop();
	}

	/**
	 * If this Bluetooth device is connected, send the given object to the server
	 * 
	 * @param messageType - the type of message this is
	 * @param obj - String to send
	 * @param address - doesn't matter for the client
	 * @return whether the message was sent or not
	 */
	@Override
	public boolean write(int messageType, Object obj, String ... address) {
		return performWrite(mService, messageType, obj);
	}
}
