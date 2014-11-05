/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um stofuna, eins og staðsetningu og
 * opnunartíma
 */

package com.example.folkmedhar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
 * Það á eftir að klára að útfæra þennan klasa
 */
public class UmStofuna extends Fragment implements android.view.View.OnClickListener {

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
		
		Button buttonStarfsfolk = (Button) rootView.findViewById(R.id.staff_text);
		
		buttonStarfsfolk.setOnClickListener(this);
		return rootView;
	}
	
	/**
	 * Kallað á aðferð sem birtir skjá með upplýsingum um starfsfólk
	 * stofunnar
	 */
    @Override
	public void onClick(View view) {
    	Fragment fragment = new Starfsfolk();
	    MainActivity.updateFragment(fragment);
	}
	
}

