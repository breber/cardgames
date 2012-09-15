package com.worthwhilegames.cardgames.shared.wifi;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.IServerSocket;
import com.worthwhilegames.cardgames.shared.connection.ISocket;

/**
 * The Wifi implementation of a ServerSocket
 */
public class WifiServerSocket implements IServerSocket {

	/**
	 * The ServerSocket
	 */
	private java.net.ServerSocket mServerSocket;

	/**
	 * The Context
	 */
	private Context mContext;

	/**
	 * The JmDNS instance
	 */
	private JmDNS jmdns = null;

	/**
	 * The ServiceInfo we are broadcasting
	 */
	private ServiceInfo serviceInfo;

	/**
	 * The Wifi MulticastLock
	 */
	private MulticastLock lock;

	/**
	 * Create a new WifiServerSocket
	 */
	public WifiServerSocket(Context ctx) {
		mContext = ctx;

		WifiManager wifi = (WifiManager) ctx.getSystemService(android.content.Context.WIFI_SERVICE);
		lock = wifi.createMulticastLock("CardGamesLock");
		lock.setReferenceCounted(true);
		lock.acquire();

		try {
			mServerSocket = new java.net.ServerSocket(GameFactory.getPortNumber(ctx), 0, Util.getLocalIpAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#setup()
	 */
	@Override
	public void setup() {
		try {
			jmdns = JmDNS.create(Util.getLocalIpAddress());
			serviceInfo = ServiceInfo.create(WifiConstants.SERVICE_TYPE, GameFactory.getGameType(mContext) + ": " + android.os.Build.MODEL, GameFactory.getPortNumber(mContext), "Card Games for Android");
			jmdns.registerService(serviceInfo);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#accept()
	 */
	@Override
	public ISocket accept() throws IOException {
		if (mServerSocket != null) {
			return new WifiSocket(mServerSocket.accept());
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#close()
	 */
	@Override
	public void close() throws IOException {
		if (jmdns != null) {
			jmdns.unregisterAllServices();
			try {
				jmdns.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			jmdns = null;
		}

		if (lock.isHeld()) {
			lock.release();
		}

		if (mServerSocket != null) {
			mServerSocket.close();
		}
	}
}
