package com.worthwhilegames.cardgames.shared.wifi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.worthwhilegames.cardgames.shared.connection.ISocket;

/**
 * The Wifi implementation of a Socket
 */
public class WifiSocket implements ISocket {

	private Socket mSocket;

	/**
	 * Create a new WifiSocket that wraps the given Socket
	 * 
	 * @param socket the Socket to wrap
	 */
	public WifiSocket(Socket socket) {
		mSocket = socket;
	}

	/**
	 * Create a new WifiSocket to the given address
	 * 
	 * @param address the address to connect to
	 */
	public WifiSocket(String address) {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(address);
			mSocket = new Socket(addr, WifiConstants.PORT_NUMBER);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#close()
	 */
	@Override
	public void close() throws IOException {
		if (mSocket != null) {
			mSocket.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		if (mSocket != null) {
			return mSocket.getInputStream();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		if (mSocket != null) {
			return mSocket.getOutputStream();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#shutdownOutput()
	 */
	@Override
	public void shutdownOutput() throws IOException {
		if (mSocket != null) {
			mSocket.shutdownOutput();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#shutdownInput()
	 */
	@Override
	public void shutdownInput() throws IOException {
		if (mSocket != null) {
			mSocket.shutdownInput();
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.connection.ISocket#connect()
	 */
	@Override
	public void connect() throws IOException {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (mSocket != null) {
			return mSocket.getInetAddress().getHostAddress();
		} else {
			return "";
		}
	}

	/**
	 * Get the Socket this object is wrapping
	 * 
	 * @return the actual Socket we are wrapping
	 */
	public Socket getSocket() {
		return mSocket;
	}
}
