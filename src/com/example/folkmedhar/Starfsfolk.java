/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um starfsfólk stofunnar
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
	public class Starfsfolk extends Fragment  {

		// Viðmótshlutir
		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		Button afram;
		
		/**
		 * Nýtt fragment er búið til fyrir Starfsfólk
		 */
		public Starfsfolk() {
		}

		@Override
		/**
		 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
		 */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_starfsfolk,
					container, false);
			
			TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
			text.setText(R.string.title_activity_starfsfolk);
			
			return rootView;
		}
	
	}
	
