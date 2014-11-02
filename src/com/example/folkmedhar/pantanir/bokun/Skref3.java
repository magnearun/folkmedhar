/**
 * @author: Birkir Pálmason
 * @since: 15.10.2014
 * Klasinn sem sér um að færa pöntun notandans yfir í gagnagrunn
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.Upphafsskjar;
import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.pantanir.JSONParser;


public class Skref3 extends Fragment implements android.view.View.OnClickListener {
	
	private ProgressDialog pDialog;	
	JSONParser jsonParser = new JSONParser();
	private String url_panta_tima = "http://prufa2.freeiz.com/pantatima.php";
	
	// Viðmótshlutir
	private Button buttonTilbaka;
	private Button buttonPanta;
	
	public TextView clientInformation;
		
	public String[] heiti = { "nafn", "simi", "starfsmadur", "adgerd", 
				"harlengd","date","time"};
	public String[] heitistreng = { MainActivity.nafn,MainActivity.simi,
				MainActivity.starfsmadur,MainActivity.adgerd,MainActivity.harlengd,
				MainActivity.date,MainActivity.time};
	public String[] heiti1 = { "nafn", "simi", 
				"adgerd", "harlengd","time","staff_id","email","lengd", "dagur", "startDate", "endDate"};
	public String[] heitistreng1 = { MainActivity.nafn,MainActivity.simi,MainActivity.adgerd,MainActivity.harlengd,
				MainActivity.time,MainActivity.staff_id,MainActivity.email,MainActivity.lengd,
				MainActivity.dagur, MainActivity.startDate, MainActivity.endDate};
	

	Context c;
	View rootView;
	
	/**
	 * Nýtt fragment er búið til fyrir Skref 3
	 */
	public Skref3() {
	}

		@Override
		/**
		 * Birtir viðmótið fyrir skref 3 í bókunarferlinu og gefur tilviksbreytum fyrir
		 * viðmótshluti gildi. Auk þess er OnClickListener tengdur við takka sem notaðir eru
		 * til að fara aftur í skref 2 eða staðfesta bókun
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_skref3,
					container, false);
			
			TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
			text.setText(R.string.title_activity_skref3);
			
			settingText();

			
			buttonTilbaka = (Button) rootView.findViewById(R.id.til_Baka);
			buttonPanta = (Button) rootView.findViewById(R.id.panta);
	        
			buttonTilbaka.setOnClickListener(this);
			buttonPanta.setOnClickListener(this);
			
			
			c = getActivity();
	
	        
			return rootView;
		}
	
    
    /**
	 * Birtir skjáinn fyrir skref 2 eða
	 * Kallar á aðferð sem sér um að færa upplýsingar um bókun
     * yfir í gagnagrunn og birtir skjá með yfirliti bókunar
	 */
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View view) {
			Fragment fragment = null;
		    switch (view.getId()) {
		        case R.id.til_Baka:
		        	fragment = new Skref2();
		            break;
		        case R.id.panta:
		        	new Stadfesta().execute();
		        	AlertDialog alertDialog = new AlertDialog.Builder(c).create();
	        		alertDialog.setMessage("Pöntunin þín hefur verið bókuð");
	        		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	        		   public void onClick(final DialogInterface dialog, final int which) {
	        		   }
	        		});
	        		// Set the Icon for the Dialog
	        		//alertDialog.setIcon(R.drawable.icon);
	        		alertDialog.show();
	        		MainActivity.bokudPontun=true;
		        	fragment = new Upphafsskjar();
		            break;
		        default:
		            break;
		    }
		    MainActivity.updateFragment(fragment);
		}
		
		//** ath breytti
		//****Dagny
		/**
		 * Birtir upplýsingar um bókun í TextView
		 * Sækir upplýsingar um nafn og síma notanda sem eru 
		 */
		public void settingText(){
			UserFunctions userFunction = new UserFunctions();
			clientInformation = new TextView(getActivity());
			Resources res = getResources();
			int id = res.getIdentifier(heiti[0], "id", getActivity().getBaseContext().getPackageName());
			clientInformation=(TextView)rootView.findViewById(id);
			clientInformation.setText(userFunction.userName(getActivity().getBaseContext()), TextView.BufferType.EDITABLE);
			
			id = res.getIdentifier(heiti[1], "id", getActivity().getBaseContext().getPackageName());
			clientInformation=(TextView)rootView.findViewById(id);
			clientInformation.setText(userFunction.userPhone(getActivity().getBaseContext()), TextView.BufferType.EDITABLE);
			
			for(int i=2;i<heiti.length;i++){
				id = res.getIdentifier(heiti[i], "id", getActivity().getBaseContext().getPackageName());
				clientInformation=(TextView)rootView.findViewById(id);
				clientInformation.setText(heitistreng[i]);
				Log.d("meiri kúkur", (heitistreng[i] == null) + "");
			}
		}	
		
		/**
		 * Færir bókun notandans yfir í gagnagrunn. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
	     * til að ná í upplýsingar ýr MySQL gagnagrunni (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
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
				pDialog = new ProgressDialog(c);
				pDialog.setMessage("Bóka tíma..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
			
			@Override
			/**
			 * Færir upplýsingar um bókun notandans yfir í gagnagrunn
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
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						getActivity().finish();
					} else {
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
			}
		}
	}
