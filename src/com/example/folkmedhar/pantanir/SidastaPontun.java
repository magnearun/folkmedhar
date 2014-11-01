/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að sækja virka pöntun notandans úr
 * gagnagrunni og birta hana á skjánum
 */

package com.example.folkmedhar.pantanir;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;

public class SidastaPontun extends Fragment  {
	
	private ProgressDialog pDialog;
	String allarPantanir;
	TextView texti;
	TextView dagatal_ar;
	TextView dagatal_manudur;
	TextView dagatal_dagur;
	JSONParser jsonParser = new JSONParser();
	
	
	private final String url_pantanir = "http://prufa2.freeiz.com/allarPantanir.php";
	private final String url_afpanta = "http://prufa2.freeiz.com/afpanta.php";

	
	
	/**
	 * Nýtt fragment er búið til fyrir síðustu pöntun notandans
	 */
	public SidastaPontun() {
	}

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sidasta_pontun,
				container, false);
		
		((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_sidasta_pontun);
		
		texti = (TextView) rootView.findViewById(R.id.texti);
		dagatal_ar = (TextView) rootView.findViewById(R.id.ar);
		dagatal_manudur = (TextView) rootView.findViewById(R.id.manudur);
		dagatal_dagur = (TextView) rootView.findViewById(R.id.dagur);
		
		new SaekjaSidustuPontun().execute();
		
		return rootView;
	}
	
	/**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja allar pantanir notandans úr gagnagrunni
     * og birta þær í textasvæði. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	class SaekjaSidustuPontun extends AsyncTask<String, String, String> {
		String t = "";
		String manudur, dagur, ar;
		int success;
		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið ég að 
		 * sækja pantanir hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Sæki pöntun..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		/**
		 * Sækir allar pantanir notanda úr gagnagrunni og býr til streng
		 * með upplýsingunum
		 */
		protected String doInBackground(String... args) {

			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.email));
			params.add(new BasicNameValuePair("sidastaPontun", "ff"));
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_pantanir, "GET", params);
			Log.e("SUCCESS1",json.toString());
			try{
				success = json.getInt("success");
				if(success == 1){
					Log.e("SUCCESS","YOLO");
					JSONArray pantanir = json.getJSONArray("pantanir");
					JSONObject pontun = pantanir.getJSONObject(0);
					t = pontun.getString("nafn") + "\n" + pontun.getString("adgerd") + "\n"
					+ "Starfsmadur: " + AllarPantanir.starfsmadurPontunar("BOB") +"\n"
					+ "Klukkan: "+ pontun.getString("time");
					
					ar = pontun.getString("dagur").substring(0,4);
					manudur = saekja_manud(pontun.getString("dagur").substring(5,7));
					dagur = pontun.getString("dagur").substring(8,10);
					
					Log.e("AR",ar);
					Log.e("MANUDUR",manudur);
					Log.e("DAGUR", dagur);
					
				}
			}
			catch(JSONException e){
				e.printStackTrace();
				
			}
          
			return null;
		}
		
		/**
		 * Lokar „progress dialog" og kallar á aðferð sem að birtir 
		 * allar pantanir á skjánum
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if(success==1){
				setText(t, ar, manudur, dagur);
				}
			}
		}
	
	public void setText(String pontun, String ar, String manudur, String dagur){
		texti.setText(pontun);
		dagatal_ar.setText(ar);
		dagatal_manudur.setText(manudur);
		dagatal_dagur.setText(dagur);
	}
	public static String saekja_manud(String s){
		String manudur;
		switch(s) {
    	
			case "01": 
				manudur = "JAN";
				break;
			
			case "02" : 
				manudur = "FEB";
				break;
			
			case "03" : 
				manudur = "MAR";
				break;
			
			case "04" : 
				manudur = "APR";
				break;
			
			case "05" :
				manudur = "MAY";
				break;
			
			case "06" : 
				manudur = "JUN";
				break;
			
			case "07" : 
				manudur = "JUL";
				break;
				
			case "08" : 
				manudur = "AUG";
				break;
				
			case "09" : 
				manudur = "SEP";
				break;
				
			case "10" : 
				manudur = "OKT";
				break;
				
			case "11" : 
				manudur = "NOV";
				break;
			
			case "12" : 
				manudur = "DES";
				break;
				
			default: manudur = "Error";
			
		}
		return manudur;
		
	}

}
