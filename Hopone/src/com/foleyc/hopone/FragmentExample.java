package com.foleyc.hopone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentExample extends Fragment {
private int nAndroids=1;

public FragmentExample() {
}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
	    int n;
	
	    View l = inflater.inflate(R.layout.afragment,null);
	    TextView tv = (TextView) l.findViewById(R.id.textView1);
	    tv.setText("value "+nAndroids);
	
	    return l;
	}
}