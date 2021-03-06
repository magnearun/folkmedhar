/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 15.10.2014
 * Klasi sér um að kalla á aðferðir sem athugar hvort að innskráning notanda hafi tekist og birtir 
 * MainActivity skjáinn ef svo er. Klasinn inniheldur einnig aðferð sem opnar skjá fyrir nýskráningu þegar
 * smellt er á hnapp.
 */

package com.example.folkmedhar.notendur;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.DatabaseHandler;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;


public class LoginActivity extends Activity implements OnClickListener {
    
	// Viðmótshutir fyrir innskráningu á notendaupplýsingum
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView loginErrorMsg;
    

    @Override
	/**
	 * Birtir viðmótið fyrir LoginActivity skjá og gefur tilviksbreytum fyrir 
	 * viðmótshluti gildi. Auk þess er OnClickListener tengdur við takka sem 
	 * notaður er til þess að fara yfir í RegisterActivity skjá.
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setVidmotshlutir();
    }
    
    /**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        Button buttonRegisterScreen = (Button) findViewById(R.id.buttonRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.loginError);
        
        buttonLogin.setOnClickListener(this);
        buttonRegisterScreen.setOnClickListener(this);
	}
        
    /**
	 * Kallar á aðferð sem sér um innskráningu eða birtir skjá fyrir 
	 * nýskráningu ef notandinne er nettengdur, ananrs eru birt villuskilaboð
	 */
    public void onClick(View view) {
    	if (Connection.isOnline(this)) {
		    switch (view.getId()) {
		    case R.id.buttonLogin:
		    	login();
		        break;
	        case R.id.buttonRegisterScreen:
	        	// Birtir skjá fyrir nýskráningu
	        	Intent register = new Intent(getApplicationContext(),
	                    RegisterActivity.class);
	            startActivity(register);
	            finish(); // Loka innskráningar skjánum
	            break;
	        default:
	            break;
	        }
    	} 
    	else {
			MainActivity.showToast("Engin nettenging!", this);
		}
	}
    
    /**
     * Athugar hvort að tekist hafi að skrá notanda inn og
     * birtir MainActivity skjá ef svo er annars villuskilaboð
     */
    private void login() {
    	String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        
        boolean isUser = DatabaseHandler.loginUser(getApplicationContext(), email, password);
        if (isUser) {
        	// Notandinn fannst í gagnagrunni
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish(); // Loka innskráningar skjánum
        } else {
        	// Notandinn fannst ekki
            loginErrorMsg.setText("Rangt notendanafn eða lykilorð");
   
        }
    } 
}