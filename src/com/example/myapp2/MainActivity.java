package com.example.myapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp2.ApiConnector;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {


	private Button panta;
	private Button minarSidur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        this.panta = (Button) this.findViewById(R.id.panta);
        panta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                panta();
            }
        });
        
        this.minarSidur = (Button) this.findViewById(R.id.minarSidur);
       minarSidur.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                minarSidur();
            }
        });

    }
     
    public void panta(){
    	 Intent intent = new Intent(this, step1.class);
         startActivity(intent);	
    }
    public void minarSidur(){
   	 Intent intent = new Intent(this, minarSidur.class);
        startActivity(intent);	
   }
    
    
}
