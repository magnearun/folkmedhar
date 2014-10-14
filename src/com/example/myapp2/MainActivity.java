/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import com.example.myapp2.UserFunctions;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;


public class MainActivity extends BaseActivity {

	UserFunctions userFunctions;
	private Button buttonPantaTima;
	private Button buttonMittSvaedi;

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // Athuga login status í gagnagrunni
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())) {
        	setContentView(R.layout.activity_main);   
        
        
        this.buttonPantaTima = (Button) this.findViewById(R.id.panta);
        buttonPantaTima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(intents[0]);;
            }
        });
        
        this.buttonMittSvaedi = (Button) this.findViewById(R.id.mittSvaedi);
        buttonMittSvaedi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mittSvaediPopup(v);
            }
        });

        }else {
        	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        	 login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(login);
             // Loka MainActivity skjánum
             finish();
             }
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
		        	startActivity(intents[3]); //activity_sidasta_pontun
		            return true;
		        case R.id.action_allar:
		        	startActivity(intents[4]); //activity_allar_pantanir
		            return true;
		        default:
		            return false;
				}
			}
		});
    	popupMenu.show();
   
   }   
}
