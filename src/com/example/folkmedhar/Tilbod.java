/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um tilboð sem stofan býður upp á
 */

package com.example.folkmedhar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/*
 * Það á eftir að klára að útfæra þennan klasa
 */
	public class Tilbod extends Fragment  {

		// Viðmótshlutir
		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		Button afram;
		
		/**
		 * Nýtt fragment er búið til fyrir tilboð
		 */
		public Tilbod() {
		}

		@Override
		/**
		 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tilbod,
					container, false);
			
			TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
			text.setText(R.string.title_activity_tilbod);

			
			return rootView;
		}
	
	}
	
