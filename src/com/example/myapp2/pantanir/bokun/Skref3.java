/**
 * @author: Birkir Pálmason
 * @since: 15.10.2014
 * Klasinn sem sér um að færa pöntun notandans yfir í gagnagrunn
 */

package com.example.myapp2.pantanir.bokun;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp2.BaseActivity;
import com.example.myapp2.R;
import com.example.myapp2.pantanir.JSONParser;


import android.app.ProgressDialog;
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
		//Log.d("HUNDUUUUUUUUUUUUr",harlengd);
		clientInformation = new TextView(this);
		clientInformation=(TextView)findViewById(R.id.nameOfClient);
		clientInformation.setText(nafn);
		clientInformation=(TextView)findViewById(R.id.telephoneClient);
		clientInformation.setText(simi);
		clientInformation=(TextView)findViewById(R.id.employeeName);
		clientInformation.setText(starfsmadur);
		clientInformation=(TextView)findViewById(R.id.typeName);
		clientInformation.setText(adgerd);
		clientInformation=(TextView)findViewById(R.id.heightClient);
		clientInformation.setText(harlengd);
		clientInformation=(TextView)findViewById(R.id.dateOfHaircut);
		clientInformation.setText(date);
		clientInformation=(TextView)findViewById(R.id.timeOfHaircut);
		clientInformation.setText(time);
		
	}
	
	/**
	 * Færir bókun notandans yfir í gagnagrunn
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
			params.add(new BasicNameValuePair("kt", "23"));
			params.add(new BasicNameValuePair("staff_id", staff_id));
			
			JSONObject json = jsonParser.makeHttpRequest(url_panta_tima,
					"POST", params);
			
			Log.d("Create Response", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
}
