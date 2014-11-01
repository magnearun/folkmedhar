/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.example.folkmedhar.notendur.LoginActivity;
import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.pantanir.AllarPantanir;
import com.example.folkmedhar.pantanir.SidastaPontun;
import com.example.folkmedhar.pantanir.bokun.Skref1;
import com.example.folkmedhar.pantanir.bokun.Skref2;
import com.example.folkmedhar.pantanir.bokun.Skref2.BokadirTimar;
import com.example.folkmedhar.pantanir.bokun.Skref3;
import com.example.folkmedhar.pantanir.bokun.StadfestingBokunar;


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
    
	
    @Override
    /**
     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
     * til að panta tíma eða fara á „Mitt svæði"
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Notanda upplýsingar
     	nafn = UserFunctions.userName;
     	simi = UserFunctions.userPhone;
     	email = UserFunctions.userEmail;

        intents[0] = new Intent(this, MainActivity.class);
        intents[1] = new Intent(this, Skref1.class);
        intents[2] = new Intent(this, Skref2.class);
        intents[3] = new Intent(this, Skref3.class);
        intents[4] = new Intent(this, StadfestingBokunar.class);
        intents[5] = new Intent(this, MittSvaedi.class);
        intents[6] = new Intent(this, SidastaPontun.class);
        intents[7] = new Intent(this, AllarPantanir.class);
     	intents[8] = new Intent(this, UmStofuna.class);
     	intents[9] = new Intent(this, Tilbod.class);
     	intents[10] = new Intent(this, LoginActivity.class);
              
        UserFunctions userFunction = new UserFunctions();
        userFunction = new UserFunctions();
        
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
            	/**
            	 * Eva
            	 */
                public void onDrawerClosed(View view) {
                    //getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu(); 
                }
                
                /**
                 * Eva
                 */
                public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu(); 
                }
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

	 	/**
	 	 * Eva
	 	 */
	    /* Called whenever we call invalidateOptionsMenu() */
	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        // If the nav drawer is open, hide action items related to the content view
	        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	        menu.findItem(R.id.settings).setVisible(!drawerOpen);
	        return super.onPrepareOptionsMenu(menu);
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
	        startActivityForResult(new Intent(Intent.ACTION_PICK).setDataAndType(null, CalendarActivity.MIME_TYPE), 100);
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
	    	        	break;
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
	    
	    /**
		 * Birtir viðmótshlut þar sem notandinn getur valið dagsetningu
		 * @param v
		 */
		public void showDatePickerDialog(View v) {
		    DialogFragment newFragment = new DatePickerFragment();
		    newFragment.show(getFragmentManager(), "datePicker");
		}
		
		/**
		 * @author: Eva Dögg Steingrímsdóttir
		 * @since: 15.10.2014
		 * Klasinn sem sem sér um að búa til viðmótshlut þar sem notandinn getur
		 * valið dagsetningu
		 */
		public class DatePickerFragment extends DialogFragment
		implements DatePickerDialog.OnDateSetListener {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
		
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
				dialog.getDatePicker().setMinDate(c.getTimeInMillis());
				c.set(year+1, month, day);
				dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
				//dialog.getDatePicker().setCalendarViewShown(true);
				//dialog.getDatePicker().setSpinnersShown(false);
				return dialog;
				}
			/**
			 * Breytan sem heldur utan um hvaða dagur var valinn fær gildi og það gildi
			 * er birt á takka. Kallað er á aðferð sem sér um að sækja lausa tíma
			 * fyrir valinn dag
			 */
			public void onDateSet(DatePicker view, int year, int month, int day) {
				MainActivity.dagur = year + "-" + (month+1) + "-" + day;
				MainActivity.date = day + "-" + (month+1) + "-" + year;
				
				
				Button buttonDagur = (Button) findViewById(R.id.buttonDagur);
				buttonDagur.setText(MainActivity.date);
				if(view.isShown()){
					new BokadirTimar().execute();
				}
				
			}
		}
		
		public void setActionBarTitle(int titleActivitySkref2) {
		    getActionBar().setTitle(titleActivitySkref2);
		}
		
		// 2) implement your own onActivityResult method to handle returned date
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    if(resultCode==RESULT_OK) {
		        int year = data.getIntExtra("year", 0);   // get number of year
		        int month = data.getIntExtra("month", 0); // get number of month 0..11
		        int day = data.getIntExtra("day", 0);     // get number of day 0..31

		        // format date and display on screen
		        final Calendar dat = Calendar.getInstance();
		        dat.set(Calendar.YEAR, year);
		        dat.set(Calendar.MONTH, month);
		        dat.set(Calendar.DAY_OF_MONTH, day);
		        
		        MainActivity.date = day + "-" + (month+1) + "-" + year;
				Button buttonDagur = (Button) findViewById(R.id.buttonDagur);
				buttonDagur.setText(MainActivity.date);
		        // show result
		        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd");
		        Toast.makeText(MainActivity.this, format.format(dat.getTime()), Toast.LENGTH_LONG).show();
		                
		    }
		}
	}


