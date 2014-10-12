/**
 * @author: Jón Jónsson
 * @since: 30.09.2014
 * Klasinn sem ......
 */

package com.example.myapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;




public class AllarPantanir extends BaseActivity {

	private TextView responseTextView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allar_pantanir);
        
    
        this.responseTextView = (TextView) this.findViewById(R.id.responseTextView);
        this.responseTextView.setMovementMethod(new ScrollingMovementMethod());
     
        
        new GetAllCustomerTask().execute(new ApiConnector());
    }
     
  
    // Magnea
    public void setTextToTextView(JSONArray jsonArray)
    {
    	String s = "";
    	for(int i=0; i <jsonArray.length();i++){
    		JSONObject json = null;
    		try{
    			json = jsonArray.getJSONObject(i);
    			s= s+ 
    					"Kennitala : "+json.getString("kt")+"\n"+"Starfsmaður: "+json.getString("staff_id")+"\n"+
    					"Byrjar : "+json.getString("startDate")+"\n"+
    					"Endar : "+json.getString("endDate")+"\n\n";
    			
    		}catch(JSONException e){
    			e.printStackTrace();
    		}
    	}
    	this.responseTextView.setText(s);
    }
    
    
    /**
     * @author: Jón Jónsson
     * @since: 30.09.2014
     * Klasinn sem ......
     */
    private class GetAllCustomerTask extends AsyncTask<ApiConnector,Long,JSONArray>
    {
    	@Override
    	// Magnea
    	protected JSONArray doInBackground(ApiConnector... params){
    		return params[0].getAllCustomers();
    		
    	}
    	
    	@Override
    	// Magnea
    	protected void onPostExecute(JSONArray jsonArray){
    		setTextToTextView(jsonArray);
    	}
    }
    
}
