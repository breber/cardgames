package com.worthwhilegames.cardgames.shared.connection;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.JmDNSImpl;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.wifi.WifiConstants;

public class JmDnsWrapper implements IDnsWrapper {

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

    public JmDnsWrapper(Context context) {
        mContext = context;
        WifiManager wifi = (WifiManager) mContext.getSystemService(android.content.Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("CardGamesLock");
        lock.setReferenceCounted(true);
        lock.acquire();
    }

    public void setup() {
        try {
            jmdns = new JmDNSImpl(Util.getLocalIpAddress(), "CardGames");
            serviceInfo = ServiceInfo.create(WifiConstants.SERVICE_TYPE, GameFactory.getGameType(mContext) + ": " + android.os.Build.MODEL, GameFactory.getPortNumber(mContext), "Card Games for Android");
            jmdns.registerService(serviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void close() {
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
    }
}
