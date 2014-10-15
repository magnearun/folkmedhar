package com.example.myapp2;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.myapp2.*;
 
import android.content.Context;
import android.util.Log;
 
public class UserFunctions {
     
    private static JSONParser jsonParser = new JSONParser();
     
    private static String loginURL = "http://prufa2.freeiz.com/login/";
    private static String registerURL = "http://prufa2.freeiz.com/login/";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
     
    // constructor
    //public UserFunctions(){
      //  jsonParser = new JSONParser();
    //}
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public static JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(loginURL, "POST", params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
     
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public static JSONObject registerUser(String name, String email, String phone, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
         
        // getting JSON Object
        JSONObject json = jsonParser.makeHttpRequest(registerURL, "POST", params);
        // return json
        //Log.e("JSON", json.toString());
        return json;
    }

    
    /**
     * Function get Login status
     * */
    public static boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
     
    /**
     * Function to logout user
     * Reset Database
     * */
    public static boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
     
}

