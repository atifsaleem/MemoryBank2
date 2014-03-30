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

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class MemoryEvent {
    
   
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

}
