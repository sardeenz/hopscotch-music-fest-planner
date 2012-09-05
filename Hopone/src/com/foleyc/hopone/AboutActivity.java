package com.foleyc.hopone;

	import com.foleyc.hopone.SeparatedListAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AboutActivity extends ListActivity {
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.about_list);
	        //setContentView(R.layout.actionbarlistnospinner);
	        //setContentView(R.layout.about_list);
	        Log.i("inside AboutActivity","AboutActivity onCreate");
	        
//	        final Button button = (Button) findViewById(R.id.emailbutton);
//	         button.setOnClickListener(new View.OnClickListener() {
//	             public void onClick(View v) {
//	                 // Perform action on click
//	            	 emailMeMethod(v);
//	             }
//	         });
	        
	        String appVersion = getVersion();
	        //String[] displayFields = new String[] {"location", "address1", "address2", "phone"};
	        //int[] displayViews = new int[] {R.id.location, R.id.address1, R.id.address2, R.id.phone};    
	        
	        //String[] aboutArray = new String[] {"+chad foley, @emulsion, sardeenz@gmail.com", "Feel free to get in touch.  Tip: You can now add 'starred' bands to your calendar via your phone's Menu Option button","version 1.11"}; 
	        String[] aboutArray = new String[] {"+chad foley, @emulsion, sardeenz@gmail.com", "Select a band to listen to their songs. Star your favorite bands. Plan your Hopscotch Music Festival experience.  If you use this app please send me a tweet and tell your friends.","version 1.0"}; 
	        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
			
			adapter.addSection("About", new ArrayAdapter<String>(this,R.layout.about_list, aboutArray));
//	    	ListView lv = getListView();
//	    	lv.setTextFilterEnabled(true);
			//(ListView) findViewById(android.R.id.list);
	    	//lv.setFastScrollEnabled(true);
			//lv.setAdapter(adapter);	
			setListAdapter(adapter); 
	    }
	    
	    
	    private String getVersion() {
			String versionNo = ""; 
	        PackageInfo pInfo = null; 
	        try{ 
	                pInfo = getPackageManager().getPackageInfo ("com.foley.hopscotch.planner",PackageManager.GET_META_DATA); 
	        } catch (NameNotFoundException e) { 
	                pInfo = null; 
	        } 
	        if(pInfo != null) 
	                versionNo = ""+pInfo.versionCode;
	        		return versionNo;
		}

		public void emailMeMethod(View view) {
	    	Log.i("inside EmailMethod","insideEmailMethod");
	    }
	    
	    public void onHomeClick(View v) {
	       	Log.i("inside onHomeClick","inside onHomeClick");
	           UIUtils.goHome(this);
	       }

	       public void onSearchClick(View v) {
	       	Log.i("inside onSearchClick","inside onSearchClick");
	           UIUtils.goSearch(this);
	       }
	    
}

