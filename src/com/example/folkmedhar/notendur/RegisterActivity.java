/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 15.10.2014
 * Klasinn sér um að athuga hvort að nýskráning notanda hafi tekist og birtir MainActivity skjáinn
 * ef svo er. Inniheldur einnig aðferð sem opnar LoginActivity skjá þegar smellt er á hnapp.
 */

package com.example.folkmedhar.notendur;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;
 
public class RegisterActivity extends Activity {
    Button buttonRegister;
    Button buttonLoginScreen;
    EditText inputName;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputPhone;
    EditText inputPasswordRepeat;
    TextView registerErrorMsg;
     
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
 
        inputName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputPhone = (EditText) findViewById(R.id.registerPhone);
        inputPasswordRepeat = (EditText) findViewById(R.id.registerPasswordRepeat);
        
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLoginScreen = (Button) findViewById(R.id.buttonLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.registerError);
         
        buttonRegister.setOnClickListener(new View.OnClickListener() {   
           /**
            * Athugar hvort tekist hafi að nýskrá notanda og skrá hann inn
            * og birtir MainActivity skjá ef svo er
            */
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String phone = inputPhone.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordRepeat = inputPasswordRepeat.getText().toString();

                if (password.equals(passwordRepeat)) {
                	UserFunctions userFunction = new UserFunctions();
	                boolean isUser = userFunction.registerUser(getApplicationContext(), name, email, phone, password);
	                if (isUser && userFunction.loginUser(getApplicationContext(), email, password)) {
	                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
	                    startActivity(mainActivity);
	                    // loka skjánum fyrir nýskráningu
	                    finish();
	                    }
	                else 
	                	registerErrorMsg.setText("Villa kom upp við skráningu, reyndu aftur");
	                }
                else
                	registerErrorMsg.setText("Rangt lykilorð slegið inn, reyndu aftur");
                }
            });
        
 
        buttonLoginScreen.setOnClickListener(new View.OnClickListener() {
        	/**
        	 * Birtir skjáinn fyrir innskráningu
        	 */
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
}