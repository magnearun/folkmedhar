/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að birta skjá þar sem hægt er að velja um að fá
 * yfirlit yfir allar pantanir eða síðustu pöntun
 */

package com.example.myapp2;

import com.example.myapp2.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MittSvaedi extends BaseActivity {


	@Override
	/**
	 * Birtir layout-ið fyrir „Mitt svæði" og tengir onClickListener við takka sem notaðir eru
     * til að fá yfirlit yfir allar pantanir eða síðustu pöntun
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitt_svaedi);
		
		
		Button buttonSidastaPontun = (Button) this.findViewById(R.id.sidasta_pontun);
		buttonSidastaPontun.setOnClickListener(new View.OnClickListener() {
			/**
			 * Birtir skjá með yfirliti yfir síðustu pöntun notandans
			 */
			public void onClick(View v) {
				startActivity(intents[6]); // SíðastaPontun
				}
			});
		
		Button buttonAllarPantanir = (Button) this.findViewById(R.id.allar_pantanir);
		buttonAllarPantanir.setOnClickListener(new View.OnClickListener() {
			/**
			 * Birtir skjá með yfirliti yfir allar pantanir notandans
			 */
			public void onClick(View v) {
				startActivity(intents[7]); // AllarPantanir
				}
			});
		}
}
