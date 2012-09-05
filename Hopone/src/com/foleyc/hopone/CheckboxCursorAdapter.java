package com.foleyc.hopone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CheckboxCursorAdapter extends SimpleCursorAdapter {

	private Cursor c;
	private Context context;
	public CheckboxCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
	        this.context = context;
	    
	}
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.check_list, null);	
		}
		//Log.i("pos = ..................", "pos = "+pos);
		this.c.moveToPosition(pos);	
		//this.c.moveToPosition(this.c.getInt(this.c.getColumnIndex("_id")));	
		CheckBox cBox = (CheckBox) v.findViewById(R.id.bcheck);
		cBox.setTag(this.c.getInt(this.c.getColumnIndex("_id")));
		//cBox.setTag(this.c.getInt(this.c.getColumnIndex("event")));

		/*
		 * when reloading the list, check for chkd status. Need to query db directly.
		 */
		int idTag = (Integer) cBox.getTag();		        
		EventDbAdapter mDbHelper = new EventDbAdapter(context);		        
		mDbHelper.open(); 
		int checked = mDbHelper.selectChk(idTag);		
        mDbHelper.close();
		if (checked == 1) {
			cBox.setChecked(true);			
		} else {
			cBox.setChecked(false);
		}

		/*
		 * Populate the list
		 */				
		TextView txtTime = (TextView)v.findViewById(R.id.time);
		if (nullCheck(this.c.getString(this.c.getColumnIndex("date")))) {
			txtTime.setText("Time: TBD");
		} else {			
			String dateTime = this.c.getString(this.c.getColumnIndex("date"));
			String time = UIUtils.parseDateTime(dateTime, "time", context);
			txtTime.setText(time);
		}
		
		TextView txtdateEvent = (TextView)v.findViewById(R.id.event);
		if (nullCheck(this.c.getString(this.c.getColumnIndex("event")))) {
			txtdateEvent.setText("Event: TBD");
		} else {
			txtdateEvent.setText(this.c.getString(this.c.getColumnIndex("event")));		
		}
		
		TextView txtdateLocation = (TextView)v.findViewById(R.id.location);
		if (nullCheck(this.c.getString(this.c.getColumnIndex("location")))) {
			txtdateLocation.setText("Location: TBD");
		} else {
			txtdateLocation.setText(this.c.getString(this.c.getColumnIndex("location")));		
		}
					
		TextView txtDate = (TextView)v.findViewById(R.id.date);
		if (nullCheck(this.c.getString(this.c.getColumnIndex("date")))) {
			txtDate.setText("Date: TBD");
		} else {
			String dateTime = this.c.getString(this.c.getColumnIndex("date"));
			String date = UIUtils.parseDateTime(dateTime, "date", context);
			txtDate.setText(date);			
		}
				
//		ImageView arrow = (ImageView) v.findViewById(R.id.arrowId);
//		arrow.setImageResource(R.drawable.rightarrow);
		//imageUrl = (this.c.getString(this.c.getColumnIndex("image")));	
		
		/*
		 * Controls action based on clicked list item (background)
		 */
		View lv = v.getRootView(); 
		lv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View lv) {

				CheckBox cBox = (CheckBox) lv.findViewById(R.id.bcheck);
				String id = cBox.getTag().toString();
//				String artist = cBox.getTag().toString();
				Intent i = new Intent(context, EventDetail.class);
				i.putExtra("_id", id);
				// CHF
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);				
			
			}			
		});
				

		/*
		 * Begin - Controls action based on clicked Text only
		 
		txtdateEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CharSequence charseq = "Darth Vader is alive";
				Toast.makeText(context, charseq, Toast.LENGTH_SHORT).show();
			}
			
		});
		
		* End - Controls action based on clicked Text only
		*/
		
		
		/*
		 * Controls action based on clicked checkbox 
		 */
		cBox.setOnClickListener(new OnClickListener() {  
			@Override
			public void onClick(View v) {
				EventDbAdapter mDbHelper = new EventDbAdapter(context);
		    	mDbHelper.open(); 
				
				CheckBox cBox = (CheckBox) v.findViewById(R.id.bcheck);
				if (cBox.isChecked()) {
					//cBox.setChecked(false);
					CharSequence charseq = "Added to 'Starred' Items";
					Toast.makeText(context, charseq, Toast.LENGTH_SHORT).show();
					
					// Update the database for each checked item
					mDbHelper.updateChecked(cBox.getTag().toString(), "1"); 	
					c.requery();
					mDbHelper.close();
					
				} else if (!cBox.isChecked()) {
					CharSequence charseq = "Removed from 'Starred' Items";
					Toast.makeText(context, charseq, Toast.LENGTH_SHORT).show();

//					String event = c.getString(c.getColumnIndex("event"));
//					int id = (Integer) cBox.getTag();
					mDbHelper.updateChecked(cBox.getTag().toString(), "0"); 
					c.requery();
					mDbHelper.close();					
				}
			}
		});
				
		return(v);
	}
		
	public static boolean nullCheck(String dbVal) {				
		return (dbVal == null || dbVal.equals(""))? true:false; 				
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.newView(context, cursor, parent);
	}
	
}