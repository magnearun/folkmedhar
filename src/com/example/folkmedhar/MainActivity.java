/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.folkmedhar.notendur.LoginActivity;
import com.example.folkmedhar.notendur.UpdateUser;
import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.pantanir.bokun.Skref1;

public class MainActivity extends Activity {
	
	// Upplýsingar um bókun
	private static String nafn, simi, adgerd, harlengd, email;
		
	// Tímasetning pöntunar
	// dagur er á forminu "26-11-2014", date á forminu "2014-11-26" og
	// startDate og endDate á forminu "2014-11-26 09:00"
	private static  String time, dagur, lengd, date, startDate, endDate;
	
	// Upplýsingar um starfsmann
	private static String staff_id, starfsmadur;
	
	// Staðsetning vals í Spinner viðmótshlut
	private static int starfsmadurPos, adgerdPos, harlengdPos;
	
	// Breytan er notuð til að halda utan hvort pöntun hafi verið
    // bókuð rétt áður en ýtt er á "back" takkann
	private static boolean bokudPontun;
		
	private Intent intent;
	
	// Navigation Drawer
	private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] menuTitles; 
    private FrameLayout frame;
    private float lastTranslate = 0.0f;
    
    /**
     * Eyða þessu?
     */
    //private CharSequence mDrawerTitle;
	
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
        
 
     	nafn = UserFunctions.userName(this.getBaseContext());
     	simi = UserFunctions.userPhone(this.getBaseContext());
     	email = UserFunctions.userEmail(this.getBaseContext());

        intent = new Intent(this, LoginActivity.class);
              
        UserFunctions userFunction = new UserFunctions();
        userFunction = new UserFunctions();
        
        fragmentManager = getFragmentManager();
        
        
        bokudPontun = false;
        
        // Athuga hvort að notandi sé innskráður
        if(userFunction.isUserLoggedIn(getApplicationContext())) {
        	setContentView(R.layout.activity_main);
        	
            menuTitles = getResources().getStringArray(R.array.menu_titles);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerList = (ListView) findViewById(R.id.left_drawer);
            
            frame = (FrameLayout) findViewById(R.id.content_frame);
            
            drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            drawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, menuTitles));
            drawerList.setOnItemClickListener(new DrawerItemClickListener());

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);

          
            drawerToggle = new ActionBarDrawerToggle(
                    this,                 
                    drawerLayout,       
                    R.drawable.ic_burger,  
                    R.string.adgerd_prompt,  //Veit ekki hvað þetta er
                    R.string.allar_pantanir  // Veit ekki hvað þetta er
                    ) {
            	@SuppressLint("NewApi")
                public void onDrawerSlide(View drawerView, float slideOffset)
                {
                    float moveFactor = (drawerList.getWidth() * slideOffset);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
                    {
                        frame.setTranslationX(moveFactor);
                    }
                    else
                    {
                        TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                        anim.setDuration(0);
                        anim.setFillAfter(true);
                        frame.startAnimation(anim);

                        lastTranslate = moveFactor;
                    }
                }
            };
            drawerLayout.setDrawerListener(drawerToggle);

            if (savedInstanceState == null) {
            	Fragment fragment = new Upphafsskjar();
            	MainActivity.fragmentManager.beginTransaction()
    	        .replace(R.id.content_frame, fragment)
    	        .commit();
            }
        
        	
        	
        }else {
        	 startActivity(intent);
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
		 startActivity(intent);
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
     
        Fragment fragment = new UpdateUser();
	    updateFragment(fragment);
    
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
    	    switch(position) {
    	        case 0:
    	            fragment = new Upphafsskjar();
    	            break;
    	        case 1:
    	            fragment = new Skref1();
    	            break;
    	        case 2:
    	            fragment = new MinarPantanir();
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
    	    
    	    updateFragment(fragment);

        drawerList.setItemChecked(position, true);
        setTitle(menuTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }


    @Override
    /**
     * Eva
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    /**
     * Eva
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
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
	
	/**
	 * Skilar nafni notandans
	 * @return String
	 */
	public static String getName() {
		return nafn;
	}
	/**
	 * Skilar síma notandans
	 * @return String
	 */
	public static String getSimi() {
		return simi;
	}
	/**
	 * Skilar netfangi notandans
	 * @return String
	 */
	public static String getEmail() {
		return email;
	}
	
	/**
	 * Gefur breytunni sem heldur utan um tíma bókunar gildi
	 * @param t
	 */
	public static void setTime(String t) {
		time = t;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um tíma bókunar gildi
	 * @return String
	 */
	public static String getTime() {
		return time;
	}
	
	/**
	 * Gefur breytunum sem halda utan um dagsetningu bókunar
	 * gildi
	 * @param b
	 * @param e
	 */
	public static void setStartEndDate(String b, String e) {
		startDate = b;
		endDate = e;
	}
	
	/**
	 * Skilar dagsetningu bókunar á forminu "2014-11-17"
	 * @return
	 */
	public static String getDate() {
		return date;
	}
	
	/**
	 * Gefur breytu sem heldur utan um dagsetningu bókunar á
	 * forminu "2014-11-17" gildi
	 * @param d
	 */
	public static void setDate(String d) {
		date = d;
	}
	
	/**
	 * Gefur breytu sem heldur utan um dagsetningu bókunar á
	 * forminu "17-11-2014" gildi
	 * @param d
	 */
	public static void setStringDate(String d) {
		dagur = d;
	}
	
	/**
	 * Skilar dagsetningu bókunar á forminu "17-11-2014"
	 * @return String
	 */
	public static String getStringDate() {
		return dagur;
	}
	
	/**
	 * Skilar byrjunar dagsetningu bókunar á forminu "2014-11-17 09:00"
	 * @return String
	 */
	public static String getStartDate() {
		return startDate;
	}
	
	/**
	 * Skilar enda dagsetningu bókunar á forminu "2014-11-17 09:00"
	 * @return String
	 */
	public static String getEndDate() {
		return endDate;
	}
	
	/**
	 * Skilar tímalengd aðgerðarinnar sem var valin
	 * @return String
	 */
	public static String getLengd() {
		return lengd;
	}
	
	/**
	 * Gefur breytu sem heldur utan um tímalengd 
	 * aðgerðar gildi
	 * @param l
	 */
	public static void setLengd(String l) {
		lengd = l;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða starfsmaður 
	 * var valinn gildi
	 * @param s
	 */
	public static void setStarfsmadur(String s) {
		starfsmadur = s;
	}
	
	/**
	 * Gefur breytu sem heldur utan um auðkenni valins stafsmanns
	 * @param id
	 */
	public static void setStaffId(String id) {
		staff_id = id;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um auðkenni valins stafsmanns
	 * @return String
	 */
	public static String getStaffId() {
		return staff_id;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um hvaða starfsmaður 
	 * var valinn gildi
	 * @return String
	 */
	public static String getStarfsmadur() {
		return starfsmadur;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða aðgerð 
	 * var valinn gildi
	 * @param a
	 */
	public static void setAdgerd(String a) {
		adgerd = a;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um hvaða aðgerð
	 * var valinn gildi
	 * @return String
	 */
	public static String getAdgerd() {
		return adgerd;
	}
		
	/**
	 * Skilar gildi breytu sem heldur utan um hvaða hárlengd
	 * var valinn gildi
	 * @return String
	 */
	public static String getHarlengd() {
		return harlengd;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða hárlengd 
	 * var valinn gildi
	 * @param l
	 */
	public static void setHarlengd(String l) {
		harlengd = l;
	}
	
	/**
	 * Gefur breytu sem heldur utan um staðsetningu valins 
	 * stafsmanns í Spinner viðmótshlut
	 * @param i
	 */
	public static void setStarfsmadurPos(int i) {
		starfsmadurPos = i;
	}
	
	/**
	 * Gefur breytu sem heldur utan um staðsetningu valinnar 
	 * aðgerðar í Spinner viðmótshlut
	 * @param i
	 */
	public static void setAdgerdPos(int i) {
		adgerdPos = i;
	}
	
	/**
	 * Gefur breytu sem heldur utan um staðsetningu valinnar 
	 * hárlengdar í Spinner viðmótshlut
	 * @param i
	 */
	public static void setHarlengdPos(int i) {
		harlengdPos = i;
	}
	
	
	/**
	 * Skilar gildi breytu sem heldur utan um staðsetningu valins 
	 * stafsmanns í Spinner viðmótshlut
	 * @return int
	 */
	public static int getStarfsmadurPos() {
		return starfsmadurPos;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um staðsetningu valinnar 
	 * aðgerðar í Spinner viðmótshlut
	 * @return int
	 */
	public static int getAdgerdPos() {
		return adgerdPos;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um staðsetningu valinnar 
	 * hárlengdar í Spinner viðmótshlut
	 * @return int
	 */
	public static int getHarlengdPos() {
		return harlengdPos;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvort nýlega hafi verið bókuð pöntun
	 * gildi
	 * @param b
	 */
	public static void setBokudPontun(boolean b) {
		bokudPontun = b;
	}
	
	/**
	 * /**
	 * Skilar gildi breytu sem heldur utan um hvort nýlega hafi verið bókuð pöntun
	 * @return boolean
	 */
	public static boolean getBokudPontun() {
		return bokudPontun;
	}
	
	
	/**
	 * Skilar nafni þess starfsmanns sem á auðkennið s
	 * @param s
	 * @return String
	 */
	public static String getStarfsmadur(String s) {
		
		String starfsmadur;
		
		switch(s) {
			case "BOB": 
				starfsmadur = "Bambi";
				break;
			
			case "PIP" : 
				starfsmadur = "Perla";
				break;
			
			case "ODO" : 
				starfsmadur = "Oddur";
				break;
			
			case "MRV" : 
				starfsmadur= "Magnea";
				break;
			
			case "EDK" :
				starfsmadur = "Eva";
				break;
			
			case "BIP" : 
				starfsmadur = "Birkir";
				break;
			
			case "DOR" : 
				starfsmadur = "Dagný";
				break;
			
			default: starfsmadur = "Error";
			
		}
		return starfsmadur;
	}
	
}


