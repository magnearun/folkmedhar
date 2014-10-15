/**
 * @author: Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að sækja allar pantanir innskráðs notanda
 * úr gagnagrunni og birta þær á skjánum
 */

package com.example.myapp2.pantanir;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp2.BaseActivity;
import com.example.myapp2.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;




public class AllarPantanir extends BaseActivity {

	TextView responseTextView;
	
	private ProgressDialog pDialog;
	String s;
	
	JSONParser jsonParser = new JSONParser();
	
	private final String url_minar_sidur = "http://prufa2.freeiz.com/minarSidur2.php";
	
    @Override
    /**
     * Birtir layout-ið fyrir yfirlit allra pantana og upphafsstillir
     * tilviksbreytur
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allar_pantanir);
        
        responseTextView = (TextView) findViewById(R.id.responseTextView);
        responseTextView.setMovementMethod(new ScrollingMovementMethod());
        
        new BirtaPantanir().execute();
    }
     
  
    /* Eftir að laga */
    /**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja allar pantanir notandans úr gagnagrunni
     * og birta þær í textasvæði
     */
	class BirtaPantanir extends AsyncTask<String, String, String> {
		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið ég að 
		 * sækja pantanir hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllarPantanir.this);
			pDialog.setMessage("Sæki pantanir..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		/**
		 * Sækir allar pantanir notanda úr gagnagrunni og býr til streng
		 * með upplýsingunum
		 */
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// updating UI from Background Thread
					
			// Check for success tag
			
			int success;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("kt", "33"));
			
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
								+ pontun.getString("endDate") + "\n\n";
						}
					}
				} 
			
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			setText();
			}
		}
	
	/**
	 * Birtir yfirlit allra pantana í TextView
	 */
	public void setText(){
		responseTextView.setText("Þínar pantanir: \n\n" + s);	
		}
	}