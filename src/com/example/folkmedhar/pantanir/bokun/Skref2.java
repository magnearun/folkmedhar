/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um annað skref bókunarferlisins. Klasinns sér um að kalla á aðferðir sem
 * gefa breytunum sem halda utan um tímasetningu bókunar gildi. Hann sér 
 * auk þess um að birta lsua tíma á valinni dagsetningu í Spinner viðmótshluti svo 
 * notandinn geti valið úr lausum tímum.
 */

package com.example.folkmedhar.pantanir.bokun;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.FerlaBokun;
import com.example.folkmedhar.pantanir.JSONParser;


public class Skref2 extends Fragment implements android.view.View.OnClickListener {

	private View rootView;
	private static Context context;
	
    private static String url_saekja_lausa_tima = "http://peoplewithhair.freevar.com/saekja_bokada_tima.php";

	// Viðmótshlutir
	private Button buttonAfram, buttonDagur;
	private static Spinner timiSpinner;
	


	/**
	 * Nýtt fragment er búið til fyrir skref 2 í bókunarferlinu
	 */
	public Skref2() {
	}

	@Override
	/**
	 * Birtir viðmótið fyrir skref 2 í bókunarferlinu og kallar á aðferð sem
	 * gefur tilviksbreytum fyrir viðmótshluti gildi. 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_skref2,
				container, false);

		setVidmotshlutir();
		context = getActivity();		
		updateVidmotshlutir();
		return rootView;
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti og OnClickListener er
	 * tengdur við takka sem notaðir eru til að fara aftur í skref 1 eða áfram í skref 3
	 */
	private void setVidmotshlutir() {
		
		timiSpinner = (Spinner) rootView.findViewById(R.id.timi);
		buttonAfram = (Button) rootView.findViewById(R.id.afram2);
		buttonDagur = (Button) rootView.findViewById(R.id.buttonDagur);
		buttonAfram.setOnClickListener(this);
		buttonDagur.setOnClickListener(this);
	}


	/**
	 * Ef notandinn er nettengdur: Birtir skjáinn fyrir skref 1, birtir dagatal þar sem hægt er að 
	 * velja dagsetningu eða kallar á aðferð sem uppfærir breytur sem halda utan um staðsetningu tímans 
	 * sem var valinn í Spinnar viðmótshlut og uppfærir auðkenni starfsmanns
	 * ef enginn sérstakur starfsmaður var valinn. Ef hann er ekki nettengdur eru birt
	 * villuskilaboð
	 */
	@Override
	public void onClick(View view) {
		if (Connection.isOnline(getActivity())) {
			Fragment fragment = null;
		    switch (view.getId()) {
			    case R.id.buttonDagur:
			    	Intent i = new Intent(getActivity(), CalendarActivity.class);
			        startActivityForResult(i, 1); // Birta dagatal
			        return;
		        case R.id.afram2:
		        	bokun();
			        break;
		        default:
		            break;
		        }
		}
		else {
			MainActivity.showToast("Engin nettenging!", getActivity());
		}
	 }

	/**
	 * Kallar á aðferð sem sér um að ferla bókun ef dagsetning hefur verið valin í 
	 * Spinner viðmótshlut og birtir skjáinn fyrir Skref3
	 */
	private void bokun() {
		Fragment fragment = null;
    	boolean dagsetningValin = FerlaBokun.bokun();
    	if(!dagsetningValin) {
    		MainActivity.showToast("Vinsamlegast veldu dag og tíma", getActivity());
    	}
    	else {
    		fragment = new Skref3();
			MainActivity.updateFragment(fragment);
    	}
	}
    		
	
	/**
     * Viðmótshlut fyrir dagsetningu er gefið það gildi sem síðast
     * var valið og kallað er á aðferð sem sækir bókaða tíma fyrir dagsetninguna
     */
	private void updateVidmotshlutir() {
		
		String date = MainActivity.getStringDate();
		// Dagsetning hefur áður verið valin
		if(date!=null) {
			buttonDagur.setText(date);
			
			// Sæki bókaða tíma fyrir dagsetninguna
			new BokadirTimar().execute();
		}
	}
	
	/**
	 * Sýnir valinn dag á takka og kallar á aðferð sem sækir bókaða tíma fyrir hann
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(!(MainActivity.getStringDate()==null)) {
			new BokadirTimar().execute();
			buttonDagur.setText(MainActivity.getStringDate());
		}	
	}
	
	 /**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja alla bókaða tíma á völdum degi úr
     * gagnagrunni
     */
	private static class BokadirTimar extends AsyncTask<String, String, String> {

		@Override
		/**
		 * Fylkið sem heldur utan um lausa tíma fyrir valinn dag er upphafsstillt
		 * þannig að allir tímar eru lausir fyrir alla starfsmenn
		 */
		protected void onPreExecute() {
			super.onPreExecute();
			FerlaBokun.setTimar();
			FerlaBokun.setLausirNum(0);
			MainActivity.showDialog("Sæki lausa tíma...");
		}
		
		
		@Override
		/**
		 * Kallar á aðferðir sem sækja allar bókaða tíma á valinni dagsetningu 
		 * úr gagnagrunni og setur þá í fylki bókaðra tíma
		 */
		protected String doInBackground(String... args) {

			String dagur = MainActivity.getDate();
			String staff_id = MainActivity.getStaffId();
			
			int success;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("dagur", dagur));
			params.add(new BasicNameValuePair("staff_id", staff_id));
			
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					url_saekja_lausa_tima, "GET", params);
			
			try {
				success = json.getInt("success");
				if(success == 1){
					JSONArray t = json.getJSONArray("pantanir");
					JSONArray bokadir = t.getJSONArray(0);
					
					// Ákveðinn starfsmaður var valinn
					if(!staff_id.equals("000")){
						FerlaBokun.getBokadirTimar(staff_id,bokadir,-1);
					} else {
						
						// Notendanum er sama um hver starfsmaðurinn er, sækjum bókaða
						// tíma fyrir alla starfsmenn
						for(int i = 0; i<7; i++) {
							FerlaBokun.getBokadirTimar(FerlaBokun.getStarfsmenn(i),bokadir,i);
						}
					}
				}
				
				} catch (JSONException e) {
				e.printStackTrace();
				
			}
			return null;
		}
		
		/**
		 * Proses dialog lokað og kallað á aðferðir sem að finna lausa tíma
		 * út frá bókuðum tímum
		 */
		protected void onPostExecute(String file_url) {
			MainActivity.hideDialog();
			FerlaBokun.finnaAllaLausaTima();
			setTimeSpinner();
		}
	}
	
	
	/**
	 * Býr til Spinner viðmótshlut sem inniheldur alla lausa tíma fyrir tiltekna
	 * dagsetningu
	 */
	private static void setTimeSpinner() {
		
		String[] spinnerTimar;
		
		//Einhverjir lausir tímar fundust á valinni dagsetningu
		if(FerlaBokun.getLausirNum()!=0) {
			spinnerTimar = FerlaBokun.getTimeSpinner();
			
		}
		else {
			spinnerTimar = new String[1];
			spinnerTimar[0] = "Tími";
			MainActivity.showToast("Engir lausir tímar fundust á þessum degi",context);
		}
		ArrayAdapter<String> spinnerArrayAdapter =
				new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerTimar); 
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timiSpinner.setAdapter(spinnerArrayAdapter);
		
	}
	
	/**
	 * Skilar Spinner viðmótshlut
	 * @return
	 */
	public static Spinner getTimiSpinner() {
		return timiSpinner;
	}
	
}

