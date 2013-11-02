package com.worthwhilegames.cardgames.shared.activities;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.wifi.WifiConstants;

public class JmDnsDeviceListActivity extends DeviceListActivity implements ServiceListener {

    private static final String TAG = JmDnsDeviceListActivity.class.getName();

    /**
     * The JmDNS instance used to find services
     */
    private JmDNS jmdns = null;

    /**
     * The Wifi Multicast Lock
     */
    private MulticastLock lock;

    @Override
    protected void doDiscovery() {
        super.doDiscovery();

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
                    jmdns.addServiceListener(WifiConstants.JMDNS_SERVICE_TYPE, JmDnsDeviceListActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    @Override
    protected void cancelDiscovery() {
        super.cancelDiscovery();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (JmDnsDeviceListActivity.this) {
                    if (!isCancelling) {
                        isCancelling = true;

                        if (jmdns != null) {
                            jmdns.removeServiceListener(WifiConstants.JMDNS_SERVICE_TYPE, JmDnsDeviceListActivity.this);
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
        }).start();
    }

    @Override
    public void serviceResolved(ServiceEvent ev) {
        if (Util.isDebugBuild()) {
            Log.d(TAG, "serviceResolved: " + deviceNames + " " + ev.getDNS().getHostName());
        }

        if (ev.getInfo().getHostAddresses().length > 0) {
            DeviceListItem item = new DeviceListItem(ev.getName(), ev.getInfo().getHostAddresses()[0], ev.getInfo().getPort());

            updateUi(item, ev.getDNS().getHostName());
        }
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

}
