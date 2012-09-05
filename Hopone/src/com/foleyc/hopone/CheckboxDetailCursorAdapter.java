package com.foleyc.hopone;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CheckboxDetailCursorAdapter extends SimpleCursorAdapter {

	private Cursor c;
	private Context context;
	//private ArrayList<Integer> checkList = new ArrayList<Integer>();
		
	public CheckboxDetailCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
	        this.context = context;	        
	}
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.check_list_detail, null);
		}
		Log.i("pos = ..................", "pos = "+pos);
		this.c.moveToPosition(pos);	
		//this.c.moveToPosition(this.c.getInt(this.c.getColumnIndex("_id")));	
		CheckBox cBox = (CheckBox) v.findViewById(R.id.bcheck);
		cBox.setTag(this.c.getInt(this.c.getColumnIndex("_id")));

		/*
		 * when reloading the list, check for chkd status, this is broken.  Need to query db directly.
		 */
		EventDbAdapter mDbHelper = new EventDbAdapter(context);
        mDbHelper.open(); 
		
        int idTag = (Integer) cBox.getTag();		        
		int checked = mDbHelper.selectChk(idTag);
        mDbHelper.close();
        Log.i("results from selectChk.....................", ""+checked);
		if (checked == 1) {
			cBox.setChecked(true);			
		} else {
			cBox.setChecked(false);
		}

		/*
		 * Populate the list
		 */		
		TextView txtdateTime = (TextView)v.findViewById(R.id.time);
		if (CheckboxCursorAdapter.nullCheck(this.c.getString(this.c.getColumnIndex("date")))) {
			txtdateTime.setText("Time: TBD");
		} else {
			
			String dateTime = this.c.getString(this.c.getColumnIndex("date"));
			String time = UIUtils.parseDateTime(dateTime, "time", context);
			txtdateTime.setText(time);
			//txtdateTime.setText(this.c.getString(this.c.getColumnIndex("date")));		
		}		
		
		TextView txtdateEvent = (TextView)v.findViewById(R.id.event);
		if (CheckboxCursorAdapter.nullCheck(this.c.getString(this.c.getColumnIndex("event")))) {
			txtdateEvent.setText("Event: TBD");
		} else {
			txtdateEvent.setText(this.c.getString(this.c.getColumnIndex("event")));		
		}	
				
		TextView txtdateLocation = (TextView)v.findViewById(R.id.location);
		if (CheckboxCursorAdapter.nullCheck(this.c.getString(this.c.getColumnIndex("location")))) {
			txtdateLocation.setText("Location: TBD");
		} else {
			txtdateLocation.setText(this.c.getString(this.c.getColumnIndex("location")));		
		}		
		
		TextView txtdateDate = (TextView)v.findViewById(R.id.date);
		if (CheckboxCursorAdapter.nullCheck(this.c.getString(this.c.getColumnIndex("date")))) {
			txtdateDate.setText("Date: TBD");
		} else {
			String dateTime = this.c.getString(this.c.getColumnIndex("date"));
			String date = UIUtils.parseDateTime(dateTime, "date", context);
			txtdateDate.setText(date);
			//txtdateDate.setText(this.c.getString(this.c.getColumnIndex("date")));		
		}
				
		//Log.i("if chk in db is = 1 then set checked.........",this.c.getString(this.c.getColumnIndex("checked")) +" " +this.c.getString(this.c.getColumnIndex("time")));		
		
		/*
		 * Controls action based on clicked list item (background)
		 */
//		View lv = v.getRootView(); 
//		lv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View lv) {
//				CheckBox cBox = (CheckBox) lv.findViewById(R.id.bcheck);
//
//				String id = cBox.getTag().toString();
//				Intent i = new Intent(context, EventDetail.class);
//				i.putExtra("_id", id);
//				context.startActivity(i);				
//			}			
//		});
		
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
//					c.requery();
//					mDbHelper.close();
					
				} else if (!cBox.isChecked()) {
					CharSequence charseq = "Removed from 'Starred' Items";
					Toast.makeText(context, charseq, Toast.LENGTH_SHORT).show();

					String event = c.getString(c.getColumnIndex("event"));
					int id = (Integer) cBox.getTag();
					mDbHelper.updateChecked(cBox.getTag().toString(), "0"); 
//					c.requery();
//					mDbHelper.close();					
				}
				//mDbHelper.close();
			}
		});
				
		return(v);
	}

}