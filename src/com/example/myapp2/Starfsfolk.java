/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um starfsfólk stofunnar
 */


package com.example.myapp2;
import com.example.myapp2.R;
import android.os.Bundle;

public class Starfsfolk extends BaseActivity {

	@Override
	/**
	 * Birtir skjá sem sýnir upplýsingar um starfsfólk stofunnar
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starfsfolk);
	}
}
