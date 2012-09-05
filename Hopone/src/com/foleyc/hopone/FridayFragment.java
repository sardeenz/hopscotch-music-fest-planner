package com.foleyc.hopone;

import com.actionbarsherlock.app.SherlockListFragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

public class FridayFragment extends SherlockListFragment {
	
	public Cursor c;	
	SharedPreferences spinnerPrefs;
	private static final String SPIN_PREFS_NAME = "SpinnerPrefsFile";
	public String spinnerID = "spinnerID";
	private long id = 0;
	
	String[] displayFields = new String[] {"date", "event", "location"};
    int[] displayViews = new int[] {R.id.time, R.id.event, R.id.location};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		EventDbAdapter mDbHelper;
		mDbHelper = new EventDbAdapter(getActivity());
		mDbHelper.open();		
    	String[] displayFields = new String[] {"event", "location"};
        int[] displayViews = new int[] {R.id.event, R.id.location}; 
        
        /*
         * if sharedpref = 0 get all events, if = 1 then get checked events
         */
        spinnerPrefs = this.getActivity().getSharedPreferences(SPIN_PREFS_NAME, 1);
        id = spinnerPrefs.getLong(spinnerID, id);
        //Log.i("ThursdayFrag spinnerPrefs == ?", "ThursdayFrag spinnerPrefs == "+id);
        if (id==0){
        	c = mDbHelper.fetchAllEventsByDateSortTime("2012-09-07 21:00:00.000000"); 	        	            
        } else if (id == 1){
        	c = mDbHelper.fetchAllCheckedEventsByDateSortByDate("2012-09-07 21:00:00.000000"); 	        	            
        }
        
        
		CheckboxCursorAdapter ca  = new CheckboxCursorAdapter(this.getActivity().getApplicationContext(), R.layout.check_list, c, displayFields, displayViews);
		setListAdapter(ca);
			
		super.onActivityCreated(savedInstanceState);
	}

}
