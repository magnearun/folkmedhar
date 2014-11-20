/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um tilboð sem stofan býður upp á
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.folkmedhar.pantanir.JSONParser;

/*
 * Það á eftir að klára að útfæra þennan klasa
 */
public class Tilbod extends Fragment  {

	private JSONParser jsonParser = new JSONParser();
	
	private ListView tilbodListView ;
	String[] nafn;
	String[] lysing;

	

	
	private final String url_tilbod = "http://www.folkmedhar.is/magnea/tilbod.php";
	/**
	 * Nýtt fragment er búið til fyrir lista tilboða
	 */
	public Tilbod() {
	}

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um tilboð
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tilbod,
				container, false);
		


		
		 
		 
		 
		tilbodListView = (ListView) rootView.findViewById( R.id.tilbodListView );
 
        
        new SaekjaTilbod().execute();
		
		return rootView;
	}
	/**
     * @author: Birkir Pálmason
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja tilboð úr gagnagrunni. 
     * Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni 
     * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	class SaekjaTilbod extends AsyncTask<String, String, String> {
	
		@Override
		/**
		 * Sækir tilboð úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {

			int success;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.getEmail()));
			
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
			}
			catch(JSONException e){
				e.printStackTrace();
			}
          
			return null;
		}
		
		/**
		 * Bætir adapter við tilboðs listann
		 * **/
		protected void onPostExecute(String file_url) {
			 CustomList adapter = new
				        CustomList(getActivity(), nafn, lysing);
			 tilbodListView.setAdapter(adapter);

			}
		}
	
}