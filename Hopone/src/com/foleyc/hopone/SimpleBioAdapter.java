package com.foleyc.hopone;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SimpleBioAdapter extends SimpleCursorAdapter {

	private Cursor c;
	private Context context;
		
	public SimpleBioAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
	        this.context = context;	        
	}
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.detailadapter, null);
		}
		//this.c.moveToPosition(pos);	

		TextView tvBio = (TextView)v.findViewById(R.id.description);
		if (this.c.getString(this.c.getColumnIndex("description")) != null) {
			tvBio.setText(this.c.getString(this.c.getColumnIndex("description")));		
		} else {
			tvBio.setVisibility(8);
		}
		


		
		return(v);
	}

}