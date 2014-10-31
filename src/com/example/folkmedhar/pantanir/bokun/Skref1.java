/**
 * @author: Birkir Pálmason og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem heldur utan um hvaða hárgreiðslumann, hárlengd og aðgerð
 * notandinn valdi fyrir seinni skref.
 */

package com.example.folkmedhar.pantanir.bokun;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;



	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class Skref1 extends Fragment implements android.view.View.OnClickListener {

		// Viðmótshlutir
		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		Button afram;
		
		/**
		 * Nýtt fragment er búið til fyrir Skref 1
		 */
		public Skref1() {
		}

		@Override
		/**
	     * Birtir layout-ið fyrir skref 1 í bókunarferlinu og upphafsstillir
	     * tilviksbreytur fyrir viðmótshluti
	     */
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_skref1,
					container, false);
			
			((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_step1);
			
			velja_starfsmann = (Spinner) rootView.findViewById(R.id.starfsmennSpinner);
	        velja_adgerd = (Spinner) rootView.findViewById(R.id.adgerdSpinner);
	        velja_harlengd = (Spinner) rootView.findViewById(R.id.harlengdSpinner);
	        
	        afram = (Button) rootView.findViewById(R.id.next);
	        afram.setOnClickListener(this);
			return rootView;
		}
		
		/**
	     * Breyturnar fyrir hvaða starfsmadur var valinn fá rétt gildi
	     * 
	     */
	    public void setStarfsmadur() {
	    	MainActivity.starfsmadur = velja_starfsmann.getSelectedItem().toString();
	    	switch(MainActivity.starfsmadur) {
	    	
	    	case "Hver sem er":
	    		MainActivity.staff_id = "000";
			case "Bambi": 
				MainActivity.staff_id = "BOB";
				break;
			
			case "Perla" : 
				MainActivity.staff_id = "PIP";
				break;
			
			case "Oddur" : 
				MainActivity.staff_id = "ODO";
				break;
			
			case "Magnea" : 
				MainActivity.staff_id = "MRV";
				break;
			
			case "Eva" :
				MainActivity.staff_id = "EDK";
				break;
			
			case "Birkir" : 
				MainActivity.staff_id = "BIP";
				break;
			
			case "Dagný" : 
				MainActivity.staff_id = "DOR";
				break;
			
			default: MainActivity.staff_id = "ERR";
		}
	    	
	   }
	    
	    /**
		 * Breyturnar fyrir hárlengd, aðgerð og starfsmann
		 * fá valin gildi og skjárinn fyrir skref 2 er birtur
		 */
	    @Override
		public void onClick(View view) {
	    	setStarfsmadur();
			MainActivity.adgerd = velja_adgerd.getSelectedItem().toString();
			MainActivity.harlengd = velja_harlengd.getSelectedItem().toString();
	    	Fragment fragment = new Skref2();
		    FragmentManager fragmentManager = getFragmentManager();
		   
		    fragmentManager.beginTransaction()
	        .replace(R.id.content_frame, fragment)
	        .commit();
		}
	    
}
	
