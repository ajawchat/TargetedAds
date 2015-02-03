package com.example.targetedads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayList extends Activity implements Serializable{
	
	ListView listView;
	Lists[] listPlaces;
	List<Place> listOfPlaces;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places);
		
		setTitle("List View of Results");
		
		// Getting and setting the listview
		listView = (ListView) findViewById(R.id.listView1);
	    
		final ObjectsHolder objectData =  (ObjectsHolder) getIntent().getSerializableExtra("serialized");
		listOfPlaces =  objectData.near_places;

		// Get the length of the ArrayList to be able to cast into an object of Lists() class
		int length = listOfPlaces.size();
		
		// listPlaces is a list of custom objects which stores the data about the places
		listPlaces = new Lists[length];
		
		// We would need a list of strings (place names) for displaying as a list
		String[] restNamesList = new String[length];
		
		
		
		 for(int i=0; i < length; i++)
		 {
			 restNamesList[i] = listOfPlaces.get(i).name;
		 }
		
	    // Define an arrayadapter to display a list with the place names
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, restNamesList);
		//Set the adapter to the current listView
		listView.setAdapter(adapter);
	    
		
		// On click event method when the user clicks on a list item, for more details
	    listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// get the index of the item clicked
				int itemPosition = position;
				
				//Get the object value from the list...For now it is string the google places api would be objects
				
				Place clickedPlace = (Place) listOfPlaces.get(itemPosition);
				
				if(clickedPlace.coupons.size() > 0){
					//Load a new intent...start a new activity which diplays the details of each restaurant
					Intent dispEachItem = new Intent(getBaseContext(), ItemViewFromList.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("item", (Serializable) clickedPlace);
					
					dispEachItem.putExtras(bundle);
					startActivity(dispEachItem);
				}
				else{
					Toast.makeText(getBaseContext(), "No coupons for this place", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
	}

	
    
}
