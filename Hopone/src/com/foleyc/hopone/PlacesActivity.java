package com.foleyc.hopone;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PlacesActivity extends MapActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maptabview);
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
	    VenueItemizedOverlay itemizedoverlay = new VenueItemizedOverlay(drawable, this);

	    
	    GeoPoint fayCityPlazaPoint = getPoint(35.77509,-78.63944);
	    GeoPoint berkeleyCafePoint = getPoint(35.77692,-78.64282);
	    GeoPoint deepSouthPoint = getPoint(35.77455,-78.64383);
	    GeoPoint fiveStarPoint = getPoint(35.77852,-78.64621);
	    GeoPoint fletcherOperaTheaterPoint = getPoint(35.77148,-78.63955);
	    GeoPoint theHiveatBusyBeeCafePoint = getPoint(35.77750,-78.63817);
	    GeoPoint kingsBarcadePoint = getPoint(35.77704,-78.63994);
	    GeoPoint theLincolnTheatrePoint = getPoint(35.77414,-78.63745);
	    GeoPoint thePourHousePoint = getPoint(35.77727,-78.63679);
	    GeoPoint slimsPoint = getPoint(35.77744,-78.63812);
	    GeoPoint tirNaNogPoint = getPoint(35.77738,-78.63666);
	    GeoPoint theUnionPoint = getPoint(35.77573,-78.64467);
	    GeoPoint whiteCollarCrimePoint = getPoint(35.77553,-78.64404);
	    GeoPoint camPoint = getPoint(35.776966,-78.645694);
	    GeoPoint longViewPoint = getPoint(35.778654,-78.635373);
	    GeoPoint memorialAuditoriumPoint = getPoint(35.771151,-78.639901);

	    //mapView.getController().animateTo(fayCityPlazaPoint);  // centers the map on this point	    
	    mapView.getController().setCenter(fayCityPlazaPoint);	 // centers the map on this point	    
	    mapView.getController().setZoom(16);
	    OverlayItem overlayitem = new OverlayItem(fayCityPlazaPoint, "Raleigh City Plaza", "450 Fayetteville St.");
	    OverlayItem overlayitem2 = new OverlayItem(berkeleyCafePoint, "Berkeley Cafe", "217 W. Martin St.");
	    OverlayItem overlayitem3 = new OverlayItem(deepSouthPoint, "Deep South", "430 S. Dawson St.");
	    OverlayItem overlayitem4 = new OverlayItem(fiveStarPoint, "Five Star", "511 W. Hargett St.");
	    OverlayItem overlayitem5 = new OverlayItem(fletcherOperaTheaterPoint, "Fletcher Opera Theater", "2 E South St");
	    OverlayItem overlayitem6 = new OverlayItem(theHiveatBusyBeeCafePoint, "The Hive at Busy Bee Cafe", "225 S. Wilmington St.");
	    OverlayItem overlayitem7 = new OverlayItem(kingsBarcadePoint, "Kings Barcade", "14 W. Martin St.");
	    OverlayItem overlayitem8 = new OverlayItem(theLincolnTheatrePoint, "The Lincoln Theatre ", "126 E. Cabarrus St.");
	    OverlayItem overlayitem9 = new OverlayItem(thePourHousePoint, "The Pour House", "224 S. Blount St.");
	    OverlayItem overlayitem10 = new OverlayItem(slimsPoint, "Slim's", "227 S. Wilmington St.");
	    OverlayItem overlayitem11 = new OverlayItem(tirNaNogPoint, "Tir na nog ", "218 S. Blount St.");
	    OverlayItem overlayitem12 = new OverlayItem(theUnionPoint, "The Union", "327 W. Davie St., No. 114");
	    OverlayItem overlayitem13 = new OverlayItem(whiteCollarCrimePoint, "White Collar Crime", "319 West Davie St.");
	    OverlayItem overlayitem14 = new OverlayItem(camPoint, "Contemporary Art Museum (CAM Raleigh)", "409 W. Martin St.");
	    OverlayItem overlayitem15 = new OverlayItem(longViewPoint, "The Long View Center", "118 S. Person St.");
	    OverlayItem overlayitem16 = new OverlayItem(memorialAuditoriumPoint, "Memorial Auditorium", "2 E. South St.");
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);	    
	    itemizedoverlay.addOverlay(overlayitem2);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem3);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem4);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem5);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem6);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem7);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem8);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem9);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem10);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem11);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem12);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem13);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem14);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem15);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem16);
	    mapOverlays.add(itemizedoverlay);

	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
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
