/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um annað skref bókunarferlisins. Klasinns sér um að
 * gefa breytunum sem halda utan um tímasetningu bókunar gildi. Hann sér 
 * auk þess um að sækja lausa tíma á valinni dagsetningu úr gagnagrunni
 * og birta þær í Spinner viðmótshluti svo notandinn geti valið úr lausum tímum.
 */

package com.example.folkmedhar.pantanir.bokun;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.folkmedhar.BaseActivity;
import com.example.folkmedhar.pantanir.JSONParser;
import com.example.folkmedhar.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class Skref2 extends BaseActivity {
	
	public Tvennd[] bokadirTimar; 
	public Timar[] lausirTimar;
	String dagur;
	
	private String url_saekja_lausa_tima = "http://prufa2.freeiz.com/saekja_bokada_tima.php";
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	ArrayAdapter<String> dataAdapter;
	
	// Viðmótshlutir
	Button buttonDagur, buttonTilbaka, buttonAfram;
	TextView dateTextView;
	Spinner timi;
	
	

	@Override
	/**
	 * Birtir viðmótið fyrir skref 2 í bókunarferlinu og gefur tilviksbreytum fyrir
	 * viðmótshluti gildi. Auk þess er OnClickListener tengdur við takka sem notaðir eru
	 * til að fara aftur í skref 1 eða áfram í skref 3
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skref2);		
		dateTextView = (TextView) findViewById(R.id.date_label);
		timi = (Spinner) findViewById(R.id.timi);
		buttonDagur = (Button) findViewById(R.id.buttonDagur);
		buttonTilbaka = (Button) findViewById(R.id.tilbaka);
		buttonAfram = (Button) findViewById(R.id.afram2);
		
        buttonTilbaka.setOnClickListener(new View.OnClickListener() {
           /**
            * Birtir skjáinn fyrir skref 1
            */
        	public void onClick(View v) {
            	startActivity(intents[1]); //Skref1
            }
        });
        
        
        buttonAfram.setOnClickListener(new View.OnClickListener() {
            /**
             * Birtir skjáinn fer skref 3
             */
        	public void onClick(View v) {
        		getDateInfo();
        		Log.d("hasjdgaskjfgaskjfgsakjfgaskjfgaskjfg",BaseActivity.endDate);
            	startActivity(intents[3]); //Skref3
            }
        });
	}
	
	/**
	 * Gefur breytum sem halda utan um þann tíma sem notandinn valdi
	 * rétt gildi
	 */
	public void getDateInfo() {
		BaseActivity.time = timi.getSelectedItem().toString();
		BaseActivity.startDate = BaseActivity.dagur + " " + BaseActivity.time;
		BaseActivity.lengd = "2";
		int x = Integer.parseInt(lengd);
		String endTime ="";
		for(int j = 0; j<lausirTimar.length; j++) {
			if(lausirTimar[j].timi==BaseActivity.time) {
				endTime = lausirTimar[j+x].timi;
			}
		}
		
		BaseActivity.endDate = BaseActivity.dagur + " " + endTime;
	}

	
	/**
	 * Birtir viðmótshlut þar sem notandinn getur valið dagsetningu
	 * @param v
	 */
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	/**
	 * @author: Eva Dögg Steingrímsdóttir
	 * @since: 15.10.2014
	 * Klasinn sem sem sér um að búa til viðmótshlut þar sem notandinn getur
	 * valið dagsetningu
	 */
	public class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
	
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		/**
		 * Breytan sem heldur utan um hvaða dagur var valinn fær gildi og það gildi
		 * er birt á takka. Kallað er á aðferð sem sér um að sækja lausa tíma
		 * fyrir valinn dag
		 */
		public void onDateSet(DatePicker view, int year, int month, int day) {
			BaseActivity.dagur = year + "-" + (month+1) + "-" + day;
			BaseActivity.date = day + "-" + (month+1) + "-" + year;
			
			buttonDagur.setText(BaseActivity.date);
			if(view.isShown()){
				new BokadirTimar().execute();
			}
			
		}
		
		  /**
	     * @author: Magnea Rún Vignisdóttir
	     * @since: 15.10.2014
	     * Klasinn sem sér um að sækja alla bókaða tíma á völdum degi úr
	     * gagnagrunni
	     */
		class BokadirTimar extends AsyncTask<String, String, String> {

			@Override
			/**
			 * Fylkið sem heldur utan um lausa tíma fyrir valinn dag er upphafsstillt
			 * þannig að allir tímar eru lausir
			 */
			protected void onPreExecute() {
				super.onPreExecute();
				
				bokadirTimar = new Tvennd[18];
				lausirTimar = new Timar[18];

				lausirTimar[0] = new Timar("09:00",true);
				lausirTimar[1] = new Timar("09:30", true);
				int b = 9;
						
				for(int i = 2; i<lausirTimar.length-1; i = i+2) {
					b = b+1;
					String s = b + ":00";
					lausirTimar[i] = new Timar(s, true);
					s = b + ":30";
					lausirTimar[i+1] = new Timar(s,true);
				}
				
				pDialog = new ProgressDialog(Skref2.this);
				pDialog.setMessage("Sæki lausa tíma..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
			
			@Override
			/**
			 * Sækir allar bókaða tíma á valinni dagsetningu úr gagnagrunni
			 * og setur þá í fylki bókaðra tíma
			 */
			protected String doInBackground(String... args) {

				String newDagur = BaseActivity.dagur;
				
				int success;
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("dagur", newDagur));
				params.add(new BasicNameValuePair("staff_id", staff_id ));
				
				JSONObject json = jsonParser.makeHttpRequest(
						url_saekja_lausa_tima, "GET", params);
				
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
					e.printStackTrace();
				}
				return null;
			}
			
			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {			
				lausirTimar(bokadirTimar, lausirTimar);
				setLausirTimar();
				pDialog.dismiss();
			}
		}
	}
	
	/**
	 * Þeir tímar sem eru bókaðir fá gildið false í fylki lausra tíma sem merkir
	 * að þeir séu ekki lausir
	 * @param a
	 * @param b
	 */
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
	
	/**
	 * Setur lausa tíma sem valmöguleika í Spinner viðmótshlut
	 */
	public void setLausirTimar() {
		
		String[] s = new String[18];
		int num = 0;
		for(int i = 0; i<lausirTimar.length; i++) {
			if(lausirTimar[i].laus==true) {
				s[num] = lausirTimar[i].timi;
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
	}
}
