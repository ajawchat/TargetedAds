package com.example.targetedads;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.targetedads.CouponList;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
 
@SuppressWarnings("deprecation")
public class GooglePlaces {
 
    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
 
    // Google API Key
    private static final String API_KEY = "AIzaSyDWwC-26e9Owa5cMeC-xFzU22IF2zr_YDk";
    
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
 
    // Google Places search url's
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    private static final String COUPONS_URL = "http://cgi.soic.indiana.edu/~ajawchat/";
    
    double _latitude = 39.1622;
    double _longitude= -86.5292;
    double _radius = 500;
    String types = "restaurant";
    
  
    public  Bitmap get_image()
    {
    	//String url = COUPONS_URL+place_id+".json";
    	String url = COUPONS_URL+"sample1.png";
    	System.out.println("URL "+url);
    	

    	Bitmap bmImg;
    	HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
       
    	try {
			HttpRequest request = httpRequestFactory
			        .buildGetRequest(new GenericUrl(url));
			InputStream is =request.execute().getContent();
			return BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
    	
        return null;
    }
    /**
     * 
     * @param place_id
     * @return coupon list
     */
    public  CouponList get_coupons(String place_id)
    {
    	String url = COUPONS_URL+place_id+".json";
    	//String url = COUPONS_URL+"Test.json";
    	System.out.println("URL "+url);
    	CouponList cp = new CouponList();
    	HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
       
    	try {
			HttpRequest request = httpRequestFactory
			        .buildGetRequest(new GenericUrl(url));
			cp=request.execute().parseAs(CouponList.class);
			return cp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("No coupons for PlaceId "+place_id);
			}
    	
        return cp;
    }
    /**
     * Searching places
     * @param latitude - latitude of place
     * @params longitude - longitude of place
     * @param radius - radius of searchable area
     * @param types - type of place to search
     * @return 
     * @return list of places
     * */
    public List<Place> search(double latitude, double longitude, double radius, String types,String opennow,String keyword)
            throws Exception {

 
        try {
 
            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", latitude + "," + longitude);
            request.getUrl().put("radius", radius); // in meters
            //request.getUrl().put("rankby", "distance"); //
            request.getUrl().put("sensor", "false");
            //request.getUrl().put("keyword","India"); 
            if(opennow.equals("now"))
            	request.getUrl().put("opennow", "true");
            if(types != null)
                request.getUrl().put("types", types);
            if(keyword != null)
                request.getUrl().put("keyword",keyword); 
            
 
            //ALL_PLACES
            List<Place> all_places=new ArrayList<Place>();
            
            
            //PlacesList placeslist = get_all_places(request); 
            
            PlacesList placeslist = request.execute().parseAs(PlacesList.class);
            all_places.addAll(placeslist.results);
            String PAGE_TOKEN=placeslist.next_page_token;
            
            while(PAGE_TOKEN != null)
            {
            	Thread.sleep(1000);
                request.getUrl().put("pagetoken",PAGE_TOKEN);
                PlacesList templist = request.execute().parseAs(PlacesList.class);
                all_places.addAll(templist.results);
                PAGE_TOKEN=templist.next_page_token;
            }
            
        /*    
            for (Place p : all_places)
            {
            	System.out.println(p.name+","+p.rating);
            }
      */
            return all_places;
        } catch (HttpResponseException e) {
           System.out.println("Error:"+e.getMessage());
            
        }
		return null;
		
       
 
    }
 
 
    /**
     * Creating http request Factory
     * */
    		public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {
 			   
    			  return transport.createRequestFactory(new HttpRequestInitializer() {
    			   public void initialize(HttpRequest request) {
    			    JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
    			    
    			    request.setParser(parser);
    			   }
        });
    }
    		
    		public static boolean exists(String url_name){
    		    try {
    		      HttpURLConnection.setFollowRedirects(false);
    		      // note : you may also need
    		      //        HttpURLConnection.setInstanceFollowRedirects(false)
    		      HttpURLConnection con =
    		         (HttpURLConnection) new URL(url_name).openConnection();
    		      con.setRequestMethod("HEAD");
    		      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    		    }
    		    catch (Exception e) {
    		       e.printStackTrace();
    		       return false;
    		    }
    		  }
 
}