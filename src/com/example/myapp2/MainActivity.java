/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;


public class MainActivity extends BaseActivity {


	private Button buttonPantaTima;
	private Button buttonMittSvaedi;
	
	private Intent[] intents = new Intent[3];

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.buttonPantaTima = (Button) this.findViewById(R.id.panta);
        buttonPantaTima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(intents[2]);;
            }
        });
        
        this.buttonMittSvaedi = (Button) this.findViewById(R.id.mittSvaedi);
        buttonMittSvaedi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mittSvaediPopup(v);
            }
        });
        
		intents[0] = new Intent(this, SidastaPontun.class);
		intents[1] = new Intent(this, AllarPantanir.class);
		intents[2] = new Intent(this, Skref1.class);

    }
    
    
    // Eva
    public void mittSvaediPopup(View v){
    	
    	PopupMenu popupMenu = new PopupMenu(this, v);
    	popupMenu.getMenuInflater().inflate(R.menu.sidasta_pontun, popupMenu.getMenu());
    	popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
    		
    		@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
		        case R.id.action_sidasta:
		        	startActivity(intents[0]); //activity_sidasta_pontun
		            return true;
		        case R.id.action_allar:
		        	startActivity(intents[1]); //activity_allar_pantanir
		            return true;
		        default:
		            return false;
				}
			}
		});
    	popupMenu.show();
   }   
}
