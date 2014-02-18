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

import falcon.EpisodicModel.GeocodeResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class MemoryEvent {
    
   public static final String allEmotions [] = new String [] {"really good","good","not good or bad","bad","really bad","really happy","happy","not happy or sad","sad","really sad"};
   public static final String allVerbs[] = new String [] {"walking","eating","chilling","watching a movie","listening to music","travelling","running","playing","swimming","talking"};
   public static final String allPeople[] = new String[] {"Pierre","Enric","Federico","Emilie","Danielle,","Kota","Tetsu","Max","Michael","Declan","Simon","Tim","Shaun"};
   
   private double curLat;
   private double curLong;
   private Timestamp curTime;
   private String[] people;
   private int emotional_state;
   private int activity;
   private String story;

    
    public MemoryEvent(double curLat, double curLong, Timestamp curTime, String[] people, int emotional_state, int activity) {
        this.curLat = curLat;
        this.curLong = curLong;
        this.curTime = curTime;
        this.people = people;
        this.emotional_state = emotional_state;
        this.activity = activity;
    }
    /**
     * @return the curLat
     */
    public MemoryEvent(double[][] ifield,int index) throws IOException  {
                   //System.out.println(Arrays.deepToString(ifield));
                    long timestamp=0;
                    story="I was with ";
                    for(int k=0;k<ifield.length;k++){
                        for(int i=0;i<ifield[k].length;i++){
                            
                            if(k==1)
                            {
                            if (i<3)
                            {
                            int num = (int) (ifield[k][i]*100);
                            if (num>0)
                            story = story + allPeople[num-1];
                            if (i==ifield[k].length-1)
                            ;
                            else story+=",";
                            }
                            else if (i==3)
                            {
                            double temp =  ifield[k][i]*1000000000;
                            timestamp = (long) temp;
                            }
                            else if (i==4)
                            {
                                double temp =  ifield[k][i]*10;
                                emotional_state = (int) temp;
                            
                            }
                            else 
                            {
                            double temp = ifield[k][i]*10;
                            activity = (int) temp;
                            }
                            }
                            if (k==0)
                            {
                            if (i==0)
                                curLat=1000*ifield[k][i];
                            else
                                curLong = 1000*ifield[k][i];   
                            }
                            
                        }
                    }
                     Date date  = new Date(timestamp);
                     long time = date.getTime();
                     curTime = new Timestamp(time);
                     //System.out.println(timestamp+","+emotional_state);
                    story=story+" "+allVerbs[activity]+" at "+getAddressFromLatLng(curLat,curLong)+" at "+". It was "+allEmotions[emotional_state]+"\n";
                    story = formatStory(story,index);
                  
    }
    protected static String getAddressFromLatLng(double lat,double lng) throws IOException
    {   
        GeocodeResponse location = new GeocodeResponse();
        try{
            /*
          HttpGet geocode = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true");
          HttpResponse response = httpclient.execute(geocode);
          HttpEntity entity2 = response.getEntity();
          StringBuilder temp = inputStreamToString(entity2.getContent());
	// convert String into InputStream
 
	// read it with BufferedReader
          location = gson.fromJson(temp.toString(), GeocodeResponse.class);
         */
        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
        finally {
          
        }
    
        //return location.getResults().get(0).getFormatted_address();
        return "Paris, France";
    }
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
    public Timestamp getCurTime() {
        return curTime;
    }

    /**
     * @param curTime the curTime to set
     */
    public void setCurTime(Timestamp curTime) {
        this.curTime = curTime;
    }

    /**
     * @return the people
     */
    public String[] getPeople() {
        return people;
    }

    /**
     * @param people the people to set
     */
    public void setPeople(String[] people) {
        this.people = people;
    }

    /**
     * @return the emotional_state
     */
    public double getEmotional_state() {
        return emotional_state;
    }

    /**
     * @param emotional_state the emotional_state to set
     */
    public void setEmotional_state(int emotional_state) {
        this.emotional_state = emotional_state;
    }

    /**
     * @return the activity
     */
    public double getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(int activity) {
        this.activity = activity;
    }
    public void setStory(String story) {
        this.story = story;
    }

    public String getStory() {
        return story;
    }

    private String formatStory(String story,int index) {
        String temp=story;
       
        if (index==0)
        temp = "Fri, 10 May 2013 20:26:23 GMT - I was with Pierre, Enric and Federico - Dubai, United Arab Emirates - It was really good.";
        else if (index==5)
        temp = "Wed, 13 Nov 2013 19:28:46 GMT - I was with Federico, Emilie and Danielle - Singapore, Singapore - It was bad.";
        else if (index==9)
        temp = "Wed, 15 Nov 2013 23:28:46 GMT - I was with Danielle, Kota and Tetsu in - Paris , France - It was not good or bad.";
        else temp="";
        return temp;
    }
}
