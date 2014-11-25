/**
 * @author: Birkir, Dagný, Eva og Magnea
 * @since: 20.11.2014
 * Klasinn sér um að birta upphafsskjá forritsins og inniheldur aðferðir sem sjá um að birta
 * ProgressDialog og Toast og aðferð sem sér um að búa til nýtt fragment fyrir aðra skjái. 
 * Klasinn býr einnig til NavigationDrawer, valmynd fyrir stillingar og „custom“ ActionBar fyrir forritið.
 */

package com.example.folkmedhar;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.example.folkmedhar.pantanir.FerlaBokun;
import com.example.folkmedhar.pantanir.MinarPantanir;
import com.example.folkmedhar.pantanir.bokun.Skref1;

@SuppressLint("InflateParams") public class MainActivity extends ActionBarActivity {
		
	
	// Navigation Drawer og Action Bar
	private DrawerLayout drawerLayout;
    private static ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] menuTitles; 
    private FrameLayout frame;
    private float lastTranslate = 0.0f;
	
	private static FragmentManager fragmentManager;
	private static ProgressDialog pDialog;
	private static Context baseContext;
	private Intent loginIntent;
	
	
    @Override
    /**
     * Birtir upphafsskjáinn og tengir onClickListener við takka sem notaðir eru
     * til að panta tíma eða fara á „Mínar pantanir"
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        pDialog = new ProgressDialog(this);
        baseContext = this.getBaseContext();
        loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        new FerlaBokun();
        
        UserFunctions userFunction = new UserFunctions();
        // Notandinn er skráður inn
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
        	startActivity(loginIntent); // Birta skjá fyrir innskráningu
            finish();
             }
        }

    /**
     * Býr til nýjan „custom“ ActionBar 
     */
    public void setActionBar() {
    	ActionBar actionbar = getSupportActionBar();
        actionbar.setCustomView(R.layout.actionbar);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);  
    }
    
    /**
     * Býr til Navigation drawer sem að ýtir öllu contentinu, ásamt ActionBar til hliðar 
     * þegar hann er opnaður
     */
    private void setNavigationDrawer() {
    	
    	// „Hack“ til að fá ActionBar til að færast með contentinu til hliðar
    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        drawerLayout = (DrawerLayout) inflater.inflate(R.layout.decor,null);
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
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
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
    }
   
    
    /**
	  * Notandi hefur verið skráður út og login síða birt 
	  */
	 private void logout() {
		 UserFunctions userFunction = new UserFunctions();
		 userFunction.logoutUser(getApplicationContext());
		 startActivity(loginIntent);
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
     * Birtir skjá fyrir það atriði sem var valið í navigation drawer. Birtir tilkynningu
     * um að engin nettenging sé til staðar ef atriði sem þarfnast nettengingar er valið í stað
     * skjásins
     * @param position
     */
    private void selectItem(int position) {
    	 Fragment fragment = null;
    	 if(position==1 || position==2 || position==4 || position==5) {
    		 if(!Connection.isOnline(this)) {
    			 showToast("Engin nettenging!",this);
	        		return;
    		 }
    	 }
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
	        case 5:
    	        fragment = new Verdlisti();
    	        break;
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
     * Birtir upplýsingarnar text á skjánum með Toast
     * @param text
     */
    public static void showToast(String text, Context c) {
    	Toast toast = Toast.makeText(c, 
				text, Toast.LENGTH_SHORT);
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
	 * Skilar FragmentManager
	 * @return
	 */
	public static FragmentManager getFM() {
		return fragmentManager;
	}
	
	
	/**
	 * Skilar hæðinni á status bar símans
	 * @return
	 */
	private int getStatusBarHeight() {
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      }
	      return result;
	}
	
	/**
	 * Birtir progress dialog með skilaboðunum text
	 * @param text
	 */
	public static void showDialog(String text) {
		pDialog.setMessage(text);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
	
	/**
	 * Lokar progress dialog
	 */
	public static void hideDialog() {
		pDialog.dismiss();
	}
	
	/**
	 * Skilar BaseContext
	 * @return
	 */
	public static Context getContext() {
		return baseContext;
	}
	
	/**
	 * Atriði númer pos er skráð sem valið í NavigationDraweer
	 * @param pos
	 */
	public static void setSelectedDrawer(int pos) {
		drawerList.setItemChecked(pos, true);
	}
}


