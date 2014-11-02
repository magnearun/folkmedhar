/**
 * @author: Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að sækja allar pantanir innskráðs notanda
 * úr gagnagrunni og birta þær á skjánum
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;

public class AllarPantanir extends Fragment  {
	
	private ProgressDialog pDialog;
	String allarPantanir;
	
	JSONParser jsonParser = new JSONParser();
	
	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ; 
	
	private final String url_pantanir = "http://prufa2.freeiz.com/allarPantanir.php";
	
	/**
	 * Nýtt fragment er búið til fyrir allar pantanir notandans
	 */
	public AllarPantanir() {
	}

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_allar_pantanir,
				container, false);
		
		((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_allar_pantanir);
		
		mainListView = (ListView) rootView.findViewById( R.id.mainListView ); 
        ArrayList<String> pantanir = new ArrayList<String>();  
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, pantanir);  
        
        new SaekjaAllarPantanir().execute();
		
		return rootView;
	}
	
	/**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja allar pantanir notandans úr gagnagrunni
     * og birta þær í lista. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	class SaekjaAllarPantanir extends AsyncTask<String, String, String> {
		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið ég að 
		 * sækja pantanir hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Sæki pantanir..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		/**
		 * Sækir allar pantanir notanda úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {

			int success;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.email));
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_pantanir, "GET", params);
			
			try{
				success = json.getInt("success");
				if(success == 1){
					
					JSONArray pantanir = json.getJSONArray("pantanir");
					
					for(int i = 0; i < pantanir.length(); i++){
						JSONObject pontun = pantanir.getJSONObject(i);
						String staff_id = pontun.getString("staff_id");
						String a = starfsmadurPontunar(staff_id);
						listAdapter.add(pontun.getString("adgerd") + "\n" + "Starfsmadur: "+a + "\n" + pontun.getString("dagur")+ "   Klukkan: "+ pontun.getString("time")); 
						
					}
				}else{
					listAdapter.add("Engar pantanir fundust");
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
			mainListView.setAdapter( listAdapter ); 

			}
		}
	
	/**
	 * Sækir nafn á starfsmanni pöntunar eftir staffanúmeri
	 */
	public static String starfsmadurPontunar(String s){
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

}