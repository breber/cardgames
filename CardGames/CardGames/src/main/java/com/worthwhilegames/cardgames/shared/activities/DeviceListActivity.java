package com.worthwhilegames.cardgames.shared.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

import com.worthwhilegames.cardgames.shared.AdActivity;
import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Util;

/**
 * This Activity lists all devices that are discoverable, or paired and
 * in range. The user can then select which device they want to connect
 * to, and the address of that device is returned in the result Intent.
 *
 * Activity Results:
 *         RESULT_OK - If the user chose a device
 *                     The Device's MAC address will be in the result
 *                     Intent with the key DeviceListActivity.EXTRA_DEVICE_ADDRESS
 *
 *         RESULT_CANCELLED - If no device was chosen
 */
public abstract class DeviceListActivity extends AdActivity {
    /**
     * The Logcat Debug tag
     */
    private static final String TAG = DeviceListActivity.class.getName();

    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "deviceAddress";

    /**
     * Return Intent extra
     */
    public static String EXTRA_PORT_NUMBER = "portNumber";

    /**
     * A list of Device names that are currently added to the DeviceListAdapter
     */
    protected List<String> deviceNames = new ArrayList<String>();

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
     * A Handler used to run things on the UI thread
     */
    private Handler handler = new Handler();

    /**
     * Represents whether we are cancelling the service
     */
    protected boolean isCancelling = false;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onPause() {
        cancelDiscovery();
        super.onPause();
    }

    /**
     * Cancel the search for services
     */
    protected void cancelDiscovery() {
        if (Util.isDebugBuild()) {
            Log.d(TAG, "cancelDiscovery()");
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    protected void doDiscovery() {
        if (Util.isDebugBuild()) {
            Log.d(TAG, "doDiscovery()");
        }

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

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, item.getDeviceMacAddress());
            intent.putExtra(EXTRA_PORT_NUMBER, item.getPortNumber());

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
    protected void updateUi(final DeviceListItem listItem, final String hostName) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If we have a host address, show it in the UI
                noDevicesFound.setVisibility(View.INVISIBLE);
                mDevicesArrayAdapter.add(listItem);
                deviceNames.add(hostName);
            }
        }, 1);
    }
}
