/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Skref1 extends BaseActivity {
	// Breytur fyrir gagnagrunn
		
		// Viðmótshlutir
		EditText kennitala;
		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		
			
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_skref1);
	        
	        kennitala = (EditText) findViewById(R.id.kennitala);
	        velja_starfsmann = (Spinner) findViewById(R.id.starfsmennSpinner);
	        velja_adgerd = (Spinner) findViewById(R.id.adgerdSpinner);
	        velja_harlengd = (Spinner) findViewById(R.id.harlengdSpinner);
	        
	        // Starfsmenn Item Selected Listener
	        velja_starfsmann.setOnItemSelectedListener(new OnItemSelectedListener() {
	 
	            public void onItemSelected(AdapterView<?> adapter, View v,
	                    int position, long id) {
	                // On selecting a spinner item
	            	String starfsmadur = adapter.getItemAtPosition(position).toString();
	            	switch(starfsmadur) {
	            		case "Bambi": 
	            			staff_id = "BOB";
	            			break;
	            		
	            		case "Perla" : 
	            			staff_id = "PIP";
	            			break;
	            		
	            		case "Oddur" : 
	            			staff_id = "ODO";
	            			break;
	            		
	            		case "Magnea" : 
	            			staff_id = "MRV";
	            			break;
	            		
	            		case "Eva" :
	            			staff_id = "EDK";
	            			break;
	            		
	            		case "Birkir" : 
	            			staff_id = "BIP";
	            			break;
	            		
	            		case "Dagný" : 
	            			staff_id = "DOR";
	            			break;
	            		
	            		default: staff_id = "ERR";
	            	}
	            	
	            
	 
	                // Showing selected spinner item
	                
	                
	                //Toast.makeText(getApplicationContext(),
	                     //   "Selected Country : " + item, Toast.LENGTH_LONG).show();
	            }
	            
	            
	 
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub
	 
	            }
	        });
	        
	        //Aðgerð Item Selected Listener
	        velja_adgerd.setOnItemSelectedListener(new OnItemSelectedListener() {
	 
	            public void onItemSelected(AdapterView<?> adapter, View v,
	                    int position, long id) {
	                // On selecting a spinner item
	                adgerd = adapter.getItemAtPosition(position).toString();
	 
	                // Showing selected spinner item
	                
	                //Toast.makeText(getApplicationContext(),
	                     //   "Selected Country : " + item, Toast.LENGTH_LONG).show();
	            }
	            
	            
	 
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub
	 
	            }
	        });
	        
	        
	       // Áfram takki kallar á activity "step 2"
	        Button afram = (Button) findViewById(R.id.next);
	        afram.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// creating new product in background thread
					harlengd = velja_harlengd.getSelectedItem().toString();
					kt = kennitala.getText().toString();
					startActivity(intents[5]);
					
				}
			});
	    }
	    
	    
}

	     