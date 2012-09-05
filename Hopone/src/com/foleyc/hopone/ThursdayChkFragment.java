package com.foleyc.hopone;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

public class ThursdayChkFragment extends ListFragment implements OnSharedPreferenceChangeListener {
	
	private static final String KEY_CONTENT = "ThursdayFragment:Content";
	private String mContent = "???";
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
        
        //id = spinnerPrefs.getLong(spinnerID, id);

        	c = mDbHelper.fetchAllCheckedEventsByDateSortByDate("2011-09-06 21:00:00.000000"); 	        	        				  
		CheckboxCursorAdapter ca  = new CheckboxCursorAdapter(this.getActivity().getApplicationContext(), R.layout.check_list, c, displayFields, displayViews);
		setListAdapter(ca);
			
		super.onActivityCreated(savedInstanceState);
	}
       
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
    
//    @Override
//    public void onResume() {
//        super.onResume();
//        mCheckBox.setChecked(load());
//    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void onResume() {
//		EventDbAdapter mDbHelper;
//		mDbHelper = new EventDbAdapter(getActivity());
//		mDbHelper.open();		
//    	String[] displayFields = new String[] {"event", "location"};
//        int[] displayViews = new int[] {R.id.event, R.id.location}; 
//
//		c = mDbHelper.fetchAllEventsByDate("2011-09-06 21:00:00.000000"); 
//
//		CheckboxCursorAdapter ca  = new CheckboxCursorAdapter(this.getActivity().getApplicationContext(), R.layout.check_list, c, displayFields, displayViews);
//		setListAdapter(ca);
//		super.onResume();
//	}
    
}

