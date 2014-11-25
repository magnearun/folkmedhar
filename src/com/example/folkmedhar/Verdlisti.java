/**
 * @author: Birkir Pálmason
 * @since: 20.11.2014
 * Klassinn sér um að kalla á klasa sem sér um að sækja verðlista úr gagnagrunni. Kalsinn birtir svo
 * verðlistann í lista.
 */

package com.example.folkmedhar;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Verdlisti extends Fragment  {
	
	private ListView verdlistiListView ; 
	
	// Fylki sem halda utan um heiti allra aðgerða sem
	// sem stofan býður uppá og verð þeirra
	private static  String[] adgerdFylki;
	private static String[] verdFylki;
	
	/**
	 * Nýtt fragment er búið til fyrir verdlista
	 */
	public Verdlisti() {
	}

	@Override
	/**
	 * Birtir layout-ið fyrir verdlista og upphafsstillir
     * tilviksbreytur
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_verdlisti,
				container, false);
		
		MainActivity.setSelectedDrawer(5);
		verdlistiListView = (ListView) rootView.findViewById( R.id.verdlistiListView ); 
        new SaekjaVerdlista().execute();
		return rootView;
	}
	
	/**
     * @author: Birkir Pálmason
     * @since: 15.10.2014
     * Klasinn sem sér um að sækja verdlista úr gagnagrunni. 
     * Við útfærslu klasanns var stuðst við tutorial um hvernig skal nota JSON 
     * til að ná í upplýsingar ýr MySQL gagnagrunni 
     * (http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/).
     */
	private class SaekjaVerdlista extends AsyncTask<String, String, String> {
	
		/**
		 * Birtir dialog sem gefur notandanaum til kynna að verið sé 
		 * að sækja verðlistann
		 */
		protected void onPreExecute() {
			MainActivity.showDialog("Sæki verðlista...");
		}
		
		@Override
		/**
		 * Sækir verðlista úr gagnagrunni og setur í lista
		 */
		protected String doInBackground(String... args) {
			DatabaseHandler.handleVerdlisti(adgerdFylki, verdFylki);
			return null;
		}
		
		/**
		 * Bætir adapter við verðlistann
		 * **/
		protected void onPostExecute(String file_url) {
			 CustomList adapter = new
				        CustomList(getActivity(), adgerdFylki, verdFylki);
			 verdlistiListView.setAdapter(adapter);
			 MainActivity.hideDialog();

			}
		}
	
	/**
	 * Breyta sem heldur utan um allar aðgerðir (Dömuklipping, litun...)
	 * fær gildið a
	 * @param a
	 */
	public static void setAdgerd(String[] a) {
		adgerdFylki = a;
	}
	
	/**
	 * Breyta sem heldur utan um verð allra aðgerða (Dömuklipping, litun...)
	 * fær gildið v
	 * @param v
	 */
	public static void setVerd(String[] v) {
		verdFylki = v;
	}
}