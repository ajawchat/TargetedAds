package com.example.targetedads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.targetedads.CouponList.Coupon;
import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class Place implements Serializable {
 
    @Key
    public String id;
     
    @Key
    public String name;
     
    @Key
    public String reference;
     
    @Key
    public String icon;
     
    @Key
    public String vicinity;
     
    @Key
    public Geometry geometry;
     
    @Key
    public String formatted_address;
     
    @Key
    public String formatted_phone_number;
    
    @Key
	public double rating;
    
    
    //Dish
    public String dish;
    
    //Distance
    public float distance;
    
    //Coupons
    public List<Coupon> coupons = new ArrayList<Coupon>(); 
 
    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }
     
    public static class Geometry implements Serializable
    {
        @Key
        public Location location;
    }
     
    public static class Location implements Serializable
    {
        @Key
        public double lat;
         
        @Key
        public double lng;
    }
    
   
}