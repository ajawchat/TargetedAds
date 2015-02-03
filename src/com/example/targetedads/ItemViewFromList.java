//***************************************************
// Authors: Ajinkya Awchat, Prasanth Velamala
// Android Program to provide targeted coupons to users by taking inputs & location
// Date : 12-Dec-2014
//***************************************************

package com.example.targetedads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ItemViewFromList extends Activity {
	
	//Global variables to keep size of text views consistent
	int titleSize = 30;
	int labelSize = 15;
	
	GooglePlaces gp1 = new GooglePlaces();
	// For alignment
	static int TEXT_ALIGNMENT_GRAVITY = 1;
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_place_details);
		
		setTitle("Additional Details!!");
		
		Place place = (Place) getIntent().getSerializableExtra("item");
		
		// Set the existing data
		TextView placeTitle = (TextView)findViewById(R.id.txtPlaceTitle);
		System.out.println("Place.name = ==="+place.name);
		placeTitle.setText(place.name);
		placeTitle.setTypeface(null, Typeface.BOLD);
		
		TextView placeRatings = (TextView)findViewById(R.id.txtRatings);
		placeRatings.setText("Ratings : "+place.rating);
		placeRatings.setTypeface(null, Typeface.BOLD);
		
		TextView dish = (TextView)findViewById(R.id.txtDish);
		dish.setText("Speciality : "+place.dish);
		dish.setTypeface(null, Typeface.BOLD);
		
		TextView placeDist = (TextView)findViewById(R.id.txtDistance);
		placeDist.setText("Distance : "+(Math.floor((place.distance*0.000621371f) * 100 + .5) / 100)+" miles");
		placeDist.setTypeface(null, Typeface.BOLD);
		
		
		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearCoupons);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		//Dynamically add coupons to the LinearLayout 
		for(int i = 0; i < place.coupons.size(); i++){
			
			// Component layout
			LinearLayout newCompLayout = new LinearLayout(getBaseContext());
			newCompLayout.setOrientation(LinearLayout.VERTICAL);
			{
				// Text View layout
				LinearLayout newDishLayout = new LinearLayout(getBaseContext());
				newDishLayout.setOrientation(LinearLayout.HORIZONTAL);
				
				TextView dishName = new TextView(this);
				dishName.setText(" "+place.coupons.get(i).dish+"     ");
				dishName.setTextSize(25);
				newDishLayout.addView(dishName);
				
				// This will display the original price. It will be STRUCK to display the reduced price
				TextView origPrice = new TextView(this);
				origPrice.setText(" "+place.coupons.get(i).OP+" ");
				origPrice.setTextSize(25);
				origPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				newDishLayout.addView(origPrice);
				
				TextView redPrice = new TextView(this);
				redPrice.setText("     "+place.coupons.get(i).RP+" ");
				redPrice.setTextSize(25);
				newDishLayout.addView(redPrice);
				
				newCompLayout.addView(newDishLayout,0);
			}
			
			TextView code = new TextView(this);
			code.setText("Coupon : "+place.coupons.get(i).CouponId);
			code.setTextSize(20);
			newCompLayout.addView(code, 1);
			
			//Add the image view for the QR code
			
			ImageView qr = new ImageView(this);
			qr.setImageBitmap(gp1.get_image());
			//qr.setImageResource(R.drawable.sample1);
			//qr.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.sample, 100, 100));
			// Attaching to parent Linear Layout first, as getLayoutParams() return null otherwise
			newCompLayout.addView(qr, 2);
			qr.getLayoutParams().height = 180;
			qr.getLayoutParams().width = 180;
			
			ll.addView(newCompLayout,i);
		}
		
		/*
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		System.out.println("Inside Indv: max Heap Memory: " + Long.toString(maxMemory));
		System.out.println("Inside Indv: total Memory: " + Long.toString(rt.totalMemory()));
		*/
		
	
	} // end of onCreate()
	
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
		    int reqWidth, int reqHeight) {
		 
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		 
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		 
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
		}
		 
		 
		 public static int calculateInSampleSize(
		        BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		 
		if (height > reqHeight || width > reqWidth) {
		 
		    // Calculate ratios of height and width to requested height and width
		    final int heightRatio = Math.round((float) height / (float) reqHeight);
		    final int widthRatio = Math.round((float) width / (float) reqWidth);
		 
		    // Choose the smallest ratio as inSampleSize value, this will guarantee
		    // a final image with both dimensions larger than or equal to the
		    // requested height and width.
		    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		 
		return inSampleSize;
		}	

}
