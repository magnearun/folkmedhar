/**
 * @author: Magnea Rún Vignisdóttir
 * @since: 20.11.2014
 * Klasinn sem sér um að kalla á klasa sem sér um að sækja allar pantanir innskráðs notanda
 * úr gagnagrunni. Klasinn birtir svo þessar pantanir á skjánum.
 */

package com.example.folkmedhar.pantanir;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.folkmedhar.DatabaseHandler;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;

public class AllarPantanir extends Fragment  {

	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ; 

	/**
	 * Nýtt fragment er búið til fyrir allar pantanir notandans
	 */
	public AllarPantanir() {
	}

	@Override
	/**
	 * Birtir layout-ið fyrir yfirlit allra pantana og upphafsstillir
     * tilviksbreytur
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_allar_pantanir,
				container, false);
		mainListView = (ListView) rootView.findViewById( R.id.mainListView ); 
        ArrayList<String> pantanir = new ArrayList<String>();  
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, pantanir);  
        
        new SaekjaAllarPantanir().execute();
        MainActivity.setSelectedDrawer(2);
		return rootView;
	}
	
	/**
     * @author: Magnea Rún Vignisdóttir
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja allar pantanir notandans úr gagnagrunni
     * og birta þær í lista. Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni 
     * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	private class SaekjaAllarPantanir extends AsyncTask<String, String, String> {
		
		@Override
		/**
		 * Birtir skilaboð sem gefa notandanum til kynna að verið ég að 
		 * sækja pantanir hans
		 * */
		protected void onPreExecute() {
			super.onPreExecute();
			MainActivity.showDialog("Sæki pantanir...");
		}
		
		@Override
		/**
		 * Sækir allar pantanir notanda úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {
			DatabaseHandler.handleAllarPantanir(listAdapter);
			return null;
		}
		
		/**
		 * Lokar „progress dialog" og kallar á aðferð sem að birtir 
		 * allar pantanir á skjánum
		 * **/
		protected void onPostExecute(String file_url) {
			MainActivity.hideDialog();
			mainListView.setAdapter( listAdapter ); 
		}
	}
}