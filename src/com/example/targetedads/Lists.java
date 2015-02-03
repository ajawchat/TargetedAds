package com.example.targetedads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class Lists implements Serializable{
	
	// Create a global hashmap of the restaurants
	String name;
	String speciality;
	String[] coupons = new String[]{"coupon1", "coupon2"};
	
	public String getName(){
		return name;
	}
	
	public String getSpeciality(){
		return speciality;
	}
	
	public String[] getCoupons(){
		return coupons;
	}
	
	public Lists(String name, String speciality){
		this.name = name;
		this.speciality = speciality;
	}

	
	
	

}
