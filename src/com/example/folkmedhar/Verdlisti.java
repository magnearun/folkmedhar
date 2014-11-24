/**
 * @author: Birkir Pálmason
 * @since: 15.10.2014
 * Klassinn sér um að sækja verðlista úr gagnagrunni og birta hann í lista.
 */

package com.example.folkmedhar;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.folkmedhar.pantanir.FerlaBokun;
import com.example.folkmedhar.pantanir.JSONParser;

public class Verdlisti extends Fragment  {
	
	private ListView verdlistiListView ;  
	// Fylki sem halda utan um heiti allra aðgerða sem
	// sem stofan býður uppá og verð þeirra
	private String[] adgerdFylki;
	private String[] verdFylki;
	
	private final String url_verdlisti = "http://www.folkmedhar.is/magnea/verdlisti.php";
	
	/**
	 * Nýtt fragment er búið til fyrir verdlista
	 */
	public Verdlisti() {
	}

	@Override
	/**
	 * Birtir layout-ið fyrir verdlista og upphafsstillir
     * tilviksbreytur
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_verdlisti,
				container, false);
		

		verdlistiListView = (ListView) rootView.findViewById( R.id.verdlistiListView ); 
        new SaekjaVerdlista().execute();
		return rootView;
	}
	
	/**
     * @author: Birkir Pálmason
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja verdlista úr gagnagrunni. 
     * Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni 
     * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	private class SaekjaVerdlista extends AsyncTask<String, String, String> {
	
		/**
		 * Birtir dialog sem gefur notandanaum til kynna að verið sé 
		 * að sækja verðlistann
		 */
		protected void onPreExecute() {
			MainActivity.showDialog("Sæki verðlista...");
		}
		
		@Override
		/**
		 * Sækir verðlista úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {

			int success;
			
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
				}
			}
			catch(JSONException e){
				e.printStackTrace();
			}
          
			return null;
		}
		
		/**
		 * Bætir adapter við verðlistann
		 * **/
		protected void onPostExecute(String file_url) {
			 CustomList adapter = new
				        CustomList(getActivity(), adgerdFylki, verdFylki);
			 verdlistiListView.setAdapter(adapter);
			 MainActivity.hideDialog();

			}
		}
}