/**
 * @author: Birkir Pálmason
 * @since: 15.10.2014
 * Klasinn sem sér um að færa pöntun notandans yfir í gagnagrunn
 */

package com.example.folkmedhar.pantanir.bokun;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.folkmedhar.AlarmReceiver;
import com.example.folkmedhar.Connection;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.Upphafsskjar;
import com.example.folkmedhar.pantanir.FerlaBokun;
import com.example.folkmedhar.pantanir.JSONParser;


public class Skref3 extends Fragment implements android.view.View.OnClickListener {
	
	
	// Viðmótshlutir
	private Button buttonPanta;
	private TextView bokunTexView;
	
	// Id þeirra viðmótshluta sem sýna bókun notandans
	private String[] vidmotsID = { "nafn", "simi", "starfsmadur", "adgerd", 
				"harlengd","date","time"};
	
	// Upplýsingar um bókun sem eru birtar notandanum
	private String[] bokunArray = { FerlaBokun.getName(),FerlaBokun.getSimi(),
				FerlaBokun.getStarfsmadur(),FerlaBokun.getAdgerd(),
				FerlaBokun.getHarlengd(),FerlaBokun.getStringDate(),FerlaBokun.getTime()};
	
	// Heiti dálka fyrir bókun í gagnagrunni
	private String[] databaseColumns = { "nafn", "simi", 
				"adgerd", "harlengd","time","staff_id","email","lengd", "dagur",
				"startDate", "endDate"};
	
	// Upplýsingar um bókun sem færðar eru í gagnagrunn
	private String[] databaseBokun = { FerlaBokun.getName(),FerlaBokun.getSimi(),
			FerlaBokun.getAdgerd(),FerlaBokun.getHarlengd(),FerlaBokun.getTime(),
			FerlaBokun.getStaffId(),FerlaBokun.getEmail(),FerlaBokun.getLengd(),
			FerlaBokun.getDate(),FerlaBokun.getStartDate(), FerlaBokun.getEndDate()};
	
	// Breyturnar halda utan um tímasetningu áminningar
	private static int ar, manudur, dagur; 

	int success; // er true ef tókst að færa pöntun í gagnagrunn
	
	private View rootView;
	private Context context;
	
	
	/**
	 * Nýtt fragment er búið til fyrir Skref 3
	 */
	public Skref3() {
	}

	@Override
	/**
	 * Birtir viðmótið fyrir skref 3 í bókunarferlinu og kallar á aðferðir sem upphafsstilla viðmótshluti
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_skref3,
				container, false);

		setVidmotshlutir();
		showBokun(); // Birta upplýsingar um bókun
		context = getActivity();
		return rootView;
	}
		
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti og OnClickListener er
	 * tengdur við takka sem notaðir eru til að fara aftur í skref 2 eða bóka
	 * pöntun
	 */
	private void setVidmotshlutir() {
		buttonPanta = (Button) rootView.findViewById(R.id.panta);  
		buttonPanta.setOnClickListener(this);
	}
	
	/**
	 * Birtir skjáinn fyrir skref 2 eða
	 * Kallar á aðferð sem sér um að færa upplýsingar um bókun
	 * yfir í gagnagrunn og birtir skjá með yfirliti bókunar ef notandinn er nettengdur.
	 * Annars eru birt villuskilaboð
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = null;
	    switch (view.getId()) {
	        case R.id.panta:
	        	if (Connection.isOnline(getActivity())) {
		        	new Stadfesta().execute();
		        	fragment = new Upphafsskjar();
	        	}
	        	else {
	    			MainActivity.showToast("Engin nettenging!", getActivity());
	        		return;
	    		}
	            break;
	        default:
	            break;
	    }
	    MainActivity.updateFragment(fragment);
	}
	
	/**
	 * Birtir upplýsingar um bókun í TextView
	 */
	private void showBokun(){

		bokunTexView = new TextView(getActivity());
		Resources res = getResources();

		// Sækir þá viðmótshluti sem hafa Id sem geymt er í heiti fylkinu
		for(int i=0;i<vidmotsID.length;i++){
			int id = res.getIdentifier(vidmotsID[i], "id", getActivity().getBaseContext().getPackageName());
			bokunTexView=(TextView)rootView.findViewById(id);
			bokunTexView.setText(bokunArray[i]);
		}
	}	
	
	/**
	 * Færir bókun notandans yfir í gagnagrunn. Við útfærslu klasanns var stuðst við tutorial um 
	 * hvernig skal nota JSON til að ná í upplýsingar ýr MySQL gagnagrunni
	 * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
	 */
	private class Stadfesta extends AsyncTask<String, String, String> {

		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið sé að ferla
		 * pöntun hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			MainActivity.showDialog("Bóka tíma...");
		}
		
		@Override
		/**
		 * Færir upplýsingar um bókun notandans yfir í gagnagrunn ef engar villur fundust.
		 * Annars eru birt villuskilaboð. Uppfærir breytur sem halda utan um tímasetningu áminningar
		 */
		protected String doInBackground(String... args) {

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
				if(success==1) {
					MainActivity.hideDialog();
					JSONArray aminning = json_aminning.getJSONArray("pantanir");
					JSONObject jObject_aminning = aminning.getJSONObject(0);
					setTimeReminder(jObject_aminning.getString("time"));
					ar=Integer.parseInt(jObject_aminning.getString("startDate").substring(0,4));
					manudur=Integer.parseInt(jObject_aminning.getString("startDate").substring(5,7))-1;
					dagur =Integer.parseInt(jObject_aminning.getString("startDate").substring(8,10));
					FerlaBokun.setBokudPontun(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Lokar „progress dialog" og bókarpöntun ef
		 * pöntunin tókst
		 * **/
		protected void onPostExecute(String file_url) {
			if (success == 1) {
				scheduleAlarm();
				FerlaBokun.setBokudPontun(true);
				showMessage("Pöntunin þín hefur verið bókuð");
			} else {
				showMessage("Ekki tókst að bóka pöntun. Vinsamlegast reyndu aftur");
			}
		}
	}
	
	/**
	 * Birtir AlertDialog með skilaboðunum message
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	private void showMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(final DialogInterface dialog, final int which) {
		   }
		});
		alertDialog.show();
	}
	
	/**
	 * Skilar true ef year er hlaupár, false annars
	 * @param year
	 * @return
	 */
	public static boolean leapYear(int year )
	{
	    if( year%400==0 ) return true;
	    if( year%100==0 ) return false;
	    if( year%4==0 ) return true;
	    return false;
	}
	
	/**
	 * Uppfærir breytur sem halda utan um mánaðardag bókunar sem áminning
	 * er skráð á ef mánaðardagur bókunar er fyrsti mánaðarins
	 */
	public static void erFyrstiManadarins(){
        if(dagur==01){
        	if(manudur==01){
        		ar=ar-1;
        		manudur=12;
        	}
        	if(manudur==4 || manudur==6 || manudur==9 || manudur==11){
        		dagur=30;
        	}
        	else if(manudur==2 && leapYear(ar)){
        		dagur=29;
        	}
        	else if(manudur==2 && !leapYear(ar)){
        		dagur=28;
        	}
        	else{
        		dagur=31;
        	}
        	if(manudur!=12)
        	manudur--;
        }
        else{
        	dagur--;
        }
    }
	
	/**
	 * Býr til nýja áminningu fyrir bókunina
	 */
	private void scheduleAlarm()
    {

        Calendar myAlarmDate = Calendar.getInstance();
        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
        erFyrstiManadarins();
        myAlarmDate.set(ar, manudur, dagur, 18, 00, 0);
        Long time = myAlarmDate.getTimeInMillis();
     
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,1,  
        		intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            
    }
	
	/**
	 * Gefur tímasetningu áminningar gildið
	 * time
	 * @param time
	 */
	private void setTimeReminder(String bokunTimi) {
		SharedPreferences prefs = context.getSharedPreferences("BokunTimi", 0);
		prefs.edit().putString("bokunTimi", bokunTimi).commit();
	}
	
	/**
	 * Gefur breytu sem heldur utan um á hvaða degi pöntun var bókuð
	 * gildið d
	 * @param d
	 */
	public static void setDagur(int d) {
		dagur = d;
	}
	
	/**
	 * Gefur breytu sem heldur utan um í hvaða mánuði pöntun var bókuð
	 * gildið m
	 * @param m
	 */
	public static void setManudur(int m) {
		manudur = m;
	}
	
	/**
	 * Gefur breytu sem heldur utan um á hvaða ári pöntun var bókuð
	 * gildið a
	 * @param a
	 */
	public static void setAr(int a) {
		ar = a;
	}
	
	/**
	 * Skilar þeim degi sem pöntun var bókuð á
	 * @return
	 */
	public static int getDagur() {
		return dagur;
	}
	
	/**
	 * Skilar þeim mánuði sem pöntun var bókuð í
	 * @return
	 */
	public static int getManudur() {
		return manudur;
	}
	
	/**
	 * Skilar því ári sem pöntun var bókuð á
	 * @return
	 */
	public static int getAr() {
		return ar;
	}
}
