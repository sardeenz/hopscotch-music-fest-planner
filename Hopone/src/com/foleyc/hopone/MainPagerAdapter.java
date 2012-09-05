package com.foleyc.hopone;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

class MainPagerAdapter extends PagerAdapter {  

	 ArrayList<String> titles = new ArrayList<String>();	   
     
     public MainPagerAdapter() {
		super();
		// TODO Auto-generated constructor stub
	 }
     
     @Override
     public void destroyItem(ViewGroup container, int position, Object object) {
         // remove the object (we don't want to keep it in memory as it will get recreated and cached when needed)

         ViewPager viewPager = (ViewPager) container;
         View view = (View) object;
         viewPager.removeView(view);
     }
     
     @Override
     public Object instantiateItem(View pager, final int position) {
         // inflate your 'content'-views here (in my case I added a listview here)
         // similar to a listadapter's `getView()`-method

    	 View wrapper = inflater.inflate(R.layout.check_list, container, false);
    	 
         View wrapper = ... // inflate your layout
         ... // fill your data to the appropriate views

         ((ViewPager) pager).addView(wrapper);
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
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}  
