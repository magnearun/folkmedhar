/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir bókun notandans
 */

package com.example.folkmedhar.pantanir.bokun;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class StadfestingBokunar extends Fragment {

		// Viðmótshlutir
		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		Button afram;
		
		/**
		 * Nýtt fragment er búið til fyrir staðfestingu bókunar
		 */
		public StadfestingBokunar() {
		}

		@Override
		/**
		 * Birtir skjá sem sýnir bókun notandans
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_stadfesting,
					container, false);
			
			((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_bokun);
			return rootView;
		}
		
	}
	
