package com.foleyc.hopone;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

//import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

public class FragmentPagerActivity extends SherlockFragmentActivity implements
		SpinnerAdapter, OnSharedPreferenceChangeListener /*,OnQueryTextListener */ {

	Context context; 
	//this.getApplicationContext()
	SharedPreferences spinnerPrefs;
	private static final String SPIN_PREFS_NAME = "SpinnerPrefsFile";
	private long id = 0;
	public String spinnerID = "spinnerID";

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	private EventDbAdapter mDbHelper;

	@Override
	protected void onResume() {
		int cnt = 0;
		boolean dbExists = mDbHelper.checkDataBase();
		if (dbExists) {
			mDbHelper.open();
			cnt = mDbHelper.selectEventCnt();
		}
		Log.i("cnt = ","cnt ="+cnt);
		if (!dbExists || cnt<175) {
			try {				
				mDbHelper.createDataBase();
			} catch (IOException ioe) {
				throw new Error("Unable to create database");
			}
		}

		try {
			mDbHelper.open();
		} catch (SQLException sqle) {
			throw sqle;
		}

		// drawUI();
		super.onResume();
	}

	private ViewPager mViewPager;
	private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
	TitlePageIndicator titleIndicator;
	
	private static int sdk = android.os.Build.VERSION.SDK_INT;
	private static boolean shinyNewAPIsSupported = android.os.Build.VERSION.SDK_INT > 7;
	
	  private static boolean fragmentsSupported = false;


	private static void checkFragmentsSupported() throws NoClassDefFoundError {
	    //fragmentsSupported = android.app.Fragment.class != null;
		
		fragmentsSupported = com.actionbarsherlock.app.SherlockFragment.class != null;
	  }

	  static {
	    try {
	      checkFragmentsSupported();
	    } catch (NoClassDefFoundError e) {
	      fragmentsSupported = false;
	    }
	  }

	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//this.context = context;

		/*
		 * Setup Menu Spinner
		 */
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);				
		
		spinnerPrefs = getSharedPreferences(SPIN_PREFS_NAME, 1);
		spinnerPrefs.registerOnSharedPreferenceChangeListener(this);

		mDbHelper = new EventDbAdapter(this);
		setContentView(R.layout.main);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		Log.i("sdk version","sdk version = "+sdk);
		Intent startActivityIntent = null;
	    if (!shinyNewAPIsSupported) {
	    	Log.i("API is not supported","API is not supported");
	    } else {
	    	
	    
		List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, ThursdayFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, FridayFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, SaturdayFragment.class.getName()));

		
		mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);

		mViewPager.setAdapter(mMyFragmentPagerAdapter);
		titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
		titleIndicator.setViewPager(mViewPager);
		
		spinnerPrefs = getSharedPreferences(SPIN_PREFS_NAME, 1);
	      SharedPreferences.Editor editor = spinnerPrefs.edit();     
	      editor.putLong(spinnerID, 0);
	      editor.commit();	     

		// actionBar.setDisplayHomeAsUpEnabled(true); // CHF - enable this on
		// subsequent activities to get back here
		SpinnerAdapter spinneradapter = ArrayAdapter.createFromResource(this,
				R.array.menu_drop_down_array,
				R.layout.spinner_dropdown_item);
		ActionBar.OnNavigationListener navigationCallback = new ActionBar.OnNavigationListener() {
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				String[] dropDown = getResources().getStringArray(
						R.array.menu_drop_down_array);
				/*
				 * When a spinner item is selected, add spinner selection (id) to shared preferences file	
				 */
				SharedPreferences.Editor editor = spinnerPrefs.edit();
				editor.putLong(spinnerID, itemPosition);
			    editor.commit();
			      
				
				Log.i("before Switch itemPosition == ?", "itemPosition == "	+ itemPosition);
				switch (itemPosition) {
				case 0: mMyFragmentPagerAdapter.notifyDataSetChanged();			
				Log.i("case 0", "case 0, Full Schedule - itemPosition == "	+ itemPosition);

				case 1: mMyFragmentPagerAdapter.notifyDataSetChanged();
				Log.i("case 1", "case 1, Favorites - itemPosition == "	+ itemPosition);
				
//				case 2: mMyFragmentPagerAdapter.notifyDataSetChanged();			
//				Log.i("case 2", "case 2, Map - itemPosition == "	+ itemPosition);
				case 2: mMyFragmentPagerAdapter.notifyDataSetChanged();
				Log.i("case 2", "case 2, Map Feed - itemPosition == "	+ itemPosition);
				
				if (dropDown[itemPosition].equals("Map")){
					mMyFragmentPagerAdapter.notifyDataSetChanged();
					Intent i = new Intent(getApplicationContext(), PlacesActivity.class);
					//i.putExtra("query", query);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(i);
				}
				
				Log.i("Map", dropDown[itemPosition]);
				
				case 3: mMyFragmentPagerAdapter.notifyDataSetChanged();
				Log.i("case 3", "case 3, Twitter - itemPosition == "	+ itemPosition);
				if (dropDown[itemPosition].equals("Twitter Feed")){
					mMyFragmentPagerAdapter.notifyDataSetChanged();
					Intent i = new Intent(getApplicationContext(), TwitterDownloader.class);
					//i.putExtra("query", query);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(i);
				}
				
				case 4: mMyFragmentPagerAdapter.notifyDataSetChanged();				
				if (dropDown[itemPosition].equals("About")){
					mMyFragmentPagerAdapter.notifyDataSetChanged();
					Intent i = new Intent(getApplicationContext(), AboutActivity.class);
					//i.putExtra("query", query);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(i);
				}
				
				
//				case 3: mMyFragmentPagerAdapter.notifyDataSetChanged();
//					Intent i = new Intent(getApplicationContext(), AboutActivity.class);
//					//i.putExtra("query", query);
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					getApplicationContext().startActivity(i);
				
				
				}
				return false;
			}
		};

		actionBar
				.setListNavigationCallbacks(spinneradapter, navigationCallback);
		// actionBar.setListNavigationCallbacks(spinneradapter,
		// ActionBar.MyOnNavigationListener);
		// CHF -
		// http://stackoverflow.com/questions/8662173/how-can-i-add-my-spinner-to-the-actionbar

	}
	    
	}  // end of else statement

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		id = spinnerPrefs.getLong(key, id);
	}

//	@Override
//	public boolean onQueryTextChange(String newText) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean onQueryTextSubmit(String query) {
//		
//		//getIntent().putExtra("query", query);
//		
//		Intent i = new Intent(this.getApplicationContext(), SearchActivity.class);
//		i.putExtra("query", query);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		this.getApplicationContext().startActivity(i);
//		
////		FragmentManager fragmentManager = getSupportFragmentManager();
////		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////		
////		SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
////		fragmentTransaction.add(R.id.viewpager, searchResultsFragment);
////		Log.i("before fragmentTransaction.commit()", "before fragmentTransaction.commit()");
////		fragmentTransaction.commit();
//		
//		
//		//searchBands(query);		
//        return true;
//	}
	
	// this method is used to support the legacy search
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_search:
	        	Log.i("inside onOptionsItemSelected","onOptionsItemSelected");
	            onSearchRequested();
	            return true;
	        default:
	            return false;
	    }
	}
	
    @TargetApi(11)
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	Log.i("inside onCreateOptionsMenu","onCreateOptionsMenu new search");
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);
        
        /*
         * only supported in api 11 and up 
         * searchView.setOnQueryTextListener(this);
         */
        
        }
        return true;
    }

    /** Build and add "sessions" tab. */
//    private void searchBands(String mQuery) {
//    	
//	    SeparatedListAdapter adapter = new SeparatedListAdapter(this);
//	    Log.i("mQuery - search query", mQuery);
//	    mDbHelper.open();
//    	Cursor c = mDbHelper.fetchAllEventsByQuery(mQuery);
// 	    startManagingCursor(c);
//	    // attempting to display alternate view if not data available.
//	    if (c.getCount()< 1) {
//			Log.i("count = ..................", "count = "+c.getCount());
//			boolean cnt = true;	
//			setContentView(R.layout.actionbar_search_empty);
//	    } else {
// 	    //Log.i("c.getString(0) = ", c.getString(0));
// 	    
// 	 	String[] displayFields = new String[] {"date", "event", "location"};
//        int[] displayViews = new int[] {R.id.time, R.id.event, R.id.location};
//        CheckboxCursorAdapter cboxAdapter = new CheckboxCursorAdapter(this, R.layout.check_list, c, displayFields, displayViews);	   	
//	   	adapter.addSection("Search Results", cboxAdapter);
//    	ListView lv = (ListView) findViewById(android.R.id.list);
//    	lv.setFastScrollEnabled(true);
//		lv.setAdapter(adapter);	
//	    }
//	   	//Log.i("after cboxadapter", "after cboxadapter");
//
//    }
    
}