package com.foleyc.hopone;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class SeparatedListAdapter extends BaseAdapter {

	public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
	public final ArrayAdapter<String> headers;
	public final static int TYPE_SECTION_HEADER = 0;
	String section = "";

	public SeparatedListAdapter(Context context) {
		headers = new ArrayAdapter<String>(context, R.layout.list_header);
	}

	public void addSection(String section, Adapter adapter) {
		Log.i("section1.........", section);
		this.headers.add(section);
		this.sections.put(section, adapter);
	}
	
//	public void addSectionNoHeader(Adapter adapter) {
//		this.sections.put("0", adapter);
//	}

	public Object getItem(int position) {
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return section;
			if(position < size) return adapter.getItem(position - 1);

			// otherwise jump into next section
			position -= size;
		}
		return null;
	}

	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for(Adapter adapter : this.sections.values())
			total += adapter.getCount() + 1;
		return total;
	}

	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for(Adapter adapter : this.sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}

	public int getItemViewType(int position) {
		int type = 1;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return TYPE_SECTION_HEADER;
			if(position < size) return type + adapter.getItemViewType(position - 1);

			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isEnabled(int position) {		
		//return false;
		int x = getItemViewType(position);
		Log.i("section isEnabled.........", section + " x= " +x);
		if (x == 2) {
			return true;
		} else {
			return false;
		}
//		if (section.equals("Listen")) {
//			return true;
//		} else {
//			return false;
//		}
		//return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		
		int sectionnum = 0;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return headers.getView(sectionnum, convertView, parent);
			if(position < size) return adapter.getView(position - 1, convertView, parent);

			// otherwise jump into next section
			position -= size;
			sectionnum++;
			//String t = sections.get(0).toString();
			Log.i("sections.....................", "get0 "+ sectionnum + " sections" + sections.toString());
			
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}



}

