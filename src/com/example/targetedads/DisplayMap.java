// This class displays the map view of the places and marks each of them

package com.example.targetedads;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayMap extends Activity {
	
	//initialize a object of map
	GoogleMap googleMap;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_on_map);
		
		setTitle("Map View of Results");
		
		final ObjectsHolder objectData =  (ObjectsHolder) getIntent().getSerializableExtra("mapData");
		
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.MapFragment)).getMap();
		
		// Animate the Google staellite camera to zoom into bloomington when opened. 
		// Otherwise, the default is over Africa with zoomed out all the way
		LatLng latlng = new LatLng(objectData.currLocation[0], objectData.currLocation[1]);
		
		Toast.makeText(getBaseContext(), ""+objectData.currLocation[0]+" "+objectData.currLocation[1], Toast.LENGTH_LONG).show();
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(latlng)			// Sets the center of the map to the specified location
			.zoom(13.5f)
			.bearing(0)                // Sets the orientation of the camera to east
		    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
		    .build();                   // Creates a CameraPosition from the builder
		
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		// Add the markers for user's current location [color: ] & nearby places [color: ]
		MarkerOptions userLoc = new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("ME");
		googleMap.addMarker(userLoc);
		
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "You clicked "+arg0.getTitle(), Toast.LENGTH_LONG).show();
				
				arg0.showInfoWindow();
				
				
				
				int index = 0;
				if(arg0.getTitle().equals("ME") == false){
					// The title is set as <Index: Title> which would give us the object from the list as well. We split based on the ":" as it differentiates the index:Title
					index = Integer.parseInt(arg0.getTitle().split(":")[0]);
					Place clickedMarkerPlace = objectData.near_places.get(index);
					
					// The particular place item is then transferred 
					Intent dispEachItem = new Intent(getBaseContext(), ItemViewFromList.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("item", (Serializable) clickedMarkerPlace);
					dispEachItem.putExtras(bundle);
					startActivity(dispEachItem);
				}
				return true;
			}
		});
		
		
		for(int i = 0; i < objectData.near_places.size(); i++){
			String title = i+":"+objectData.near_places.get(i).name;
			LatLng lat_lng = new LatLng(objectData.near_places.get(i).geometry.location.lat, objectData.near_places.get(i).geometry.location.lng);
			googleMap.addMarker(new MarkerOptions().position(lat_lng).title(title));
		}
		
		
		
		
		
		
		
		
	}
	

}
