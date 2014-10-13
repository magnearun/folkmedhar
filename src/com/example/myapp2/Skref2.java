/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class Skref2 extends BaseActivity {
	
	public Tvennd[] bokadirTimar; 
	public Timar[] lausirTimar;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static String url_saekja_lausa_tima = "http://prufa2.freeiz.com/saekja_bokada_tima.php";
	// Progress Dialog
	private ProgressDialog pDialog;
	
	Button buttonDagur;
	Button buttonTilbaka;
	Button buttonAfram;

	
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	public static TextView date;
	
	Spinner timi;
	ArrayAdapter<String> dataAdapter;
	
	/**
	 * @author: Jón Jónsson
	 * @since: 30.09.2014
	 * Klasinn sem ......
	 */
	private static class Timar
	{
		String timi;
		boolean laus;
	
	 // EFtir: Eva
	 Timar( String t, boolean b )
		{
			timi = t;
			laus= b;
		}
	}
	
	/**
	 * @author: Jón Jónsson
	 * @since: 30.09.2014
	 * Klasinn sem ......
	 */
	private static class Tvennd
	{
		String timi;
		int lengd;
		
		 // EFtir: Eva
		Tvennd( String t, int l )
		{
			timi = t;
			lengd = l;
		}
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skref2);
		
		//bokadirTimar[0] = new Tvennd("10:00",2);
		//bokadirTimar[1] = new Tvennd("12:00",3);
		//bokadirTimar[2] = new Tvennd("14:00",1);
		
		
		
		date = (TextView) findViewById(R.id.date);
		
		
		timi = (Spinner) findViewById(R.id.timi);
		
		buttonDagur = (Button) findViewById(R.id.buttonDagur);
		
		buttonTilbaka = (Button) findViewById(R.id.tilbaka);
        buttonTilbaka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(intents[0]);
            }
        });
        
        buttonAfram = (Button) findViewById(R.id.afram2);
        buttonAfram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(intents[6]);
            }
        });
		
		
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.skref2, menu);
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
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	

	public class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			BaseActivity.date = year + "-" + (month+1) + "-" + day;
			Log.d("HHHHHHNNNJJKK", BaseActivity.date);
			buttonDagur.setText(day + "-" + (month+1) + "-" + year);
			if(view.isShown()){
				
				new BokadirTimar().execute();
			}
			
		}
		
		/**
		 * Background Async Task
		 * */
		class BokadirTimar extends AsyncTask<String, String, String> {

			private  String TAG_SUCCESS = null;
			String dagur = BaseActivity.date;

			/**
			 * Before starting background thread Show Progress Dialog - "skilaboð á meðan verið er að bíða"
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				bokadirTimar = new Tvennd[18];
				lausirTimar = new Timar[18];
				lausirTimar[0] = new Timar("09:00", true);
				lausirTimar[1] = new Timar("09:30", true);
				lausirTimar[2] = new Timar("10:00", true);
				lausirTimar[3] = new Timar("10:30", true);
				lausirTimar[4] = new Timar("11:00", true);
				lausirTimar[5] = new Timar("11:30", true);
				lausirTimar[6] = new Timar("12:00", true);
				lausirTimar[7] = new Timar("12:30", true);
				lausirTimar[8] = new Timar("13:00", true);
				lausirTimar[9] = new Timar("13:30", true);
				lausirTimar[10] = new Timar("14:00", true);
				lausirTimar[11] = new Timar("14:30", true);
				lausirTimar[12] = new Timar("15:00", true);
				lausirTimar[13] = new Timar("15:30", true);
				lausirTimar[14] = new Timar("16:00", true);
				lausirTimar[15] = new Timar("16:30", true);
				lausirTimar[16] = new Timar("17:00", true);
				lausirTimar[17] = new Timar("17:30", true);
				pDialog = new ProgressDialog(Skref2.this);
				pDialog.setMessage("Sæki lausa tíma..");
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
				params.add(new BasicNameValuePair("dagur", dagur));
				
				JSONObject json = jsonParser.makeHttpRequest(
						url_saekja_lausa_tima, "GET", params);
				Log.d("SVAAARRR", json.toString());
			
			try {
				success = json.getInt("success");
				if(success == 1){

			
				JSONArray t = json.getJSONArray("pantanir");
				JSONArray bokadir = t.getJSONArray(0);

				for(int i = 0; i < bokadir.length(); i++){
					JSONObject timi = bokadir.getJSONObject(i);

					bokadirTimar[i] = new Tvennd(timi.getString("time"), timi.getInt("lengd"));	
					
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
			
				lausirTimar(bokadirTimar, lausirTimar);
				setLausirTimar();
				pDialog.dismiss();
			}
		}
	}
	


	
	private  void lausirTimar(Tvennd[] a, Timar[] b) {
		

		for(int i = 0; i<a.length; i++) {

			for(int j = 0; j<b.length; j++) {
				if(a[i]!=null){
				if(a[i].timi.equals(b[j].timi)) {


						
						for(int k = 0; k<a[i].lengd; k++) {
							b[j+k].laus = false;

						
					}
				}
				}
			}
		}
	}
	
	public void setLausirTimar() {
		
		String[] s = new String[18];
		int num = 0;
		
		for(int i = 0; i<lausirTimar.length; i++) {
			if(lausirTimar[i].laus==true) {
				s[num] =  lausirTimar[i].timi;
				num++;
			}
			
		}
		
		
		
		String [] spinnerTimar = new String[num];
		for (int i = 0; i<spinnerTimar.length; i++) {
			spinnerTimar[i] = s[i];
		}
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTimar); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timi.setAdapter(spinnerArrayAdapter);
		//time.setText(s);
	}
	
	

}
