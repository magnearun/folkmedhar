/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að birta skjá þar sem hægt er að velja um að fá
 * yfirlit yfir allar pantanir eða síðustu pöntun
 */

package com.example.folkmedhar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.pantanir.AllarPantanir;
import com.example.folkmedhar.pantanir.JSONParser;
import com.example.folkmedhar.pantanir.SidastaPontun;


public class MittSvaedi extends Fragment implements  android.view.View.OnClickListener  {
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	TextView texti;
	TextView dagatal_ar;
	TextView dagatal_manudur;
	TextView dagatal_dagur;
	Button afpanta;
	
	public static String pontunar_ID;
	public static String t = "";
	public static String manudur, dagur, ar;
	
	private final String url_pantanir = "http://prufa2.freeiz.com/allarPantanir.php";
	/**
	 * Nýtt fragment er búið til fyrir „Mínar pantanir"
	 */
	public MittSvaedi() {
	}

	@Override
	/**
	 * Birtir layout-ið fyrir „Mínar pantanir" og tengir onClickListener við takka sem notaðir eru
     * til að fá yfirlit yfir allar pantanir eða síðustu pöntun
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mitt_svaedi,
				container, false);
		
		((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_mitt_svaedi);
		
		Button buttonSidastaPontun  = (Button) rootView.findViewById(R.id.sidasta_pontun);
		Button buttonAllarPantanir  = (Button) rootView.findViewById(R.id.allar_pantanir);
		
		buttonSidastaPontun.setOnClickListener(this);
		buttonAllarPantanir.setOnClickListener(this);

		
		return rootView;
	}

	/**
	 * Birtir skjá með yfirliti yfir síðustu pöntun notandans eða
	 * Birtir skjá með yfirliti yfir allar pantanir notandans
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = new MittSvaedi();
	    FragmentManager fragmentManager = getFragmentManager();
	    switch (view.getId()) {
	        case R.id.sidasta_pontun:
	        	new ErTilPontun().execute();
	        	//fragment = new SidastaPontun(); // SíðastaPontun
	            break;
	        case R.id.allar_pantanir:
	        	fragment = new AllarPantanir();// AllarPantanir
	            break;
	        default:
	            break;
	    }
	    fragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment)
        .addToBackStack("fragment")
        .commit();
	}
	
	/**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja allar pantanir notandans úr gagnagrunni
     * og birta þær í textasvæði. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	private class ErTilPontun extends AsyncTask<String, String, String> {
	
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
		 * Athuga hvort að það sé til pöntun
		 * 
		 */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.getEmail()));
			params.add(new BasicNameValuePair("sidastaPontun", "ff"));
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_pantanir, "GET", params);
			
			try{
				success = json.getInt("success");
				if(success == 1){
					
					JSONArray pantanir = json.getJSONArray("pantanir");
					JSONObject pontun = pantanir.getJSONObject(0);
					t = pontun.getString("nafn") + "\n" + pontun.getString("adgerd") + "\n"
					+ "Starfsmadur: " + MainActivity.getStarfsmadur(("BOB")) +"\n"
					+ "Klukkan: "+ pontun.getString("time");
					
					// dagatals breytur fá gildi
					ar = pontun.getString("startDate").substring(0,4);
					manudur = saekja_manud(pontun.getString("startDate").substring(5,7));
					dagur = pontun.getString("startDate").substring(8,10);
					
					pontunar_ID = pontun.getString("ID");
					
				
					
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
				Fragment fragment = new MittSvaedi();
			    FragmentManager fragmentManager = getFragmentManager();
				fragment = new SidastaPontun(); // SíðastaPontun
				fragmentManager.beginTransaction()
			    .replace(R.id.content_frame, fragment)
			    .addToBackStack("fragment")
			    .commit();
				}
			else{
				Toast toast = Toast.makeText(getActivity(),"Engin pöntun fannst", Toast.LENGTH_LONG);
            	toast.setGravity(Gravity.CENTER, 0, 0);
            	toast.show();
			}
		}
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