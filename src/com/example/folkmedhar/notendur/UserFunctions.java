/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 15.10.2014
 * Klasinn inniheldur aðferðir til þess að skrá notanda inn og nýskrá notanda. Einnig eru 
 * aðferðir til þess að skrá út notanda og athuga hvort að notandi sé nú þegar innskráður.
 * Klasinn byggir að hluta til á eftirfarandi tutorial:
 * http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
 */

package com.example.folkmedhar.notendur;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.folkmedhar.BaseActivity;
import com.example.folkmedhar.pantanir.JSONParser;
 
import android.content.Context;
import android.content.SharedPreferences;
 
public class UserFunctions extends BaseActivity{
     
    private JSONParser jsonParser;
     
    private static String loginURL = "http://prufa2.freeiz.com/login/";
    private static String registerURL = "http://prufa2.freeiz.com/login/";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
    
    private static String kSuccess = "success";
    private static String kUID = "uid";
    private static String kName = "name";
    private static String kEmail = "email";
    private static String kPhone = "phone";    
    private static String kCreatedAt = "created_at";
    
    public static String userName;
    public static String userPhone;
    public static String userEmail;
    
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
     
     
    /**
     * Sendir POST skipun með innslegnum upplýsingum frá notanda
     * og skráir í gagnagrunn. Skilar true ef tekist hefur að nýskrá notanda.
     * */
    public boolean registerUser(Context context, String name, String email, String phone, String password){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(registerURL, "POST", params);
           

           try {
           	// Athuga hvort notandi sé nú þegar til í gagnagrunni
               if (json.getString(kSuccess) != null) {
                   String result = json.getString(kSuccess); 
                   return (Integer.parseInt(result) == 1);
               }
           } catch (JSONException e) {
           	e.printStackTrace();
           }
           return false;
    }
           
    
    
    /**
     * Sendir POST skipun með innslegnum upplýsingum frá notanda
     * og athugar hvort notandi sé skráður í gagnagrunn. 
     * Vistar upplýsingar um notanda á símann og skilar true ef 
     * það hefur tekist og notandi verið skráður inn.
     * */
    public boolean loginUser(Context context, String email, String password) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(loginURL, "POST", params);
        
        try {
        	// Athuga hvort notandi fannst í gagnagrunni
            if (json.getString(kSuccess) != null) {
                String result = json.getString(kSuccess); 
                if(Integer.parseInt(result) == 1){
                	
                	// User upplýsingar úr gagnagrunni
                	JSONObject json_user = json.getJSONObject("user");
                	
                	// Vista upplýsingar úr gagnagrunni á símann
					SharedPreferences prefs = context.getSharedPreferences("Login", 0);
					boolean user = prefs.edit().putString(kName, json_user.getString(kName))
					.putString(kEmail, json_user.getString(kEmail))
					.putString(kPhone, json_user.getString(kPhone))
					.putString(kUID, json_user.getString(kUID))
					.putString(kCreatedAt, json_user.getString(kCreatedAt))		
					.commit();
					
					userName = json_user.getString(kName);
					userPhone = json_user.getString(kPhone);
					userEmail = json_user.getString(kEmail);
					return user;
                }
            }
                
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Skilar true ef notandinn er skráður inn í kerfið
     * */
    public boolean isUserLoggedIn(Context context){
        SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	if (prefs.getString(kEmail, null) != null) {
    		return true;
    	}
    	return false;
    }
    	
     
    /**
     * Skilar true ef notandinn hefur verið skráður úr kerfinu
     * */
    public boolean logoutUser(Context context){
    	SharedPreferences prefs = context.getSharedPreferences("Login", 0);
    	return prefs.edit().clear().commit();
    }
     
}