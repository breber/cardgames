package com.worthwhilegames.cardgames.shared.connection;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.wifi.WifiConstants;

@TargetApi(16)
public class NsdWrapper implements IDnsWrapper, RegistrationListener {

    private static final String TAG = NsdWrapper.class.getName();

    /**
     * The Context
     */
    private Context mContext;

    /**
     * The JmDNS instance
     */
    private NsdManager mNsdManager = null;

    /**
     * The ServiceInfo we are broadcasting
     */
    private NsdServiceInfo serviceInfo;

    public NsdWrapper(Context context) {
        mContext = context;
    }

    @Override
    public void setup() {
        serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName(GameFactory.getGameType(mContext) + ": (NSD) " + android.os.Build.MODEL);
        serviceInfo.setPort(GameFactory.getPortNumber(mContext));
        serviceInfo.setServiceType(WifiConstants.SERVICE_TYPE);

        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    public void close() {
        if (mNsdManager != null) {
            mNsdManager.unregisterService(this);

            mNsdManager = null;
        }
    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
        Log.e(TAG, "registration failed: " + arg0.getServiceName() + " " + arg1);
    }

    @Override
    public void onServiceRegistered(NsdServiceInfo arg0) {
        Log.e(TAG, "service registered: " + arg0.getServiceName());
    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo arg0) {
        Log.e(TAG, "service unregistered: " + arg0.getServiceName());
    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo arg0, int arg1) {
        Log.e(TAG, "unregistration failed: " + arg0.getServiceName() + " " + arg1);
    }
}
