package com.foleyc.hopone;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

class MyCheckedFragmentPagerAdapter extends FragmentPagerAdapter {  

	 ArrayList<String> titles = new ArrayList<String>();	
	 private Context context;
	 
     public MyCheckedFragmentPagerAdapter(FragmentManager fm) {  
          super(fm);  
          titles.add("Thursday");
          titles.add("Friday");
          titles.add("Saturday");
     }  
     
     
     @Override  
     public Fragment getItem(int index) {
    	 getPageTitle(index);
    	 //Fragment newFragment = ThursdayFragment.newInstance(titles.get(0));
    	 switch (index) {
         case 0: return new ThursdayChkFragment();
         case 1: return new FridayFragment();
         case 2: return new SaturdayFragment();
         case 3: return new SaturdayFragment();
     }
     return null;
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
