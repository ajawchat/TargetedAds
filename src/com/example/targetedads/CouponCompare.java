package com.example.targetedads;

import android.annotation.TargetApi;
import android.os.Build;
import java.util.Comparator;

public class CouponCompare implements Comparator<Place> {

		@TargetApi(Build.VERSION_CODES.KITKAT)
		@Override
		public int compare(Place p1, Place p2) {
			// TODO Auto-generated method stub
			return Integer.compare(p1.coupons.size(), p2.coupons.size());
		}
	
}
