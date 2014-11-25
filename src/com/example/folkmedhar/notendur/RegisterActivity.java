/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 15.10.2014
 * Klasinn sér um að kalla á aðferðir sem athuga hvort 
 * að nýskráning notanda hafi tekist. Klasinn birtir MainActivity skjáinn
 * ef svo er en annars skjáinn fyrir innskráningu. 
 * Klasinn inniheldur einnig aðferð sem opnar skjá fyrir innskráningu þegar smellt er á hnapp.
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


 
public class RegisterActivity extends Activity implements OnClickListener {
    
	// Viðmótshlutir fyrir nýskráningu notanda
    private EditText inputName, inputEmail, inputPassword,inputPhone,
    inputPasswordRepeat;
    
    private TextView registerErrorMsg; // Sýnir villur ef einhverjar eru
     
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setVidmotshlutir();

    }
    
    /**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		
		inputName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputPhone = (EditText) findViewById(R.id.registerPhone);
        inputPasswordRepeat = (EditText) findViewById(R.id.registerPasswordRepeat);
        
        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        Button buttonLoginScreen = (Button) findViewById(R.id.buttonLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.registerError);
        
        buttonRegister.setOnClickListener(this);
        buttonLoginScreen.setOnClickListener(this);
	}
	
	/**
	 * Kallar á aðferð sem athugar hvort tekist hafi að nýskrá notanda og skrá hann inn
     * og birtir MainActivity skjá ef svo er eða birtir skjáinn fyrir innskráningu ef notandinn
     * er nettengdur. Annars eru birt villuskilaboð
	 */
	public void onClick(View view) {
    	if (Connection.isOnline(this)) {
		    switch (view.getId()) {
		    case R.id.buttonRegister:
		    	register();
		        break;
	        case R.id.buttonLoginScreen:
	        	Intent login = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(login);
                finish();
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
	 * Athugar hvort tekist hafi að nýskrá notanda og skrá hann inn
     * og birtir MainActivity skjá ef svo er
	 */
	private void register() {
		 String name = inputName.getText().toString();
         String email = inputEmail.getText().toString();
         String phone = inputPhone.getText().toString();
         String password = inputPassword.getText().toString();
         String passwordRepeat = inputPasswordRepeat.getText().toString();

         // Athuga hvort að sama lykilorð hafi verið slegið tvisvar inn
         if (password.equals(passwordRepeat)) {
        	 boolean isUser = DatabaseHandler.registerUser(getApplicationContext(), name, email, 
            		 phone, password);
             if (isUser && DatabaseHandler.loginUser(getApplicationContext(), email, password)) {
            	 //Birta upphafsskjá
                 Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                 startActivity(mainActivity);
                 // loka skjánum fyrir nýskráningu
                 finish();
             }
             
             else {
            	 registerErrorMsg.setText("Villa kom upp við skráningu, reyndu aftur");
             }
         }
         
         else {
        	 registerErrorMsg.setText("Rangt lykilorð slegið inn, reyndu aftur");
         }
	}
}

