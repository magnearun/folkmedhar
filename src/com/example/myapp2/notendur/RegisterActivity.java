package com.example.myapp2.notendur;
 
import com.example.myapp2.MainActivity;
import com.example.myapp2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class RegisterActivity extends Activity {
    Button buttonRegister;
    Button buttonLoginScreen;
    EditText inputName;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputPhone;
    TextView registerErrorMsg;
     
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
 
        // Importing all assets like buttons, text fields
        inputName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputPhone = (EditText) findViewById(R.id.registerPhone);
        
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLoginScreen = (Button) findViewById(R.id.buttonLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.registerError);
         
        // Register Button Click event
        buttonRegister.setOnClickListener(new View.OnClickListener() {         
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String phone = inputPhone.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                boolean ret = userFunction.registerUser(getApplicationContext(), name, email, phone, password);
                if (ret && userFunction.loginUser(getApplicationContext(), email, password)) {
                	// Launch Main Activity Screen
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    // Close Registration Screen
                    finish();
                }
                else 
                	// Error in registration
                    registerErrorMsg.setText("Villa kom upp við skráningu, reyndu aftur");
                }
            });
        
 
        // Link to Login Screen
        buttonLoginScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }
}




