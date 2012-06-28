package com.worthwhilegames.cardgames.shared.connection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Util;

/**
 * A Client connection. This will manage a connection of
 * some sort with the server. The user will be able to connect,
 * disconnect, send and receive messages through this interface.
 */
public class ConnectionClient extends ConnectionCommon {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = ConnectionClient.class.getName();

	/**
	 * The Singleton instance of this class
	 */
	private static ConnectionClient instance = null;

	/**
	 * The context of this thread
	 */
	protected static Context mContext;

	/**
	 * The address of the device to connect to
	 */
	protected String mAddress;

	/**
	 * The IConnectionService associated with this client
	 */
	protected ConnectionService mService;

	/**
	 * The Handler to handle all messages coming from the BluetoothConnectionService
	 * 
	 * This should pretty much just pass the message on to the UI/GameLayer
	 */
	protected static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (Util.isDebugBuild()) {
				Log.d(TAG, "handleMessage: " + msg.what);
			}

			Bundle data = msg.getData();
			switch (msg.what) {
			case ConnectionConstants.STATE_MESSAGE:
				// When the state of the Bluetooth connection has changed
				// let all the listeners know that this has happened
				Intent i = new Intent(ConnectionConstants.STATE_CHANGE_INTENT);
				i.putExtra(ConnectionConstants.KEY_STATE_MESSAGE, data.getInt(ConnectionConstants.KEY_STATE_MESSAGE));
				mContext.sendBroadcast(i);
				break;
			case ConnectionConstants.READ_MESSAGE:
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
	 * Create a new ConnectionClient
	 * @param ctx
	 */
	protected ConnectionClient(Context ctx) {
		mContext = ctx;
		mService = new ConnectionService(mContext, mHandler);
	}

	/**
	 * Gets the instance of the ConnectionClient
	 * 
	 * @param ctx the Context of this ConnectionClient
	 * @return the ConnectionClient
	 */
	public static ConnectionClient getInstance(Context ctx) {
		if (instance == null) {
			instance = new ConnectionClient(ctx);
		} else {
			ConnectionClient.mContext = ctx;
		}

		return instance;
	}

	/**
	 * Connect to the device given by the macAddress
	 * 
	 * This method only starts trying to connect. It would be beneficial
	 * to create a BroadcastReceiver that listens on the ConnectionConstants.STATE_CHANGE_INTENT
	 * in order to figure out whether the connection was actually made.
	 * 
	 * @param macAddress
	 */
	public void connect(String macAddress) {
		setAddress(macAddress);

		mService.connect(macAddress);
	}

	/**
	 * Terminate the connection with the server
	 */
	public void disconnect() {
		mService.stop();
	}

	/**
	 * Write an object to the given addresses
	 * 
	 * @param messageType the type of message this is
	 * @param obj the object to write
	 * @param address the addresses to write it to
	 * @return whether the message was written or not
	 */
	public boolean write(int messageType, Object obj, String ... address) {
		return performWrite(mService, messageType, obj);
	}

	/**
	 * @return the macAddress
	 */
	public String getAddress() {
		return mAddress;
	}

	/**
	 * @param address the macAddress to set
	 */
	public void setAddress(String address) {
		this.mAddress = address;
	}
}
