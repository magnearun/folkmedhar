/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.folkmedhar.pantanir.bokun.Skref1;


	public class Upphafsskjar extends Fragment implements  android.view.View.OnClickListener {
		
		Button buttonPantaTima;
		Button buttonMittSvaedi;
		
		/**
		 * Nýtt fragment er búið til fyrir upphafsskjá
		 */
		public Upphafsskjar() {
			if(MainActivity.getBokudPontun()==true) {
				clearBackStack();
			}
		}

		@Override
		/**
	     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
	     * til að panta tíma eða fara á „Mitt svæði"
	     */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_upphafsskjar,
					container, false);
			
			TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
			text.setText(R.string.title_activity_upphafsskjar);
			
			buttonMittSvaedi = (Button) rootView.findViewById(R.id.mittSvaedi);
			buttonPantaTima = (Button) rootView.findViewById(R.id.panta);
			
			buttonPantaTima.setOnClickListener(this);
			buttonMittSvaedi.setOnClickListener(this);
			
			
			return rootView;
		}

		/**
		 * Birtir fyrsta skjáinn í pöntunarferlinu eða kallar 
		 * Kallar á aðferð sem að birtir aðgerðslá fyrir „Mínar pantanir"
		 */
		@Override
		public void onClick(View view) {
			Fragment fragment = null;
		    switch (view.getId()) {
		        case R.id.mittSvaedi:
		        	fragment = new MittSvaedi();
		            break;
		        case R.id.panta:
		        	fragment = new Skref1();
		            break;
		        default:
		            break;
		    }
		    MainActivity.updateFragment(fragment);
		}
		
		public void clearBackStack() {
			
			MainActivity.fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			MainActivity.setBokudPontun(false);
			
		}
	}
