/**
 * @author: J—n J—nsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class UmStofuna extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_um_stofuna);
		
		Button buttonStarfsfolk = (Button) this.findViewById(R.id.staff);
		buttonStarfsfolk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getStarfsfolk();
				}
			});
		}
	
	// Eva
	public void getStarfsfolk() {
		Intent intent = new Intent(this, Starfsfolk.class);
		this.startActivity(intent);
		}
	}
