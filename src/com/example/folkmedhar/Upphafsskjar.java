/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.folkmedhar.pantanir.FerlaBokun;
import com.example.folkmedhar.pantanir.MinarPantanir;
import com.example.folkmedhar.pantanir.bokun.Skref1;


public class Upphafsskjar extends Fragment implements  android.view.View.OnClickListener {
	
	// Viðmótshlutir
	private Button buttonPantaTima;
	private Button buttonMittSvaedi;
	
	private View rootView;
	
	/**
	 * Nýtt fragment er búið til fyrir upphafsskjá
	 */
	public Upphafsskjar() {
		if(FerlaBokun.getBokudPontun()) {
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
			MainActivity.showToast("Engin nettenging!", getActivity());
		}
	}
	
	/**
	 * Hreinsar „backstack" fyrir „Fragmentin" þannig að ef smellt er a til baka takkann
	 * í símanum þá er farið út úr appinu
	 */
	private void clearBackStack() {
		MainActivity.getFM().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FerlaBokun.setBokudPontun(false); // Bara hreinsa „backstack" ef síðasta fragmentið er síðasta
										    // skref bókunarferlisins
	}
}
