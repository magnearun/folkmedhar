/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Skref1 extends BaseActivity {

	EditText kennitala;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	private static String url_panta_tima = "http://prufa2.freeiz.com/pantatima.php";
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skref1);
        
        kennitala = (EditText) findViewById(R.id.kennitala);
        
       // Áfram takki kallar á activity "skref 2"
        Button buttonAfram = (Button) findViewById(R.id.afram);
        buttonAfram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new afram().execute();
			}
		});
    }
    
    /* Þetta Skref færist svo yfir í activity Skref 3  */
    /**
	 * Background Async Task
	 * */
	class afram extends AsyncTask<String, String, String> {

		private  String TAG_SUCCESS = null;

		/**
		 * Before starting background thread Show Progress Dialog - "skilaboð á meðan verið er að bíða"
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Skref1.this);
			pDialog.setMessage("Creating Product..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			String kt = kennitala.getText().toString();
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("kt", kt));
			
			JSONObject json = jsonParser.makeHttpRequest(url_panta_tima,
					"POST", params);
			
			// check log cat for response
			Log.d("Create Response", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					
					
					// closing this screen
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
			// dismiss the dialog once done
			pDialog.dismiss();
		}
	}
	}

     