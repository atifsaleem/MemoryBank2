/*
 * Please set a correct file i/o directry in EpisodicTest class as the event read
 * directry for noise generation
 * Please change value of write_dir, errorRate and errorRate before running it
 *  
 */

package falcon;

import java.util.Vector;
import java.io.*;
import java.text.NumberFormat;

/**
 *
 * @author WA0003EN
 */
public class NoiseGenerator {
    protected String writeFilePath="Player_3_noise_event1_new_10_percent.ext";
    protected String write_dir="/Users/atifsaleem/NetBeansProjects/Falcon_14_05_10_atif/data";
    
    protected Vector<Event> origEvents=new Vector<Event>(0);
    protected Vector<Event> noiseEvents=new Vector<Event>(0);
    
    protected Vector<Vector<Event>> origSequences=new  Vector<Vector<Event>>(0);
    protected Vector<Vector<Event>> noiseSequences=new  Vector<Vector<Event>>(0);
    
    protected EpisodicTest learners=new EpisodicTest();
    
    //protected int maxSeqLen;
    
    protected double errorRate=0.1;
    protected int numEventAttributes=16;
    
    public NoiseGenerator(){
         learners.setVisible(false);
         
         learners.EventListLoad();
         origEvents=learners.events;
         
         //maxSeqLen=(int)(1/learners.episodic.seqLearner.v);
         getAllSequences();
    }
    
    protected void finalize() throws Throwable {
        try {
            learners.dispose();
        } finally {
            super.finalize();
        }
    }
    
    protected void getAllSequences(){
        int curSeqNum=0;
        
        origSequences.add(new Vector<Event>(0));
        
        for(int i=0;i<origEvents.size();i++){
            
            Event e=new Event();
            this.copyEvent(origEvents.get(i), e);
            
            if(e.isForcedTer(e)){
                origSequences.get(curSeqNum).add(e);
                if(i!=origEvents.size()-1){
                   origSequences.add(new Vector<Event>(0));
                   curSeqNum++;
                }
            }
            else{
                 origSequences.get(curSeqNum).add(e);
            }
        }
    }
    
    protected void getAllNSequences(){
        int curSeqNum=0;
        
        noiseSequences.add(new Vector<Event>(0));
        
        for(int i=0;i<noiseEvents.size();i++){
            
            Event e=new Event();
            this.copyEvent(noiseEvents.get(i), e);
            
            if(e.isForcedTer(e)){
                noiseSequences.get(curSeqNum).add(e);
                if(i!=noiseEvents.size()-1){
                   noiseSequences.add(new Vector<Event>(0));
                   curSeqNum++;
                }
            }
            else{
                 noiseSequences.get(curSeqNum).add(e);
            }
        }
    }
    
   /* protected void noiziseEvents(){
        int noiseAttr=(int)(errorRate*numEventAttributes);
        
        for(int i=0;i<origEvents.size();i++){
            int[] attrArray=getWrongAttr(noiseAttr);
            Event ne=new Event();
            Event oe=origEvents.get(i);
            copyEvent(oe,ne);
            
            for(int j=0;j<attrArray.length;j++){
                toggle(attrArray[j],ne);
            }
            
            noiseEvents.add(ne);
        }
    }*/
    
    protected void noiziseEvents(){
        int totalLen=origEvents.size();
        int noiseNum=(int)(errorRate*totalLen);
        int[] noiseArray=getWrongEvent(noiseNum,totalLen);
        
        System.out.println("selected index for noizinize: ");
        for(int j=0;j<noiseArray.length;j++){
                System.out.println(""+noiseArray[j]);
        }
        
        for(int i=0;i<origEvents.size();i++){
            Event ne=new Event();
            Event oe=origEvents.get(i);
            copyEvent(oe,ne);
            
            for(int m=0;m<noiseArray.length;m++){
                if(i==noiseArray[m]){
                    this.toggleEvent(ne);
                    System.out.println("NOISE @ "+i);
                    break;
                }
            }
            
            noiseEvents.add(ne);
        }
        
        this.getAllNSequences();
    }
    
    //to toggle each attribute in all available events with the probability 
    //eqeals to the error rate
    protected void noiziseEvents2(){

        
        for(int i=0;i<origEvents.size();i++){
            Event ne=new Event();
            Event oe=origEvents.get(i);
            copyEvent(oe,ne);
            
            for(int m=0;m<this.numEventAttributes-1;m++){
                double prob=Math.random();
                
                if(prob<errorRate){
                    this.toggle(m,ne);
                    System.out.println("NOISE @ "+i+":"+m);
                    break;
                }
            }
            
            noiseEvents.add(ne);
        }
        
        this.getAllNSequences();
    }
    
    private void copyEvent(Event o, Event n){
        n.x=o.x;
        n.y=o.y;
        n.z=o.z;
        
        n.curHealth=o.curHealth;
        n.hasAmmo=o.hasAmmo;
        n.emeDistance=o.emeDistance;
        n.reachableItem=o.reachableItem;
        n.getHealth=o.getHealth;
        n.getWeapon=o.getWeapon;
        n.changeWeapon=o.changeWeapon;
        n.isShooting=o.isShooting;
        
        n.behave1=o.behave1;
        n.behave2=o.behave2;
        n.behave3=o.behave3;
        n.behave4=o.behave4;
        
        n.reward=o.reward;      
    }
    
    protected int[] getWrongAttr(int num){
        int[] arr=new int[num];
        
        for(int i=0;i<num;i++){
            arr[i]=-1;
        }
        
        for(int i=0;i<num;i++){
            do{
                arr[i]=(int)(Math.random()*this.numEventAttributes);
                if(arr[i]==numEventAttributes){
                    arr[i]--;
                }
                
                boolean repeated=false;
                for(int j=0;j<i;j++){
                    if(arr[i]==arr[j]){
                        repeated=true;
                        break;
                    }
                }
                
                if(!repeated)
                    break;
            }while(true);
        }
        
        return arr;
    }
    
    protected int[] getWrongEvent(int num, int totalLen){
        int[] arr=new int[num];
        
        for(int i=0;i<num;i++){
            arr[i]=-1;
        }
        
        for(int i=0;i<num;i++){
            do{
                arr[i]=(int)(Math.random()*totalLen);
                if(arr[i]==totalLen){
                    arr[i]--;
                }
                
                boolean repeated=false;
                for(int j=0;j<i;j++){
                    if(arr[i]==arr[j]){
                        repeated=true;
                        break;
                    }
                }
                
                if(!repeated)
                    break;
            }while(true);
        }
        
        return arr;
    }
    
    private void toggle(int attrIndex, Event e){
        switch(attrIndex){
            case 0:
                e.x=toggleDouble(e.x);
                break;
            case 1:
                e.z=toggleDouble(e.z);
                break;
            case 2:
                e.y=toggleDouble(e.y);
                break;
            case 3:
                e.curHealth=toggleDouble(e.curHealth);
                break;
            case 4:
                e.hasAmmo=toggleBool(e.hasAmmo);
                break;
            case 5:
                e.emeDistance=toggleDouble(e.emeDistance);
                break;
            case 6:
                e.reachableItem=toggleBool(e.reachableItem);
                break;
            case 7:
                e.getHealth=toggleBool(e.getHealth);
                break;
            case 8:
                e.getWeapon=toggleBool(e.getWeapon);
                break;
            case 9:
                e.changeWeapon=toggleBool(e.changeWeapon);
                break;
            case 10:
                e.isShooting=toggleBool(e.isShooting);
                break;
            case 11:
                e.behave1=toggleBool(e.behave1);
                break;
            case 12:
                e.behave2=toggleBool(e.behave2);
                break;
            case 13:
                e.behave3=toggleBool(e.behave3);
                break;
            case 14:
                e.behave4=toggleBool(e.behave4);
                break;
            case 15:
                e.reward=toggleDouble(e.reward);
                break;
            
        }
    }
    
    private void toggleEvent(Event e){
                e.x=toggleDouble(e.x);
                e.z=toggleDouble(e.z);
                e.y=toggleDouble(e.y);
                e.curHealth=toggleDouble(e.curHealth);
                e.hasAmmo=toggleBool(e.hasAmmo);
                e.emeDistance=toggleDouble(e.emeDistance);
                e.reachableItem=toggleBool(e.reachableItem);
                e.getHealth=toggleBool(e.getHealth);
                e.getWeapon=toggleBool(e.getWeapon);
                e.changeWeapon=toggleBool(e.changeWeapon);
                e.isShooting=toggleBool(e.isShooting);
                e.behave1=toggleBool(e.behave1);
                e.behave2=toggleBool(e.behave2);
                e.behave3=toggleBool(e.behave3);
                e.behave4=toggleBool(e.behave4);
                e.reward=toggleDouble(e.reward);
    }
    
    private boolean toggleBool(Boolean v){
        return (!v);
    }
    
    private double toggleDouble(double v){
        return 1-(v);
    }
    
    protected void noiziseSeq(){
        for(int i=0,k=0;i<origSequences.size();i++,k++){
            noiseSequences.add(new Vector<Event> (0));
            for(int j=0;j<origSequences.get(i).size();j++){
                Event e=new Event();
                this.copyEvent(origSequences.get(i).get(j), e);
                noiseSequences.get(k).add(e);
            }
            System.out.println("SEQ"+i);
            if(!noiziseOneSeq2(origSequences.get(i),noiseSequences.get(k))){
                //noiseSequences.remove(k);
                //k--;
            }    
        }
        
        this.seq2Events();
    }
    
    protected void noiziseOneSeq(Vector<Event> os, Vector<Event> ns){
        int noiseLen=(int)(errorRate*os.size());
        
        if(os.size()-noiseLen<0){
            System.out.println("one sequence: os.size()-noiseLen<0");
            return;
        }
        
        if(noiseLen<=1){
            System.out.println("noiseLen<=1");
            return;
        }
        
        int sIndex=(int)(Math.random()*(os.size()-noiseLen));
        if(sIndex==(os.size()-noiseLen)){
            sIndex--;
        }
        int eIndex=sIndex+noiseLen-1;
        
        System.out.println("one sequence noise: "+sIndex+"~"+eIndex);
        
        Vector<Event> temp=new Vector<Event>();
        System.out.println("temp len: "+temp.size());
        System.out.println("ns len: "+ns.size());
        System.out.println("os len: "+os.size());
        
        for(int j=0;j<os.size();j++){
                System.out.print(os.get(j).toString());
                System.out.print(ns.get(j).toString());
                System.out.println("\n");
            }
        
        for(int i=sIndex;i<=eIndex;i++){
            temp.add(ns.remove(sIndex));
        }
        
        for(int i=sIndex;i<=eIndex;i++){
            ns.add(i,temp.remove(temp.size()-1));
        }
        System.out.println("temp len: "+temp.size());
        System.out.println("ns len: "+ns.size());
        System.out.println("os len: "+os.size());
        
            for(int j=0;j<os.size();j++){
                System.out.print(os.get(j).toString());
                System.out.print(ns.get(j).toString());
                System.out.println("\n");
            }
    }
    
    protected boolean noiziseOneSeq2(Vector<Event> os, Vector<Event> ns){
        int noiseLen=(int)(errorRate*os.size());
        
        if(os.size()-noiseLen<0){
            System.out.println("one sequence: os.size()-noiseLen<0");
            return false;
        }
        
        if(noiseLen<=1){
            System.out.println("noiseLen<=1");
            return false;
        }
        
        int sIndex=(int)(Math.random()*(os.size()-noiseLen));
        if(sIndex==(os.size()-noiseLen)){
            sIndex--;
        }
        int eIndex=sIndex+noiseLen-1;
        
        System.out.println("one sequence noise: "+sIndex+"~"+eIndex);
        
        //Vector<Event> temp=new Vector<Event>();
        //System.out.println("temp len: "+temp.size());
        System.out.println("ns len: "+ns.size());
        System.out.println("os len: "+os.size());
        
        for(int j=0;j<os.size();j++){
                System.out.print(os.get(j).toString());
                System.out.print(ns.get(j).toString());
                System.out.println("\n");
            }
        
        for(int i=sIndex;i<=eIndex;i++){
            ns.remove(sIndex);
        }
        
        int[] noiseArray=getWrongEvent(noiseLen,origEvents.size()-1);
        
        System.out.println("selected index for noizinize: ");
        for(int j=0;j<noiseArray.length;j++){
                System.out.println(""+noiseArray[j]);
        }
        
        for(int i=0, j=sIndex;i<noiseArray.length;i++,j++){
            Event e=new Event();
            this.copyEvent(origEvents.get(noiseArray[i]), e);
            ns.add(j,e);
        }
        //System.out.println("temp len: "+temp.size());
        System.out.println("ns len: "+ns.size());
        System.out.println("os len: "+os.size());
        
            for(int j=0;j<os.size();j++){
                System.out.print(os.get(j).toString());
                System.out.print(ns.get(j).toString());
                System.out.println("\n");
            }
        
                return true;
    }
    
    protected void seq2Events(){
        for(int i=0;i<noiseSequences.size();i++){
            for(int j=0;j<noiseSequences.get(i).size();j++){
                Event e=new Event ();
                this.copyEvent(noiseSequences.get(i).get(j), e);
                noiseEvents.add(e);
            }
        }
        

    }
    
    public static void main(String[] agrs){
        NoiseGenerator ng=new NoiseGenerator();
        
        
        //ng.noiziseSeq();
        ng.noiziseEvents2();
        
        //System.out.println("noise seq:"+ng.noiseSequences.size());
        System.out.println("noise events:"+ng.noiseEvents.size());
        
        System.out.println("Events:");
        for(int i=0;i<ng.origEvents.size();i++){
            System.out.println(ng.origEvents.get(i).toString());
            System.out.println(ng.noiseEvents.get(i).toString());
            System.out.println("");
        }
        
        /*
        System.out.println("Sequences:");
        for(int i=0;i<ng.origSequences.size();i++){
            System.out.println("S"+i+" ("+ng.origSequences.get(i).size()+"):");
            for(int j=0;j<ng.origSequences.get(i).size();j++){
                System.out.print(ng.origSequences.get(i).get(j).toString());
                System.out.print(ng.noiseSequences.get(i).get(j).toString());
                System.out.println("\n");
            }
        }*/
        
        ng.saveEvent();
        
        System.exit(0);
    }
    
    protected void saveEvent() {
            try {
                //mainprojectdir = System.getProperty("user.dir")+"\\";
                FileWriter fw = new FileWriter(this.write_dir + this.writeFilePath);
                saveEvents(fw);
            }
            catch (Exception e){
    		e.printStackTrace();
            }
        }
    
    public void saveEvents (FileWriter fw) {
    	try{
                String str;
    		for (int j=0; j<noiseEvents.size(); j++) {
    			str="";
                        double[][] eArray=noiseEvents.get(j).toArray();
    			for (int k=0; k<learners.numFieldE; k++) {
    				for (int i=0; i<learners.lenE[k]; i++) {
    					str=str+NumberFormat.getInstance().format(eArray[k][i])+" ";
    				}
    			}
    			fw.write(str+"\n");
    		}
    		fw.close();
    	}
    	catch (Exception e){
            e.printStackTrace();
    		System.out.println("Cannot save @event!!!");
    	}
    }
}
