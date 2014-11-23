/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um stofuna, eins og staðsetningu og
 * opnunartíma
 */

package com.example.folkmedhar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Það á eftir að klára að útfæra þennan klasa
 */
public class UmStofuna extends Fragment {

	/**
	 * Nýtt fragment er búið til fyrir upplýsingar um stofuna
	 */
	public UmStofuna() {
	}

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um stofuna
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_um_stofuna,
				container, false);
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_um_stofuna);
		
		return rootView;
	}
}

