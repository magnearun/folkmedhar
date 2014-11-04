package com.example.folkmedhar.notendur;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;

public class UpdateUser extends Fragment implements android.view.View.OnClickListener {
    Button buttonUpdateUser;
    EditText inputName;
    EditText inputEmail;
    EditText inputOldPassword;
    EditText inputPassword;
    EditText inputPhone;
    EditText inputPasswordRepeat;
    TextView updateErrorMsg;
    
	View rootView;
	static Context c;

		public UpdateUser() {
		}

				
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_update_user,
					container, false);
			
			((MainActivity) getActivity()).setActionBarTitle(R.string.title_activity_update_user);
			
	        inputName = (EditText) rootView.findViewById(R.id.userName);
	        inputEmail = (EditText) rootView.findViewById(R.id.userEmail);
	        inputPhone = (EditText) rootView.findViewById(R.id.userPhone);
	        inputOldPassword = (EditText) rootView.findViewById(R.id.userOldPassword);
	        inputPassword = (EditText) rootView.findViewById(R.id.userPassword);
	        inputPasswordRepeat = (EditText) rootView.findViewById(R.id.userPasswordRepeat);
	        
	        buttonUpdateUser = (Button) rootView.findViewById(R.id.buttonUpdateUser);
	        updateErrorMsg = (TextView) rootView.findViewById(R.id.updateError);
	        c = getActivity();
	        
	        UserFunctions userFunction = new UserFunctions();
	        inputName.setText(userFunction.userName(c), TextView.BufferType.EDITABLE);
	        inputEmail.setText(userFunction.userEmail(c), TextView.BufferType.EDITABLE);
	        inputPhone.setText(userFunction.userPhone(c), TextView.BufferType.EDITABLE);

	        buttonUpdateUser.setOnClickListener(this);
			return rootView;
		}
		

			@Override
			public void onClick(View view) {
				
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String phone = inputPhone.getText().toString();

                String oldPassword	= inputOldPassword.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordRepeat = inputPasswordRepeat.getText().toString();
                
                if (password.equals("")) {
                	password = oldPassword;
                	passwordRepeat = oldPassword;
                }
                
                if (password.equals(passwordRepeat)) {
	                
	                UserFunctions userFunction = new UserFunctions();
	                String oldEmail = userFunction.userEmail(c);
	
	                boolean isUpdated = userFunction.updateUser(c, oldEmail, oldPassword, name, email, phone, password);
	                
	                if (isUpdated) {
	                	String userSaved = "Notendaupplýsingar þínar hafa verið uppfærðar!";
	                	Toast toast = Toast.makeText(c, userSaved,Toast.LENGTH_LONG);
	                	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	                	toast.show();
	                	userFunction.logoutUser(c);
	                	Intent intent = new Intent(getActivity(), LoginActivity.class);
	                	startActivity(intent);
	                	}
	                else 
	                    updateErrorMsg.setText("Villa kom upp við uppfærslu á notendaupplýsingum, reyndu aftur.");
	                }
                else 
                	updateErrorMsg.setText("Rangt lykilorð slegið inn, reyndu aftur.");
                	     
			}
				
}