/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem heldur utan um þær breytur og aðferðir sem
 * aðrir klasar forritsins eiga sameiginlegar. Klasinn heldur
 * utan um allar upplýsingar um notandann og þann tíma sem
 * hann bókar.Einnig býr hann til aðgerðaslá sem er aðgengileg
 * á öllum skjám
 */

package com.example.myapp2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapp2.notendur.LoginActivity;
import com.example.myapp2.notendur.UserFunctions;
import com.example.myapp2.pantanir.AllarPantanir;
import com.example.myapp2.pantanir.SidastaPontun;
import com.example.myapp2.pantanir.bokun.Skref1;
import com.example.myapp2.pantanir.bokun.Skref2;
import com.example.myapp2.pantanir.bokun.Skref3;

public class BaseActivity extends Activity {
	
	// Upplýsingar um notandann
	public static String nafn, simi, adgerd, harlengd;
	
	// Tímasetning pöntunar
	public static  String time, dateTime, date;
	
	// Upplýsingar um starfsmann
	public static String staff_id, starfsmadur;
	
	public Intent[] intents = new Intent[11];
	
	

	@Override
	/**
     * Býr til fylki intenta sem aðrir klasar forritsins nota til 
     * að flytja stjórn á milli skjáa
     */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    	intents[0] = new Intent(this, MainActivity.class);
    	intents[1] = new Intent(this, Skref1.class);
    	intents[2] = new Intent(this, Skref2.class);
    	intents[3] = new Intent(this, Skref3.class);
    	intents[4] = new Intent(this, StadfestingBokunar.class);
    	intents[5] = new Intent(this, MittSvaedi.class);
    	intents[6] = new Intent(this, SidastaPontun.class);
		intents[7] = new Intent(this, AllarPantanir.class);
		intents[8] = new Intent(this, UmStofuna.class);
		intents[9] = new Intent(this, Tilbod.class);
		intents[10] = new Intent(this, LoginActivity.class);
	

	}

	 /**
	  * Bætir aðgerðum á aðal aðgerðaslána sem er sýnileg á öllum skjám	
	  */
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	 	getMenuInflater().inflate(R.menu.main, menu);
	 	return true;
	 }

	 /**
	  * Birtir viðeigandi skjá fyrir þann möguleika sem valinn var í aðgerðaslá
	  */
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	 	switch (item.getItemId()) {
	        case R.id.panta:
	        	startActivity(intents[1]);	// Skref1
	            return true;
	        case R.id.mitt_svaedi: 
	        	startActivity(intents[5]); // MittSvaedi
	            return true;
	        case R.id.about:
	        	startActivity(intents[8]); // UmStofuna
	            return true;
	        case R.id.tilbod:
	        	startActivity(intents[9]); // Tilbod
	            return true;
	        case R.id.logout:
	        	logout();
	            return true;
	        default:
	            return false;
		}
	 }
	 
	 /**
	  * Notandi hefur verið skráður út og login síða birt 
	  */
	 public void logout() {
		 UserFunctions.logoutUser(getApplicationContext());
		 intents[10].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intents[10]);
         finish();
	 }
}
