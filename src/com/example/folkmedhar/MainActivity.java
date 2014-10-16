/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.R;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;


public class MainActivity extends BaseActivity {

	private Button buttonPantaTima;
	private Button buttonMittSvaedi;	
	
    @Override
    /**
     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
     * til að panta tíma eða fara á „Mitt svæði"
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        UserFunctions userFunction = new UserFunctions();
        userFunction = new UserFunctions();
        
        // Athuga hvort að notandi sé innskráður
        if(userFunction.isUserLoggedIn(getApplicationContext())) {
        	setContentView(R.layout.activity_main);
        
        	
        	this.buttonPantaTima = (Button) this.findViewById(R.id.panta);
        	buttonPantaTima.setOnClickListener(new View.OnClickListener() {
        		/**
        		 * Birtir fyrsta skjáinn í pöntunarferlinu
        		 */
        		public void onClick(View v) {
        			startActivity(intents[1]); // Skref1
        			}
        		});
        	
        	this.buttonMittSvaedi = (Button) this.findViewById(R.id.mittSvaedi);
        	buttonMittSvaedi.setOnClickListener(new View.OnClickListener() {
        		/**
        		 * Kallar á aðferð sem að birtir aðgerðslá fyrir „Mínar pantanir"
        		 */
        		public void onClick(View v) {
        			mittSvaediPopupMenu(v);
        			}
        		});
        	
        }else {
        	 startActivity(intents[10]);
        	 // Loka MainActivity skjánum
             finish();
             }
        }
   
    
    /**
     * Birtir „popup" aðgerðaslá með þeim valmöguleikum að fá yfirlit yfir síðustu
     * pöntun og yfirlit yfir allar pantanir
     */
    public void mittSvaediPopupMenu(View v){
    	
    	PopupMenu popupMenu = new PopupMenu(this, v);
    	popupMenu.getMenuInflater().inflate(R.menu.sidasta_pontun, popupMenu.getMenu());
    	popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
    		
    		@Override
    		/**
    		  * Birtir viðeigandi skjá fyrir þann möguleika sem valinn var í aðgerðaslá
    		  */
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
		        case R.id.action_sidasta:
		        	startActivity(intents[6]); //SidastaPontun
		            return true;
		        case R.id.action_allar:
		        	startActivity(intents[7]); //AllarPantanir
		            return true;
		        default:
		            return false;
				}
			}
		});
    	popupMenu.show();
   }   
}