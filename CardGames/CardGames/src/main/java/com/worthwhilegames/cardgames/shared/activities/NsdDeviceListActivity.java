package com.worthwhilegames.cardgames.shared.activities;

import java.net.InetAddress;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.wifi.WifiConstants;

public class NsdDeviceListActivity extends DeviceListActivity implements DiscoveryListener, ResolveListener {

    private static final String TAG = NsdDeviceListActivity.class.getName();

    /**
     * The JmDNS instance used to find services
     */
    private NsdManager mNsdManager = null;

    @Override
    protected void doDiscovery() {
        super.doDiscovery();

        // Create the JmDNS instance and start listening
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNsdManager = (NsdManager) NsdDeviceListActivity.this.getSystemService(Context.NSD_SERVICE);
                mNsdManager.discoverServices(WifiConstants.SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, NsdDeviceListActivity.this);
            }
        }).start();
    }

    @Override
    protected void cancelDiscovery() {
        super.cancelDiscovery();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (NsdDeviceListActivity.this) {
                    if (!isCancelling) {
                        isCancelling = true;

                        if (mNsdManager != null) {
                            mNsdManager.stopServiceDiscovery(NsdDeviceListActivity.this);
                        }
                    }
                }
            }
        }).start();
    }


    @Override
    public void onDiscoveryStarted(String arg0) {
        Log.d(TAG, "Service discovery started");
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.i(TAG, "Discovery stopped: " + serviceType);
    }

    @Override
    public void onServiceFound(NsdServiceInfo service) {
        // A service was found!  Do something with it.
        Log.d(TAG, "Service discovery success" + service);
        if (service.getServiceType().equals(WifiConstants.SERVICE_TYPE)) {
            mNsdManager.resolveService(service, this);
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo service) {
        // When the network service is no longer available.
        // Internal bookkeeping code goes here.
        Log.e(TAG, "service lost" + service);
    }

    @Override
    public void onStartDiscoveryFailed(String arg0, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        mNsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onStopDiscoveryFailed(String arg0, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        mNsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onResolveFailed(NsdServiceInfo arg0, int errorCode) {
        // Called when the resolve fails.  Use the error code to debug.
        Log.e(TAG, "Resolve failed" + errorCode);
    }

    @Override
    public void onServiceResolved(NsdServiceInfo serviceInfo) {
        Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

        int port = serviceInfo.getPort();
        InetAddress host = serviceInfo.getHost();

        if (Util.isDebugBuild()) {
            Log.d(TAG, "serviceResolved: " + deviceNames + " " + serviceInfo.getServiceName());
        }

        if (host != null) {
            DeviceListItem item = new DeviceListItem(host.getHostName(), host.getHostAddress(), port);

            updateUi(item, host.getHostName());
        }
    }

}
