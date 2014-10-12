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
import android.widget.DatePicker;
import android.widget.TextView;

public class Skref2 extends Activity {
	
	public Timar[] lausirTimar = new Timar[18];
	public Tvennd[] bokadirTimar = new Tvennd[18];
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static String url_saekja_lausa_tima = "http://prufa2.freeiz.com/saekja_bokada_tima.php";
	// Progress Dialog
	private ProgressDialog pDialog;

	
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	public static TextView date;
	
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
		
		date = (TextView) findViewById(R.id.date);
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
	    new BokadirTimar().execute();
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
			BaseActivity.date = year + "-" + (month+1) + "-" + day + " ";
			date.setText(day + "-" + (month+1) + "-" + year);
			new BokadirTimar().execute();
			lausirTimar(bokadirTimar, lausirTimar);
		}
	}
	

	
	/**
	 * Background Async Task
	 * */
	class BokadirTimar extends AsyncTask<String, String, String> {

		private  String TAG_SUCCESS = null;

		/**
		 * Before starting background thread Show Progress Dialog - "skilaboð á meðan verið er að bíða"
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Skref2.this);
			pDialog.setMessage("Leita að lausum tímum...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Building Parameters
			
			int success;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("date", BaseActivity.date));
			//Log.d("staff_id er : ", staff_id);

			
			
			JSONObject json = jsonParser.makeHttpRequest(url_saekja_lausa_tima,
					"GET", params);
			
			// check log cat for response
			Log.d("Create Response", json.toString());
			try {
				success = json.getInt("success");
				if(success == 1){
			
				JSONArray t = json.getJSONArray("lausirTimar");
				JSONArray lausirTimar = t.getJSONArray(0);
				for(int i = 0; i < lausirTimar.length(); i++){
					JSONObject timi = lausirTimar.getJSONObject(i);
					bokadirTimar[i] = new Tvennd(timi.getString("time"), timi.getInt("lengd"));	
				}
				
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
	
	private static void lausirTimar(Tvennd[] a, Timar[] b) {

		for(int i = 0; i<a.length; i++) {

			for(int j = 0; j<b.length; j++) {
				if(a[i].timi.equals(b[j].timi)) {

					for(int k = 0; k<a[i].lengd; k++) {
						b[j+k].laus = false;
					}
				}
			}
		}
		//Log.d("Prufffffffa", a[0].timi);
	}
	
	

}
