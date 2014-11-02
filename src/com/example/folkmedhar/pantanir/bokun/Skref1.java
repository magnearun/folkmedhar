/**
 * @author: Birkir Pálmason og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem heldur utan um hvaða hárgreiðslumann, hárlengd og aðgerð
 * notandinn valdi fyrir seinni skref.
 */

package com.example.folkmedhar.pantanir.bokun;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
		
		View rootView;
	
		
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
			rootView = inflater.inflate(R.layout.fragment_skref1,
					container, false);
			
			TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
			text.setText(R.string.title_activity_step1);
			
			velja_starfsmann = (Spinner) rootView.findViewById(R.id.starfsmennSpinner);
	        velja_adgerd = (Spinner) rootView.findViewById(R.id.adgerdSpinner);
	        velja_harlengd = (Spinner) rootView.findViewById(R.id.harlengdSpinner);
	        
	        afram = (Button) rootView.findViewById(R.id.next);
	        afram.setOnClickListener(this);
	        
	        update();
			return rootView;
		}
		
		/**
	     * Breyturnar fyrir hvaða starfsmadur var valinn fá rétt gildi
	     * 
	     */
	    public void setStarfsmadur() {
	    	
	    	MainActivity.starfsmadur = velja_starfsmann.getSelectedItem().toString();
	    	MainActivity.starfsmadurSelection = velja_starfsmann.getSelectedItemPosition();
	    	
	    	switch(MainActivity.starfsmadur) {
	    	
	    	case "Hver sem er":
	    		MainActivity.staff_id = "000";
	    		break;
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
			default: MainActivity.staff_id = "000";
		}
	}
	    
	    /**
	     * Breyturnar fyrir hvaða starfsmadur var valinn fá rétt gildi
	     * 
	     */
	    public void setTimaLengd() {
	    	
	    	MainActivity.adgerd = velja_adgerd.getSelectedItem().toString();
			MainActivity.adgerdSelection = velja_adgerd.getSelectedItemPosition();
			
			MainActivity.harlengd = velja_harlengd.getSelectedItem().toString();
			MainActivity.harlengdSelection = velja_harlengd.getSelectedItemPosition();
	    	
	    	switch(MainActivity.adgerd) {
	    	
	    	case "Dömuklipping":
	    		MainActivity.lengd = "2";
	    		break;
			case "Herraklipping": 
				MainActivity.lengd = "1";
				break;
			case "Barnaklipping" : 
				MainActivity.lengd = "1";
				break;
			case "Heillitun" : 
				MainActivity.lengd = "2";
				break;
			case "Litur í rót" : 
				MainActivity.lengd = "2";
				break;
			case "Strípur" :
				MainActivity.lengd = "3";
				break;
			case "Litun og strípur" : 
				MainActivity.lengd = "3";
				break;
			case "Dömuklipping og litun/strípur" : 
				MainActivity.lengd = "4";
				break;
			case "Herraklipping og litun/strípur" : 
				MainActivity.lengd = "3";
				break;
			case "Greiðsla" : 
				MainActivity.lengd = "2";
				break;
			case "Permanett" : 
				MainActivity.lengd = "4";
				break;
			case "Blástur" : 
				MainActivity.lengd = "2";
				break;
			default: MainActivity.lengd = "ERR";
		}
	}
	    
	    /**
		 * Breyturnar fyrir hárlengd, aðgerð og starfsmann
		 * fá valin gildi og skjárinn fyrir skref 2 er birtur
		 */
	    @Override
		public void onClick(View view) {
	    	setStarfsmadur();
	    	setTimaLengd();

	    	Fragment fragment = new Skref2();
		   
	    	MainActivity.updateFragment(fragment);
		}
	    
	    public void update() {
	    	Spinner starfsmadur = (Spinner) rootView.findViewById(R.id.starfsmennSpinner);
	    	starfsmadur.setSelection(MainActivity.starfsmadurSelection);
	    	
	    	Spinner adgerd = (Spinner) rootView.findViewById(R.id.adgerdSpinner);
	    	adgerd.setSelection(MainActivity.adgerdSelection);
	    	
	    	Spinner harlengd = (Spinner) rootView.findViewById(R.id.harlengdSpinner);
	    	harlengd.setSelection(MainActivity.harlengdSelection);

	    	
	    	//Log.d("kajflkajf",MainActivity.starfsmadur);
			
		}
}
	
