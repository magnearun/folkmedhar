/**
 * @author: Birkir Pálmason
 * @since: 15.10.2014
 * Klasinn sem sér um að færa pöntun notandans yfir í gagnagrunn
 */

/**
 * Búin að refactor-a
 */
package com.example.folkmedhar.pantanir.bokun;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.Upphafsskjar;
import com.example.folkmedhar.pantanir.JSONParser;


public class Skref3 extends Fragment implements android.view.View.OnClickListener {
	
	
	// Viðmótshlutir
	private Button buttonTilbaka;
	private Button buttonPanta;
	private TextView bokunTexView;
	
	int success; // er true ef tókst að færa pöntun í gagnagrunn
	
	
	// Id þeirra viðmótshluta sem sýna bókun notandans
	private String[] heiti = { "nafn", "simi", "starfsmadur", "adgerd", 
				"harlengd","date","time"};
	
	// Upplýsingar um bókun sem eru birtar notandanum
	private String[] heitistreng = { MainActivity.getName(),MainActivity.getSimi(),
				MainActivity.getStarfsmadur(),MainActivity.getAdgerd(),
				MainActivity.getHarlengd(),MainActivity.getStringDate(),MainActivity.getTime()};
	
	// Heiti dálka fyrir bókun í gagnagrunni
	private String[] heiti1 = { "nafn", "simi", 
				"adgerd", "harlengd","time","staff_id","email","lengd", "dagur",
				"startDate", "endDate"};
	
	// Upplýsingar um bókun sem færðar eru í gagnagrunn
	private String[] heitistreng1 = { MainActivity.getName(),MainActivity.getSimi(),
			MainActivity.getAdgerd(),MainActivity.getHarlengd(),MainActivity.getTime(),
			MainActivity.getStaffId(),MainActivity.getEmail(),MainActivity.getLengd(),
			MainActivity.getDate(),MainActivity.getStartDate(), MainActivity.getEndDate()};
	
	private ProgressDialog pDialog;	
	private JSONParser jsonParser = new JSONParser();
	private String url_panta_tima = "http://prufa2.freeiz.com/pantatima.php";

	private Context context;
	private View rootView;
	
	
	
	/**
	 * Nýtt fragment er búið til fyrir Skref 3
	 */
	public Skref3() {
	}

		@Override
		/**
		 * Birtir viðmótið fyrir skref 3 í bókunarferlinu og gefur tilviksbreytum fyrir
		 * viðmótshluti gildi. Kallar á aðferðir sem upphafsstilla viðmótshluti
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_skref3,
					container, false);
			
			//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
			//text.setText(R.string.title_activity_skref3);
			
			setVidmotshlutir();
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
		
		buttonTilbaka = (Button) rootView.findViewById(R.id.til_Baka);
		buttonPanta = (Button) rootView.findViewById(R.id.panta);
        
		buttonTilbaka.setOnClickListener(this);
		buttonPanta.setOnClickListener(this);
	}
	
    
	/**
	 * Birtir skjáinn fyrir skref 2 eða
	 * Kallar á aðferð sem sér um að færa upplýsingar um bókun
	 * yfir í gagnagrunn og birtir skjá með yfirliti bókunar ef notandinn er nettengdur.
	 * Ananrs eru birt villuskilaboð
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = null;
	    switch (view.getId()) {
	        case R.id.til_Baka:
	        	fragment = new Skref2();
	            break;
	        case R.id.panta:
	        	if (Connection.isOnline(getActivity())) {
		        	new Stadfesta().execute();
		        	fragment = new Upphafsskjar();
	        	}
	        	else {
	    			Toast toast = Toast.makeText(getActivity(), 
	        				"Engin nettenging!", Toast.LENGTH_LONG);
	        		toast.setGravity(Gravity.CENTER, 0, 0);
	        		toast.show();
	        		return;
	    		}
	            break;
	        default:
	            break;
	    }
	    MainActivity.updateFragment(fragment);
	}
	
	/**
	 * Kallar á aðferð sem bókar pöntun í gagnagrunn og birtir upplýsingar um
	 * að pöntun hafi verið bókuð
	 */
	@SuppressWarnings("deprecation")
	private void bokaPontun() {
    	AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage("Pöntunin þín hefur verið bókuð");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(final DialogInterface dialog, final int which) {
		   }
		});
		alertDialog.show();
		MainActivity.setBokudPontun(true);
	}
		

	/**
	 * Birtir upplýsingar um bókun í TextView
	 * Sækir upplýsingar um nafn og síma notanda sem eru 
	 */
	public void showBokun(){

		bokunTexView = new TextView(getActivity());
		Resources res = getResources();

		// Sækir þá viðmótshluti sem hafa Id sem geymt er í heiti fylkinu
		for(int i=0;i<heiti.length;i++){
			int id = res.getIdentifier(heiti[i], "id", getActivity().getBaseContext().getPackageName());
			bokunTexView=(TextView)rootView.findViewById(id);
			bokunTexView.setText(heitistreng[i]);
		}
	}	
	
	/**
	 * Færir bókun notandans yfir í gagnagrunn. Við útfærslu klasanns var stuðst við tutorial um 
	 * hvernig skal nota JSON til að ná í upplýsingar ýr MySQL gagnagrunni
	 * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
	 */
	class Stadfesta extends AsyncTask<String, String, String> {

		private  String TAG_SUCCESS = null;

		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið sé að ferla
		 * pöntun hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Bóka tíma..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@SuppressWarnings("deprecation")
		@Override
		/**
		 * Færir upplýsingar um bókun notandans yfir í gagnagrunn ef engar villur fundust.
		 * Annars eru birt villuskilaboð
		 */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for(int i=0;i<heiti1.length;i++){
				params.add(new BasicNameValuePair(heiti1[i], heitistreng1[i]));
				Log.d("ælsfkædslfksæf",params+"");
			}
			
			JSONObject json = jsonParser.makeHttpRequest(url_panta_tima,
					"POST", params);
			
			Log.d("Create Response", json.toString());
			try {
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					getActivity().finish();
				} else {
					AlertDialog alertDialog = new AlertDialog.Builder(context).create();
					alertDialog.setMessage("Ekki tókst að bóka pöntun. Vinsamlegast reyndu aftur");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					   public void onClick(final DialogInterface dialog, final int which) {
					   }
					});
					alertDialog.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * Lokar „progress dialog"
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (success==1) {
				bokaPontun();
			}
		}
	}
}
