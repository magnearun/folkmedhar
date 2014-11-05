/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 05.11.2014
 * Klasinn sér um að athuga hvort að nettengin sé til staðar
 */

package com.example.folkmedhar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection extends MainActivity {
	
	static final int DIALOG_ERROR_CONNECTION = 1;
	
	/**
	 * Skilar true ef nettengin er til staðar, false annars
	 * @param c
	 * @return
	 */
	public static boolean isOnline(Context c) {
		boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
}
