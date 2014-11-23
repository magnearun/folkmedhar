/**
 * @author: Dagný Ósk Ragnarsdóttir
 * @since: 03.11.2014
 * Klasinn sér um að uppfæra notenda upplýsingar þegar þeim er breytt
 */

package com.example.folkmedhar.notendur;

import android.support.v4.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.folkmedhar.Connection;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;

public class UpdateUser extends Fragment implements android.view.View.OnClickListener {
    
	// Viðmótshlutir
	private Button buttonUpdateUser;
    private EditText inputName, inputEmail, inputOldPassword, inputPassword,
    inputPhone, inputPasswordRepeat;
    private TextView updateErrorMsg;
    
	private View rootView;
	private static Context context;

	/**
	 * Nýtt fragment er búið til
	 */
	public UpdateUser() {
		
	}

		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_update_user,
				container, false);
		
		//TextView text = (TextView)getActivity().findViewById(R.id.actionbar);
		//text.setText(R.string.title_activity_update_user);
		
	    setVidmotshlutir();
	    
	    context = getActivity();
	    
	    setUserInfo();
	
	    buttonUpdateUser.setOnClickListener(this);
		return rootView;
	}
	
	/**
	 * Upphafsstillir tilviksbreytur fyrir viðmótshluti
	 */
	private void setVidmotshlutir() {
		
		inputName = (EditText) rootView.findViewById(R.id.userName);
	    inputEmail = (EditText) rootView.findViewById(R.id.userEmail);
	    inputPhone = (EditText) rootView.findViewById(R.id.userPhone);
	    inputOldPassword = (EditText) rootView.findViewById(R.id.userOldPassword);
	    inputPassword = (EditText) rootView.findViewById(R.id.userPassword);
	    inputPasswordRepeat = (EditText) rootView.findViewById(R.id.userPasswordRepeat);
	    
	    buttonUpdateUser = (Button) rootView.findViewById(R.id.buttonUpdateUser);
	    updateErrorMsg = (TextView) rootView.findViewById(R.id.updateError);
	}
	
	/**
	 * Sækkir upplýsingar um notanda og birtir þær í TextView
	 */
	private void setUserInfo() {
	    inputName.setText(UserFunctions.userName(context), TextView.BufferType.EDITABLE);
	    inputEmail.setText(UserFunctions.userEmail(context), TextView.BufferType.EDITABLE);
	    inputPhone.setText(UserFunctions.userPhone(context), TextView.BufferType.EDITABLE);
	}
	
	/**
	 * Ef notandinn er nettengdur er kallað á aðferð sem sér um að uppfæra upplýsingar um
	 * hann í gagnagrunni. Annars eru birt villuskilaboð
	 */
	@Override
	public void onClick(View view) {
		if (Connection.isOnline(getActivity())) {
			updateUser();
		}
		else {
			MainActivity.showToast("Engin nettenging!", getActivity());
		}
	}
	
	/**
	 * Notenda upplýsingar uppfærðar ef engin villa kom upp
	 * Annar eru birt villuskilaboð
	 */
	private void updateUser() {
		String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String phone = inputPhone.getText().toString();

        String oldPassword	= inputOldPassword.getText().toString();
        String password = inputPassword.getText().toString();
        String passwordRepeat = inputPasswordRepeat.getText().toString();
        
        // Lykilorði er ekki breytt ef ekkert er slegið inní þann reit
        if (password.equals("")) {
        	password = oldPassword;
        	passwordRepeat = oldPassword;
        }
        
        if (password.equals(passwordRepeat)) {
            
            UserFunctions userFunction = new UserFunctions();
            String oldEmail = UserFunctions.userEmail(context);

            boolean isUpdated = userFunction.updateUser(context, oldEmail, 
            		oldPassword, name, email, phone, password);
            
            if (isUpdated) {
            	MainActivity.showToast("Notendaupplýsingar þínar hafa verið uppfærðar!", getActivity());
            	userFunction.logoutUser(context);
            	Intent intent = new Intent(getActivity(), LoginActivity.class);
            	startActivity(intent);
            } else 
                updateErrorMsg.setText("Villa kom upp við uppfærslu á notendaupplýsingum, " +
                		"reyndu aftur.");
        } else 
        	updateErrorMsg.setText("Rangt lykilorð slegið inn, reyndu aftur.");
     }
}
				
