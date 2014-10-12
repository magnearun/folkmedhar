package com.example.myapp2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Skref3 extends BaseActivity {
	private TextView clientInformation;
	// Progress Dialog
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skref3);
		settingText();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.skref3, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void stadfesta(View view){
		// creating new product in background thread
     }
	
	
	public void settingText(){
		clientInformation = new TextView(this);
		clientInformation=(TextView)findViewById(R.id.nameOfClient);
		clientInformation.setText(BaseActivity.nafn);
		clientInformation=(TextView)findViewById(R.id.telephoneClient);
		clientInformation.setText(BaseActivity.simi);
		clientInformation=(TextView)findViewById(R.id.employeeName);
		clientInformation.setText(BaseActivity.staff_id);
		clientInformation=(TextView)findViewById(R.id.typeName);
		clientInformation.setText(BaseActivity.adgerd);
		clientInformation=(TextView)findViewById(R.id.heightClient);
		clientInformation.setText(BaseActivity.harlengd);
		clientInformation=(TextView)findViewById(R.id.dateOfHaircut);
		clientInformation.setText(BaseActivity.date);
		clientInformation=(TextView)findViewById(R.id.timeOfHaircut);
		clientInformation.setText(BaseActivity.time);
		
	}
	
 
}
