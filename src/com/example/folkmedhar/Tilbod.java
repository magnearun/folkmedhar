/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 20.11.2014
 * Klasinn sér um að kalla á klase sem sér um að sækja upplýsingar um þau tilboð
 * sem stofan býður upp á úr gagnagrunni. Klasinn birtir svo tilboðin í lista. 
 */

package com.example.folkmedhar;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class Tilbod extends Fragment  {

	private ListView tilbodListView;
	
	// Fylki sem halda utan um heiti og lýsingu 
	// allra tilboða
	private static String[] nafn; 
	private static String[] lysing; 

	/**
	 * Nýtt fragment er búið til fyrir lista tilboða
	 */
	public Tilbod() {
	}

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um tilboð
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tilbod,
				container, false);
		tilbodListView = (ListView) rootView.findViewById( R.id.tilbodListView );
        new SaekjaTilbod().execute();
        MainActivity.setSelectedDrawer(4);
		return rootView;
	}
	/**
     * @author: Birkir Pálmason
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja tilboð úr gagnagrunni. 
     * Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni 
     * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	private class SaekjaTilbod extends AsyncTask<String, String, String> {
	
		/**
		 * Birtir dialog sem gefur notandanaum til kynna að verið sé 
		 * að sækja tilboðin
		 */
		protected void onPreExecute() {
			MainActivity.showDialog("Sæki tilboð...");
		}
		
		@Override
		/**
		 * Sækir tilboð úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {
			DatabaseHandler.handleTilbod(nafn,lysing);
			return null;
		}
		
		/**
		 * Bætir adapter við tilboðs listann
		 * **/
		protected void onPostExecute(String file_url) {
			 CustomList adapter = new
				        CustomList(getActivity(), nafn, lysing);
			 tilbodListView.setAdapter(adapter);
			 MainActivity.hideDialog();

			}
		}
	
	/**
	 * Breyta sem heldur utan um heiti allra tilboða
	 * fær gildið n
	 * @param n
	 */
	public static void setNafn(String[] n) {
		nafn = n;
	}
	
	/**
	 * Breyta sem heldur utan um lýsingu allra tilboða fær 
	 * gildið l
	 * @param l
	 */
	public static void setLysing(String[] l) {
		lysing = l;
	}
}