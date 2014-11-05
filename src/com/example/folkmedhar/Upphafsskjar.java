/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.pantanir.bokun.Skref1;


public class Upphafsskjar extends Fragment implements  android.view.View.OnClickListener {
	
	// Viðmótshlutir
	Button buttonPantaTima;
	Button buttonMittSvaedi;
	
	private View rootView;
	
	/**
	 * Nýtt fragment er búið til fyrir upphafsskjá
	 */
	public Upphafsskjar() {

		if(MainActivity.getBokudPontun()==true) {
			clearBackStack(); // Svo að ekki sé hægt að fara aftur
							  // til baka í síðasta skref pöntunarferlisins eftir bókun
		}
	}

	@Override
	/**
     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
     * til að panta tíma eða fara á „Mínar pantanir"
     */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_upphafsskjar,
				container, false);
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_upphafsskjar);
		
		setVidmotshlutir();
		
		return rootView;
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		
		buttonMittSvaedi = (Button) rootView.findViewById(R.id.mittSvaedi);
		buttonPantaTima = (Button) rootView.findViewById(R.id.panta);
		
		buttonPantaTima.setOnClickListener(this);
		buttonMittSvaedi.setOnClickListener(this);
	}

	/**
	 * Ef að notandinn er nettengdur er fyrsti skjárinn í pöntunarferlinu 
	 * birtur eða skjárinn fyrir „Mínar pantanir“. Annars eru birt villuskilaboð.
	 */
	@Override
	public void onClick(View view) {
		if (Connection.isOnline(getActivity())) {
			Fragment fragment = null;
		    switch (view.getId()) {
		        case R.id.mittSvaedi:
		        	fragment = new MinarPantanir();
		            break;
		        case R.id.panta:
		        	fragment = new Skref1();
		            break;
		        default:
		            break;
		    }
		    MainActivity.updateFragment(fragment);
		}
		else {
			Toast toast = Toast.makeText(getActivity(), 
    				"Engin nettenging!", Toast.LENGTH_LONG);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
		}
	}
	
	/**
	 * Hreinsar „backstack" fyrir „Fragmentin" þannig að ef smellt er a til baka takkann
	 * í símanum þá er farið út úr appinu
	 */
	private void clearBackStack() {
		MainActivity.fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		MainActivity.setBokudPontun(false); // Bara hreinsa „backstack" ef síðasta fragmentið er síðasta
										    // skref bókunarferlisins
	}
}
