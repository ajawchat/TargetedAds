package com.example.targetedads;

import java.util.Comparator;


public class DistanceCompare implements Comparator<Place> {
	
	@Override
	public int compare(Place p1,Place p2) {
		// TODO Auto-generated method stub
		 return Double.compare(p1.distance, p2.distance);
	}

}
