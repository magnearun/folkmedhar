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

	public static String nameOfClient;
	public static String telephoneClient;
	public static String employeeName;
	public static String type;
	public static String height;
	public static String date;
	public static String time;
	private Button panta;
	private Button minarSidur;
	private Button step3;
	
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
    
    public void step3(View view){
      	 Intent intent = new Intent(this, Step3.class);
      	 nameOfClient= "Jón Jónsson";
      	 telephoneClient= "846-1392";
      	 employeeName= "Bambi";
      	 type= "Herraklipping";
     	 height= "Sítt";
     	 date= "19.09.14";
     	 time = "19:00";
           startActivity(intent);	
      }
    
}
