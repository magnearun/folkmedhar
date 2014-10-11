package com.example.myapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class minarSidur extends Activity {

	private TextView responseTextView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minar_sidur);
        
    
        this.responseTextView = (TextView) this.findViewById(R.id.responseTextView);
     
        
        new GetAllCustomerTask().execute(new ApiConnector());
    }
     
  
    public void setTextToTextView(JSONArray jsonArray)
    {
    	String s = "";
    	for(int i=0; i <jsonArray.length();i++){
    		JSONObject json = null;
    		try{
    			json = jsonArray.getJSONObject(i);
    			s= s+ 
    					"Kennitala : "+json.getString("kennitala")+"\n"+"Starfsmaður: "+json.getString("starfsmadur")+"\n"+
    					"Byrjar : "+json.getString("startTime")+"\n"+
    					"Endar : "+json.getString("endTime")+"\n"+
    					"Dagsetning : "+json.getString("dagsetning")+"\n\n";
    			
    		}catch(JSONException e){
    			e.printStackTrace();
    		}
    	}
    	this.responseTextView.setText(s);
    }
    
    
    private class GetAllCustomerTask extends AsyncTask<ApiConnector,Long,JSONArray>
    {
    	@Override
    	protected JSONArray doInBackground(ApiConnector... params){
    		return params[0].GetAllCustomers();
    		
    	}
    	
    	@Override
    	protected void onPostExecute(JSONArray jsonArray){
    		setTextToTextView(jsonArray);
    	}
    }
    
}
