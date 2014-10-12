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


public class MittSvaedi extends BaseActivity {
	
	private Intent[] intents = new Intent[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitt_svaedi);
		
		
		Button buttonSidastaPontun = (Button) this.findViewById(R.id.sidasta_pontun);
		buttonSidastaPontun.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(intents[0]);;
				}
			});
		
		Button buttonAllarPantanir = (Button) this.findViewById(R.id.allar_pantanir);
		buttonAllarPantanir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(intents[1]);;
				}
			});
		
		intents[0] = new Intent(this, SidastaPontun.class);
		intents[1] = new Intent(this, AllarPantanir.class);
		}

}
