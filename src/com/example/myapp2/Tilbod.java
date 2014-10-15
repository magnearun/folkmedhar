/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir upplýsingar um tilboð sem stofan býður upp á
 */

package com.example.myapp2;

import com.example.myapp2.R;
import android.os.Bundle;

public class Tilbod extends BaseActivity {

	@Override
	/**
	 * Birtir skjá sem sýnir þau tilboð sem stofan býður upp á
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tilbod);
	}
}
