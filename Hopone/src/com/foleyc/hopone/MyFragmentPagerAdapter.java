package com.foleyc.hopone;

import java.util.ArrayList;
import java.util.List;

//import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

class MyFragmentPagerAdapter extends FragmentStatePagerAdapter  {  

	 ArrayList<String> titles = new ArrayList<String>();
	 private List<Fragment> fragments;
	 private Context context;
	 SharedPreferences spinnerPrefs;
 	 private static final String SPIN_PREFS_NAME = "SpinnerPrefsFile";
 	 public String spinnerID = "spinnerID";
 	 private long id = 0;
     
 	 public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> frags) {  
          super(fm);  
          Log.i("fragment from Adapter", frags.toString());
          fragments = frags;
          titles.add("Thursday");
          titles.add("Friday");
          titles.add("Saturday");
          // if they've done a search before, add this fragment
//          if (frags.equals(SearchResultsFragment.class)) {
//        	  titles.add("Search Results");
//          }
          
          
     }  
     
     
//     public MyFragmentPagerAdapter(FragmentManager supportFragmentManager,
//			List<SherlockFragment> fragments2) {
//		// TODO Auto-generated constructor stub
//	}


	@Override  
     public Fragment getItem(int index) {
    	 
    	 return fragments.get(index);
//    	 getPageTitle(index);
//    	 //Fragment newFragment = ThursdayFragment.newInstance(titles.get(0));
//    	 switch (index) {
//         case 0: return new ThursdayFragment();
//         case 1: return new FridayFragment();
//         case 2: return new SaturdayFragment();
//         case 3: return new SearchResultsFragment();
//     }
//     return null;
     }  

     @Override
	public Object instantiateItem(View pager, int position) {
    	 //View wrapper = inflate(R.layout.check_list, container, false);    	 
    	 LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 View wrapper = inflater.inflate(R.layout.check_list, null);	
    	 
    	 ((ViewPager) pager).addView(wrapper);
    	 
    	 return wrapper;
    	 
    	 //return super.instantiateItem(container, position);
	}


	@Override  
     public int getCount() {  

          //return NUMBER_OF_PAGES;
    	 return titles.size();
     }
     
     @Override
     public CharSequence getPageTitle(int index) {
         return titles.get(index);         
     }


	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
		//return super.getItemPosition(object);
	}
	
}  
