/**
 * @author: MAgnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að birta skjá þar sem hægt er að velja um að fá
 * yfirlit yfir allar pantanir eða næstu pöntun
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
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.pantanir.AllarPantanir;
import com.example.folkmedhar.pantanir.JSONParser;
import com.example.folkmedhar.pantanir.NaestaPontun;


public class MinarPantanir extends Fragment implements  android.view.View.OnClickListener  {
	
	private ProgressDialog pDialog;
	private JSONParser jsonParser = new JSONParser();
	
	private static String pontunarId; // Auðkenni pöntunar
	private static String pontunText = ""; // Upplýsingar um pöntun
	private static String manudur, dagur, ar; // Dagsetning pöntunar
	
	private final String url_pantanir = "http://prufa2.freeiz.com/allarPantanir.php";
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
	private class ErTilPontun extends AsyncTask<String, String, String> {
	
		int success;
		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið ég að 
		 * sækja pöntun hans
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
		 * Sækir nýjustu pöntun notandans úr
		 * gagnagrunni (ef það er einhver til)
		 */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", MainActivity.getEmail()));
			Log.e("lskfjslkdfhslkfhsdkfhlsdkfhdl",MainActivity.getEmail());
			params.add(new BasicNameValuePair("sidastaPontun", "ff")); 
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_pantanir, "GET", params);
			
			try{
				success = json.getInt("success");
				if(success == 1){
					JSONArray pantanir = json.getJSONArray("pantanir");
					JSONObject pontun = pantanir.getJSONObject(0);
					pontunText = pontun.getString("nafn") + "\n" + pontun.getString("adgerd") + "\n"
					+ "Starfsmadur: " + MainActivity.getStarfsmadur(pontun.getString("staff_id")) +"\n"
					+ "Klukkan: "+ pontun.getString("time");
					Log.e("kakakakakakakakakaka",pontun.toString());
					
					// Upplýsingar um pöntun notandans
					ar = pontun.getString("startDate").substring(0,4);
					manudur = getManudur(pontun.getString("startDate").substring(5,7));
					dagur = pontun.getString("startDate").substring(8,10);
					pontunarId = pontun.getString("ID");
					
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
			pDialog.dismiss();
			if(success==1){
				Fragment fragment = new NaestaPontun(); 
				MainActivity.updateFragment(fragment);
			}
			else {
				Toast toast = Toast.makeText(getActivity(),"Engin pöntun fannst", Toast.LENGTH_LONG);
            	toast.setGravity(Gravity.CENTER, 0, 0);
            	toast.show();
			}
		}
	}
	
	/**
	 * Skilar mánuði á forminu "Jan" fyrir steng á forminu "01"
	 * @param s
	 * @return
	 */
	public static String getManudur(String s){
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
	
	/**
	 * Skilar upplýsingum um pöntun notandans
	 * @return
	 */
	public static String getPontun() {
		return pontunText;
	}
	
	/**
	 * Skilar árinu sem pöntunin var bókuð á
	 * @return
	 */
	public static String getAr() {
		return ar;
	}
	
	/**
	 * Skilar mánuðinum sem pöntunin var bókuð á
	 * @return
	 */
	public static String getManudur() {
		return manudur;
	}
	
	/**
	 * Skilar deginum sem pöntunin var bókuð á
	 * @return
	 */
	public static String getDagur() {
		return dagur;
	}
	
	/**
	 * Skilar auðkenni pöntunar
	 * @return
	 */
	public static String getID() {
		return pontunarId;
	}
}