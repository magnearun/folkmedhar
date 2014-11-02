/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að sækja virka pöntun notandans úr
 * gagnagrunni og birta hana á skjánum
 */

package com.example.folkmedhar.pantanir;

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

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.MittSvaedi;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.bokun.Skref2;

public class SidastaPontun extends Fragment implements android.view.View.OnClickListener   {
	
	private ProgressDialog pDialog;
	TextView texti;
	TextView dagatal_ar;
	TextView dagatal_manudur;
	TextView dagatal_dagur;
	Button afpanta;
	JSONParser jsonParser = new JSONParser();

	private final String url_afpanta = "http://prufa2.freeiz.com/afpanta.php";


	/**
	 * Nýtt fragment er búið til fyrir síðustu pöntun notandans
	 */
	public SidastaPontun() {
	}

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sidasta_pontun,
				container, false);
		
		((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_sidasta_pontun);
		
		texti = (TextView) rootView.findViewById(R.id.texti);
		dagatal_ar = (TextView) rootView.findViewById(R.id.ar);
		dagatal_manudur = (TextView) rootView.findViewById(R.id.manudur);
		dagatal_dagur = (TextView) rootView.findViewById(R.id.dagur);
		afpanta = (Button) rootView.findViewById(R.id.afpanta);
        afpanta.setOnClickListener(this);
        
        setText(MittSvaedi.t, MittSvaedi.ar, MittSvaedi.manudur, MittSvaedi.dagur);
		
		return rootView;
	}
	
	/**
	 * Breyturnar fyrir hárlengd, aðgerð og starfsmann
	 * fá valin gildi og skjárinn fyrir skref 2 er birtur
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
                params.add(new BasicNameValuePair("id",MittSvaedi.pontunar_ID));
 
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
            	Toast toast = Toast.makeText(getActivity(),"Tíminn þinn hefur verið afpantaður!", Toast.LENGTH_LONG);
            	toast.setGravity(Gravity.CENTER, 0, 0);
            	toast.show();
            }
            Fragment fragment = new MittSvaedi();
		    FragmentManager fragmentManager = getFragmentManager();
		   
		    fragmentManager.beginTransaction()
	        .replace(R.id.content_frame, fragment)
	        .addToBackStack("fragment")
	        .commit();
        }
    }

	public void setText(String pontun, String ar, String manudur, String dagur){
		texti.setText(pontun);
		dagatal_ar.setText(ar);
		dagatal_manudur.setText(manudur);
		dagatal_dagur.setText(dagur);
	}	
}