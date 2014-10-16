package com.example.myapp2.notendur;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp2.BaseActivity;
import com.example.myapp2.pantanir.JSONParser;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
 
public class UserFunctions extends BaseActivity{
     
    private JSONParser jsonParser;
     
    private static String loginURL = "http://prufa2.freeiz.com/login/";
    private static String registerURL = "http://prufa2.freeiz.com/login/";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
    
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_PHONE = "phone";    
    private static String KEY_CREATED_AT = "created_at";
    
    public static String userName;
    public static String userPhone;
    
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
     
     
    /**
     * function make Register Request
     * @param name
     * @param email
     * @param password
     * */
    public boolean registerUser(Context context, String name, String email, String phone, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(registerURL, "POST", params);
           
        // check for register response
           try {
           	// Athuga hvort notandi sé til í gagnagrunni
               if (json.getString(KEY_SUCCESS) != null) {
                   String result = json.getString(KEY_SUCCESS); 
                   return (Integer.parseInt(result) == 1);
               }
           } catch (JSONException e) {
           	e.printStackTrace();
           }
           return false;
    }
           
    
    
    /**
     * Skilar true ef notandi hefur verið skráður inn
     */
    public boolean loginUser(Context context, String email, String password) {
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(loginURL, "POST", params);
        
     // check for login response
        try {
        	// Athuga hvort notandi sé til í gagnagrunni
            if (json.getString(KEY_SUCCESS) != null) {
                String result = json.getString(KEY_SUCCESS); 
                if(Integer.parseInt(result) == 1){
                	
                	Log.d("loginUser", "json success");
	
                	JSONObject json_user = json.getJSONObject("user");
                	
                	// Vista upplýsingar úr gagnagrunni á símann
					SharedPreferences prefs = context.getSharedPreferences("Login", 0);
					boolean ret = prefs.edit().putString(KEY_NAME, json_user.getString(KEY_NAME))
					.putString(KEY_EMAIL, json_user.getString(KEY_EMAIL))
					.putString(KEY_PHONE, json_user.getString(KEY_PHONE))
					.putString(KEY_UID, json_user.getString(KEY_UID))
					.putString(KEY_CREATED_AT, json_user.getString(KEY_CREATED_AT))		
					.commit();
					userName = json_user.getString(KEY_NAME);
					userPhone = json_user.getString(KEY_PHONE);
					return ret;
                }
            }
                
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	if (prefs.getString(KEY_EMAIL, null) != null) {
    		return true;
    		}
    	return false;
    }
    	
     
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
    	SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	return prefs.edit().clear()
    		.commit();
    }
     
}

