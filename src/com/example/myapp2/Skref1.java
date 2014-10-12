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
		String kt;
		String staff_id;
		String adgerd;
		
		// Viðmótshlutir
		EditText kennitala;
		Spinner velja_starfsmann;
		Spinner velja_adgerd;
		Spinner velja_harlengd;
		
		// Progress Dialog
		private ProgressDialog pDialog;
		JSONParser jsonParser = new JSONParser();
		
		// url til ap panta tíma panta.php
		private static String url_panta_tima = "http://prufa2.freeiz.com/pantatima.php";
			
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_skref1);
	        
	        kennitala = (EditText) findViewById(R.id.kennitala);
	        velja_starfsmann = (Spinner) findViewById(R.id.starfsmenn);
	        velja_adgerd = (Spinner) findViewById(R.id.adgerd);
	        velja_harlengd = (Spinner) findViewById(R.id.harlengd);
	        
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
	            	
	            	Log.d("staff_id er", staff_id);
	 
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
	        Button afram = (Button) findViewById(R.id.afram);
	        afram.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// creating new product in background thread
					BaseActivity.staff_id = velja_starfsmann.getSelectedItem().toString();;
					BaseActivity.adgerd = velja_adgerd.getSelectedItem().toString();;
					BaseActivity.harlengd = velja_harlengd.getSelectedItem().toString();;
					startActivity(intents[5]);;
					new afram().execute();
				}
			});
	    }
	    
	    /* Þetta Skref færist svo yfir í activity Skref 3  */
	    /**
		 * Background Async Task
		 * */
		class afram extends AsyncTask<String, String, String> {

			private  String TAG_SUCCESS = null;

			/**
			 * Before starting background thread Show Progress Dialog - "skilaboð á meðan verið er að bíða"
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(Skref1.this);
				pDialog.setMessage("Creating Product..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
			
			@Override
			protected String doInBackground(String... args) {
				// TODO Auto-generated method stub
				kt = kennitala.getText().toString();
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("kt", kt));
				Log.d("staff_id er : ", staff_id);
				params.add(new BasicNameValuePair("staff_id", staff_id));
				
				JSONObject json = jsonParser.makeHttpRequest(url_panta_tima,
						"POST", params);
				
				// check log cat for response
				Log.d("Create Response", json.toString());
				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// successfully created product
						
						
						// closing this screen
						finish();
					} else {
						// failed to create product
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}
			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
			}
		}
		}

	     