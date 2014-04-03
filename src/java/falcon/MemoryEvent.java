/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author atifsaleem
 */
package falcon;

/**
 *
 * @author WA0003EN
 */

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderResultType;
import com.google.code.geocoder.model.LatLng;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class MemoryEvent {
    
public static enum peopleList {
	FAMILY, FRIENDS, COLLEAGUES, CLASSMATES, RELATIVES, NEIGHBOURS, ACQUAINTANCES;
 
}
public static enum moodList {
	HAPPY, SAD, EXCITED;
 
}
public static enum activityList {
	LUNCH, DINNER, TRAVEL,PICNIC,SHOPPING,NIGHTOUT,SPORTS,WORK,GATHERING,PARTY,WEDDING;
}
public static HashMap<Integer,String> symlinks;
static {
        symlinks.put(1, "images/photos/Memory1/");
        symlinks.put(2, "images/photos/Memory2/");
        symlinks.put(3, "images/photos/Memory3/");
        symlinks.put(4, "images/photos/Memory4/");
        symlinks.put(5, "images/photos/Memory5/");
        symlinks.put(6, "images/photos/Memory6/");
        symlinks.put(7, "images/photos/Memory7/");
        symlinks.put(8, "images/photos/Memory8/");
        symlinks.put(9, "images/photos/Memory9/");
        symlinks.put(10, "images/photos/Memory10/");
        symlinks.put(11, "images/photos/Memory11/");
        symlinks.put(12, "images/photos/Memory12/");
        symlinks.put(13, "images/photos/Memory13/");
        symlinks.put(14, "images/photos/Memory14/");
        symlinks.put(15, "images/photos/Memory15/");
        symlinks.put(16, "images/photos/Memory16/");
        symlinks.put(17, "images/photos/Memory17/");
        symlinks.put(18, "images/photos/Memory18/");
        symlinks.put(19, "images/photos/Memory19/");
        symlinks.put(20, "images/photos/Memory20/");
        symlinks.put(21, "images/photos/Memory21/");
    }
public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
     Set<T> keys = new HashSet<T>();     
     for (Entry<T, E> entry : map.entrySet()) {
         if (value.equals(entry.getValue())) {
             keys.add(entry.getKey());
         }
     }
     return keys;
}
   private double curLat;
   private double curLong;
   private Timestamp curTime;
   private String people;
   private String mood;
   private String activity;
   private int imagery;

    
    public MemoryEvent(double curLat, double curLong, Timestamp curTime, String people, String mood, String activity) {
        this.curLat = curLat;
        this.curLong = curLong;
        this.curTime = curTime;
        this.people = people;
        this.mood = mood;
        this.activity = activity;
    }

    /**
     * @return the curLat
     */
    public double getCurLat() {
        return curLat;
    }

    /**
     * @param curLat the curLat to set
     */
    public void setCurLat(double curLat) {
        this.curLat = curLat;
    }

    /**
     * @return the curLong
     */
    public double getCurLong() {
        return curLong;
    }

    /**
     * @param curLong the curLong to set
     */
    public void setCurLong(double curLong) {
        this.curLong = curLong;
    }

    /**
     * @return the curTime
     */
    public String getLocationString(){
    final Geocoder geocoder = new Geocoder();
    GeocodeResponse geocoderResponse;
    GeocoderRequest req = new GeocoderRequest();
    
    BigDecimal l1 = new BigDecimal(curLat, MathContext.DECIMAL64);
    BigDecimal l2 = new BigDecimal(curLong, MathContext.DECIMAL64);
    LatLng rev = new LatLng(l1,l2);
    req.setLocation(rev);
    geocoderResponse = geocoder.geocode(req);
    final GeocoderResult geocoderResult = geocoderResponse.getResults().iterator().next();
    String address = geocoderResult.getFormattedAddress();
    return address;
    }
    public Timestamp getCurTime() {
        return curTime;
    }

    /**
     * @param curTime the curTime to set
     */
    public void setCurTime(Timestamp curTime) {
        this.curTime = curTime;
    }
    public String getFormattedTime(){
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");  
    Date date; 
    date = new Date(curTime.getTime());
    String formattedDate= df.format(date);
    return formattedDate;
    }
    /**
     * @return the people
     */
    public String getPeople() {
        return people;
    }

    /**
     * @param people the people to set
     */
    public void setPeople(String people) {
        this.people = people;
    }

    /**
     * @return the emotional_state
     */
    public String getMood() {
        return mood;
    }

    /**
     * @param emotional_state the emotional_state to set
     */
    public void setMood(String mood) {
        this.mood = mood;
    }

    /**
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public void setImagery(int story) {
        this.imagery = imagery;
    }

    public int getImagery() {
        return imagery;
    }

}
