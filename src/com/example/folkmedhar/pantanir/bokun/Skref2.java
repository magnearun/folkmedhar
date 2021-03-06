/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 20.11.2014
 * Klasinn sér um annað skref bókunarferlisins með því að kalla á aðferðir sem
 * gefa breytunum sem halda utan um tímasetningu bókunar gildi. Hann sér 
 * auk þess um að birta lausa tíma á valinni dagsetningu í Spinner viðmótshluti svo 
 * notandinn geti valið úr lausum tímum.
 */

package com.example.folkmedhar.pantanir.bokun;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.DatabaseHandler;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.FerlaBokun;


public class Skref2 extends Fragment implements android.view.View.OnClickListener {

	private View rootView;
	private static Context context;
	

	// Viðmótshlutir sem að gefa notanda kleift að velja dagsetningu og
	// tíma bókunar og að komast áfram í skref 3
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
		MainActivity.setSelectedDrawer(1);
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
		
		String date = FerlaBokun.getStringDate();
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
		if(!(FerlaBokun.getStringDate()==null)) {
			new BokadirTimar().execute();
			buttonDagur.setText(FerlaBokun.getStringDate());
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
			MainActivity.showDialog("Sæki lausa tíma...");
			super.onPreExecute();
			FerlaBokun.setTimar();
			FerlaBokun.setLausirNum(0);
		}
		
		
		@Override
		/**
		 * Kallar á aðferðir sem sækja allar bókaða tíma á valinni dagsetningu 
		 * úr gagnagrunni og setur þá í fylki bókaðra tíma
		 */
		protected String doInBackground(String... args) {
			DatabaseHandler.handleTimar();
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
			spinnerTimar[0] = "Veldu tíma";
			FerlaBokun.setTime("Veldu tíma");
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

