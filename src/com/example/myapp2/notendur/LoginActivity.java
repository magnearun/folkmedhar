/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 15.10.2014
 * Klasi sem athugar hvort að inn skráning notanda hafi tekist og birtir MainActivity skjáinn
 * ef svo er. Inniheldur einnig aðferð sem opnar RegisterActivity skjá þegar smellt er á hnapp.
 */

package com.example.myapp2.notendur;

import com.example.myapp2.BaseActivity;
import com.example.myapp2.MainActivity;
import com.example.myapp2.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
    Button buttonLogin;
    Button buttonRegisterScreen;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;

    @Override
	/**
	 * Birtir viðmótið fyrir LoginActivity skjá og gefur tilviksbreytum fyrir 
	 * viðmótshluti gildi. Auk þess er OnClickListener tengdur við takka sem 
	 * notaður er til þess að fara yfir í RegisterActivity skjá.
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
 
        // Textasvæði og hnappar á skjá
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegisterScreen = (Button) findViewById(R.id.buttonRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.loginError);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	/**
            	 * Athugar hvort 
            	 */
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                boolean ret = userFunction.loginUser(getApplicationContext(), email, password);
                if (ret) {
                	// Launch Main Activity Screen
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    // Close Registration Screen
                    finish();
                } else {
					// Error in login
                    loginErrorMsg.setText("Rangt notendanafn eða lykilorð");
           
                }
            }
        });      
        
        // Link to Login Screen
        buttonRegisterScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }
}