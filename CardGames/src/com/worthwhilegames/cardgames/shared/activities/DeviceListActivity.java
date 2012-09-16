package com.worthwhilegames.cardgames.shared.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.wifi.WifiConstants;

/**
 * This Activity lists all devices that are discoverable, or paired and
 * in range. The user can then select which device they want to connect
 * to, and the address of that device is returned in the result Intent.
 *
 * Activity Results:
 * 		RESULT_OK - If the user chose a device
 * 					The Device's MAC address will be in the result
 * 					Intent with the key DeviceListActivity.EXTRA_DEVICE_ADDRESS
 *
 * 		RESULT_CANCELLED - If no device was chosen
 */
public class DeviceListActivity extends Activity implements ServiceListener {
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = DeviceListActivity.class.getName();

	/**
	 * Return Intent extra
	 */
	public static String EXTRA_DEVICE_ADDRESS = "deviceAddress";

	/**
	 * A list of Device names that are currently added to the DeviceListAdapter
	 */
	private List<String> deviceNames = new ArrayList<String>();

	/**
	 * The TextView resource that contains the text "No Devices Found"
	 */
	private TextView noDevicesFound;

	/**
	 * The ProgressBar that indicates that we are currently searching for devices
	 */
	private ProgressBar deviceListProgress;

	/**
	 * The Button that allows the user to refresh the list of devices
	 */
	private ImageButton refreshDeviceListButton;

	/**
	 * The ArrayAdapter that is displayed in the ListView
	 */
	private ArrayAdapter<DeviceListItem> mDevicesArrayAdapter;

	/**
	 * The JmDNS instance used to find services
	 */
	private JmDNS jmdns = null;

	/**
	 * The Wifi Multicast Lock
	 */
	private MulticastLock lock;

	/**
	 * A Handler used to run things on the UI thread
	 */
	private Handler handler = new Handler();

	/**
	 * Represents whether we are cancelling the service
	 */
	private boolean isCancelling = false;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list);

		noDevicesFound = (TextView) findViewById(R.id.noDevicesFoundText);
		noDevicesFound.setText(R.string.scanning);

		deviceListProgress = (ProgressBar) findViewById(R.id.titleProgress);
		refreshDeviceListButton = (ImageButton) findViewById(R.id.titleRefreshButton);

		refreshDeviceListButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshDeviceListButton.setVisibility(View.INVISIBLE);
				deviceListProgress.setVisibility(View.VISIBLE);

				noDevicesFound.setVisibility(View.VISIBLE);
				noDevicesFound.setText(R.string.scanning);

				mDevicesArrayAdapter.clear();
				doDiscovery();
			}
		});

		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		mDevicesArrayAdapter = new ArrayAdapter<DeviceListItem>(this, R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView devicesListView = (ListView) findViewById(R.id.devices);
		devicesListView.setAdapter(mDevicesArrayAdapter);
		devicesListView.setOnItemClickListener(mDeviceClickListener);

		// Otherwise start discovering devices
		mDevicesArrayAdapter.clear();
		doDiscovery();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		cancelDiscovery();

		super.onDestroy();
	}

	/**
	 * Cancel the search for services
	 */
	private void cancelDiscovery() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "cancelDiscovery()");
		}

		synchronized (DeviceListActivity.this) {
			if (!isCancelling) {
				isCancelling = true;

				if (jmdns != null) {
					jmdns.removeServiceListener(WifiConstants.SERVICE_TYPE, this);
					try {
						jmdns.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					jmdns = null;
				}

				lock.release();
			}
		}
	}

	@Override
	public void serviceResolved(ServiceEvent ev) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "serviceResolved: " + deviceNames + " " + ev.getDNS().getHostName());
		}

		updateUi(ev);
	}

	@Override
	public void serviceRemoved(ServiceEvent ev) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "Service removed: " + ev.getName());
		}
	}

	@Override
	public void serviceAdded(ServiceEvent event) {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "serviceAdded");
		}

		// Required to force serviceResolved to be called again (after the first search)
		jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		if (Util.isDebugBuild()) {
			Log.d(TAG, "doDiscovery()");
		}

		// Create a Wifi Multicast Lock
		WifiManager wifi = (WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
		lock = wifi.createMulticastLock("CardGamesLock");
		lock.setReferenceCounted(true);
		lock.acquire();

		// Create the JmDNS instance and start listening
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					jmdns = JmDNS.create(Util.getLocalIpAddress());
					jmdns.addServiceListener(WifiConstants.SERVICE_TYPE, DeviceListActivity.this);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}).start();

		refreshDeviceListButton.setVisibility(View.INVISIBLE);
		deviceListProgress.setVisibility(View.VISIBLE);
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * The on-click listener for all devices in the ListViews
	 */
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					cancelDiscovery();
				}
			}).start();

			DeviceListItem item = mDevicesArrayAdapter.getItem(arg2);
			String address = item.getDeviceMacAddress();

			// Create the result Intent and include the MAC address
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			// Set result and finish this Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};

	/**
	 * Update the UI based on the given event
	 * 
	 * @param event the ServiceEvent
	 */
	private void updateUi(final ServiceEvent event) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// If we have a host address, show it in the UI
				if (event.getInfo().getHostAddresses().length > 0) {
					noDevicesFound.setVisibility(View.INVISIBLE);
					mDevicesArrayAdapter.add(new DeviceListItem(event.getName(), event.getInfo().getHostAddresses()[0]));
					deviceNames.add(event.getDNS().getHostName());
				}
			}
		}, 1);
	}
}

