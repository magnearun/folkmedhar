package com.example.myapp2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp2.step1.afram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Step3 extends Activity {
	private TextView clientInformation;
	// Progress Dialog
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step3);
		settingText();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.step3, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void stadfesta(View view){
		// creating new product in background thread
		new afram().execute();
     }
	
	
	public void settingText(){
		clientInformation = new TextView(this);
		clientInformation=(TextView)findViewById(R.id.nameOfClient);
		clientInformation.setText(MainActivity.nameOfClient);
		clientInformation=(TextView)findViewById(R.id.telephoneClient);
		clientInformation.setText(MainActivity.telephoneClient);
		clientInformation=(TextView)findViewById(R.id.employeeName);
		clientInformation.setText(MainActivity.employeeName);
		clientInformation=(TextView)findViewById(R.id.typeName);
		clientInformation.setText(MainActivity.type);
		clientInformation=(TextView)findViewById(R.id.heightClient);
		clientInformation.setText(MainActivity.height);
		clientInformation=(TextView)findViewById(R.id.dateOfHaircut);
		clientInformation.setText(MainActivity.date);
		clientInformation=(TextView)findViewById(R.id.timeOfHaircut);
		clientInformation.setText(MainActivity.time);
		
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
			pDialog = new ProgressDialog(step1.this);
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
