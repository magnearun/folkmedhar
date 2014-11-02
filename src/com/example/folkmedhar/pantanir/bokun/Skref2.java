/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um annað skref bókunarferlisins. Klasinns sér um að
 * gefa breytunum sem halda utan um tímasetningu bókunar gildi. Hann sér 
 * auk þess um að sækja lausa tíma á valinni dagsetningu úr gagnagrunni
 * og birta þær í Spinner viðmótshluti svo notandinn geti valið úr lausum tímum.
 */

package com.example.folkmedhar.pantanir.bokun;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.CalendarActivity;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.JSONParser;


public class Skref2 extends Fragment implements android.view.View.OnClickListener {
	
	private static ThrenndArray[] bokadirStarfsmenn;
	private static Timar[] lausirTimarHeild;
	
	private static int lausirNum;
	private static String[] lausirString;
	
	private View rootView;
	private static Context context;
	
	private static String url_saekja_lausa_tima = "http://prufa2.freeiz.com/saekja_bokada_tima.php";
	private static ProgressDialog pDialog;
	private static JSONParser jsonParser = new JSONParser();
	
	// Viðmótshlutir
	private Button buttonTilbaka, buttonAfram, buttonDagur;
	private static Spinner timiSpinner;
	


	/**
	 * Nýtt fragment er búið til fyrir skref 2 í bókunarferlinu
	 */
	public Skref2() {
	}

	@Override
	/**
	 * Birtir viðmótið fyrir skref 2 í bókunarferlinu og kallar á aðferð sem
	 * gefur tilviksbreytum fyrir viðmótshluti gildi. 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_skref2,
				container, false);
		
		TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		text.setText(R.string.title_activity_skref2);
		
		setHlutir();
		
		context = getActivity();
        			
		update();
        
		return rootView;
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti og OnClickListener er
	 * tengdur við takka sem notaðir eru til að fara aftur í skref 1 eða áfram í skref 3
	 */
	public void setHlutir() {
		
		timiSpinner = (Spinner) rootView.findViewById(R.id.timi);
		buttonTilbaka = (Button) rootView.findViewById(R.id.tilbaka);
		buttonAfram = (Button) rootView.findViewById(R.id.afram2);
		buttonAfram = (Button) rootView.findViewById(R.id.afram2);
		buttonDagur = (Button) rootView.findViewById(R.id.buttonDagur);
		
		buttonTilbaka.setOnClickListener(this);
		buttonAfram.setOnClickListener(this);
		buttonDagur.setOnClickListener(this);
	}


	/**
	 * Birtir skjáinn fyrir skref 1 eða 3 og uppfærir breytur sem halda utan um staðsetningu tímans 
	 * sem var valinn í Spinnar viðmótshlut og kallar á aðferð sem að uppfærir auðkenni starfsmanns
	 * ef enginn sérstakur starfsmaður var valinn
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = null;
		
	    switch (view.getId()) {
	    case R.id.buttonDagur:
	    	Intent i = new Intent(getActivity(), CalendarActivity.class);
	        startActivityForResult(i, 1);
	        return;
        case R.id.tilbaka:
        	fragment = new Skref1();
            break;
        case R.id.afram2:
        	if(MainActivity.date==null) {
        		Toast toast = Toast.makeText(getActivity(), 
        				"Vinsamlegast veldu dag", Toast.LENGTH_LONG);
        		toast.setGravity(Gravity.CENTER, 0, 0);
        		toast.show();
        		return;
        	} 
        	
        	else {
        		
        		getDateInfo();
        		if(MainActivity.staff_id.equals("000")) {
        			getStaffId();
        		}
        		MainActivity.timiSelection = timiSpinner.getSelectedItemPosition();
	        	fragment = new Skref3();
	        	break;
	        }
        	default:
            break;
        }
	    
	    MainActivity.updateFragment(fragment);
	 }
	
	/**
	 * Gefur breytum sem halda utan um þann tíma sem notandinn valdi
	 * rétt gildi
	 */
	public void getDateInfo() {
		
		MainActivity.time = timiSpinner.getSelectedItem().toString();
		MainActivity.startDate = MainActivity.dagur + " " + MainActivity.time;
		
		int x = Integer.parseInt(MainActivity.lengd);
		String endTime ="";
		for(int j = 0; j<lausirTimarHeild.length; j++) {
			if(lausirTimarHeild[j].timi==MainActivity.time) {
				endTime = lausirTimarHeild[j+x].timi;
			}
		}
		
		MainActivity.endDate = MainActivity.dagur + " " + endTime;
	}
	
	/**
	 * Gefur auðkenni starfsmanns rétt gildi ef enginn sérstakur starfsmaður var valinn,
	 * það er að segja auðkenni starfsmannsins sem á tímann sem var valinn
	 */	
	public void getStaffId() {
		
		for(int i = 0; i<lausirTimarHeild.length; i++) {
			if(lausirTimarHeild[i].timi!=null){
				if(lausirTimarHeild[i].timi.equals(MainActivity.time)) {
					MainActivity.staff_id = lausirTimarHeild[i].id;
					MainActivity.starfsmadur = getStarfsmadur(MainActivity.staff_id);
					}
				}
			}
	 }

	  /**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja alla bókaða tíma á völdum degi úr
     * gagnagrunni
     */
	public static class BokadirTimar extends AsyncTask<String, String, String> {

		@Override
		/**
		 * Fylkið sem heldur utan um lausa tíma fyrir valinn dag er upphafsstillt
		 * þannig að allir tímar eru lausir fyrir alla starfsmenn
		 */
		protected void onPreExecute() {
			super.onPreExecute();
			
			bokadirStarfsmenn = new ThrenndArray[7];
			for (int i = 0; i<7; i++) {
				bokadirStarfsmenn[i] = new ThrenndArray(new Thrennd[18],new Timar[18]);
			}
			
			String[] starfsmenn = {"BOB","PIP","ODO","MRV","EDK","BIP","DOR"};
			
			for (int j = 0; j<7; j++) {
				bokadirStarfsmenn[j].laust[0] = new Timar("09:00",true,starfsmenn[j]);
				bokadirStarfsmenn[j].laust[1] = new Timar("09:30", true,starfsmenn[j]);
				int b = 9;
						
				for(int i = 2; i<bokadirStarfsmenn[j].laust.length-1; i = i+2) {
					b = b+1;
					String s = b + ":00";
					bokadirStarfsmenn[j].laust[i] = new Timar(s, true,starfsmenn[j]);
					s = b + ":30";
					bokadirStarfsmenn[j].laust[i+1]  = new Timar(s,true,starfsmenn[j]);
				}
				
			}
			lausirNum = 0;
			
			lausirString = new String[18];
			lausirTimarHeild = new Timar[18];

			
			
			for (int i = 0; i<lausirTimarHeild.length; i++) {
				
				lausirTimarHeild[i]  = new Timar(null,true,null);
			}
			
			
			
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sæki lausa tíma..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		/**
		 * Sækir allar bókaða tíma á valinni dagsetningu úr gagnagrunni
		 * og setur þá í fylki bókaðra tíma
		 */
		protected String doInBackground(String... args) {

			String newDagur = MainActivity.dagur;
			
			int success;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("dagur", newDagur));
			params.add(new BasicNameValuePair("staff_id", MainActivity.staff_id));
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_saekja_lausa_tima, "GET", params);
			
			try {
				success = json.getInt("success");
				if(success == 1){
					JSONArray t = json.getJSONArray("pantanir");
					JSONArray bokadir = t.getJSONArray(0);
					
					if(MainActivity.staff_id!="000"){
						getBokadirTimar(MainActivity.staff_id,bokadir,-1);
					}
					
					
					else {
						String[] starfsmenn = {"BOB","PIP","ODO","MRV","EDK","BIP","DOR"};
						for(int i = 0; i<7; i++) {
							
							getBokadirTimar(starfsmenn[i],bokadir,i);
						}
					}
				}
				
				} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public void getBokadirTimar(String id, JSONArray bokadir, int num) throws JSONException {
			
			
			if(num==-1){
			for(int i = 0; i < bokadir.length(); i++){
				JSONObject timi = bokadir.getJSONObject(i);
				//Log.d("KKKKKKKKKKKKKKKKKKKK", timi.toString());
				bokadirStarfsmenn[0].bokad[i] = new Thrennd(timi.getString("time"), timi.getInt("lengd"),timi.getString("staff_id"));	
				
				}
			
			}
			
			
			else {
				for(int i = 0; i < bokadir.length(); i++){
					JSONObject timi = bokadir.getJSONObject(i);
					if (timi.getString("staff_id").equals(id)){
						
					bokadirStarfsmenn[num].bokad[i] = new Thrennd(timi.getString("time"), timi.getInt("lengd"),timi.getString("staff_id"));
			
						Log.d("ÞEtta",id + "   "+ num);
					
					}
					
				}
				}
			

			
		}
			
		
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			if(MainActivity.staff_id!="000"){
				lausirTimar(bokadirStarfsmenn[0].bokad,bokadirStarfsmenn[0].laust);
				setLausirTimar(bokadirStarfsmenn[0].laust);
			}
			
			else {
				for (int i = 0; i< 7; i++) {
					lausirTimar(bokadirStarfsmenn[i].bokad,bokadirStarfsmenn[i].laust);
					setLausirTimar(bokadirStarfsmenn[i].laust);
					
				}
			}
			getTimeSpinner();
			pDialog.dismiss();
		}
	}
		
	
	
	
	/**
	 * Þeir tímar sem eru bókaðir fá gildið false í fylki lausra tíma sem merkir
	 * að þeir séu ekki lausir
	 * @param a
	 * @param b
	 */
	private static  void lausirTimar(Thrennd[] a, Timar[] b) {

		for(int i = 0; i<a.length; i++) {
			for(int j = 0; j<b.length; j++) {
				if(a[i]!=null){
					b[i].id = a[i].id;
					if(a[i].timi.equals(b[j].timi)) {
						for(int k = 0; k<a[i].lengd; k++) {
							//Log.d("MAgneaaaaaaaa", "afjalsfjslkfjsaklfj");
							b[j+k].laus = false;
							}
						}
					}
				}
			
			
			}
	}
	
	
	
	/**
	 * Setur lausa tíma sem valmöguleika í Spinner viðmótshlut
	 */
	public static void setLausirTimar(Timar[] lausirTimar) {
		
	
		int timaLengd = Integer.parseInt(MainActivity.lengd);
		for(int i = 0; i<lausirTimar.length; i++) {
			Log.d("null",(lausirTimarHeild[i].timi==null)+"");
			if(lausirTimar[i].laus==true && lausirTimarHeild[i].timi==null) {
				boolean laust = true;
				int j = 0;
				while(j<timaLengd && (j+i) < lausirTimar.length) {
					if (lausirTimar[j+i].laus==false) {
						laust = false;
						break;
					}
					j++;
				}
				if (laust==true) {
					
				lausirTimarHeild[i].timi = lausirTimar[i].timi;
				lausirTimarHeild[i].id=lausirTimar[i].id;
				lausirString[lausirNum] =lausirTimar[i].timi;
				
				lausirNum++;
				
				Log.d("lausirNum",lausirNum+"");
				}
			}
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		buttonDagur.setText(MainActivity.date);
			new BokadirTimar().execute();
		
	}
	
	
	
	public void update() {
		if(MainActivity.date!=null) {
			buttonDagur.setText(MainActivity.date);
			new BokadirTimar().execute();
			//timi.setSelection(MainActivity.timiSelection);
		}
	}
	
	/**
	 * Sækir nafn á starfsmanni pöntunar eftir staffanúmeri
	 */
	public static String getStarfsmadur(String s) {
		String starfsmadur;
		switch(s) {
    	
			case "BOB": 
				starfsmadur = "Bambi";
				break;
			
			case "PIP" : 
				starfsmadur = "Perla";
				break;
			
			case "ODO" : 
				starfsmadur = "Oddur";
				break;
			
			case "MRV" : 
				starfsmadur= "Magnea";
				break;
			
			case "EDK" :
				starfsmadur = "Eva";
				break;
			
			case "BIP" : 
				starfsmadur = "Birkir";
				break;
			
			case "DOR" : 
				starfsmadur = "Dagný";
				break;
			
			default: starfsmadur = "Error";
			
		}
		return starfsmadur;
	}
	
	public static void getTimeSpinner() {
		
		String [] spinnerTimar = new String[lausirNum];
		for (int i = 0; i<spinnerTimar.length; i++) {
			spinnerTimar[i] = lausirString[i];
		}
		
		java.util.Arrays.sort(spinnerTimar);
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerTimar); 
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timiSpinner.setAdapter(spinnerArrayAdapter);
		
		
	}	
	
}