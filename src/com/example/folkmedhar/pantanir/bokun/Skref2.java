/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um annað skref bókunarferlisins. Klasinns sér um að
 * gefa breytunum sem halda utan um tímasetningu bókunar gildi. Hann sér 
 * auk þess um að sækja lausa tíma á valinni dagsetningu úr gagnagrunni
 * og birta þær í Spinner viðmótshluti svo notandinn geti valið úr lausum tímum.
 */

package com.example.folkmedhar.pantanir.bokun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.JSONParser;


public class Skref2 extends Fragment implements android.view.View.OnClickListener {
	
	// Heldur utan um bókaða og lausa tíma fyrir alla
	// starfsmenn
	private static BokadirLausir[] bokadirStarfsmenn;
	
	// Auðkenni allra starfsmanna
	// Sækja frá öðrum stað + randomize MISSING
	private static String[] starfsmenn = {"BOB","PIP","ODO","MRV","EDK","BIP","DOR"};
	
	// Heldur utan um hvaða tímar eru lausir og hjá hvaða starfsmanni
	private static Lausir[] lausirTimarHeild;
	
	// Fjöldi lausra tíma
	private static int lausirNum;
	
	// Tímasetning lausra tíma
	private static String[] lausirString;
	
	private View rootView;
	private static Context context;
	
	private static String url_saekja_lausa_tima = "http://prufa2.freeiz.com/saekja_bokada_tima.php";
	private static ProgressDialog pDialog;
	private static JSONParser jsonParser = new JSONParser();

	// Viðmótshlutir
	private Button buttonTilbaka, buttonAfram, buttonDagur;
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
		
		TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		text.setText(R.string.title_activity_skref2);
		
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
		buttonTilbaka = (Button) rootView.findViewById(R.id.tilbaka);
		buttonAfram = (Button) rootView.findViewById(R.id.afram2);
		buttonDagur = (Button) rootView.findViewById(R.id.buttonDagur);
		
		buttonTilbaka.setOnClickListener(this);
		buttonAfram.setOnClickListener(this);
		buttonDagur.setOnClickListener(this);
	}


	/**
	 * Ef notandinn er ntettengdur: Birtir skjáinn fyrir skref 1, birtir dagatal þar sem hægt er að 
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
	        case R.id.tilbaka:
	        	fragment = new Skref1();
	        	MainActivity.updateFragment(fragment);
	            break;
	        case R.id.afram2:
	        	bokun();
		        break;
	        default:
	            break;
	        }
		}
		else {
			Toast toast = Toast.makeText(getActivity(), 
    				"Engin nettenging!", Toast.LENGTH_LONG);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
		}
	 }

	/**
	 * Uppfærir breytur sem halda utan um staðsetningu tímans 
	 * sem var valinn í Spinnar viðmótshlut. Kallar á aðferð sem að uppfærir auðkenni starfsmanns
	 * ef enginn sérstakur starfsmaður var valinn.
	 */
	private void bokun() {
		Fragment fragment = null;
		// Engin dagsetning valin
    	if(MainActivity.getDate()==null) {
    		Toast toast = Toast.makeText(getActivity(), 
    				"Vinsamlegast veldu dag", Toast.LENGTH_LONG);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
    		return;
    	} 
    	
    	else {
    		setDateInfo();
    		// Notandanum er sama um hver starfsmaðurinn er
    		if(MainActivity.getStaffId().equals("000")) {
    			setStaffId(); // Sækja auðkenni starfsmannsins sem var úthlutað
    			              // tímanum sem var valinn
    			Log.e("Ssss", "ýtti");
    			fragment = new Skref3();
    			MainActivity.updateFragment(fragment);
    		}
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
	 * Sýnir valinn dag og kallar á aðferð sem sækir bókaða tíma fyrir hann
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		buttonDagur.setText(MainActivity.getStringDate());
		new BokadirTimar().execute();
		
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
			
			
			setTimar();
			lausirNum = 0;
			
			// Búa til sameiginlega aðferð fyrir þetta? MISSING
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sæki lausa tíma..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		/**
		 * Upphafsstillir breytur sem halda utan um lausa og bókaða tíma
		 * starfsmanna
		 */
		private void setTimar() {

			// Hér þarf að sækja frekar lengdina á því sem heldur utan um alla
			// starfsmenn, MISSING
			bokadirStarfsmenn = new BokadirLausir[7];
			for (int i = 0; i<7; i++) {
				bokadirStarfsmenn[i] = new BokadirLausir(new Bokadir[18],new Lausir[18]);
			}
			
			
			// Hvert sæti í bokadirStarfsmenn (eitt fyrir hvern starfsmann) er 
			// fyllt af Bokadir hlut sem heldur utan um auðkenni tiltekins starfsmanns
			// og tímasetningu frá 9:00 - 17:30. Allir tímar eru upphafsstilltir sem lausir
			for (int j = 0; j<7; j++) {
				bokadirStarfsmenn[j].laust[0] = new Lausir("09:00",true,starfsmenn[j]);
				bokadirStarfsmenn[j].laust[1] = new Lausir("09:30", true,starfsmenn[j]);
				int b = 9;
						
				for(int i = 2; i<bokadirStarfsmenn[j].laust.length-1; i = i+2) {
					b = b+1;
					String s = b + ":00";
					bokadirStarfsmenn[j].laust[i] = new Lausir(s, true,starfsmenn[j]);
					s = b + ":30";
					bokadirStarfsmenn[j].laust[i+1]  = new Lausir(s,true,starfsmenn[j]);
				}
				
			}
			
			lausirString = new String[18];
			lausirTimarHeild = new Lausir[18];
			
			// Höfum ekki enn fundið neina lausa tíma svo öll sætin í lausirTimarHeild
			// eru fyllt af Lausir hlut sem inniheldur null í öllum tilviksbreytum
			for (int i = 0; i<lausirTimarHeild.length; i++) {	
				lausirTimarHeild[i]  = new Lausir(null,true,null);
			}
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
			
			JSONObject json = jsonParser.makeHttpRequest(
					url_saekja_lausa_tima, "GET", params);
			
			try {
				success = json.getInt("success");
				if(success == 1){
					JSONArray t = json.getJSONArray("pantanir");
					JSONArray bokadir = t.getJSONArray(0);
					
					// Ákveðinn starfsmaður var valinn
					if(staff_id!="000"){
						getBokadirTimar(staff_id,bokadir,-1);
					} else {
						
						// Notendanum er sama um hver starfsmaðurinn er, sækjum bókaða
						// tíma fyrir alla starfsmenn
						for(int i = 0; i<7; i++) {
							getBokadirTimar(starfsmenn[i],bokadir,i);
						}
					}
				}
				
				} catch (JSONException e) {
				e.printStackTrace();
				
			}
			return null;
		}
		
		/**
		 * Sækir allar bókaða tíma á valinni dagsetningu úr gagnagrunni
		 * og setur þá í fylki bókaðra tíma
		 * @param id
		 * @param bokadir
		 * @param num
		 * @throws JSONException
		 */
		private void getBokadirTimar(String id, JSONArray bokadir, int num) throws JSONException {
			
			// Ákveðinn starfsmaður var valinn, sæki bókaða tíma hans
			// úr gagnagrunni. Auðkenni hans er í breytunni MainActivity.staff_id
			if(num==-1){
				for(int i = 0; i < bokadir.length(); i++){
					JSONObject timi = bokadir.getJSONObject(i);
					bokadirStarfsmenn[0].bokad[i] = 
							new Bokadir(timi.getString("time"), timi.getInt("lengd"),timi.getString("staff_id"));	
				}
			}
			
			else {
				for(int i = 0; i < bokadir.length(); i++){
					JSONObject timi = bokadir.getJSONObject(i);
					// Sæki bókaða tíma úr gagnagrunni fyrir þann starfsmann sem
					// sem á auðkennið sem kallaði á aðerðina
					if (timi.getString("staff_id").equals(id)){
						bokadirStarfsmenn[num].bokad[i] = 
								new Bokadir(timi.getString("time"), timi.getInt("lengd"),timi.getString("staff_id"));
					}
				}
			}
		}
			
		
		
		/**
		 * Proses dialog lokað og kallað á aðferðir sem að finna lausa tíma
		 * út frá bókuðum tímum
		 */
		protected void onPostExecute(String file_url) {
			
			// Ákveðinn starfsmaður var valinn
			if(MainActivity.getStaffId()!="000"){
				// Finn lausa tíma starfsmannsins út frá bókuðum tímum hans
				lausirTimar(bokadirStarfsmenn[0].bokad,bokadirStarfsmenn[0].laust);
				// Birti lausa tíma svo notandinn geti valið úr þeim
				setLausirTimar(bokadirStarfsmenn[0].laust);
			}
			
			else {
				// Notandanum er sama um hver stafsmaðurinn er
				// Finn lausa tíma allra stafsmanna út frá bókuðum tímum þeirra
				for (int i = 0; i< 7; i++) {
					lausirTimar(bokadirStarfsmenn[i].bokad,bokadirStarfsmenn[i].laust);
					setLausirTimar(bokadirStarfsmenn[i].laust);	
				}
			}
			setTimeSpinner();
			pDialog.dismiss();
		}
	}
	
	/**
	 * Gefur breytum sem halda utan um þann tíma sem notandinn valdi
	 * rétt gildi
	 */
	private void setDateInfo() {
		
		String time = timiSpinner.getSelectedItem().toString();
		String date = MainActivity.getDate();
		String lengd = MainActivity.getLengd();

		MainActivity.setTime(time);
		
		int timaLengd = Integer.parseInt(lengd);
		String endTime ="";
		
		// Reikna klukkan hvað tíminn endar miðað við valda aðgerð
		for(int j = 0; j<lausirTimarHeild.length; j++) {
			// Fann tímann
			if(lausirTimarHeild[j].getTimi()==time) {
				// Bæti tímalengdina við tímann og fæ endatíma
				if(j+timaLengd >= 17) {
					endTime = "18:00";
				}
				else {
					endTime = lausirTimarHeild[j+timaLengd].getTimi();
				}
			}
		}
		
		MainActivity.setStartEndDate(date + " " + time, date + " " +  endTime);

	 }
	
	/**
	 * Gefur auðkenni starfsmanns rétt gildi ef enginn sérstakur starfsmaður var valinn,
	 * það er að segja auðkenni starfsmannsins sem á tímann sem var valinn
	 */	
	private void setStaffId() {
		
		for(int i = 0; i<lausirTimarHeild.length; i++) {
			if(lausirTimarHeild[i].getTimi()!=null){
				
				// Fann tímann
				if(lausirTimarHeild[i].getTimi().equals(MainActivity.getTime())) {
					
					// Uppfæri hvaða starfsmanni var úthlutaður tíminn
					MainActivity.setStaffId(lausirTimarHeild[i].getId());
					MainActivity.setStarfsmadur(MainActivity.getStarfsmadur(MainActivity.getStaffId()));
					}
				}
			}
		}
	
	
	
	/**
	 * Þeir tímar sem eru bókaðir fá gildið false í fylki lausra tíma sem merkir
	 * að þeir séu ekki lausir
	 * @param a
	 * @param b
	 */
	private static void lausirTimar(Bokadir[] a, Lausir[] b) {

		for(int i = 0; i<a.length; i++) {
			for(int j = 0; j<b.length; j++) {
				if(a[i]!=null){
					// Tíminn er bókaður
					if(a[i].getTimi().equals(b[j].getTimi())) {
						// Skrái samliggjandi tíma sem bókaða miðað
						// við tímalengd aðgerðar
						for(int k = 0; k<a[i].getLengd(); k++) {
							b[j+k].setLaus(false);
							}
						}
					}
				}
			}
		}
	
	
	/**
	 * Setur lausa tíma sem valmöguleika í Spinner viðmótshlut
	 * @param lausirTimar
	 */
	private static void setLausirTimar(Lausir[] lausirTimar) {
		int timaLengd = Integer.parseInt(MainActivity.getLengd());
		

		for(int i = 0; i<lausirTimar.length; i++) {
			
			// Tíminn er laus og hefur ekki áður verið skráður laus hjá öðrum
			// starfsmanni
			if(lausirTimar[i].isLaus()==true && lausirTimarHeild[i].getTimi()==null &&
					!timiLidinn(lausirTimar[i].getTimi())) {
				boolean laust = true;
				int j = 0;
				// Athuga hvort að fjöldi samliggjandi lausra tíma sé nógu mikill
				// fyrir tímalengd aðgerðar
				if( (i+timaLengd) > lausirTimar.length) {
					break;
				}
				while(j<timaLengd && (j+i) < lausirTimar.length) {
					if (lausirTimar[j+i].isLaus()==false) {
						laust = false;
						break;
					}
					j++;
				}
				
				// Fjöldi samliggjandi lausra tíma var nógu mikill fyrir
				// tímalengd aðgerðar og uppfæri fjölda lausra tíma
				if (laust==true) {
					
					// Skrái tímann sem lausan ásamt auðkenni starfsmannsins
					lausirTimarHeild[i].setTimi(lausirTimar[i].getTimi());
					lausirTimarHeild[i].setId(lausirTimar[i].getId());
					lausirString[lausirNum] = lausirTimar[i].getTimi();	
					lausirNum++;
				
				}
			}
		}
	}
	
	/**
	 * Býr til Spinner viðmótshlut sem inniheldur alla lausa tíma fyrir tiltekna
	 * dagsetningu
	 */
	private static void setTimeSpinner() {
		
		String [] spinnerTimar = new String[lausirNum];
		for (int i = 0; i<spinnerTimar.length; i++) {
			spinnerTimar[i] = lausirString[i];
		}
		
		java.util.Arrays.sort(spinnerTimar);
		
		ArrayAdapter<String> spinnerArrayAdapter =
				new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerTimar); 
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timiSpinner.setAdapter(spinnerArrayAdapter);	
		
	}
	
	/**
	 * Skilar true ef að tíminn timi er liðinn miðað við daginn
	 * í dag
	 * @param timi
	 * @return
	 */
	private static boolean timiLidinn(String timi) {
		
		Locale locale = new Locale("IS");
		
		SimpleDateFormat format = new SimpleDateFormat("HHmm",locale);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timiNuna = format.format(Calendar.getInstance().getTime());
		
		String timiSpinner = timi.substring(0,2) + timi.substring(3);
		format = new SimpleDateFormat("yyyMMdd",locale);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dagurNuna = format.format(Calendar.getInstance().getTime());
		
		String dagurSpinner = MainActivity.getDate();
		dagurSpinner = dagurSpinner.substring(0,4) + dagurSpinner.substring(5,7) +
				dagurSpinner.substring(8);
		
		if(dagurSpinner.equals(dagurNuna)) {
			// Gefa starfsmönnum að minnsta kosti hálftíma fyrirvara
			return ((Integer.parseInt(timiSpinner) - 30) - Integer.parseInt(timiNuna) <0);
		}
		return false;	
	}
	
}