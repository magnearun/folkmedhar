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



public class Skref1 extends Fragment implements android.view.View.OnClickListener {

	// Viðmótshlutir
	private Spinner velja_starfsmann;
	private Spinner velja_adgerd;
	private Spinner velja_harlengd;
	private Button afram;
	
	private View rootView;

	
	/**
	 * Nýtt fragment er búið til fyrir skref 1 í bókunarferlinu
	 */
	public Skref1() {
		
	}

	@Override
	/**
     * Birtir layout-ið fyrir skref 1 í bókunarferlinu og kallar á aðferð sem upphafsstillir
     * tilviksbreytur fyrir viðmótshluti
     */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_skref1,
				container, false);

		TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		text.setText(R.string.title_activity_step1);
		
		setHlutir();
        
        update();
        
		return rootView;
	}
	
	/**
	 * Breyturnar fyrir fyrir hárlengd, aðgerð og starfsmann fá
	 * valin gildi
	 */
	public void setInfo() {
		
		MainActivity.starfsmadur = velja_starfsmann.getSelectedItem().toString();
    	MainActivity.starfsmadurSelection = velja_starfsmann.getSelectedItemPosition();
    	
    	MainActivity.adgerd = velja_adgerd.getSelectedItem().toString();
		MainActivity.adgerdSelection = velja_adgerd.getSelectedItemPosition();
		
		MainActivity.harlengd = velja_harlengd.getSelectedItem().toString();
		MainActivity.harlengdSelection = velja_harlengd.getSelectedItemPosition();
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	public void setHlutir() {
		
		velja_starfsmann = (Spinner) rootView.findViewById(R.id.starfsmennSpinner);
        velja_adgerd = (Spinner) rootView.findViewById(R.id.adgerdSpinner);
        velja_harlengd = (Spinner) rootView.findViewById(R.id.harlengdSpinner);
        
        afram = (Button) rootView.findViewById(R.id.next);
        afram.setOnClickListener(this);
	}
	
	/**
     * Breytan sem heldur utanum auðkenni valins starfsmanns
     * fær rétt gildi
     * 
     */
    public void setStaffId() {
    	
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
		default: MainActivity.staff_id = "ERR";
		
    	}
    }
    
    /**
     * Breytan sem heldur utan um tímalengd aðgerðar fær rétt gildi
     * 
     */
    public void setTimaLengd() {
    	
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
	 * Kallað er á aðferðir sem gefa breytunum fyrir fyrir hárlengd, aðgerð og starfsmann
	 * valin gildi og skjárinn fyrir skref 2 er birtur
	 */
    @Override
	public void onClick(View view) {
    	
    	setInfo();
    	setStaffId();
    	setTimaLengd();

    	Fragment fragment = new Skref2();
    	MainActivity.updateFragment(fragment);
	}
    
    /**
     * Spinner viðmótshlutum fyrir starfsmann, hárlengd og aðgerð er gefið það gildi sem síðast
     * var valið
     */
    public void update() {
    	
    	velja_starfsmann.setSelection(MainActivity.starfsmadurSelection);
    	velja_adgerd.setSelection(MainActivity.adgerdSelection);
    	velja_harlengd.setSelection(MainActivity.harlengdSelection);

	}
}