/**
 * @author: Birkir Pálmason, Magnea Rún Vignisdóttir og Eva Dögg Steingrímsdóttir
 * @since: 03.11.2014
 * Klasinn sem heldur utan um hvaða hárgreiðslumann, hárlengd og aðgerð
 * notandinn valdi fyrir seinni skref.
 */


/**
 * Búið að refactor-a
 */
package com.example.folkmedhar.pantanir.bokun;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
import com.example.folkmedhar.pantanir.FerlaBokun;



public class Skref1 extends Fragment implements android.view.View.OnClickListener {
	
	// Viðmótshlutir
	private Spinner starfsmadurSpinner;
	private Spinner adgerdSpinner;
	private Spinner harlengdSpinner;
	private Button buttonAfram;
	
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

		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_step1);
		
		setVidmotshlutir();
        
        updateVidmotshlutir();
        
		return rootView;
	}
	
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		
		starfsmadurSpinner = (Spinner) rootView.findViewById(R.id.starfsmennSpinner);
        adgerdSpinner = (Spinner) rootView.findViewById(R.id.adgerdSpinner);
        harlengdSpinner = (Spinner) rootView.findViewById(R.id.harlengdSpinner);
        
        buttonAfram = (Button) rootView.findViewById(R.id.next);
        buttonAfram.setOnClickListener(this);
	}
	
    
    /**
	 * Kallað er á aðferðir sem gefa breytunum fyrir fyrir hárlengd, aðgerð og starfsmann
	 * valin gildi og skjárinn fyrir skref 2 er birtur
	 */
    @Override
	public void onClick(View view) {
    	
    	FerlaBokun.setBokunarUpplysingar(starfsmadurSpinner,adgerdSpinner,harlengdSpinner);
    	FerlaBokun.setStaffId();
    	FerlaBokun.setTimaLengd();

    	Fragment fragment = new Skref2();
    	MainActivity.updateFragment(fragment);
	}
    
    /**
     * Spinner viðmótshlutum fyrir starfsmann, hárlengd og aðgerð er gefið það gildi sem síðast
     * var valið
     */
    private void updateVidmotshlutir() {
    	
    	starfsmadurSpinner.setSelection(MainActivity.getStarfsmadurPos());
    	adgerdSpinner.setSelection(MainActivity.getAdgerdPos());
    	harlengdSpinner.setSelection(MainActivity.getHarlengdPos());

	} 
}