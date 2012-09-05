/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.foleyc.hopone;

import com.actionbarsherlock.app.ActionBar;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends ListActivity {

	private EventDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        ActionBar actionBar = getSupportActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//		actionBar.setDisplayShowHomeEnabled(true);
//		actionBar.setDisplayShowTitleEnabled(false);
// CHF        setContentView(R.layout.actionbarlistnospinner);
        //setContentView(R.layout.actionbar_list);
        Log.i("inside SearchActivity!!!!", "inside SearchActivity!!!!" );
	    mDbHelper = new EventDbAdapter(this);	    
        onNewIntent(getIntent());
        String query  = getIntent().getStringExtra("query");
        Log.i("before query - bands", query);
        searchBands(query);
        Log.i("after query - bands", query);
        
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//    	setIntent(intent);
//    	handleIntent(intent);      
//    }
    
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//          //String query = intent.getStringExtra(SearchManager.QUERY);
//          String mQuery = intent.getStringExtra(SearchManager.QUERY);
//          //mQuery = "%"+mQuery+"%";
//          searchBands(mQuery);
//        }
//    }    

    public void onHomeClick(View v) {
        UIUtils.goHome(this);
    }

    public void onSearchClick(View v) {
        //UIUtils.goSearch(this);
    }
    
    /** Build and add "sessions" tab. */
    private void searchBands(String mQuery) {
    	
	    //SeparatedListAdapter adapter = new SeparatedListAdapter(this);
	    mDbHelper.open();
    	Cursor c = mDbHelper.fetchAllEventsByQuery(mQuery);
	    // attempting to display alternate view if not data available.
	    if (c.getCount()< 1) {
			Log.i("count = ..................", "count = "+c.getCount());
			boolean cnt = true;	
// CHF			setContentView(R.layout.actionbar_search_empty);
	    } else {
 	    
 	 	String[] displayFields = new String[] {"date", "event", "location"};
        int[] displayViews = new int[] {R.id.time, R.id.event, R.id.location};
        CheckboxCursorAdapter cboxAdapter = new CheckboxCursorAdapter(this, R.layout.check_list, c, displayFields, displayViews);	   	
	   	//adapter.addSection("Search Results", cboxAdapter);
	   	setListAdapter(cboxAdapter);
//    	ListView lv = (ListView) findViewById(android.R.id.list);
//    	lv.setFastScrollEnabled(true);
//		lv.setAdapter(adapter);	
	    }
	   	//Log.i("after cboxadapter", "after cboxadapter");

    }
    
//    @Override
//    public boolean onSearchRequested() {
//        pauseSomeStuff();
//        return super.onSearchRequested();
//    }


}
