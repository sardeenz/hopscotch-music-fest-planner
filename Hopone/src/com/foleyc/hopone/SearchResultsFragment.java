package com.foleyc.hopone;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ListView;

public class SearchResultsFragment extends ListFragment {
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
        Log.i("inside SearchResultsFragment", "inside SearchResultsFragment!!!!");
        
        String query  = getActivity().getIntent().getStringExtra("query");
        //Log.i("before query - bands", query);
        
        SeparatedListAdapter adapter = new SeparatedListAdapter(this.getActivity().getApplicationContext());
	    //Log.i("mQuery - bands", mQuery);
	    mDbHelper.open();
    	Cursor c = mDbHelper.fetchAllEventsByQuery(query);
 	    //startManagingCursor(c);
	    // attempting to display alternate view if not data available.
	    if (c.getCount()< 1) {
			Log.i("count = ..................", "count = "+c.getCount());
			boolean cnt = true;	
// CHF			setContentView(R.layout.actionbar_search_empty);
	    } else {
 	    //Log.i("c.getString(0) = ", c.getString(0));
 	    
        CheckboxCursorAdapter cboxAdapter = new CheckboxCursorAdapter(this.getActivity().getApplicationContext(), R.layout.check_list, c, displayFields, displayViews);	   	
        
        //adapter.addSection("Search Results", cboxAdapter);
        setListAdapter(cboxAdapter);
        //ListView lv = (ListView) findViewById(android.R.id.list);
    	//lv.setFastScrollEnabled(true);
		//lv.setAdapter(adapter);	
	    }
        
        
//        if (id==0){
//        	c = mDbHelper.fetchAllEventsByDateSortTime("2011-09-07 21:00:00.000000"); 	        	            
//        } else if (id == 1){
//        	c = mDbHelper.fetchAllCheckedEventsByDateSortByDate("2011-09-07 21:00:00.000000"); 	        	            
//        }
//		CheckboxCursorAdapter ca  = new CheckboxCursorAdapter(this.getActivity().getApplicationContext(), R.layout.check_list, c, displayFields, displayViews);
//		setListAdapter(ca);
			
		super.onActivityCreated(savedInstanceState);
	}
}
