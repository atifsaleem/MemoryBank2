/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package falcon;

/**
 *
 * @author WA0003EN
 */

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Vector;

public class Event {
   public static final String allWeapons [] = new String [] {"FLAK_CANNON", "MINIGUN",
            "ROCKET_LAUNCHER", "LINK_GUN", "BIO_RIFLE", "LIGHTNING_GUN",
            "SHOCK_RIFLE", "SNIPER_RIFLE", "ASSAULT_RIFLE", "SHIELD_GUN"};
   public static final int divX=10;
   public static final int divY=10;
   public static final int divZ=10;
   public static final String xGridIndex []=new String[] {"a","b","c","d","e","f","g",
             "h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"}; 
           
    public static final String yGridIndex []=new String[] {"alpha", "beta", "gamma", "delta",
          "epsilon", "zeta", "eta", "thet", "iot", "kappa", "lambda", "mu", "nu", "xi", "omicron", 
          "pi", "rho", "sigma", "tau", "upsilon", "phi", "chi", "psi", "omega"};
    
    public double x;
    public double y;
    public double z;
    public boolean hasLocCategorized=false;
    
    public double curHealth;
    public boolean hasAmmo;
    public double emeDistance; //if not seen, set 1
    public boolean reachableItem;
    public boolean getHealth=false;
    public boolean getWeapon=false;
    public boolean changeWeapon=false;
    public boolean isShooting;
    public boolean pickupHealS=false;
    public boolean pickupHealL=false;
    public boolean pickupWeapon1=false;
    public boolean pickupWeapon2=false;
    public boolean pickupWeapon3=false;
    public boolean pickupWeapon4=false;
    public boolean pickupWeapon5=false;
    public boolean pickupWeapon6=false;
    public boolean pickupWeapon7=false;
    public boolean pickupWeapon8=false;
    public boolean pickupWeapon9=false;
    public boolean pickupWeapon10=false;    
    public boolean curWeapon1=false;
    public boolean curWeapon2=false;
    public boolean curWeapon3=false;
    public boolean curWeapon4=false;
    public boolean curWeapon5=false;
    public boolean curWeapon6=false;
    public boolean curWeapon7=false;
    public boolean curWeapon8=false;
    public boolean curWeapon9=false;
    public boolean curWeapon10=false;
    //public int shotNum=1;
        
    public boolean behave1;
    public boolean behave2;
    public boolean behave3;
    public boolean behave4;
    
    public double reward;
    
    //the following to attribute is for story telling purpose
    //dont forward them to the em for learning
    
    
    public boolean isDefalt=false;
    
    public static NumberFormat numForm_Double=new DecimalFormat("0.00");
    public static NumberFormat numForm_Int=new DecimalFormat("0000");
    public static NumberFormat numForm_Heal=new DecimalFormat("000");
    
    public Event(){
        
    }
    
    public void setDefault(){
        isDefalt=true;
    }
    
    public String toString(){
        String str="";
        str+=this.getMapArea();
        str+=", ";
        str+=numForm_Double.format(curHealth);
        str+=", ";
        str+=stringBool(hasAmmo);
        str+=", ";
        str+=numForm_Double.format(emeDistance);
        str+=", ";
        str+=stringBool(reachableItem);
        str+=", ";
        str+=stringBool(getHealth);
        str+=", ";
        str+=stringBool(getWeapon);
        str+=", ";
        str+=stringBool(changeWeapon);
        str+=", ";
        str+=stringBool(isShooting);
        str+=", ";
        str+=stringBool(behave1);
        str+=", ";
        str+=stringBool(behave2);
        str+=", ";
        str+=stringBool(behave3);
        str+=", ";
        str+=stringBool(behave4);
        str+=", ";
        str+=numForm_Double.format(reward);
        str+=".\n";
        return str;
    }
    
    public String toStringDispt(){
        String str="Location Area = ";
        str+=this.getMapArea();
        str+=", \n";
        
        str+="current health level = ";
        str+=(int)(curHealth*100)+"%";
        str+=", \n";
        
        str+="has enough ammo = ";
        str+=stringBool(hasAmmo);
        str+=", \n";
        
        str+="emeney distance = ";
        if(emeDistance==1){
            str+="out of sight";
        }
        else{
            str+=(int)(emeDistance*10*300);
        }
        str+=", \n";
        
        if(this.reachableItem){
            str+="has reachable item";
            //str+=stringBool(reachableItem);
            str+=", \n";
        }
        
        if(this.getHealth){
             str+="get reachable health = ";
             int[] picks=this.getMedKit();
             if(picks!=null){
             for(int i=0;i<picks.length;i++){
                 if(picks[i]==0){
                     str+="S MedKit\t";
                 }
                 if(picks[i]==1){
                     str+="L MedKit\t";
                 }
             }
             }
             str+=", \n";
        }
        
        if(this.getWeapon){
             str+="get reachable weapon = ";
             int[] picks=this.getPickWeapon();
             if(picks!=null){
             for(int i=0;i<picks.length;i++){
                 str+=allWeapons[picks[i]]+"\t";
             }}
             str+=", \n";
        }
        
        //str+="changing weapon now = ";
        //str+=stringBool(changeWeapon);
        //str+=", \n";
        
        str+="is shooting = ";
        str+=stringBool(isShooting);
        str+=", \n";
        
        if(this.isShooting){
             str+="current using weapon = ";
             int[] picks=this.getCurWeapon();
             if(picks!=null){
             for(int i=0;i<picks.length;i++){
                 str+=allWeapons[picks[i]]+"\t";
             }}
             str+=", \n";
        }
        
        str+="behavior state = ";
        str+=this.convertFuzziedState();
        str+="\n";
        
        str+="consequence get = ";      
        str+=convertFuzziedRewards();
        str+=".\n";
        return str;
    }
    
    protected int[] getMedKit(){
        Vector<Integer> picks=new Vector<Integer>(0);
        int[] picksIdx;
        
        if(this.pickupHealS){
            picks.add(new Integer(0));
        }
        
        if(this.pickupHealL){
            picks.add(new Integer(1));
        }
        
        if(picks.size()==0){
            return null;
        }
        
        picksIdx=new int[picks.size()];
        for(int i=0;i<picks.size();i++){
            picksIdx[i]=picks.get(i).intValue();
        }
        
        return picksIdx;
    }
    
    protected int[] getPickWeapon(){
        Vector<Integer> picks=new Vector<Integer>(0);
        int[] picksIdx;
        
        if(this.pickupWeapon1){
            picks.add(new Integer(0));
        }
        if(this.pickupWeapon2){
            picks.add(new Integer(1));
        }
        if(this.pickupWeapon3){
            picks.add(new Integer(2));
        }
        if(this.pickupWeapon4){
            picks.add(new Integer(3));
        }
        if(this.pickupWeapon5){
            picks.add(new Integer(4));
        }
        if(this.pickupWeapon6){
            picks.add(new Integer(5));
        }
        if(this.pickupWeapon7){
            picks.add(new Integer(6));
        }
        if(this.pickupWeapon8){
            picks.add(new Integer(7));
        }
        if(this.pickupWeapon9){
            picks.add(new Integer(8));
        }
        if(this.pickupWeapon10){
            picks.add(new Integer(9));
        }
        
        picksIdx=new int[picks.size()];
        for(int i=0;i<picks.size();i++){
            picksIdx[i]=picks.get(i).intValue();
        }
        
        return picksIdx;
    }
    
    protected int[] getCurWeapon(){
        Vector<Integer> picks=new Vector<Integer>(0);
        int[] picksIdx;
        
        if(this.curWeapon1){
            picks.add(new Integer(0));
        }
        if(this.curWeapon2){
            picks.add(new Integer(1));
        }
        if(this.curWeapon3){
            picks.add(new Integer(2));
        }
        if(this.curWeapon4){
            picks.add(new Integer(3));
        }
        if(this.pickupWeapon5){
            picks.add(new Integer(4));
        }
        if(this.curWeapon6){
            picks.add(new Integer(5));
        }
        if(this.curWeapon7){
            picks.add(new Integer(6));
        }
        if(this.curWeapon8){
            picks.add(new Integer(7));
        }
        if(this.curWeapon9){
            picks.add(new Integer(8));
        }
        if(this.curWeapon10){
            picks.add(new Integer(9));
        }
        
        picksIdx=new int[picks.size()];
        for(int i=0;i<picks.size();i++){
            picksIdx[i]=picks.get(i).intValue();
        }
        
        return picksIdx;
    }
    
    protected String stringBool(boolean p){
        if(p)
            return " true";
        else 
            return "false";
    }
    
    protected static boolean convertBool(double p){
        if(p==0.0){
            return false;
        }
        else{
            return true;
        }
    }
    
    protected boolean isForcedTer(Event e){
        if(e.reward==0||e.reward==1){
            return true;
        }
        return false;
    }
    
    public static Event getEvent(double[][] inputs){
        if(inputs.length!=4){
            System.out.println("input space="+inputs.length);
            return null;
        }
        if(inputs[0].length!=6){
            System.out.println("input channel 1="+inputs[0].length);
            return null;
        }
        
        if(inputs[1].length!=60){
            System.out.println("input channel 2="+inputs[1].length);
            return null;
        }
        if(inputs[2].length!=8){
            System.out.println("input channel 3="+inputs[2].length);
            return null;
        }
        if(inputs[3].length!=2){
            System.out.println("input channel 4="+inputs[3].length);
            return null;
        }
        
        Event e=new Event();
        e.x=inputs[0][0];
        e.z=inputs[0][2];
        e.y=inputs[0][4];
        
        e.curHealth=inputs[1][0];
        e.hasAmmo=convertBool(inputs[1][2]);
        e.emeDistance=inputs[1][4];
        e.reachableItem=convertBool(inputs[1][6]);
        e.getHealth=convertBool(inputs[1][8]);
        e.getWeapon=convertBool(inputs[1][10]);
        e.changeWeapon=convertBool(inputs[1][12]);
        e.isShooting=convertBool(inputs[1][14]);
        e.pickupHealS=convertBool(inputs[1][16]);
        e.pickupHealL=convertBool(inputs[1][18]);
        e.pickupWeapon1=convertBool(inputs[1][20]);
        e.pickupWeapon2=convertBool(inputs[1][22]);
        e.pickupWeapon3=convertBool(inputs[1][24]);
        e.pickupWeapon4=convertBool(inputs[1][26]);
        e.pickupWeapon5=convertBool(inputs[1][28]);
        e.pickupWeapon6=convertBool(inputs[1][30]);
        e.pickupWeapon7=convertBool(inputs[1][32]);
        e.pickupWeapon8=convertBool(inputs[1][34]);
        e.pickupWeapon9=convertBool(inputs[1][36]);
        e.pickupWeapon10=convertBool(inputs[1][38]);        
        e.curWeapon1=convertBool(inputs[1][40]);
        e.curWeapon2=convertBool(inputs[1][42]);
        e.curWeapon3=convertBool(inputs[1][44]);
        e.curWeapon4=convertBool(inputs[1][46]);
        e.curWeapon5=convertBool(inputs[1][48]);
        e.curWeapon6=convertBool(inputs[1][50]);
        e.curWeapon7=convertBool(inputs[1][52]);
        e.curWeapon8=convertBool(inputs[1][54]);
        e.curWeapon9=convertBool(inputs[1][56]);
        e.curWeapon10=convertBool(inputs[1][58]);
        //e.shotNum=(int)inputs[1][60];
        
        e.behave1=convertBool(inputs[2][0]);
        e.behave2=convertBool(inputs[2][2]);
        e.behave3=convertBool(inputs[2][4]);
        e.behave4=convertBool(inputs[2][6]);
        
        e.reward=inputs[3][0];
        
        return e;
    }
    
    public double[][] toArray(){
        double[][] eventArray=new double[4][];
        
        eventArray[0]=new double[6];
        eventArray[0][0]=x;
        eventArray[0][1]=1-x;
        eventArray[0][2]=z;
        eventArray[0][3]=1-z;
        eventArray[0][4]=y;
        eventArray[0][5]=1-y;
        
        eventArray[1]=new double[60];
        eventArray[1][0]=curHealth;
        eventArray[1][1]=1-curHealth;
        if(hasAmmo){
            eventArray[1][2]=(double)1;
            eventArray[1][3]=(double)0;
        }
        else{
            eventArray[1][2]=(double)0;
            eventArray[1][3]=(double)1;
        }
        eventArray[1][4]=emeDistance;
        eventArray[1][5]=1-emeDistance;    
        if(reachableItem){
            eventArray[1][6]=(double)1;
            eventArray[1][7]=(double)0;
        }
        else{
            eventArray[1][6]=(double)0;
            eventArray[1][7]=(double)1;
        }
        if(getHealth){
            eventArray[1][8]=(double)1;
            eventArray[1][9]=(double)0;
        }
        else{
            eventArray[1][8]=(double)0;
            eventArray[1][9]=(double)1;
        }
        if(getWeapon){
            eventArray[1][10]=(double)1;
            eventArray[1][11]=(double)0;
        }
        else{
            eventArray[1][10]=(double)0;
            eventArray[1][11]=(double)1;
        }
        if(changeWeapon){
            eventArray[1][12]=(double)1;
            eventArray[1][13]=(double)0;
        }
        else{
            eventArray[1][12]=(double)0;
            eventArray[1][13]=(double)1;
        }
        if(isShooting){
            eventArray[1][14]=(double)1;
            eventArray[1][15]=(double)0;
        }
        else{
            eventArray[1][14]=(double)0;
            eventArray[1][15]=(double)1;
        }
        
        if(pickupHealS){
            eventArray[1][16]=(double)1;
            eventArray[1][17]=(double)0;
        }
        else{
            eventArray[1][16]=(double)0;
            eventArray[1][17]=(double)1;
        }
        
        if(pickupHealL){
            eventArray[1][18]=(double)1;
            eventArray[1][19]=(double)0;
        }
        else{
            eventArray[1][18]=(double)0;
            eventArray[1][19]=(double)1;
        }
        
        if(pickupWeapon1){
            eventArray[1][20]=(double)1;
            eventArray[1][21]=(double)0;
        }
        else{
            eventArray[1][20]=(double)0;
            eventArray[1][21]=(double)1;
        }
        
        if(pickupWeapon2){
            eventArray[1][22]=(double)1;
            eventArray[1][23]=(double)0;
        }
        else{
            eventArray[1][22]=(double)0;
            eventArray[1][23]=(double)1;
        }
        
        if(pickupWeapon3){
            eventArray[1][24]=(double)1;
            eventArray[1][25]=(double)0;
        }
        else{
            eventArray[1][24]=(double)0;
            eventArray[1][25]=(double)1;
        }
        
        if(pickupWeapon4){
            eventArray[1][26]=(double)1;
            eventArray[1][27]=(double)0;
        }
        else{
            eventArray[1][26]=(double)0;
            eventArray[1][27]=(double)1;
        }
        
        if(pickupWeapon5){
            eventArray[1][28]=(double)1;
            eventArray[1][29]=(double)0;
        }
        else{
            eventArray[1][28]=(double)0;
            eventArray[1][29]=(double)1;
        }
        
        if(pickupWeapon6){
            eventArray[1][30]=(double)1;
            eventArray[1][31]=(double)0;
        }
        else{
            eventArray[1][30]=(double)0;
            eventArray[1][31]=(double)1;
        }
        
        if(pickupWeapon7){
            eventArray[1][32]=(double)1;
            eventArray[1][33]=(double)0;
        }
        else{
            eventArray[1][32]=(double)0;
            eventArray[1][33]=(double)1;
        }
        
        if(pickupWeapon8){
            eventArray[1][34]=(double)1;
            eventArray[1][35]=(double)0;
        }
        else{
            eventArray[1][34]=(double)0;
            eventArray[1][35]=(double)1;
        }
        
        if(pickupWeapon9){
            eventArray[1][36]=(double)1;
            eventArray[1][37]=(double)0;
        }
        else{
            eventArray[1][36]=(double)0;
            eventArray[1][37]=(double)1;
        }
        
        if(pickupWeapon10){
            eventArray[1][38]=(double)1;
            eventArray[1][39]=(double)0;
        }
        else{
            eventArray[1][38]=(double)0;
            eventArray[1][39]=(double)1;
        }
        
         if(curWeapon1){
            eventArray[1][40]=(double)1;
            eventArray[1][41]=(double)0;
        }
        else{
            eventArray[1][40]=(double)0;
            eventArray[1][41]=(double)1;
        }
        
        if(curWeapon2){
            eventArray[1][42]=(double)1;
            eventArray[1][43]=(double)0;
        }
        else{
            eventArray[1][42]=(double)0;
            eventArray[1][43]=(double)1;
        }
        
        if(curWeapon3){
            eventArray[1][44]=(double)1;
            eventArray[1][45]=(double)0;
        }
        else{
            eventArray[1][44]=(double)0;
            eventArray[1][45]=(double)1;
        }
        
        if(curWeapon4){
            eventArray[1][46]=(double)1;
            eventArray[1][47]=(double)0;
        }
        else{
            eventArray[1][46]=(double)0;
            eventArray[1][47]=(double)1;
        }
        
        if(curWeapon5){
            eventArray[1][48]=(double)1;
            eventArray[1][49]=(double)0;
        }
        else{
            eventArray[1][48]=(double)0;
            eventArray[1][49]=(double)1;
        }
        
        if(curWeapon6){
            eventArray[1][50]=(double)1;
            eventArray[1][51]=(double)0;
        }
        else{
            eventArray[1][50]=(double)0;
            eventArray[1][51]=(double)1;
        }
        
        if(curWeapon7){
            eventArray[1][52]=(double)1;
            eventArray[1][53]=(double)0;
        }
        else{
            eventArray[1][52]=(double)0;
            eventArray[1][53]=(double)1;
        }
        
        if(curWeapon8){
            eventArray[1][54]=(double)1;
            eventArray[1][55]=(double)0;
        }
        else{
            eventArray[1][54]=(double)0;
            eventArray[1][55]=(double)1;
        }
        
        if(curWeapon9){
            eventArray[1][56]=(double)1;
            eventArray[1][57]=(double)0;
        }
        else{
            eventArray[1][36]=(double)0;
            eventArray[1][37]=(double)1;
        }
        
        if(curWeapon10){
            eventArray[1][58]=(double)1;
            eventArray[1][59]=(double)0;
        }
        else{
            eventArray[1][58]=(double)0;
            eventArray[1][59]=(double)1;
        }
        
        //eventArray[1][60]=shotNum;
        
        eventArray[2]=new double[8];
        if(behave1){
            eventArray[2][0]=(double)1;
            eventArray[2][1]=(double)0;
        }
        else{
            eventArray[2][0]=(double)0;
            eventArray[2][1]=(double)1;
        }
        if(behave2){
            eventArray[2][2]=(double)1;
            eventArray[2][3]=(double)0;
        }
        else{
            eventArray[2][2]=(double)0;
            eventArray[2][3]=(double)1;
        }
        if(behave3){
            eventArray[2][4]=(double)1;
            eventArray[2][5]=(double)0;
        }
        else{
            eventArray[2][4]=(double)0;
            eventArray[2][5]=(double)1;
        }
        if(behave4){
            eventArray[2][6]=(double)1;
            eventArray[2][7]=(double)0;
        }
        else{
            eventArray[2][6]=(double)0;
            eventArray[2][7]=(double)1;
        }
        
        eventArray[3]=new double[2];
        eventArray[3][0]=reward;
        eventArray[3][1]=1-reward;
        
        return eventArray;
    }
    
    public double getDistance(Event e){
        if(this.isDefalt){
            return 0;
        }
        
        double cos=dotProduction(this, e)/(this.scale()*e.scale());
        
        return Math.abs(cos);
    }
    
    public double getBool(boolean b){
        if(b){
            return 1;
        }
        else{
            return 0;
        }
    }
    
    public double scale(){
        int s=0;
        
        s+=Math.pow(this.x, 2);
        s+=Math.pow(this.z, 2);
        s+=Math.pow(this.y, 2);
        
        s+=Math.pow(this.curHealth, 2);
        s+=Math.pow(getBool(this.hasAmmo), 2);
        s+=Math.pow(this.emeDistance, 2);
        s+=Math.pow(getBool(this.reachableItem), 2);
        s+=Math.pow(getBool(this.getHealth), 2);
        s+=Math.pow(getBool(this.getWeapon), 2);
        s+=Math.pow(getBool(this.changeWeapon), 2);
        s+=Math.pow(getBool(this.isShooting), 2);
        s+=Math.pow(getBool(this.pickupHealS), 2);
        s+=Math.pow(getBool(this.pickupHealL), 2);
        s+=Math.pow(getBool(this.pickupWeapon1), 2);
        s+=Math.pow(getBool(this.pickupWeapon2), 2);
        s+=Math.pow(getBool(this.pickupWeapon3), 2);
        s+=Math.pow(getBool(this.pickupWeapon4), 2);
        s+=Math.pow(getBool(this.pickupWeapon5), 2);
        s+=Math.pow(getBool(this.pickupWeapon6), 2);
        s+=Math.pow(getBool(this.pickupWeapon7), 2);
        s+=Math.pow(getBool(this.pickupWeapon8), 2);
        s+=Math.pow(getBool(this.pickupWeapon9), 2);
        s+=Math.pow(getBool(this.pickupWeapon10), 2);
        s+=Math.pow(getBool(this.curWeapon1), 2);
        s+=Math.pow(getBool(this.curWeapon2), 2);
        s+=Math.pow(getBool(this.curWeapon3), 2);
        s+=Math.pow(getBool(this.curWeapon4), 2);
        s+=Math.pow(getBool(this.curWeapon5), 2);
        s+=Math.pow(getBool(this.curWeapon6), 2);
        s+=Math.pow(getBool(this.curWeapon7), 2);
        s+=Math.pow(getBool(this.curWeapon8), 2);
        s+=Math.pow(getBool(this.curWeapon9), 2);
        s+=Math.pow(getBool(this.curWeapon10), 2);
        
        s+=Math.pow(getBool(this.behave1), 2);
        s+=Math.pow(getBool(this.behave2), 2);
        s+=Math.pow(getBool(this.behave3), 2);
        s+=Math.pow(getBool(this.behave4), 2);
        
        s+=Math.pow(this.reward, 2);
        
        return Math.sqrt(s);
    }
    
    public double dotProduction(Event e1, Event e2){
        int m=0;
        
        m+=e1.x*e2.x;
        m+=e1.z*e2.z;
        m+=e1.y*e2.y;
        
        m+=e1.curHealth*e2.curHealth;
        m+=(e1.hasAmmo==e2.hasAmmo) ? 1:0;
        m+=e1.emeDistance*e2.emeDistance;
        m+=(e1.reachableItem==e2.reachableItem) ? 1:0;
        
        m+=(e1.behave1==e2.behave1) ? 1:0;
        m+=(e1.behave2==e2.behave2) ? 1:0;
        m+=(e1.behave3==e2.behave3) ? 1:0;
        m+=(e1.behave4==e2.behave4) ? 1:0;
        
        m+=e1.reward*e2.reward;
        
        return m;
    }
    
    public boolean isEqual(Event e){
        if(Math.abs(this.x-e.x)>0)
            return false;
        if(Math.abs(this.z-e.z)>0)
            return false;
        if(Math.abs(this.y-e.y)>0)
            return false;
        
        if(Math.abs(this.curHealth-e.curHealth)>0.05)
            return false;
        if(this.hasAmmo!=e.hasAmmo)
            return false;
        if(Math.abs(this.emeDistance-e.emeDistance)>0.05)
            return false;
        if(this.reachableItem!=e.reachableItem)
            return false;
        if(this.getHealth!=e.getHealth)
            return false;
        if(this.getWeapon!=e.getWeapon)
            return false;
        if(this.changeWeapon!=e.changeWeapon)
            return false;
        if(this.isShooting!=e.isShooting)
            return false;
        
        if(this.behave1!=e.behave1)
            return false;
        if(this.behave2!=e.behave2)
            return false;
        if(this.behave3!=e.behave3)
            return false;
        if(this.behave4!=e.behave4)
            return false;
        
        if(Math.abs(this.reward-e.reward)>0.05)
            return false;
        
        return true;
    }
    
    public boolean isExactEqual(Event e){
        if(Math.abs(this.x-e.x)>0.00)
            return false;
        if(Math.abs(this.z-e.z)>0.00)
            return false;
        if(Math.abs(this.y-e.y)>0.00)
            return false;
        
        if(Math.abs(this.curHealth-e.curHealth)>0.00)
            return false;
        if(this.hasAmmo!=e.hasAmmo)
            return false;
        if(Math.abs(this.emeDistance-e.emeDistance)>0.00)
            return false;
        if(this.reachableItem!=e.reachableItem)
            return false;
        if(this.getHealth!=e.getHealth)
            return false;
        if(this.getWeapon!=e.getWeapon)
            return false;
        if(this.changeWeapon!=e.changeWeapon)
            return false;
        if(this.isShooting!=e.isShooting)
            return false;
        
        if(this.behave1!=e.behave1)
            return false;
        if(this.behave2!=e.behave2)
            return false;
        if(this.behave3!=e.behave3)
            return false;
        if(this.behave4!=e.behave4)
            return false;
        
        if(Math.abs(this.reward-e.reward)>0.00)
            return false;
        
        return true;
    }
    
    protected Vector<String> printStoryBegin(boolean isNewStart){
        Vector<String> story=new Vector<String>(0);
         
        if(isNewStart) {
            story.add("A game starts...\n");
            story.add("The agent starts at Location Area "+this.getMapArea()+" \n");
            story.add("with a health level of "+this.curHealth*100+"% of the full range\n");
            if(this.emeDistance < 1){
                story.add("An ammo appears at the distance of "+numForm_Int.format(convertEme())+", \n");
                if(hasAmmo){
                    story.add("has some ammos already.\n");
                }
                else{
                    story.add("starts without any ammo. \n");
                    if(this.reachableItem){
                        story.add("Fortunately, some reachable items is shown around.\n");
                    }
                }
            }
            else{
                story.add("No enemy appears in the observable range "+numForm_Int.format(convertEme())+", \n");
                if(hasAmmo){
                    story.add(" and has some ammos already.\n");
                }
                else{
                    story.add("starts without any ammo. \n");
                    if(this.reachableItem){
                        story.add("Fortunately, some reachable items is shown around.\n");
                    }
                }
            }
            
           if(this.behave1){
                story.add(""+this.convertState()+"\n");
            }

            if(this.behave2){
                story.add(""+this.convertState()+"\n");
                if(this.getHealth&&this.getWeapon){
                    story.add("got some more health and picked up a weapon.\n");
                }
                else if(this.getHealth&&(!this.getWeapon)){
                    story.add("got some more health.\n");
                }
                else if((!this.getHealth)&&this.getWeapon){
                    story.add("picked up a weapon.\n");
                }
            }
        }
        else{
            story.add("Currently, the agent is situated at Location Area "+this.getMapArea()+" \n");
            story.add("with a health level of "+this.curHealth*100+"% of the full range\n");
            if(this.emeDistance < 1){
                story.add("An enemy appears at the distance of "+numForm_Int.format(convertEme())+", \n");
                if(hasAmmo){
                    story.add(" and has some ammos already.\n");
                }
                else{
                    story.add("starts without any ammo. \n");
                    if(this.reachableItem){
                        story.add("Fortunately, some reachable items is shown around.\n");
                    }
                }
            }
            else{
                story.add("No enemy appears in the observable range"+numForm_Int.format(convertEme())+",\n");
                if(hasAmmo){
                    story.add("has some ammos already.\n");
                }
                else{
                    story.add("starts without any ammo. \n");
                    if(this.reachableItem){
                        story.add("Fortunately, some reachable items is shown around.\n");
                    }
                }
            }
            
            if(this.behave1){
                story.add(""+this.convertState()+"\n");
            }

            if(this.behave2){
                story.add(""+this.convertState()+"\n");
                if(this.getHealth&&this.getWeapon){
                    story.add("got some more health and picked up a weapon.\n");
                }
                else if(this.getHealth&&(!this.getWeapon)){
                    story.add("got some more health.\n");
                }
                else if((!this.getHealth)&&this.getWeapon){
                    story.add("picked up a weapon.\n");
                }            
           }
            
           if(this.behave3){
               story.add("ESCAPING FROM A BATTLE\n");
           }
           if(this.behave4){
               story.add("ACTIVE ENGAGING IN A BATTLE \n");
               if(this.isShooting){
                   story.add("and firing at enemy.\n");
               }
               else{
                   story.add("and holding a weapon.\n");
               }
           } 
        }
        
        story.add("\n");
         return story;
    }
    
    protected int convertLoc(double x){
        return (int) ((x*10000)-5000);
    }
    
    protected int convertHeal(){
        return (int) (curHealth*10*15);
    }
    
    protected int convertEme(){
        return (int) (emeDistance*300*10);
    }
     
    protected String convertState(){
        if(behave1)
             return "EXPLOITING THE MAP";
        if(behave2)
             return "SEARCHING FOR MORE ITEMS FOR COLLECTION";
        if(behave3)
             return  "ESCAPING FORM THE BATTLE";
        if(behave4)
             return  "ACTIVE ENGAGING IN THE BATTLE";
        return null;
    }
    
    protected String convertFuzziedState(){
        String state="";
        
        if(behave1)
             state+= "EXPLOITING THE MAP ";
        if(behave2)
             state+= "SEARCHING FOR MORE ITEMS FOR COLLECTION ";
        if(behave3)
             state+=  "ESCAPING FORM THE BATTLE ";
        if(behave4)
             state+= "ACTIVE ENGAGING IN THE BATTLE ";
        
        return state;
    }
    
    protected String convertFuzziedRewards(){
        if(reward==0){
            return "is killed";
        }
        else if(reward>0&&reward<=0.4){
            return "is hitten by enemy";
        }
        
        else if(reward>0.4&&reward<0.6){
            return "n.a.";
        }
        else if(reward>=0.6&&reward<1){
            return "damage the enemy";
        }
        else if(reward==1){
            return "kills the emeney";
        }
        
        return null;
        
    }
    
    protected void categorizeLoc(){
            double xGrid=1.0/(double)divX;
            int xIndex=(int)(x/xGrid);
            if(xIndex==divX){
                xIndex--;
            }
            x=xIndex*xGrid;
            
            double yGrid=1.0/(double)divY;
            int yIndex=(int)(y/yGrid);
            if(yIndex==divY){
                yIndex--;
            }
            y=yIndex*yGrid;
            
            double zGrid=1.0/(double)divZ;
            int zIndex=(int)(z/zGrid);
            if(zIndex==divZ){
                zIndex--;
            }
            z=zIndex*zGrid;
            
            this.hasLocCategorized=true;
    }
    
    public String getMapArea(){
        String locArea;
        
        
        double xGrid=1.0/(double)divX;
        int xIndex=(int)(x/xGrid);
        double yGrid=1.0/(double)divY;
        int yIndex=(int)(y/yGrid);
        double zGrid=1.0/(double)divZ;
        int zIndex=(int)(z/zGrid);
        
        locArea=xGridIndex[xIndex]+"_";
        locArea+=zIndex;
        locArea+=("_"+yGridIndex[yIndex]);
        return locArea;
    }
}
