package com.example.targetedads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.targetedads.CouponList;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.widget.AdapterView.OnItemSelectedListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity implements OnItemSelectedListener, LocationListener{
	
	// Places Listview
    ListView lv;
    GooglePlaces gp = new GooglePlaces();
    List<Place> near_places = new ArrayList<Place>();
    Location location;
    String provider;
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    
    double[] locs = new double[2];
    
    //Variables to collect the user input
    private String categoryUsrInput;
    private String distanceUsrInput;
    private String sortByUsrInput;
    private String timingsUsrInput;
    private String prefUsrInput;

    // true - list, false - map
    boolean btnSelectFlag = true;
    boolean locReturnFlag = true;
    boolean resultReturnFlag = false;
    
    EditText prefText;
    TextView status;
    
    Long stTime;
    Long endTime;
    
  //***********************************************************************************
    
	protected void onCreate(Bundle savedInstanceState) {
		
		setTitle("Home Screen");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		System.out.println("max Heap Memory: " + Long.toString(maxMemory));
		System.out.println("total Memory: " + Long.toString(rt.totalMemory()));
		*/
		
		Spinner categorySpin = (Spinner) findViewById(R.id.spinner_categories);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_data, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		categorySpin.setAdapter(adapter);
		categorySpin.setOnItemSelectedListener(this);
		
				
		Spinner sortBySpin = (Spinner) findViewById(R.id.spinner_sortBy);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(this, R.array.sortBy_data, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sortBySpin.setAdapter(sortByAdapter);
		sortBySpin.setOnItemSelectedListener(this);
		
		status = (TextView) findViewById(R.id.txtStatus);
		status.setText("");
		
		getLocation();
	}

	//***********************************************************************************
	
	
	public void getLocation(){
		LocationManager locationManager;
		
		String message;
		// Get LocationManager object
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		// Creating an empty criteria object
		Criteria criteria = new Criteria();
		// Getting the name of the provider that meets the criteria
		//provider = locationManager.getBestProvider(criteria, false);
		provider = locationManager.GPS_PROVIDER;
		// If the provider is not null or an empty string, get the location coordinates
		if(provider!=null && !provider.equals(""))
		{
			//Get the location from the given provider
			location = locationManager.getLastKnownLocation(provider);
			locationManager.requestLocationUpdates(provider, 2000, 1, this);
			if(location!=null)
				{
					// If the location is not null, then get the GPS coordinates and add them to a string 
					message = " Longitude: " + location.getLongitude() + " Latitude: " + location.getLatitude();
					Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
					
				}
				else
				{
					Toast.makeText(getBaseContext(), "Please Turn on your GPS", Toast.LENGTH_SHORT).show();
				}
		}
		else  // No provider is found
		{
			Toast.makeText(getBaseContext(), "Provider cannot be retrieved", Toast.LENGTH_SHORT).show();		
		}
		
	}
	
	
	



	@Override
	// Code to display what is selected from the drop down list
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		
		Spinner spinCatg = (Spinner)parent;
        Spinner spinTime = (Spinner)parent;
        if(spinCatg.getId() == R.id.spinner_categories)
        {
        	categoryUsrInput = (String) spinCatg.getItemAtPosition(position);                 
        }
        if(spinTime.getId() == R.id.spinner_sortBy)
        {
        	sortByUsrInput = (String) spinTime.getItemAtPosition(position);      
        }
		
	}

	//***********************************************************************************

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	//***********************************************************************************
	
	// Logic of what happens when a radio button is selected
	public void onRadioButtonClicked(View view){
		// Check if the radio button is selected
		boolean checked = ((RadioButton) view).isChecked();
	
		// Check which radio button was clicked
		switch(view.getId()){
			case R.id.radio_nearby:
				if(checked)
					distanceUsrInput = "nearby";
				break;
			
			case R.id.radio_entire:
				if(checked)
					distanceUsrInput = "entire";
				break;
				
			case R.id.radio_now:
				if(checked)
					timingsUsrInput = "now";
				break;
				
			case R.id.radio_later:
				if(checked)
					timingsUsrInput = "later";
				break;
		}
	}
	
	
	//***********************************************************************************
	
	// When the user clicks the list button
	public void displayList(View view) throws InterruptedException{
		
		stTime = System.currentTimeMillis();
				
		btnSelectFlag = true;
		
		prefText = (EditText) findViewById(R.id.editPreference);
		prefUsrInput = prefText.getText().toString();
			 
		if(distanceUsrInput == null)
			Toast.makeText(getBaseContext(), "Please select Distance", Toast.LENGTH_LONG).show();
		else if(timingsUsrInput == null)
			Toast.makeText(getBaseContext(), "Please select Timings", Toast.LENGTH_LONG).show();
		else{
			status.setTextSize(20);
			status.setText("Fetching Results...");
			new LoadPlaces().execute();
		}
		
	}
	
	
	
	//***********************************************************************************
	
	
	// The "show map" button directs the user to a map view with the places marked with markers
	public void displayMap(View view){
		
		//getLocation();
		
		stTime = System.currentTimeMillis();
		
		btnSelectFlag = false;
		
		prefText = (EditText) findViewById(R.id.editPreference);
		prefUsrInput = prefText.getText().toString();
		
		if(distanceUsrInput == null)
			Toast.makeText(getBaseContext(), "Please select Distance", Toast.LENGTH_LONG).show();
		else if(timingsUsrInput == null)
			Toast.makeText(getBaseContext(), "Please select Timings", Toast.LENGTH_LONG).show();
		else{
			status.setTextSize(20);
			status.setText("Fetching Results...");
			new LoadPlaces().execute();
		}
	}
	
	
	
	//***********************************************************************************
	
	
	private class LoadPlaces extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			 double _radius =0.0;
			 String types = null;
			
			 //double _latitude = location.getLatitude();
			 double _latitude = 39.1622;
			
			 
			 //double _longitude= location.getLongitude();
			 double _longitude= -86.5292;
			 if (distanceUsrInput.equalsIgnoreCase("nearby"))
				  _radius = 300;
			 else
				  _radius = 1000;
			 if (categoryUsrInput.equalsIgnoreCase("restaurants"))
				 types = "restaurant";
			 
			 location.setLatitude(_latitude);
			 location.setLongitude(_longitude);
			 
			 
			 try {
				//System.out.println("Inside aysc task");
				
				//status.setText("Fetching Results");
				
				near_places=gp.search(_latitude,_longitude,_radius,types,timingsUsrInput,prefUsrInput);
				
				if(near_places.size() == 0)
					resultReturnFlag = false;
				else
					resultReturnFlag = true;
				
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 return null;
		}	
		
		/**
		 * To add distance and coupons
		 * @param near_places
		 * @param location
		 */
		private void set_additional(List<Place> near_places, Location location) {
			// TODO Auto-generated method stub
			CouponList cplist = new CouponList();
			Location location2 = new Location(provider);
			
			status.setTextSize(20);
			status.setText("Integrating coupons");
			
			for(Place p : near_places)
			{					
				location2.setLatitude(p.geometry.location.lat);
				location2.setLongitude(p.geometry.location.lng); 	
				p.distance=location.distanceTo(location2);
				//System.out.println("Distance for "+p.name+" "+p.distance);
				
				 cplist=gp.get_coupons(p.id);
				 //System.out.println("Coupon List "+cplist);
				 
				p.dish=cplist.Specality_dish;
				  if(cplist.Coupons!=null||cplist.Coupons.size()>0)
					  p.coupons=cplist.Coupons;
			  
				  //System.out.println("Coupons for "+p.name+" "+p.coupons.size());
			}
			
			status.setTextSize(20);
			status.setText("Done...");
		
			//If Sort by Distance
			if(sortByUsrInput.equalsIgnoreCase("distance"))
				Collections.sort(near_places,new DistanceCompare());
			//If Sort by Coupons
			if(sortByUsrInput.equalsIgnoreCase("coupons"))
				Collections.sort(near_places, Collections.reverseOrder(new CouponCompare()));
			//If Sort by Rating
			if(sortByUsrInput.equalsIgnoreCase("ratings"))
				Collections.sort(near_places,Collections.reverseOrder(new RatingCompare()));

			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//System.out.println("Near Places "+near_places.size());
			//Set Distance,no_of_coupons,coupons,dish
			set_additional(near_places,location);		
			
			if(resultReturnFlag == true){
				
				// Initialize the serializable object to transfer to a new activity
				ObjectsHolder objectData = new ObjectsHolder();
				objectData.currLocation[0] = location.getLatitude();
				objectData.currLocation[1] = location.getLongitude(); 
				objectData.near_places = near_places;
				
				//System.out.println("Execution time for "+near_places.size()+" is "+(System.currentTimeMillis() - stTime)+" millisecs");
				
				if(btnSelectFlag == true){
					status.setText("");
					Intent dispList = new Intent(MainActivity.this, DisplayList.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("serialized", (Serializable) objectData);
					dispList.putExtras(bundle);
					startActivity(dispList);
				}
				else{
					status.setText("");
					Intent displayMap = new Intent(MainActivity.this, DisplayMap.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("mapData", (Serializable) objectData);
					displayMap.putExtras(bundle);
					startActivity(displayMap);
				}
			}
			else{
				Toast.makeText(getBaseContext(), "No Results returned for the selected criteria. Please select different criteria.", Toast.LENGTH_LONG).show();
				status.setText("No Results matched");
			}
		}
	}



	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}

