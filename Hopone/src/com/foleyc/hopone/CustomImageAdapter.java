package com.foleyc.hopone;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomImageAdapter extends SimpleCursorAdapter {

	private Cursor c;
	private Context context;
		
	public CustomImageAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
	        this.context = context;	  	        			
	}	
	
	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_image, null);
		}				
		
		ImageView iv = (ImageView)v.findViewById(R.id.image);
		String imageUrl = (this.c.getString(this.c.getColumnIndex("visual")));	
		ImageDownloader imgDl = new ImageDownloader();
		imgDl.download(imageUrl, iv);
		
		TextView tvEvent = (TextView)v.findViewById(R.id.event);
		if (nullCheck(this.c.getString(this.c.getColumnIndex("event")))) {
			tvEvent.setText("N/A");
		} else {
			tvEvent.setText(this.c.getString(this.c.getColumnIndex("event")));		
		}
		//tvEvent.setText(this.c.getString(this.c.getColumnIndex("event")));
		
		return(v);
	}
	
	public static boolean nullCheck(String dbVal) {				
		return (dbVal == null || dbVal.equals(""))? true:false; 				
	}
	
}