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

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.JSONParser;

public class Verdlisti extends Fragment  {
	
	private JSONParser jsonParser = new JSONParser();
	
	private ListView verdlistiListView ;  
	private ArrayAdapter<String> listAdapter ; 
	
	
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
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_fragment_verdlisti);
	
		
		verdlistiListView = (ListView) rootView.findViewById( R.id.verdlistiListView ); 
        ArrayList<String> verdlisti = new ArrayList<String>();  
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, verdlisti); 
        
        
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
	class SaekjaVerdlista extends AsyncTask<String, String, String> {
		
	
		
		@Override
		/**
		 * Sækir verðlista úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {

			int success;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.getEmail()));
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_verdlisti, "GET", params);
			
			try{
				success = json.getInt("success");
				if(success == 1){
					
					JSONArray verdlisti = json.getJSONArray("verdlisti");
					for(int i = 0; i < verdlisti.length(); i++){
						JSONObject adgerd = verdlisti.getJSONObject(i);
						listAdapter.add(adgerd.getString("adgerd")+"\n" + adgerd.getString("verd") + " kr"); 
						
					}
				}else{
					listAdapter.add("Ooops! Enginn verdlisti fannst");
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
			verdlistiListView.setAdapter( listAdapter ); 

			}
		}
}