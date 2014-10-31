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
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.Upphafsskjar;
import com.example.folkmedhar.pantanir.JSONParser;


	public class Skref2 extends Fragment implements android.view.View.OnClickListener {
		
		public static Tvennd[] bokadirTimar; 
		public static Timar[] lausirTimar;
		String dagur;
		View rootView;
		static Context c;
		
		private static String url_saekja_lausa_tima = "http://prufa2.freeiz.com/saekja_bokada_tima.php";
		private static ProgressDialog pDialog;
		static JSONParser jsonParser = new JSONParser();
		ArrayAdapter<String> dataAdapter;
		
		// Viðmótshlutir
		Button buttonTilbaka, buttonAfram;
		TextView dateTextView;
		static Spinner timi;


		/**
		 * Nýtt fragment er búið til fyrir Skref 2
		 */
		public Skref2() {
		}

		@Override
		/**
		 * Birtir viðmótið fyrir skref 2 í bókunarferlinu og gefur tilviksbreytum fyrir
		 * viðmótshluti gildi. Auk þess er OnClickListener tengdur við takka sem notaðir eru
		 * til að fara aftur í skref 1 eða áfram í skref 3
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_skref2,
					container, false);
			
			((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_skref2);
			
			dateTextView = (TextView) rootView.findViewById(R.id.date_label);
			timi = (Spinner) rootView.findViewById(R.id.timi);
			buttonTilbaka = (Button) rootView.findViewById(R.id.tilbaka);
			buttonAfram = (Button) rootView.findViewById(R.id.afram2);
			c = getActivity();
	        
			buttonTilbaka.setOnClickListener(this);
			buttonAfram.setOnClickListener(this);
	        
			return rootView;
		}
	
    
    /**
	 * Birtir skjáinn fyrir skref 1 eða 3
	 */
		@Override
		public void onClick(View view) {
			Fragment fragment = new Upphafsskjar();
    	    FragmentManager fragmentManager = getFragmentManager();
		    switch (view.getId()) {
		        case R.id.tilbaka:
		        	fragment = new Skref1();
		            break;
		        case R.id.afram2:
		        	getDateInfo();
		        	fragment = new Skref3();
		            break;
		        default:
		            break;
		    }
		    fragmentManager.beginTransaction()
	        .replace(R.id.content_frame, fragment)
	        .commit();
		}
		
		/**
		 * Gefur breytum sem halda utan um þann tíma sem notandinn valdi
		 * rétt gildi
		 */
		public void getDateInfo() {
			MainActivity.time = timi.getSelectedItem().toString();
			Log.d("HALLÓÓÓÓÓÓ", timi.getSelectedItem().toString());
			MainActivity.startDate = MainActivity.dagur + " " + MainActivity.time;
			MainActivity.lengd = "2";
			int x = Integer.parseInt(MainActivity.lengd);
			String endTime ="";
			for(int j = 0; j<lausirTimar.length; j++) {
				if(lausirTimar[j].timi==MainActivity.time) {
					endTime = lausirTimar[j+x].timi;
				}
			}
			
			MainActivity.endDate = MainActivity.dagur + " " + endTime;
		}
		
		

		
		

		
			
			  /**
		     * @author: Magnea Rún Vignisdóttir
		     * @since: 15.10.2014
		     * Klasinn sem sér um að sækja alla bókaða tíma á völdum degi úr
		     * gagnagrunni
		     */
			public static class BokadirTimar extends AsyncTask<String, String, String> {

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
					
					pDialog = new ProgressDialog(c);
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

					String newDagur = MainActivity.dagur;
					
					int success;
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("dagur", newDagur));
					params.add(new BasicNameValuePair("staff_id", MainActivity.staff_id ));
					
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
		
		
		/**
		 * Þeir tímar sem eru bókaðir fá gildið false í fylki lausra tíma sem merkir
		 * að þeir séu ekki lausir
		 * @param a
		 * @param b
		 */
		private static  void lausirTimar(Tvennd[] a, Timar[] b) {
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
		public static void setLausirTimar() {
			
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
			
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, spinnerTimar); 
			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			timi.setAdapter(spinnerArrayAdapter);
		}
	}

