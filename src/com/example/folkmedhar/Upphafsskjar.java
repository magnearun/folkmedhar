/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.folkmedhar.pantanir.bokun.Skref1;


	public class Upphafsskjar extends Fragment implements  android.view.View.OnClickListener {
		
		Button buttonPantaTima;
		Button buttonMittSvaedi;
		
		/**
		 * Nýtt fragment er búið til fyrir upphafsskjá
		 */
		public Upphafsskjar() {
		}

		@Override
		/**
	     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
	     * til að panta tíma eða fara á „Mitt svæði"
	     */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_upphafsskjar,
					container, false);
			
			((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_upphafsskjar);
			
			buttonMittSvaedi = (Button) rootView.findViewById(R.id.mittSvaedi);
			buttonPantaTima = (Button) rootView.findViewById(R.id.panta);
			
			buttonPantaTima.setOnClickListener(this);
			buttonMittSvaedi.setOnClickListener(this);
			
			return rootView;
		}

		/**
		 * Birtir fyrsta skjáinn í pöntunarferlinu eða kallar 
		 * Kallar á aðferð sem að birtir aðgerðslá fyrir „Mínar pantanir"
		 */
		@Override
		public void onClick(View view) {
			Fragment fragment = null;
    	    FragmentManager fragmentManager = getFragmentManager();
		    switch (view.getId()) {
		        case R.id.mittSvaedi:
		        	fragment = new MittSvaedi();
		            break;
		        case R.id.panta:
		        	fragment = new Skref1();
		            break;
		        default:
		            break;
		    }
		    fragmentManager.beginTransaction()
	        .replace(R.id.content_frame, fragment)
	        .addToBackStack("fragment")
	        .commit();
		}
	}
