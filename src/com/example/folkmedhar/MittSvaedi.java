/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem sér um að birta skjá þar sem hægt er að velja um að fá
 * yfirlit yfir allar pantanir eða síðustu pöntun
 */

package com.example.folkmedhar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MittSvaedi extends Fragment implements  android.view.View.OnClickListener  {
	/**
	 * Nýtt fragment er búið til fyrir „Mínar pantanir"
	 */
	public MittSvaedi() {
	}

	@Override
	/**
	 * Birtir layout-ið fyrir „Mínar pantanir" og tengir onClickListener við takka sem notaðir eru
     * til að fá yfirlit yfir allar pantanir eða síðustu pöntun
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mitt_svaedi,
				container, false);
		
		((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_mitt_svaedi);
		
		Button buttonSidastaPontun  = (Button) rootView.findViewById(R.id.sidasta_pontun);
		Button buttonAllarPantanir  = (Button) rootView.findViewById(R.id.allar_pantanir);
		
		buttonSidastaPontun.setOnClickListener(this);
		buttonAllarPantanir.setOnClickListener(this);

		
		return rootView;
	}

	/**
	 * Birtir skjá með yfirliti yfir síðustu pöntun notandans eða
	 * Birtir skjá með yfirliti yfir allar pantanir notandans
	 */
	@Override
	public void onClick(View view) {
		Fragment fragment = new MittSvaedi();
	    FragmentManager fragmentManager = getFragmentManager();
	    switch (view.getId()) {
	        case R.id.mittSvaedi:
	        	startActivity(MainActivity.intents[6]); // SíðastaPontun
	            break;
	        case R.id.panta:
	        	startActivity(MainActivity.intents[7]); // AllarPantanir
	            break;
	        default:
	            break;
	    }
	    fragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment)
        .commit();
	}
}


