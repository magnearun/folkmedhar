/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem birtir bókun notandans
 */

package com.example.myapp2;

import com.example.myapp2.R;
import android.os.Bundle;


public class StadfestingBokunar extends BaseActivity {

	@Override
	/**
	 * Birtir skjá sem sýnir bókun notandans
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bokun);
	}
	
}
