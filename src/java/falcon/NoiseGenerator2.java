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
public class NoiseGenerator2 {
    protected String writeFilePath="Player_3_noise_event2_new_2_percent.ext";
    protected String write_dir="/Users/atifsaleem/NetBeansProjects/Falcon_14_05_10_atif/data";
    
    protected Vector<Event> origEvents=new Vector<Event>(0);
    protected Vector<Event> noiseEvents=new Vector<Event>(0);
    
    protected Vector<Vector<Event>> origSequences=new  Vector<Vector<Event>>(0);
    protected Vector<Vector<Event>> noiseSequences=new  Vector<Vector<Event>>(0);
    
    protected PlayBackPanel2 learners=new PlayBackPanel2();
    
    protected int maxSeqLen;
    
    protected double errorRate=0.02;
    protected int numEventAttributes=38;
    
    public NoiseGenerator2(){
         learners.setVisible(false);
         
         learners.isStart=true;
         learners.EventListLoad();
         origEvents=learners.events;
         
         //maxSeqLen=(int)(1/learners.episodic.seqLearner.v);
         maxSeqLen=600;
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
            Event e=origEvents.get(i);
            
            if((e.isForcedTer(e))||origSequences.get(curSeqNum).size()==maxSeqLen-1){
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
    
    protected void noiziseEvents(){
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
        n.curWeapon1=o.curWeapon1;
        n.curWeapon2=o.curWeapon2;
        n.curWeapon3=o.curWeapon3;
        n.curWeapon4=o.curWeapon4;
        n.curWeapon5=o.curWeapon5;
        n.curWeapon6=o.curWeapon6;
        n.curWeapon7=o.curWeapon7;
        n.curWeapon8=o.curWeapon8;
        n.curWeapon9=o.curWeapon9;
        n.curWeapon10=o.curWeapon10;
        n.pickupHealL=o.pickupHealL;
        n.pickupHealS=o.pickupHealS;
        n.pickupWeapon1=o.pickupWeapon1;
        n.pickupWeapon2=o.pickupWeapon2;
        n.pickupWeapon3=o.pickupWeapon3;
        n.pickupWeapon4=o.pickupWeapon4;
        n.pickupWeapon5=o.pickupWeapon5;
        n.pickupWeapon6=o.pickupWeapon6;
        n.pickupWeapon7=o.pickupWeapon7;
        n.pickupWeapon8=o.pickupWeapon8;
        n.pickupWeapon9=o.pickupWeapon9;
        n.pickupWeapon10=o.pickupWeapon10;
        
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
                e.behave1=toggleBool(e.behave1);
                break;
            case 8:
                e.getHealth=toggleBool(e.getHealth);
                break;
            case 9:
                e.getWeapon=toggleBool(e.getWeapon);
                break;
            case 10:
                e.isShooting=toggleBool(e.isShooting);
                break;
            case 11:
                e.changeWeapon=toggleBool(e.changeWeapon);
                break;
            case 12:
                e.curWeapon1=toggleBool(e.curWeapon1);
                break;
            case 13:
                e.curWeapon2=toggleBool(e.curWeapon2);
                break;
            case 14:
                e.curWeapon3=toggleBool(e.curWeapon3);
                break;
            case 15:
                e.curWeapon4=toggleBool(e.curWeapon4);
                break;
            case 16:
                e.curWeapon5=toggleBool(e.curWeapon5);
                break;
            case 17:
                e.curWeapon6=toggleBool(e.curWeapon6);
                break;
            case 18:
                e.curWeapon7=toggleBool(e.curWeapon7);
                break;
            case 19:
                e.curWeapon8=toggleBool(e.curWeapon8);
                break;
            case 20:
                e.curWeapon9=toggleBool(e.curWeapon9);
                break;
            case 21:
                e.curWeapon10=toggleBool(e.curWeapon10);
                break;
            case 22:
                e.pickupHealL=toggleBool(e.pickupHealL);
                break;
            case 23:
                e.pickupHealS=toggleBool(e.pickupHealS);
                break;
            case 24:
                e.pickupWeapon1=toggleBool(e.pickupWeapon1);
                break;
            case 25:
                e.pickupWeapon2=toggleBool(e.pickupWeapon2);
                break;
            case 26:
                e.pickupWeapon3=toggleBool(e.pickupWeapon3);
                break;
            case 27:
                e.pickupWeapon4=toggleBool(e.pickupWeapon4);
                break;
            case 28:
                e.pickupWeapon5=toggleBool(e.pickupWeapon5);
                break;
            case 29:
                e.pickupWeapon6=toggleBool(e.pickupWeapon6);
                break;
            case 30:
                e.pickupWeapon7=toggleBool(e.pickupWeapon7);
                break;
            case 31:
                e.pickupWeapon8=toggleBool(e.pickupWeapon8);
                break;
            case 32:
                e.pickupWeapon9=toggleBool(e.pickupWeapon9);
                break;
            case 33:
                e.pickupWeapon10=toggleBool(e.pickupWeapon10);
                break;
            case 34:
                e.behave1=toggleBool(e.behave1);
                break;
            case 35:
                e.behave2=toggleBool(e.behave2);
                break;
            case 36:
                e.behave3=toggleBool(e.behave3);
                break;
            case 37:
                e.behave4=toggleBool(e.behave4);
                break;
            case 38:
                e.reward=toggleDouble(e.reward);
                break;
            
        }
    }
    
    private boolean toggleBool(Boolean v){
        return (!v);
    }
    
    private double toggleDouble(double v){
        return 1-(v);
    }
    
    protected void noiziseSeq(){
        for(int i=0;i<origSequences.size();i++){
            noiseSequences.add(new Vector<Event> (0));
            for(int j=0;j<origSequences.get(i).size();j++){
                noiseSequences.get(i).add(origSequences.get(i).get(j));
            }
            
            noiziseOneSeq(origSequences.get(i),noiseSequences.get(i));
        }
        
        this.seq2Events();
    }
    
    protected void noiziseOneSeq(Vector<Event> os, Vector<Event> ns){
        int noiseLen=(int)(errorRate*os.size());
        
        if(os.size()-noiseLen<0){
            return;
        }
        
        if(noiseLen==0){
            return;
        }
        
        int sIndex=(int)(Math.random()*(os.size()-noiseLen));
        if(sIndex==(os.size()-noiseLen)){
            sIndex--;
        }
        int eIndex=sIndex+noiseLen-1;
        int exSeq=0;
                
        int trials=0;
        do{
            exSeq=(int)(Math.random()*origSequences.size());
            if(exSeq==origSequences.size()){
                exSeq--;
            }
            if(origSequences.get(exSeq).size()>eIndex){
                break;
            }
            trials++;
            
            if(trials>1000){
                break;
            }
        }while(true);
        
        if(trials>1000){
            sIndex=0;
            eIndex=sIndex+noiseLen-1;
            
            do{
                exSeq=(int)(Math.random()*origSequences.size());
                if(exSeq==origSequences.size()){
                    exSeq--;
                }
                if(origSequences.get(exSeq).size()>eIndex){
                    break;
                }
                trials++;
            
                if(trials>1000){
                    break;
                }
            }while(true);
        }
        
        int i=sIndex;
        int j=0;
        for(;i<=eIndex;i++,j++){
            ns.set(i, origSequences.get(exSeq).get(j));
        }
    }
    
    protected void seq2Events(){
        for(int i=0;i<noiseSequences.size();i++){
            for(int j=0;j<noiseSequences.get(i).size();j++){
                noiseEvents.add(noiseSequences.get(i).get(j));
            }
        }
    }
    
    public static void main(String[] agrs){
        NoiseGenerator2 ng=new NoiseGenerator2();
        //ng.noiziseEvents();
        ng.noiziseSeq();
        
        System.out.println("# of Events = "+ng.origEvents.size());
        System.out.println("# of noised Events = "+ng.noiseEvents.size());
        System.out.println("# of Sequences = "+ng.origSequences.size());
        System.out.println("# of noised Sequences = "+ng.noiseSequences.size());
        
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
    		for (int j=0; j<origEvents.size(); j++) {
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
    		System.out.println("Cannot save @event!!!");
    	}
    }
}
