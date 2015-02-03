/* This class is used to store the user's current location and place list, so that it can be transferred to the 
 * MapFragment for display of maps. This is mainly done because the Object class is not serializable
 */

package com.example.targetedads;

import java.io.Serializable;
import java.util.List;

public class ObjectsHolder implements Serializable{
	
	// currLocation[0] == Latitude
	// currLocation[1] == Longitude
	double[] currLocation = new double[2];
	
	// Holds the places returned by the Google Places API
	List<Place> near_places;
}
