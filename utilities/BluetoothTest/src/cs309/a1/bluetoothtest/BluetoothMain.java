package cs309.a1.bluetoothtest;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * A simple SPP client that connects with an SPP server
 */
public class BluetoothMain {

	public static void main(String[] args) throws IOException {
		// display local device address and name
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		System.out.println("Address: " + localDevice.getBluetoothAddress());
		System.out.println("Name: "+ localDevice.getFriendlyName());

		// Get the DiscoveryAgent that will search for devices
		DiscoveryAgent agent = localDevice.getDiscoveryAgent();

		// Try and get the URL of a device that is running a service with the same UUID as the CrazyEights game uses
		String connectionURL = agent.selectService(new UUID("9d6b7fe4d2cd37f9950b0aad096c2d57", false), ServiceRecord.AUTHENTICATE_NOENCRYPT, false);

		System.out.println("URL: " + connectionURL);

		// connect to the server
		StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);

		// Start the ConnectedThread. This is essentially the same thread that is running on the handheld/
		// It will listen for messages, and print them out in the console.
		new ConnectedThread(streamConnection).start();
	}

}
