package com.worthwhilegames.cardgames.shared.connection;

import java.net.InetAddress;

import android.content.Context;
import android.content.SharedPreferences;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.Util;
import com.worthwhilegames.cardgames.shared.wifi.WifiServerSocket;
import com.worthwhilegames.cardgames.shared.wifi.WifiSocket;

/**
 * A Factory class for getting instances of a connection
 * client or server.
 */
public class ConnectionFactory {

    public static final String CONNECTION_ENABLED = "com.worthwhilegames.cardgames.shared.connection.ConnectionFactory.CONNECTION_ENABLED";

    /**
     * Get the type of connection that is currently in use
     * 
     * @return the type of connection in use
     */
    public static ConnectionType getConnectionType(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);
        String connectionType = prefs.getString(Constants.PREF_CONNECTION_TYPE, ConnectionType.WiFi.toString());

        if (ConnectionType.WiFi.toString().equals(connectionType)) {
            return ConnectionType.WiFi;
        }

        return ConnectionType.WiFi;
    }

    /**
     * Get the string to display on the connection screen
     * 
     * @return the string device id
     */
    public static String getDeviceDisplayName(Context ctx) {
        StringBuilder sb = new StringBuilder(ctx.getResources().getString(R.string.deviceName));
        sb.append("\n");

        if (ConnectionType.WiFi.equals(getConnectionType(ctx))) {
            InetAddress currentAddress = Util.getLocalIpAddress();
            if (currentAddress == null) {
                sb.append("Unknown");
            } else {
                sb.append(android.os.Build.MODEL);
            }
        }

        return sb.toString();
    }

    /**
     * Get a new ServerSocket based on the current connection type
     * 
     * @return a ServerSocket
     */
    public static IServerSocket getServerSocket(Context ctx) {
        ConnectionType currentType = getConnectionType(ctx);

        switch (currentType) {
        case WiFi:
        default:
            return new WifiServerSocket(ctx);
        }
    }

    /**
     * Get a new Socket based on the current connection type
     * 
     * @return a ServerSocket
     */
    public static ISocket getSocket(Context ctx, String address) {
        return getSocket(ctx, address, GameFactory.getPortNumber(ctx));
    }

    /**
     * Get a new Socket based on the current connection type
     * 
     * @return a ServerSocket
     */
    public static ISocket getSocket(Context ctx, String address, int portNumber) {
        ConnectionType currentType = getConnectionType(ctx);

        switch (currentType) {
        case WiFi:
        default:
            return new WifiSocket(address, portNumber);
        }
    }
}
