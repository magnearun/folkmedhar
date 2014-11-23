/**
 * @author: MAgnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að birta skjá þar sem hægt er að velja um að fá
 * yfirlit yfir allar pantanir eða næstu pöntun
 */

package com.example.folkmedhar.pantanir;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;


public class MinarPantanir extends Fragment implements  android.view.View.OnClickListener  {
	
    private static String url_pantanir = "http://peoplewithhair.freevar.com/allarPantanir.php";
	private View rootView; 
	/**
	 * Nýtt fragment er búið til fyrir „Mínar pantanir"
	 */
	public MinarPantanir() {
	}

	@Override
	/**
	 * Birtir layout-ið fyrir „Mínar pantanir" og tengir onClickListener við takka sem notaðir eru
     * til að fá yfirlit yfir allar pantanir eða næstu pöntun pöntun
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_minar_pantanir,
				container, false);
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_mitt_svaedi);
		
		setVidmotshlutir();
		
		return rootView;
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		
		Button buttonNaestaPontun  = (Button) rootView.findViewById(R.id.sidasta_pontun);
		Button buttonAllarPantanir  = (Button) rootView.findViewById(R.id.allar_pantanir);
		
		buttonNaestaPontun.setOnClickListener(this);
		buttonAllarPantanir.setOnClickListener(this);
	}

	/**
	 * Birtir skjá með yfirliti yfir næstu pöntun notandans eða
	 * Birtir skjá með yfirliti yfir allar pantanir notandans
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = new MinarPantanir();
	    switch (view.getId()) {
	        case R.id.sidasta_pontun:
	        	new ErTilPontun().execute();
	            break;
	        case R.id.allar_pantanir:
	        	fragment = new AllarPantanir();
	            break;
	        default:
	            break;
	    }
	    MainActivity.updateFragment(fragment);
	}
	
	/**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja næstu pöntun notandans úr gagnagrunni
     * og birta hana í textasvæði. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni 
     * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	public class ErTilPontun extends AsyncTask<String, String, String> {
	
		int success;
		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið ég að 
		 * sækja pöntun hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			MainActivity.showDialog("Sæki pöntun..");
		}
		
		@Override
		/**
		 * Sækir nýjustu pöntun notandans úr
		 * gagnagrunni (ef það er einhver til)
		 */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.getEmail()));
			params.add(new BasicNameValuePair("sidastaPontun", "ff")); 
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					url_pantanir, "GET", params);
			
			try{
				success = json.getInt("success");
				if(success == 1){
					JSONArray pantanir = json.getJSONArray("pantanir");
					JSONObject pontun = pantanir.getJSONObject(0);
					FerlaBokun.setPontunText(pontun.getString("nafn") + "\n" + pontun.getString("adgerd") + "\n"
					+ "Starfsmadur: " + MainActivity.getStarfsmadur(pontun.getString("staff_id")) +"\n"
					+ "Klukkan: "+ pontun.getString("time"));
					
					// Upplýsingar um pöntun notandans
					FerlaBokun.setAr(pontun.getString("startDate").substring(0,4));
					FerlaBokun.setManudur(pontun.getString("startDate").substring(5,7));
					FerlaBokun.setDagur(pontun.getString("startDate").substring(8,10));
					FerlaBokun.setID(pontun.getString("ID"));
					
				}
			}
			catch(JSONException e){
				e.printStackTrace();
				
			}
          
			return null;
		}
		
		/**
		 * Lokar „progress dialog" og kallar á aðferð sem að birtir næstu pöntun
		 * á skjánum eða tilkynningu um að engin pöntun hafi fundist
		 * **/
		protected void onPostExecute(String file_url) {
			MainActivity.hideDialog();
			if(success==1){
				Fragment fragment = new NaestaPontun(); 
				MainActivity.updateFragment(fragment);
			}
			else {
				MainActivity.showToast("Engin pöntun fannst", getActivity());
			}
		}
	}
}