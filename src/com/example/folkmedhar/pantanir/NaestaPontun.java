/**
 * @author: Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um að sækja virka pöntun notandans úr
 * gagnagrunni og birta hana á skjánum
 */

package com.example.folkmedhar.pantanir;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.MinarPantanir;
import com.example.folkmedhar.R;

public class NaestaPontun extends Fragment implements android.view.View.OnClickListener   {
	
	// Viðmótshlutir
	private TextView texti;
	private TextView dagatalAr;
	private TextView dagatalManudur;
	private TextView dagatalDagur;
	private Button buttonAfpanta;
	
	private ProgressDialog pDialog;
	private JSONParser jsonParser = new JSONParser();
	private View rootView;

	private final String url_afpanta = "http://prufa2.freeiz.com/afpanta.php";


	/**
	 * Nýtt fragment er búið til fyrir síðustu pöntun notandans
	 */
	public NaestaPontun() {
	}

	@Override
	/**
	 * /**
     * Birtir layout-ið fyrir skjáinn og upphafsstillir tilviksbreytur
     */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_naesta_pontun,
				container, false);
        
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_sidasta_pontun);
		
		setVidmotshlutir();
        
        setText(MinarPantanir.getPontun(), MinarPantanir.getAr(), MinarPantanir.getManudur(),
        		MinarPantanir.getDagur());
		
		return rootView;
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		texti = (TextView) rootView.findViewById(R.id.texti);
		dagatalAr = (TextView) rootView.findViewById(R.id.ar);
		dagatalManudur = (TextView) rootView.findViewById(R.id.manudur);
		dagatalDagur = (TextView) rootView.findViewById(R.id.dagur);
		buttonAfpanta = (Button) rootView.findViewById(R.id.afpanta);
	    buttonAfpanta.setOnClickListener(this);		
	}
	
	
	/**
	 * Kallar á aðferð sem að eyðir pöntun
	 */
	@Override
	public void onClick(View view) {
    
    	new EydaPontun().execute();
	}
	
	
	/**
     * @author: Magnea Rún Vignisdóttir
     * @since: 01.11.2014
     * Klasinn sem sér um að eyða pöntun úr gagnagrunni. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
    class EydaPontun extends AsyncTask<String, String, String> {
 
    	int success;
    	
    	/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið sé að 
		 * afpanta tímann hans
		 * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Afpanta tíma..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Eyði pöntun
         * */
        protected String doInBackground(String... args) {

            try {
               
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",MinarPantanir.getID()));
 
                JSONObject json = jsonParser.makeHttpRequest(
                        url_afpanta, "POST", params);

                success = json.getInt("success");
               
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * Fer aftur í Mitt svæði þegar pöntun hefur verið eytt
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (success == 1) {
            	Toast toast = Toast.makeText(getActivity(),
            			"Tíminn þinn hefur verið afpantaður!", Toast.LENGTH_LONG);
            	toast.setGravity(Gravity.CENTER, 0, 0);
            	toast.show();
            }
            Fragment fragment = new MinarPantanir();
		    MainActivity.updateFragment(fragment);
        }
    }

    /**
     * Gefur viðmótshlutum sem sjá um að pirta síðustu pöntun notandans 
     * gildi
     * @param pontun
     * @param ar
     * @param manudur
     * @param dagur
     */
	private void setText(String pontun, String ar, String manudur, String dagur){
		texti.setText(pontun);
		dagatalAr.setText(ar);
		dagatalManudur.setText(manudur);
		dagatalDagur.setText(dagur);
	}	
}