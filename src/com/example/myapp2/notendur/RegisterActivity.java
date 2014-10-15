/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2.notendur;

import org.json.JSONException;
import org.json.JSONObject;
 
import com.example.myapp2.*;
import com.example.myapp2.R.id;
import com.example.myapp2.R.layout;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class RegisterActivity extends BaseActivity {
    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputPhone;
    TextView registerErrorMsg;
     
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_PHONE = "phone";    
    private static String KEY_CREATED_AT = "created_at";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
 
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputPhone = (EditText) findViewById(R.id.registerPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
         
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {         
            public void onClick(View view) {
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String phone = inputPhone.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = UserFunctions.registerUser(name, email, phone, password);

                // check for login response
                //Log.d("key success json : ", KEY_SUCCESS);
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS); 
                        if(Integer.parseInt(res) == 1){
                            // user successfully registered
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            
                            //*****ATH user er lykill
                            JSONObject json_user = json.getJSONObject("Notendur");
                            
                            //Log.d("json user : ", json_user.toString());
                            
                            // Clear all previous data in database
                            UserFunctions.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), 
                            		json_user.getString(KEY_PHONE), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                        
                            // Launch Main Activity Screen
                            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                            // Close all views before launching Main Activity
                            mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainActivity);
                            // Close Registration Screen
                            finish();
                        }else{
                            // Error in registration
                            registerErrorMsg.setText("Villa kom upp við skráningu, reyndu aftur");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
 
        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
 
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


