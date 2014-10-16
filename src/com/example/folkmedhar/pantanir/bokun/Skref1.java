/**
 * @author: Birkir Pálmason og Magnea Rún Vignisdóttir
 * @since: 15.10.2014
 * Klasinn sem heldur utan um hvaða hárgreiðslumann, hárlengd og aðgerð
 * notandinn valdi fyrir seinni skref.
 */

package com.example.folkmedhar.pantanir.bokun;

import com.example.folkmedhar.BaseActivity;
import com.example.folkmedhar.R;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;;

public class Skref1 extends BaseActivity {
		
		// Viðmótshlutir

		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		
			
	    @Override
	    /**
	     * Birtir layout-ið fyrir skref 1 í bókunarferlinu og upphafsstillir
	     * tilviksbreytur fyrir viðmótshluti
	     */
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_skref1);
	        
	        velja_starfsmann = (Spinner) findViewById(R.id.starfsmennSpinner);
	        velja_adgerd = (Spinner) findViewById(R.id.adgerdSpinner);
	        velja_harlengd = (Spinner) findViewById(R.id.harlengdSpinner);
	        
	        	
	        Button afram = (Button) findViewById(R.id.next);
	        afram.setOnClickListener(new View.OnClickListener() {

				@Override
				/**
				 * Breyturnar fyrir hárlengd, aðgerð og starfsmann
				 * fá valin gildi og skjárinn fyrir skref 2 er birtur
				 */
				public void onClick(View view) {
					setStarfsmadur();
					BaseActivity.adgerd = velja_adgerd.getSelectedItem().toString();
					BaseActivity.harlengd = velja_harlengd.getSelectedItem().toString();
					startActivity(intents[2]); // Skref2
				}
			});
	    }
	    
	    /**
	     * Breyturnar fyrir hvaða starfsmadur var valinn fá rétt gildi
	     * 
	     */
	    public void setStarfsmadur() {
	    	starfsmadur = velja_starfsmann.getSelectedItem().toString();
	    	switch(starfsmadur) {
	    	
	    	case "Hver sem er":
				staff_id = "000";
			case "Bambi": 
    			staff_id = "BOB";
    			break;
    		
    		case "Perla" : 
    			staff_id = "PIP";
    			break;
    		
    		case "Oddur" : 
    			staff_id = "ODO";
    			break;
    		
    		case "Magnea" : 
    			staff_id = "MRV";
    			break;
    		
    		case "Eva" :
    			staff_id = "EDK";
    			break;
    		
    		case "Birkir" : 
    			staff_id = "BIP";
    			break;
    		
    		case "Dagný" : 
    			staff_id = "DOR";
    			break;
    		
    		default: staff_id = "ERR";
    	}
	}
}

	     