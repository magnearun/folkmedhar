/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 15.10.2014
 * Klasinn sem sem sér um að birta upphafsskjá forritsins
 */

package com.example.folkmedhar;


import com.cengalabs.flatui.FlatUI;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.folkmedhar.notendur.LoginActivity;
import com.example.folkmedhar.notendur.UpdateUser;
import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.pantanir.MinarPantanir;
import com.example.folkmedhar.pantanir.bokun.Skref1;

@SuppressLint("InflateParams") public class MainActivity extends Activity {
	
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
	
	// Navigation Drawer og Action Bar
	private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] menuTitles; 
    private FrameLayout frame;
    private float lastTranslate = 0.0f;
    private ViewGroup decor;
	
	public static FragmentManager fragmentManager;
    
	
    @Override
    /**
     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
     * til að panta tíma eða fara á „Mínar pantanir"
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Uppfæra upplýsingar um notanda
     	nafn = UserFunctions.userName(this.getBaseContext());
     	simi = UserFunctions.userPhone(this.getBaseContext());
     	email = UserFunctions.userEmail(this.getBaseContext());

        intent = new Intent(this, LoginActivity.class);
              
        UserFunctions userFunction = new UserFunctions();
        fragmentManager = getFragmentManager();
        
        
        
        bokudPontun = false;
        time = "";
        
        // Notanndinn er skráður inn
        if(userFunction.isUserLoggedIn(getApplicationContext())) {
        	setContentView(R.layout.activity_main);
        	
        	setNavigationDrawer();
        	setActionBar();
            
        	// Birta upphafsskjá ef appið er opnað í fyrsta skiptið
            if (savedInstanceState == null) {
            	Fragment fragment = new Upphafsskjar();
            	fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment) // Ekki bæta við backstack
                .commit();
            }
        
        // Notandinn er ekki innskráður
        }else {
        	 startActivity(intent); // Birta skjá fyrir innskráningu
             finish();
             }
        }

    /**
     * Býr til nýjan „custom“ ActionBar 
     */
    private void setActionBar() {
    	ActionBar actionbar = getActionBar();
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
        actionbar.setCustomView(R.layout.actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }
    
    /**
     * Býr til Navigation drawer sem að ýtir öllu contentinu, ásamt ActionBar til hliðar 
     * þegar hann er opnaður
     */
    private void setNavigationDrawer() {
    	
    	// „Hack“ til að fá ActionBar til að færast með conteninu til hliðar
    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        drawerLayout = (DrawerLayout) inflater.inflate(R.layout.decor,null);
        decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        frame = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        frame.addView(child);
        decor.addView(drawerLayout);
    	
        menuTitles = getResources().getStringArray(R.array.menu_titles);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        drawerLayout.setDrawerShadow(R.drawable.navbar_shadow, Gravity.LEFT);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerList.setPadding(0, getStatusBarHeight(),0, 0); // Leiðrétting útaf „hack-i“
       
        drawerToggle = new ActionBarDrawerToggle(
                this,                 
                drawerLayout,       
                R.drawable.ic_burger_white,  
                R.string.app_name,  
                R.string.app_name 
                ) {
        	@SuppressLint("NewApi")
        	/**
        	 * Navigation drawer ýtir “content-inu“ til hliðar þegar hann er opnaður eða lokaður
        	 */
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
       
        	/**
        	 * Sér til þess að ActionBarDrawerToggle færist ekki til þegar navigation drawer
        	 * er opnaður
        	 */
        	public void onDrawerOpened(View drawerView) {
        		super.onDrawerSlide(drawerView, 0);
        		
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
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
	  * Býr til menu sem er notað til að opna skjá fyrir uppfærslu
	  * á notendaupplýsingum
	  */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu); 
        return super.onCreateOptionsMenu(menu);
    }
	 
    @Override
    /**
     * Birtir skjá fyrrir uppfærslu á notendaupplýsingum
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Fragment fragment = new UpdateUser();
	    updateFragment(fragment);
        return true;
    }

    /**
     * Hlustari fyrir navigation drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Birtir skjá fyrir það atriði sem var valið í navigation drawer
     * @param position
     */
    private void selectItem(int position) {
    	 Fragment fragment = null;
    	    switch(position) {
    	        case 0:
    	            fragment = new Upphafsskjar();
    	            break;
    	        case 1:
    	        	if (Connection.isOnline(this))
    	        	{
    	        		fragment = new Skref1();
    	        		 break;
    	        	}
    	        	else {
    	        		
    	        		return;
    	        	}
    	        case 2:
    	        	if (Connection.isOnline(this)) {
    	        		fragment = new MinarPantanir();
    	        		break;
    	        	}
    	        	else {
    	        		setToast("Engin nettenging!");
    	        		return;
    	        	}
    	        case 3:
    	        	fragment = new UmStofuna();
    	        	break;
    	        case 4: 
    	        	fragment = new Tilbod();
    	        	break;
    	        case 5:
    	        	if (Connection.isOnline(this)) {
	    	        	fragment = new Verdlisti();
	    	        	break;
    	        	}
    	        	else {
    	        		setToast("Engin nettenging!");
    	        		return;
    	        	}
    	        case 6: logout();
    	        	return;
    	        default: break;
    	    }
    	    
    	    updateFragment(fragment);
    	    
    	    drawerList.setItemChecked(position, true);
    	    setTitle(menuTitles[position]);
    	    drawerLayout.closeDrawer(drawerList);
    }
    
    /**
     * Birtir upplýsingarnar text á skjánum
     * @param text
     */
    private void setToast(String text) {
    	Toast toast = Toast.makeText(this, 
				"Engin nettenging!", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
    }


    @Override
    /**
     * Sync-a navigation drawer
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    /**
     * Uppfærir ástandi á navigation drawer
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Birtir skjá fyrir fragment
     * @param fragment
     */
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
	
	/**
	 * Skilar hæðinni á Status bar símans
	 * @return
	 */
	public int getStatusBarHeight() {
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      }
	      return result;
	}	
}


