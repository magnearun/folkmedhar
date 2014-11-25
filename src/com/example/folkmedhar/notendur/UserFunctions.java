/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 20.11.2014
 * Klasinn sér um að ferla upplýsingar um notanda. Hann kallar á klasa sem sér um að skrá og sækja
 * notanda úr gagnagrunni. Klasinn inniheldur einnig aðferðir sem að skrá og sækja upplýsingar um notanda
 * úr gagnagrunni símans og athuga hvort að hann sé innskráður.
 * Klasinn byggir að hluta til á eftirfarandi tutorial:
 * http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
 */

package com.example.folkmedhar.notendur;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
 
public class UserFunctions extends Activity{

    /**
     * Skilar true ef notandinn er skráður inn í kerfið
     * */
    public boolean isUserLoggedIn(Context context){
        SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	if (prefs.getString("email", null) != null) {
    		return true;
    	}
    	return false;
    }
  
    /**
     * Skilar netfangi innskráðs notanda
     * @param context
     * @return
     */
    public static String getUserEmail(Context context) {
    	SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	String oldEmail = prefs.getString("email", "");
    	return oldEmail;
    }
    
    /**
     * Skilar nafni notandans sem er innskráður
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
    	SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	String oldName = prefs.getString("name", "");
    	return oldName;
    }
   
    /**
     * Skilar símanúmeri innskráðs notanda
     * @param context
     * @return
     */
    public static String getUserPhone(Context context) {
    	SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	String oldPhone = prefs.getString("phone", "");
    	return oldPhone;
    }
    
    /**
     * Skilar true ef notandinn hefur verið skráður úr kerfinu
     * */
    public boolean logoutUser(Context context){
    	SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	return prefs.edit().clear().commit();
    }
     
}