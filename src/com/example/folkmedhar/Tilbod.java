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

/*
 * Það á eftir að klára að útfæra þennan klasa
 */
public class Tilbod extends Fragment  {

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
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_tilbod);

		return rootView;
	}

}
	
