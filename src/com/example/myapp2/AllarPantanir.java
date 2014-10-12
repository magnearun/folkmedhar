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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;




public class AllarPantanir extends BaseActivity {

	TextView responseTextView;
	String kt = "2212902169";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	
	// Progress Dialog
	private ProgressDialog pDialog;
	String s = "";
	
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	
	// single product url
	private static final String url_minar_sidur = "http://prufa2.freeiz.com/minarSidur2.php";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allar_pantanir);
        
    
        this.responseTextView = (TextView) this.findViewById(R.id.responseTextView);
        this.responseTextView.setMovementMethod(new ScrollingMovementMethod());
     
        
        new birtaPantanir().execute();
    }
     
  
    /* Eftir að laga */
    /**
	 * Background Async Task
	 * */
	class birtaPantanir extends AsyncTask<String, String, String> {

		private  String TAG_SUCCESS = null;

		/**
		 * Before starting background thread Show Progress Dialog - "skilaboð á meðan verið er að bíða"
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllarPantanir.this);
			pDialog.setMessage("Sæki pantanir..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// updating UI from Background Thread
					
			// Check for success tag
			int success;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("kt", kt));
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_minar_sidur, "GET", params);
			
			
			try {
				success = json.getInt("success");
				if(success == 1){
				
					JSONArray p = json.getJSONArray("pantanir");
					JSONArray pantanir = p.getJSONArray(0);
					for(int i = 0; i < pantanir.length(); i++){
						JSONObject pontun = pantanir.getJSONObject(i);
						s = s + pontun.getString("nafn") + "\n"
							+ pontun.getString("kt") + "\n"
							+ pontun.getString("adgerd") + "\n"
							+ pontun.getString("startDate") + "\n"
							+ pontun.getString("endDate") + "\n\n"
							;
					}
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
			setText();
			
		}
	}
	
		 public void setText(){
			responseTextView.setText("Þínar pantanir: \n\n" + s);	
	     }

    
}
