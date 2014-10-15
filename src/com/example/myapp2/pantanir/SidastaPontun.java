/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 30.09.2014
 * Klasinn sem sem sér um að sækja virka pöntun notandans úr
 * gagnagrunni og birta hana á skjánum
 */

package com.example.myapp2.pantanir;

import com.example.myapp2.BaseActivity;
import com.example.myapp2.R;
import android.os.Bundle;


public class SidastaPontun extends BaseActivity {

	@Override
	/**
     * Birtir layout-ið fyrir skjáinn
     */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sidasta_pontun);
	}

}
