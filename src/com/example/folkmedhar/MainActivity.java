/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.folkmedhar.notendur.LoginActivity;
import com.example.folkmedhar.notendur.UpdateUser;
import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.pantanir.AllarPantanir;
import com.example.folkmedhar.pantanir.SidastaPontun;
import com.example.folkmedhar.pantanir.bokun.Skref1;
import com.example.folkmedhar.pantanir.bokun.Skref2;
import com.example.folkmedhar.pantanir.bokun.Skref3;

//kljlkjlkjlkj
public class MainActivity extends Activity {
	
	// Upplýsingar um notandann
	public static String nafn, simi, adgerd, harlengd, email;
		
	// Tímasetning pöntunar
	public static  String time, date, lengd, dagur, startDate, endDate;

		
	// Upplýsingar um starfsmann
	public static String staff_id, starfsmadur;
		
	public static Intent[] intents = new Intent[11];
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    //private CharSequence mTitle;
    private String[] menuTitles; 
    
	public static int starfsmadurSelection;
	public static int adgerdSelection;
	public static int harlengdSelection;
	public static int timiSelection;
	
	public static boolean bokudPontun;
	
	public static FragmentManager fragmentManager;
    
	
    @Override
    /**
     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
     * til að panta tíma eða fara á „Mitt svæði"
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        getActionBar().setCustomView(R.layout.actionbar);
        
        // Notanda upplýsingar
     	//nafn = UserFunctions.userName;
     	//simi = UserFunctions.userPhone;
     	//email = UserFunctions.userEmail;

        intents[0] = new Intent(this, CalendarActivity.class);
        intents[1] = new Intent(this, Skref1.class);
        intents[2] = new Intent(this, Skref2.class);
        intents[3] = new Intent(this, Skref3.class);
        intents[5] = new Intent(this, MittSvaedi.class);
        intents[6] = new Intent(this, SidastaPontun.class);
        intents[7] = new Intent(this, AllarPantanir.class);
     	intents[8] = new Intent(this, UmStofuna.class);
     	intents[9] = new Intent(this, Tilbod.class);
     	intents[10] = new Intent(this, LoginActivity.class);
              
        UserFunctions userFunction = new UserFunctions();
        userFunction = new UserFunctions();
        
        fragmentManager = getFragmentManager();
        
        
        bokudPontun = false;
        
        // Athuga hvort að notandi sé innskráður
        if(userFunction.isUserLoggedIn(getApplicationContext())) {
        	setContentView(R.layout.activity_main);
        	
            menuTitles = getResources().getStringArray(R.array.menu_titles);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.left_drawer);
            
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, menuTitles));
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

          
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                 
                    mDrawerLayout,       
                    R.drawable.ic_burger,  
                    R.string.adgerd_prompt,  //Veit ekki hvað þetta er
                    R.string.allar_pantanir  // Veit ekki hvað þetta er
                    ) {
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            if (savedInstanceState == null) {
            	Fragment fragment = new Upphafsskjar();
    		    FragmentManager fragmentManager = getFragmentManager();
    		   
    		    fragmentManager.beginTransaction()
    	        .replace(R.id.content_frame, fragment)
    	        .commit();
            }
        
        	
        	
        }else {
        	 startActivity(intents[10]);
        	 // Loka MainActivity skjánum
             finish();
             }
        }
   
    
    /**
	  * Notandi hefur verið skráður út og login síða birt 
	  */
	 public void logout() {
		 UserFunctions userFunction = new UserFunctions();
		 userFunction.logoutUser(getApplicationContext());
		 startActivity(intents[10]);
		 finish();
	 }
	 
	 @Override
	 /**
	  * Eva
	  */
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main, menu);
	        return super.onCreateOptionsMenu(menu);
	    }


	    @Override
	    /**
	     * Eva
	     */
	    public boolean onOptionsItemSelected(MenuItem item) {
	         // The action bar home/up action should open or close the drawer.
	         // ActionBarDrawerToggle will take care of this.
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	     
	        Fragment fragment = new UpdateUser();
		    FragmentManager fragmentManager = getFragmentManager();
		   
		    fragmentManager.beginTransaction()
	        .replace(R.id.content_frame, fragment)
	        .addToBackStack("fragment")
	        .commit();
	    
	        return true;
	    }

	    /**
	     * Eva
	     * @author evadoggsteingrimsdottir
	     *
	     */
	    /* The click listner for ListView in the navigation drawer */
	    private class DrawerItemClickListener implements ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectItem(position);
	        }
	    }

	    /**
	     * Eva
	     * @param position
	     */
	    private void selectItem(int position) {
	        // update the main content by replacing fragments
	    	 Fragment fragment = null;
	    	    FragmentManager fragmentManager = getFragmentManager();
	    	    switch(position) {
	    	        case 0:
	    	            fragment = new Upphafsskjar();
	    	            break;
	    	        case 1:
	    	            fragment = new Skref1();
	    	            break;
	    	        case 2:
	    	            fragment = new MittSvaedi();
	    	            break;
	    	        case 3:
	    	        	fragment = new UmStofuna();
	    	        	break;
	    	        case 4: 
	    	        	fragment = new Tilbod();
	    	        	break;
	    	        case 5: logout();
	    	        	return;
	    	        default: break;
	    	    }
	    	    
	    	    fragmentManager.beginTransaction()
	    	        .replace(R.id.content_frame, fragment)
	    	        .addToBackStack("fragment")
	    	        .commit();

	        mDrawerList.setItemChecked(position, true);
	        setTitle(menuTitles[position]);
	        mDrawerLayout.closeDrawer(mDrawerList);
	    }


	    @Override
	    /**
	     * Eva
	     */
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    /**
	     * Eva
	     */
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Pass any configuration change to the drawer toggls
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }

		
		
		public void setActionBarTitle(int titleActivitySkref2) {
		    getActionBar().setTitle(titleActivitySkref2);
		}	
		
		public static void updateFragment(Fragment fragment) {
			MainActivity.fragmentManager.beginTransaction()
	        .replace(R.id.content_frame, fragment)
	        .addToBackStack("fragment")
	        .commit();
		}
			
	}


