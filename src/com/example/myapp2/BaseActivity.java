/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	
	public Intent[] intents = new Intent[5];
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		
		intents[0] = new Intent(this, Skref1.class);
        intents[1] = new Intent(this, MittSvaedi.class);
		intents[2] = new Intent(this, UmStofuna.class);
		intents[3] = new Intent(this, SidastaPontun.class);
		intents[4] = new Intent(this, AllarPantanir.class);
	}

	 // Eftir: Búið er að bæta viðeigandi aðgerðum á aðgerðaskrá
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	 	getMenuInflater().inflate(R.menu.main, menu);
	 	return true;
	 }

	 // Eftir: Búið er að kalla á aðferð sem sér um að opna þann skjá sem tilheyrir
	 //        aðgerðinni sem var valin í aðgerðaslá.
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	 	// Handle action bar item clicks here. The action bar will
	 	// automatically handle clicks on the Home/Up button, so long
	 	// as you specify a parent activity in AndroidManifest.xml.
	 	switch (item.getItemId()) {
	        case R.id.panta:
	        	startActivity(intents[0]);	// activity_skref1
	            return true;
	        case R.id.mitt_svaedi: 
	        	startActivity(intents[1]); // activity_mitt_svaedi
	            return true;
	        case R.id.about:
	        	startActivity(intents[2]); // activity_um_stofuna
	            return true;
	        case R.id.logout:
	            logout();
	            return true;
	        default:
	            return false;
		}
	 }
	 	
	 // Notandinn hefur verið skráður út úr kerfinu
	 public void logout(){
	 }
}
