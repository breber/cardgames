package com.worthwhilegames.cardgames.shared.wifi;

import java.io.IOException;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.connection.IServerSocket;
import com.worthwhilegames.cardgames.shared.connection.ISocket;

/**
 * The Wifi implementation of a ServerSocket
 */
public class WifiServerSocket implements IServerSocket {

	private java.net.ServerSocket mServerSocket;

	/**
	 * Create a new WifiServerSocket
	 */
	public WifiServerSocket() {
		try {
			mServerSocket = new java.net.ServerSocket(WifiConstants.PORT_NUMBER, 0, Util.getLocalIpAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#accept()
	 */
	@Override
	public ISocket accept() throws IOException {
		return new WifiSocket(mServerSocket.accept());
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.IServerSocket#close()
	 */
	@Override
	public void close() throws IOException {
		mServerSocket.close();
	}
}
