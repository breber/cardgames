package cs309.a1.bluetoothtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

/**
 * This thread runs during a connection with a remote device.
 * It handles all incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread {
	/**
	 * The BluetoothSocket that messages are sent/received on
	 */
	private final StreamConnection mmSocket;

	/**
	 * The InputStream to read messages from
	 */
	private final InputStream mmInStream;

	/**
	 * The OutputStream to write messages to
	 */
	private final OutputStream mmOutStream;

	/**
	 * Create a new ConnectedThread with the given socket
	 * 
	 * @param socket the socket to use for this thread
	 */
	public ConnectedThread(StreamConnection socket) {
		mmSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;

		// Get the BluetoothSocket input and output streams
		try {
			tmpIn = socket.openInputStream();
			tmpOut = socket.openOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mmInStream = tmpIn;
		mmOutStream = tmpOut;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;

		// Keep listening to the InputStream while connected
		while (true) {
			try {
				// Read from the InputStream
				bytes = mmInStream.read(buffer);

				System.out.println("MSG: " + new String(buffer, 0, bytes));
			} catch (IOException e) {
				System.err.println("Disconnected...");

				break;
			}
		}
	}

	/**
	 * Write to the connected OutStream
	 * 
	 * @param buffer The bytes to write
	 */
	public void write(byte[] buffer) {
		try {
			mmOutStream.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cancel the current operation
	 */
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}