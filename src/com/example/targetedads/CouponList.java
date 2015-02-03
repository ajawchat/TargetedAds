package com.example.targetedads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

public class CouponList implements Serializable {

	@Key
    public List<Coupon> Coupons = new ArrayList<Coupon>();
    
    @Key
    public String Specality_dish;
    
    public static class Coupon implements Serializable
    {
        @Key
        public String CouponId;
         
        @Key
        public String dish;
        
        @Key
        public String OP;
        
        @Key
        public String RP;
    }
    
}
