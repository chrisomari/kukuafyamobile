package com.example.kukuafya;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class NetworkUtils {
    public static String detectLocalIp(Context context) {
        // 1. Try WiFi first
        String wifiIp = getWifiIp(context);
        if (wifiIp != null) return wifiIp;

        // 2. General network interfaces
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("NetworkUtils", "IP detection failed", e);
        }
        return null;
    }

    private static String getWifiIp(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        NetworkCapabilities nc = cm.getNetworkCapabilities(network);

        if (nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            try {
                for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (intf.getName().startsWith("wlan")) {
                        for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                            if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                                return addr.getHostAddress();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "WiFi IP detection failed", e);
            }
        }
        return null;
    }
}