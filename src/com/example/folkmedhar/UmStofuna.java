/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um stofuna, eins og staðsetningu og
 * opnunartíma
 */

package com.example.folkmedhar;


import com.example.folkmedhar.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * Það á eftir að klára að útfæra þennan klasa
 */
public class UmStofuna extends BaseActivity {
	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um stofuna
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_um_stofuna);
		
		Button buttonStarfsfolk = (Button) this.findViewById(R.id.staff_text);
		buttonStarfsfolk.setOnClickListener(new View.OnClickListener() {
			/**
			 * Kallað á aðferð sem birtir skjá með upplýsingum um starfsfólk
			 * stofunnar
			 */
			public void onClick(View v) {
				getStarfsfolk();
				}
			});
		}
	
	/**
	 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
	 */
	public void getStarfsfolk() {
		Intent intent = new Intent(this, Starfsfolk.class);
		this.startActivity(intent);
		}
	}
