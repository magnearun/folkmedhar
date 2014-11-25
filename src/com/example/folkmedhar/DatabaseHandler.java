/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 19.11.2014
 * Klasinn sér um að að sækja gögn úr gagnagrunni og kallar á aðferðir sem að gefa breytum
 * þau gildi sem sótt voru úr gagnagrunni
 */

package com.example.folkmedhar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.example.folkmedhar.pantanir.FerlaBokun;
import com.example.folkmedhar.pantanir.JSONParser;

public class DatabaseHandler {
	
	/**
	 * Sækir bókaða tíma úr gagnagrunni
	 */
	public static void handleTimar() {
		String url_saekja_lausa_tima = "http://peoplewithhair.freevar.com/saekja_bokada_tima.php";
		String dagur = FerlaBokun.getDate();
		String staff_id = FerlaBokun.getStaffId();
		
		int success = 0;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("dagur", dagur));
		params.add(new BasicNameValuePair("staff_id", staff_id));
		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = jsonParser.makeHttpRequest(
				url_saekja_lausa_tima, "GET", params);
		
		try {
			success = json.getInt("success");
			if(success == 1){
				JSONArray t = json.getJSONArray("pantanir");
				JSONArray bokadir = t.getJSONArray(0);
				
				// Ákveðinn starfsmaður var valinn
				if(!staff_id.equals("000")){
					FerlaBokun.getBokadirTimar(staff_id,bokadir,-1);
				} else {
					
					// Notendanum er sama um hver starfsmaðurinn er, sækjum bókaða
					// tíma fyrir alla starfsmenn
					for(int i = 0; i<7; i++) {
						FerlaBokun.getBokadirTimar(FerlaBokun.getStarfsmenn(i),bokadir,i);
					}
				}
			}
			
			} catch (JSONException e) {
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Færir bókun yfir í gagnagrunn og skilar true ef það tókst,
	 * false annars
	 * @return
	 */
	public static int handlePontun() {
		
		// Heiti dálka fyrir bókun í gagnagrunni
		
		String[] databaseColumns = { "nafn", "simi", 
					"adgerd", "harlengd","time","staff_id","email","lengd", "dagur",
					"startDate", "endDate"};
		
		// Upplýsingar um bókun sem færðar eru í gagnagrunn
		String[] databaseBokun = { FerlaBokun.getName(),FerlaBokun.getSimi(),
				FerlaBokun.getAdgerd(),FerlaBokun.getHarlengd(),FerlaBokun.getTime(),
				FerlaBokun.getStaffId(),FerlaBokun.getEmail(),FerlaBokun.getLengd(),
				FerlaBokun.getDate(),FerlaBokun.getStartDate(), FerlaBokun.getEndDate()};
		
		int success = 0;
		// Færa upplýsingar um bókun í gagnagrunn
	    String url_panta_tima = "http://peoplewithhair.freevar.com/pantatima.php";
		List<NameValuePair> params_panta = new ArrayList<NameValuePair>();
		for(int i=0;i<databaseColumns.length;i++){
			params_panta.add(new BasicNameValuePair(databaseColumns[i], databaseBokun[i]));
		}
		JSONParser jsonParser = new JSONParser();
		JSONObject json_panta = jsonParser.makeHttpRequest(url_panta_tima,
				"POST", params_panta);
		
		// Sækja þá pöntun sem búa á til áminningu fyrir
		String url_reminder = "http://peoplewithhair.freevar.com/allarPantanir.php";
		List<NameValuePair> params_aminning = new ArrayList<NameValuePair>();
		params_aminning.add(new BasicNameValuePair("email", FerlaBokun.getEmail()));
		params_aminning.add(new BasicNameValuePair("sidastaPontun", "ff")); 
		
		JSONObject json_aminning = jsonParser.makeHttpRequest(
				url_reminder, "GET", params_aminning);

		try {
			success = json_panta.getInt("success");

			if(FerlaBokun.getStarfsmadur().equals("Hver sem er")) {
				success=0;
			}
			if(success==1) {
				MainActivity.hideDialog();
				JSONArray aminning = json_aminning.getJSONArray("pantanir");
				JSONObject jObject_aminning = aminning.getJSONObject(0);
				FerlaBokun.setReminderTime(jObject_aminning.getString("time"));
				FerlaBokun.setReminderAr(Integer.parseInt(jObject_aminning.getString("startDate").substring(0,4)));
				FerlaBokun.setReminderManudur(Integer.parseInt(jObject_aminning.getString("startDate").substring(5,7))-1);
				FerlaBokun.setReminderDagur(Integer.parseInt(jObject_aminning.getString("startDate").substring(8,10)));
				FerlaBokun.setBokudPontun(true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Eyðir pöntun úr gagnagrunni og skilar true ef það tókst,
	 * false annars
	 * @return
	 */
	public static int handleAfpontun() {
		int success = 0;
		String url_afpanta = "http://peoplewithhair.freevar.com/afpanta.php";
        try {
           
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id",FerlaBokun.getID()));
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(
                    url_afpanta, "POST", params);

            success = json.getInt("success");
           
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
        
	}
	
	/**
	 * Sækir nýjustu pöntun notanda úr gagnagrunni og skilar true ef það tókst
	 * false annars
	 * @return
	 */
	public static int handleNaestaPontun() {
		int success = 0;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", FerlaBokun.getEmail()));
		params.add(new BasicNameValuePair("sidastaPontun", "ff")); 
		JSONParser jsonParser = new JSONParser();
		String url_pantanir = "http://peoplewithhair.freevar.com/allarPantanir.php";
		JSONObject json = jsonParser.makeHttpRequest(
				url_pantanir, "GET", params);
		
		try{
			success = json.getInt("success");
			if(success == 1){
				JSONArray pantanir = json.getJSONArray("pantanir");
				JSONObject pontun = pantanir.getJSONObject(0);
				FerlaBokun.setPontun(pontun.getString("nafn") + "\n" + pontun.getString("adgerd") + "\n"
				+ "Starfsmadur: " + FerlaBokun.getStarfsmadur(pontun.getString("staff_id")) +"\n"
				+ "Klukkan: "+ pontun.getString("time"));
				
				// Upplýsingar um pöntun notandans
				FerlaBokun.setNaestaPontunAr(getAr(pontun.getString("startDate")));
				FerlaBokun.setNaestaPontunManudur(getManudur(pontun.getString("startDate")));
				FerlaBokun.setNaestaPontunDay(getDagur(pontun.getString("startDate")));
				FerlaBokun.setID(pontun.getString("ID"));
			}
		}
		catch(JSONException e){
			e.printStackTrace();
			
		}
		return success;
	}
	
	/**
	 * Sækir allar pantanir notanda úr gagnagrunni og skilar ArrayAdapter
	 * sem inniheldur þær allar ef það tókst. Annars inniheldurArrayAdapter
	 * streng sem gefur til kynna að ekki hafi fundist neinar pantanir
	 * @param listAdapter
	 */
	public static void handleAllarPantanir(ArrayAdapter<String> listAdapter) {
		
		int success;
		String url_pantanir = "http://peoplewithhair.freevar.com/allarPantanir.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", FerlaBokun.getEmail()));
		JSONParser jsonParser = new JSONParser();
		JSONObject json = jsonParser.makeHttpRequest(
				url_pantanir, "GET", params);
		
		try{
			success = json.getInt("success");
			if(success == 1){
				JSONArray pantanir = json.getJSONArray("pantanir");
				for(int i = 0; i < pantanir.length(); i++){
					JSONObject pontun = pantanir.getJSONObject(i);
					String dagur = pontun.getString("dagur");
					dagur = getDagur(dagur) + "-" + getManudur(dagur) + "-" + getAr(dagur);
					
					String staff_id = pontun.getString("staff_id");
					String a = FerlaBokun.getStarfsmadur(staff_id);
					listAdapter.add(pontun.getString("adgerd") + "\n" + 
					"Starfsmadur: "+a + "\n" + dagur
					+ "   Klukkan: "+ pontun.getString("time")); 
					
				}
			}else{
				listAdapter.add("Engar pantanir fundust");
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
	}
	
	/**
     * Sendir POST skipun með innslegnum upplýsingum frá notanda
     * og skráir í gagnagrunn. Skilar true ef tekist hefur að nýskrá notanda.
     * */
	public static boolean registerUser(Context context, String name, String email, String phone, String password) {
		JSONParser jsonParser = new JSONParser();
		String loginURL = "http://peoplewithhair.freevar.com/login/";
    	String kSuccess = "success";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "register"));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(loginURL, "POST", params);
           

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
	public static boolean loginUser(Context context, String email, String password) {
		JSONParser jsonParser = new JSONParser();
		String loginURL = "http://peoplewithhair.freevar.com/login/";
    	String kSuccess = "success";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "login"));
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
					boolean user = prefs.edit().putString("name", json_user.getString("name"))
					.putString("email", json_user.getString("email"))
					.putString("phone", json_user.getString("phone"))
					.putString("uid", json_user.getString("uid"))
					.putString("created_at", json_user.getString("created_at"))		
					.commit();
					return user;
                }
            }
                
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        return false;
	}
	
	
	/**
     * Sendir POST skipun með núverandi emaili og lykilorði auk
     * uppfærðra notendaupplýsinga og uppfærir upplýsingar í gagnagrunni. 
     * Skilar true ef tekist hefur að uppfæra notanda.
     * */
	public static boolean updateUser(Context context, String oldEmail, String oldPassword, String name, 
		String email, String phone, String password) {
		
		JSONParser jsonParser = new JSONParser();
		String loginURL = "http://peoplewithhair.freevar.com/login/";
    	String kSuccess = "success";
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", "update"));
        params.add(new BasicNameValuePair("oldEmail", oldEmail));
        params.add(new BasicNameValuePair("oldPassword", oldPassword));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("password", password));
         
        JSONObject json = jsonParser.makeHttpRequest(loginURL, "POST", params);
         
        try {
         	// Athuga hvort notandi fannst í gagnagrunni
             if (json.getString(kSuccess) != null) {
                 String result = json.getString(kSuccess); 
                 if(Integer.parseInt(result) == 1){
                 	
                 	// User upplýsingar úr gagnagrunni
                 	JSONObject json_user = json.getJSONObject("user");
                 	
                 	
                	// Vista nýjar upplýsingar úr gagnagrunni á símann
					SharedPreferences prefs = context.getSharedPreferences("Login", 0);
					boolean user = prefs.edit().putString("name", json_user.getString("name"))
					.putString("email", json_user.getString("email"))
					.putString("phone", json_user.getString("phone"))
					.putString("uid", json_user.getString("uid"))
					.putString("created_at", json_user.getString("created_at"))	
					.putString("updated_at", json_user.getString("updated_at"))
					.commit();
					
					return user;
                 }
             }
             
         } catch (JSONException e) {
         	e.printStackTrace();
         }
         return false;
	}
	
	
	/**
	 * Sækir tilboð úr gagnagrunni og setur í lista
	 */
	public static void handleTilbod(String[] nafn, String[] lysing) {
		int success;
		JSONParser jsonParser = new JSONParser();
		String url_tilbod = "http://www.folkmedhar.is/magnea/tilbod.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", FerlaBokun.getEmail()));
		
		JSONObject json = jsonParser.makeHttpRequest(
				url_tilbod, "GET", params);
		
		try{
			success = json.getInt("success");
			if(success == 1){
				JSONArray tilbod = json.getJSONArray("tilbod");
				nafn = new String[tilbod.length()];
				lysing = new String[tilbod.length()];
				for(int i = 0; i < tilbod.length(); i++){
					JSONObject staktTilbod = tilbod.getJSONObject(i);
					nafn[i] = staktTilbod.getString("name");
					lysing[i] = staktTilbod.getString("description"); 
					
				}
			}
			
			Tilbod.setNafn(nafn);
			Tilbod.setLysing(lysing);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
      
	}
	
	/**
	 * Sækir verðlista úr gagnagrunni og setur í lista
	 */
	public static void handleVerdlisti(String[] adgerdFylki, String[] verdFylki) {
		int success;
		String url_verdlisti = "http://www.folkmedhar.is/magnea/verdlisti.php";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", FerlaBokun.getEmail()));
		JSONParser jsonParser = new JSONParser();
		JSONObject json = jsonParser.makeHttpRequest(
				url_verdlisti, "GET", params);
		
		try{
			success = json.getInt("success");
			if(success == 1){
				JSONArray verdlisti = json.getJSONArray("verdlisti");
				adgerdFylki = new String[verdlisti.length()];
				verdFylki = new String[verdlisti.length()];
				
				for(int i = 0; i < verdlisti.length(); i++){
					JSONObject adgerd = verdlisti.getJSONObject(i);
					adgerdFylki[i] = adgerd.getString("adgerd");
					verdFylki[i] = adgerd.getString("verd") + "kr"; 	
				}
				
				Verdlisti.setAdgerd(adgerdFylki);
				Verdlisti.setVerd(verdFylki);
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Tekur inn streng á forminu "yyyy-mm-dd" og skilar yyyy
	 * @param a
	 * @return
	 */
	private static String getAr(String a) {
		return a.substring(0,4);
	}
	
	/**
	 * Tekur inn streng á forminu "yyyy-mm-dd" og skilar mm
	 * @param m
	 * @return
	 */
	private static String getManudur(String m) {
		return m.substring(5,7);
	}
	
	/**
	 * Tekur inn streng á forminu "yyyy-mm-dd" og skilar dd
	 * @param d
	 * @return
	 */
	private static String getDagur(String d) {
		return d.substring(8,10);
	}

}
