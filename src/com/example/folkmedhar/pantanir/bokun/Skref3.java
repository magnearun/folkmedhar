/**
 * @author: Birkir Pálmason
 * @since: 15.10.2014
 * Klasinn sem sér um að færa pöntun notandans yfir í gagnagrunn
 */

package com.example.folkmedhar.pantanir.bokun;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.folkmedhar.BaseActivity;
import com.example.folkmedhar.pantanir.JSONParser;
import com.example.folkmedhar.R;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Skref3 extends BaseActivity {
	
	private ProgressDialog pDialog;	
	JSONParser jsonParser = new JSONParser();
	private String url_panta_tima = "http://prufa2.freeiz.com/pantatima.php";
	
	// Viðmótshlutir
	private Button buttonTilbaka;
	private Button buttonPanta;
	private TextView clientInformation;
	
	private String[] heiti = { "nafn", "simi", "starfsmadur", "adgerd", 
			"harlengd","date","time"};
	private String[] heitistreng = { BaseActivity.nafn,BaseActivity.simi,
			BaseActivity.starfsmadur,BaseActivity.adgerd,BaseActivity.harlengd,
			BaseActivity.date,BaseActivity.time};
	private String[] heiti1 = { "nafn", "simi", 
			"adgerd", "harlengd","time","staff_id","email","lengd", "dagur", "startDate", "endDate"};
	private String[] heitistreng1 = { BaseActivity.nafn,BaseActivity.simi,BaseActivity.adgerd,BaseActivity.harlengd,
			BaseActivity.time,BaseActivity.staff_id,BaseActivity.email,BaseActivity.lengd,
			BaseActivity.dagur, BaseActivity.startDate, BaseActivity.endDate};
	
	
	
	
	@Override
	/**
	 * Birtir viðmótið fyrir skref 3 í bókunarferlinu og gefur tilviksbreytum fyrir
	 * viðmótshluti gildi. Auk þess er OnClickListener tengdur við takka sem notaðir eru
	 * til að fara aftur í skref 2 eða staðfesta bókun
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skref3);
		settingText();
		buttonTilbaka = (Button) findViewById(R.id.til_Baka);
        buttonTilbaka.setOnClickListener(new View.OnClickListener() {
            /**
             * Birtir skjáinn fyrir skref 2
             */
        	public void onClick(View v) {
            	startActivity(intents[2]); //Skref2
            }
        });
        
        buttonPanta = (Button) findViewById(R.id.panta);
        buttonPanta.setOnClickListener(new View.OnClickListener() {
            /**
             * Kallar á aðferð sem sér um að færa upplýsingar um bókun
             * yfir í gagnagrunn og birtir skjá með yfirliti bókunar
             */
        	public void onClick(View v) {
            	new Stadfesta().execute(); 
            	startActivity(intents[4]); //Staðfesting bókunar
            }
        });
		
	}
	
	/**
	 * Birtir upplýsingar um bókun í TextView
	 */
	public void settingText(){
		clientInformation = new TextView(this);
		Resources res = getResources();
		for(int i=0;i<heiti.length;i++){
			int id = res.getIdentifier(heiti[i], "id", getBaseContext().getPackageName());
			clientInformation=(TextView)findViewById(id);
			clientInformation.setText(heitistreng[i]);
		}
	}
	
	/**
	 * Færir bókun notandans yfir í gagnagrunn. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
	 */
	class Stadfesta extends AsyncTask<String, String, String> {

		private  String TAG_SUCCESS = null;

		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið sé að ferla
		 * pöntun hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Skref3.this);
			pDialog.setMessage("Bóka tíma..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		/**
		 * Færir upplýsingar um bókun notandans yfir í gagnagrunn
		 */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for(int i=0;i<heiti1.length;i++){
				params.add(new BasicNameValuePair(heiti1[i], heitistreng1[i]));
			}
			
			JSONObject json = jsonParser.makeHttpRequest(url_panta_tima,
					"POST", params);
			
			Log.d("Create Response", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					finish();
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Lokar „progress dialog"
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
}
