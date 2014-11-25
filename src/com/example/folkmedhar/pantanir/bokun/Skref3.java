/**
 * @author: Birkir Pálmason og Eva Dögg Steingrímsdóttir
 * @since: 20.11.2014
 * Klasinn sér um að kalla á klasa sem sér um að færa pöntun notandans yfir í gagnagrunn. 
 * Hann sér einnig um að kalla á aðferðir sem að útfæra ámnningu fyrir bókun og birtir 
 * upplýsingar um bókun á skjá.
 */

package com.example.folkmedhar.pantanir.bokun;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.DatabaseHandler;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.Upphafsskjar;
import com.example.folkmedhar.pantanir.FerlaBokun;


public class Skref3 extends Fragment implements android.view.View.OnClickListener {
	
	
	// Viðmótshlutur sem að birtir bókun notandans
	private TextView bokunTexView;
	
	// Id þeirra viðmótshluta sem sýna bókun notandans
	private String[] vidmotsID = { "nafn", "simi", "starfsmadur", "adgerd", 
				"harlengd","date","time"};
	
	// Upplýsingar um bókun sem eru birtar notandanum
	private String[] bokunArray = { FerlaBokun.getName(),FerlaBokun.getSimi(),
				FerlaBokun.getStarfsmadur(),FerlaBokun.getAdgerd(),
				FerlaBokun.getHarlengd(),FerlaBokun.getStringDate(),FerlaBokun.getTime()};
	
	
	private static int success; // er true ef tókst að færa pöntun í gagnagrunn
	private View rootView;
	private Context context;
	
	/**
	 * Nýtt fragment er búið til fyrir Skref 3
	 */
	public Skref3() {
	}

	@Override
	/**
	 * Birtir viðmótið fyrir skref 3 í bókunarferlinu og kallar á aðferðir sem upphafsstilla viðmótshluti
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_skref3,
				container, false);

		setVidmotshlutir();
		MainActivity.setSelectedDrawer(1);
		showBokun(); // Birta upplýsingar um bókun
		context = getActivity();
		return rootView;
	}
		
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti og OnClickListener er
	 * tengdur við takka sem notaðir eru til að fara aftur í skref 2 eða bóka
	 * pöntun
	 */
	private void setVidmotshlutir() {
		Button buttonPanta = (Button) rootView.findViewById(R.id.panta);  
		buttonPanta.setOnClickListener(this);
	}
	
	/**
	 * Birtir skjáinn fyrir skref 2 eða
	 * Kallar á aðferð sem sér um að færa upplýsingar um bókun
	 * yfir í gagnagrunn og birtir skjá með yfirliti bókunar ef notandinn er nettengdur.
	 * Annars eru birt villuskilaboð
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = null;
	    switch (view.getId()) {
	        case R.id.panta:
	        	if (Connection.isOnline(getActivity())) {
		        	new Stadfesta().execute();
		        	FerlaBokun.setBokudPontun(true);
		        	fragment = new Upphafsskjar();
	        	}
	        	else {
	    			MainActivity.showToast("Engin nettenging!", getActivity());
	        		return;
	    		}
	            break;
	        default:
	            break;
	    }
	    MainActivity.updateFragment(fragment);
	}
	
	/**
	 * Birtir upplýsingar um bókun í TextView
	 */
	private void showBokun(){

		bokunTexView = new TextView(getActivity());
		Resources res = getResources();

		// Sækir þá viðmótshluti sem hafa Id sem geymt er í heiti fylkinu
		for(int i=0;i<vidmotsID.length;i++){
			int id = res.getIdentifier(vidmotsID[i], "id", getActivity().getBaseContext().getPackageName());
			bokunTexView=(TextView)rootView.findViewById(id);
			bokunTexView.setText(bokunArray[i]);
		}
	}	
	
	/**
	 * Færir bókun notandans yfir í gagnagrunn. Við útfærslu klasanns var stuðst við tutorial um 
	 * hvernig skal nota JSON til að ná í upplýsingar ýr MySQL gagnagrunni
	 * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
	 */
	private class Stadfesta extends AsyncTask<String, String, String> {

		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið sé að ferla
		 * pöntun hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			MainActivity.showDialog("Bóka tíma...");
		}
		
		@Override
		/**
		 * Færir upplýsingar um bókun notandans yfir í gagnagrunn ef engar villur fundust.
		 * Annars eru birt villuskilaboð. Uppfærir breytur sem halda utan um tímasetningu áminningar
		 */
		protected String doInBackground(String... args) {

			success = DatabaseHandler.handlePontun();
			
			if(success==1) {
				FerlaBokun.setTimeReminder(FerlaBokun.getReminderTime(),context);
			}
			
			return null;
		}
		
		/**
		 * Lokar „progress dialog" og bókarpöntun ef
		 * pöntunin tókst
		 * **/
		protected void onPostExecute(String file_url) {
			MainActivity.hideDialog();
			if (success == 1) {
				FerlaBokun.scheduleReminder(context);
				showMessage("Pöntunin þín hefur verið bókuð");
			} else {
				showMessage("Ekki tókst að bóka pöntun. Vinsamlegast reyndu aftur");
			}
		}
	}
	
	/**
	 * Birtir AlertDialog með skilaboðunum message
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	private void showMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(final DialogInterface dialog, final int which) {
		   }
		});
		alertDialog.show();
	}
}
