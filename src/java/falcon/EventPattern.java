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

public class EventPattern {
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
    public double x_compl;
    public double y;
    public double y_compl;
    public double z;
    public double z_compl;
    public boolean hasLocCategorized=false;
    
    public double curHealth;
    public double curHealth_compl;
    public double hasAmmo;
    public double hasAmmo_compl;
    public double emeDistance; //if not seen, set 1
    public double emeDistance_compl;
    public double reachableItem;
    public double reachableItem_compl;
    public double getHealth=0;
    public double getHealth_compl=1;
    public double getWeapon=0;
    public double getWeapon_compl=1;
    public double changeWeapon=0;
    public double changeWeapon_compl=1;
    public double isShooting;
    public double isShooting_compl;
    public double pickupHealS=0;
    public double pickupHealS_compl=1;
    public double pickupHealL=0;
    public double pickupHealL_compl=1;
    public double pickupWeapon1=0;
    public double pickupWeapon1_compl=1;
    public double pickupWeapon2=0;
    public double pickupWeapon2_compl=1;
    public double pickupWeapon3=0;
    public double pickupWeapon3_compl=1;
    public double pickupWeapon4=0;
    public double pickupWeapon4_compl=1;
    public double pickupWeapon5=0;
    public double pickupWeapon5_compl=1;
    public double pickupWeapon6=0;
    public double pickupWeapon6_compl=1;
    public double pickupWeapon7=0;
    public double pickupWeapon7_compl=1;;
    public double pickupWeapon8=0;
    public double pickupWeapon8_compl=1;
    public double pickupWeapon9=0;
    public double pickupWeapon9_compl=1;
    public double pickupWeapon10=0;
    public double pickupWeapon10_compl=1;    
    public double curWeapon1=0;
    public double curWeapon1_compl=1;
    public double curWeapon2=0;
    public double curWeapon2_compl=1;
    public double curWeapon3=0;
    public double curWeapon3_compl=1;
    public double curWeapon4=0;
    public double curWeapon4_compl=1;
    public double curWeapon5=0;
    public double curWeapon5_compl=1;
    public double curWeapon6=0;
    public double curWeapon6_compl=1;
    public double curWeapon7=0;
    public double curWeapon7_compl=1;
    public double curWeapon8=0;
    public double curWeapon8_compl=1;
    public double curWeapon9=0;
    public double curWeapon9_compl=1;
    public double curWeapon10=0;
    public double curWeapon10_compl=1;
    //public int shotNum=1;
        
    public double behave1;
    public double behave1_compl;
    public double behave2;
    public double behave2_compl;
    public double behave3;
    public double behave3_compl;
    public double behave4;
    public double behave4_compl;
    
    public double reward;
    public double reward_compl;
    
    //the following to attribute is for story telling purpose
    //dont forward them to the em for learning
    
    
    public boolean isDefalt=false;
    
    public static NumberFormat numForm_Double=new DecimalFormat("0.00");
    public static NumberFormat numForm_Int=new DecimalFormat("0000");
    public static NumberFormat numForm_Heal=new DecimalFormat("000");
    
    public EventPattern(){
        
    }
    
    public void setDefault(){
        isDefalt=true;
    }
    
    /*public String toString(){
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
    }*/
    
    public String toStringDispt(){
        String str="Location Area = ";
        str+=this.getMapArea();
        str+=", \n";
        
        str+="current health level = ";
        if(curHealth==1-curHealth){
             str+=(int)(curHealth*100)+"%";
        }
        else{
             str+=(int)(curHealth*100)+"~"+(int)((1-curHealth_compl)*100)+"%";
        }
        str+=", \n";
        
        str+="has enough ammo = ";
        if(hasAmmo==1-hasAmmo_compl){
             str+=hasAmmo;
        }
        else{
             str+=hasAmmo+"~"+(1-hasAmmo_compl);
        }
        str+=", \n";
        
        str+="emeney distance = ";
        if(emeDistance==1-emeDistance_compl){
            str+=(int)(emeDistance*10*300);
        }
        else{
            str+=(int)(emeDistance*10*300)+"~"+(int)((1-emeDistance_compl)*10*300);
        }
        str+=", \n";
        
        str+="has reachable item";
        if(reachableItem==1-reachableItem_compl){
             str+=reachableItem;
        }
        else{
             str+=reachableItem+"~"+(1-reachableItem_compl);
        }
        str+=", \n";
        
        str+="get MedKitS";
        if(pickupHealS==1-pickupHealS_compl){
             str+=pickupHealS;
        }
        else{
             str+=pickupHealS+"~"+(1-pickupHealS_compl);
        }
        str+=", \n";
        
        str+="get MedKitL";
        if(pickupHealL==1-pickupHealL_compl){
             str+=pickupHealL;
        }
        else{
             str+=pickupHealL+"~"+(1-pickupHealL_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[0]+" = ";
        if(pickupWeapon1==1-pickupWeapon1_compl){
             str+=pickupWeapon1;
        }
        else{
             str+=pickupWeapon1+"~"+(1-pickupWeapon1_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[1]+" = ";
        if(pickupWeapon2==1-pickupWeapon2_compl){
             str+=pickupWeapon2;
        }
        else{
             str+=pickupWeapon2+"~"+(1-pickupWeapon2_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[2]+" = ";
        if(pickupWeapon3==1-pickupWeapon3_compl){
             str+=pickupWeapon3;
        }
        else{
             str+=pickupWeapon3+"~"+(1-pickupWeapon3_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[3]+" = ";
        if(pickupWeapon4==1-pickupWeapon4_compl){
             str+=pickupWeapon4;
        }
        else{
             str+=pickupWeapon4+"~"+(1-pickupWeapon4_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[4]+" = ";
        if(pickupWeapon5==1-pickupWeapon5_compl){
             str+=pickupWeapon5;
        }
        else{
             str+=pickupWeapon5+"~"+(1-pickupWeapon5_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[5]+" = ";
        if(pickupWeapon6==1-pickupWeapon6_compl){
             str+=pickupWeapon6;
        }
        else{
             str+=pickupWeapon6+"~"+(1-pickupWeapon6_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[6]+" = ";
        if(pickupWeapon7==1-pickupWeapon7_compl){
             str+=pickupWeapon7;
        }
        else{
             str+=pickupWeapon7+"~"+(1-pickupWeapon7_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[7]+" = ";
        if(pickupWeapon8==1-pickupWeapon8_compl){
             str+=pickupWeapon8;
        }
        else{
             str+=pickupWeapon8+"~"+(1-pickupWeapon8_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[8]+" = ";
        if(pickupWeapon9==1-pickupWeapon9_compl){
             str+=pickupWeapon9;
        }
        else{
             str+=pickupWeapon9+"~"+(1-pickupWeapon9_compl);
        }
        str+=", \n";
        
        str+="get "+allWeapons[9]+" = ";
        if(pickupWeapon1==1-pickupWeapon10_compl){
             str+=pickupWeapon10;
        }
        else{
             str+=pickupWeapon10+"~"+(1-pickupWeapon10_compl);
        }
        str+=", \n";
        
        //str+="changing weapon now = ";
        //str+=stringBool(changeWeapon);
        //str+=", \n";
        
        str+="is shooting = ";
        if(isShooting==1-isShooting_compl){
             str+=isShooting;
        }
        else{
             str+=isShooting+"~"+(1-isShooting_compl);
        }
        str+=", \n";
        
        
        str+="current holding weapon "+allWeapons[0]+" = ";
        if(curWeapon1==1-curWeapon1_compl){
             str+=curWeapon1;
        }
        else{
             str+=curWeapon1+"~"+(1-curWeapon1_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[1]+" = ";
        if(curWeapon2==1-curWeapon2_compl){
             str+=curWeapon2;
        }
        else{
             str+=curWeapon2+"~"+(1-curWeapon2_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[2]+" = ";
        if(curWeapon3==1-curWeapon3_compl){
             str+=curWeapon3;
        }
        else{
             str+=curWeapon3+"~"+(1-curWeapon3_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[3]+" = ";
        if(curWeapon4==1-curWeapon4_compl){
             str+=curWeapon4;
        }
        else{
             str+=curWeapon4+"~"+(1-curWeapon4_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[4]+" = ";
        if(curWeapon5==1-curWeapon5_compl){
             str+=curWeapon5;
        }
        else{
             str+=curWeapon5+"~"+(1-curWeapon5_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[5]+" = ";
        if(curWeapon6==1-curWeapon6_compl){
             str+=curWeapon6;
        }
        else{
             str+=curWeapon6+"~"+(1-curWeapon6_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[6]+" = ";
        if(curWeapon7==1-curWeapon7_compl){
             str+=curWeapon7;
        }
        else{
             str+=curWeapon7+"~"+(1-curWeapon7_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[7]+" = ";
        if(curWeapon8==1-curWeapon8_compl){
             str+=curWeapon8;
        }
        else{
             str+=curWeapon8+"~"+(1-curWeapon8_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[8]+" = ";
        if(curWeapon9==1-curWeapon9_compl){
             str+=curWeapon9;
        }
        else{
             str+=curWeapon9+"~"+(1-curWeapon9_compl);
        }
        str+=", \n";
        
        str+="current holding weapon "+allWeapons[9]+" = ";
        if(curWeapon10==1-curWeapon10_compl){
             str+=curWeapon10;
        }
        else{
             str+=curWeapon10+"~"+(1-curWeapon10_compl);
        }
        str+=", \n";
        
        str+="currently changing weapon = ";
        if(changeWeapon==1-changeWeapon_compl){
             str+=changeWeapon;
        }
        else{
             str+=changeWeapon+"~"+(1-changeWeapon_compl);
        }
        str+=", \n";
        
        str+="EXPLOITING THE MAP = ";
        if(behave1==1-behave1_compl){
             str+=behave1;
        }
        else{
             str+=behave1+"~"+(1-behave1_compl);
        }
        str+="\n";
        
        str+="SEARCHING FOR MORE ITEMS FOR COLLECTION = ";
        if(behave2==1-behave2_compl){
             str+=behave2;
        }
        else{
             str+=behave2+"~"+(1-behave2_compl);
        }
        str+="\n";
        
        str+="ESCAPING FORM THE BATTLE = ";
        if(behave3==1-behave3_compl){
             str+=behave3;
        }
        else{
             str+=behave3+"~"+(1-behave3_compl);
        }
        str+="\n";
        
        str+="ACTIVE ENGAGING IN THE BATTLE ";
        if(behave4==1-behave4_compl){
             str+=behave4;
        }
        else{
             str+=behave4+"~"+(1-behave4_compl);
        }
        str+="\n";
        
        str+="consequence get = ";      
        if(reward==1-reward_compl){
             str+=reward;
        }
        else{
             str+=reward+"~"+(1-reward_compl);
        }
        str+=".\n";
        return str;
    }
    
   /* protected int[] getMedKit(){
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
    }*/
    
    protected boolean isForcedTer(Event e){
        if(e.reward==0||e.reward==1){
            return true;
        }
        return false;
    }
    
    public static EventPattern getEventPattern(double[][] inputs){
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
        
        EventPattern e=new EventPattern();
        e.x=inputs[0][0];
        e.x_compl=inputs[0][1];
        e.z=inputs[0][2];
        e.z_compl=inputs[0][3];
        e.y=inputs[0][4];
        e.y_compl=inputs[0][5];
        
        e.curHealth=inputs[1][0];
        e.curHealth_compl=inputs[1][1];
        e.hasAmmo=inputs[1][2];
        e.hasAmmo_compl=inputs[1][3];
        e.emeDistance=inputs[1][4];
        e.emeDistance_compl=inputs[1][5];
        e.reachableItem=inputs[1][6];
        e.reachableItem_compl=inputs[1][7];
        e.getHealth=inputs[1][8];
        e.getHealth_compl=inputs[1][9];
        e.getWeapon=inputs[1][10];
        e.getWeapon_compl=inputs[1][11];
        e.changeWeapon=inputs[1][12];
        e.changeWeapon_compl=inputs[1][13];
        e.isShooting=inputs[1][14];
        e.isShooting_compl=inputs[1][15];
        e.pickupHealS=inputs[1][16];
        e.pickupHealS_compl=inputs[1][17];
        e.pickupHealL=inputs[1][18];
        e.pickupHealL_compl=inputs[1][19];
        e.pickupWeapon1=inputs[1][20];
        e.pickupWeapon1_compl=inputs[1][21];
        e.pickupWeapon2=inputs[1][22];
        e.pickupWeapon2_compl=inputs[1][23];
        e.pickupWeapon3=inputs[1][24];
        e.pickupWeapon3_compl=inputs[1][25];
        e.pickupWeapon4=inputs[1][26];
        e.pickupWeapon4_compl=inputs[1][27];
        e.pickupWeapon5=inputs[1][28];
        e.pickupWeapon5_compl=inputs[1][29];
        e.pickupWeapon6=inputs[1][30];
        e.pickupWeapon6_compl=inputs[1][31];
        e.pickupWeapon7=inputs[1][32];
        e.pickupWeapon7_compl=inputs[1][33];
        e.pickupWeapon8=inputs[1][34];
        e.pickupWeapon8_compl=inputs[1][35];
        e.pickupWeapon9=inputs[1][36];
        e.pickupWeapon9_compl=inputs[1][37];
        e.pickupWeapon10=inputs[1][38];   
        e.pickupWeapon10_compl=inputs[1][39];
        e.curWeapon1=inputs[1][40];
        e.curWeapon1_compl=inputs[1][41];
        e.curWeapon2=inputs[1][42];
        e.curWeapon2_compl=inputs[1][43];
        e.curWeapon3=inputs[1][44];
        e.curWeapon3_compl=inputs[1][45];
        e.curWeapon4=inputs[1][46];
        e.curWeapon4_compl=inputs[1][47];
        e.curWeapon5=inputs[1][48];
        e.curWeapon5_compl=inputs[1][49];
        e.curWeapon6=inputs[1][50];
        e.curWeapon6_compl=inputs[1][51];
        e.curWeapon7=inputs[1][52];
        e.curWeapon7_compl=inputs[1][53];
        e.curWeapon8=inputs[1][54];
        e.curWeapon8_compl=inputs[1][55];
        e.curWeapon9=inputs[1][56];
        e.curWeapon9_compl=inputs[1][57];
        e.curWeapon10=inputs[1][58];
        e.curWeapon10_compl=inputs[1][59];
        //e.shotNum=(int)inputs[1][60];
        
        e.behave1=inputs[2][0];
        e.behave1_compl=inputs[2][1];
        e.behave2=inputs[2][2];
        e.behave2_compl=inputs[2][3];
        e.behave3=inputs[2][4];
        e.behave3_compl=inputs[2][5];
        e.behave4=inputs[2][6];
        e.behave4_compl=inputs[2][7];
        
        e.reward=inputs[3][0];
        e.reward_compl=inputs[3][1];
        
        return e;
    }
    
    public double[][] toArray(){
        double[][] eventArray=new double[4][];
        
        eventArray[0]=new double[6];
        eventArray[0][0]=x;
        eventArray[0][1]=x_compl;
        eventArray[0][2]=z;
        eventArray[0][3]=z_compl;
        eventArray[0][4]=y;
        eventArray[0][5]=y_compl;
        
        eventArray[1]=new double[60];
        eventArray[1][0]=curHealth;
        eventArray[1][1]=curHealth_compl;
        eventArray[1][2]=hasAmmo;
        eventArray[1][3]=hasAmmo_compl;
        eventArray[1][4]=emeDistance;
        eventArray[1][5]=emeDistance_compl;    
        eventArray[1][6]=reachableItem;
        eventArray[1][7]=reachableItem_compl;
        eventArray[1][8]=getHealth;
        eventArray[1][9]=getHealth_compl;
        eventArray[1][10]=getWeapon;
        eventArray[1][12]=changeWeapon;
        eventArray[1][13]=changeWeapon_compl;
        eventArray[1][14]=isShooting;
        eventArray[1][15]=isShooting_compl;
        eventArray[1][16]=pickupHealS;
        eventArray[1][17]=pickupHealS_compl;
        eventArray[1][18]=pickupHealL;
        eventArray[1][19]=pickupHealL_compl; 
        eventArray[1][20]=pickupWeapon1;
        eventArray[1][21]=pickupWeapon1_compl;
        eventArray[1][22]=pickupWeapon2;
        eventArray[1][23]=pickupWeapon2_compl;
        eventArray[1][24]=pickupWeapon3;
        eventArray[1][25]=pickupWeapon3_compl;
        eventArray[1][26]=pickupWeapon4;
        eventArray[1][27]=pickupWeapon4_compl;
        eventArray[1][28]=pickupWeapon5;
        eventArray[1][29]=pickupWeapon5_compl;
        eventArray[1][30]=pickupWeapon6;
        eventArray[1][31]=pickupWeapon6_compl;
        eventArray[1][32]=pickupWeapon7;
        eventArray[1][33]=pickupWeapon7_compl;
        eventArray[1][34]=pickupWeapon8;
        eventArray[1][35]=pickupWeapon8_compl;
        eventArray[1][36]=pickupWeapon9;
        eventArray[1][37]=pickupWeapon9_compl;
        eventArray[1][38]=pickupWeapon10;
        eventArray[1][39]=pickupWeapon10_compl;
        eventArray[1][40]=curWeapon1;
        eventArray[1][41]=curWeapon1_compl;
        eventArray[1][42]=curWeapon2;
        eventArray[1][43]=curWeapon2_compl;
        eventArray[1][44]=curWeapon3;
        eventArray[1][45]=curWeapon3_compl;
        eventArray[1][46]=curWeapon4;
        eventArray[1][47]=curWeapon4_compl;
        eventArray[1][48]=curWeapon5;
        eventArray[1][49]=curWeapon5_compl;
        eventArray[1][50]=curWeapon6;
        eventArray[1][51]=curWeapon6_compl;
        eventArray[1][52]=curWeapon7;
        eventArray[1][53]=curWeapon7_compl;
        eventArray[1][54]=curWeapon8;
        eventArray[1][55]=curWeapon8_compl;
        eventArray[1][56]=curWeapon9;
        eventArray[1][57]=curWeapon9_compl;
        eventArray[1][58]=curWeapon10;
        eventArray[1][59]=curWeapon10_compl;
        
        
        //eventArray[1][60]=shotNum;
        
        eventArray[2]=new double[8];
        eventArray[2][0]=behave1;
        eventArray[2][1]=behave1_compl;
        eventArray[2][2]=behave2;
        eventArray[2][3]=behave2_compl;
        eventArray[2][4]=behave3;
        eventArray[2][5]=behave3_compl;
        eventArray[2][6]=behave4;
        eventArray[2][7]=behave4_compl;
        
        eventArray[3]=new double[2];
        eventArray[3][0]=reward;
        eventArray[3][1]=reward_compl;
        
        return eventArray;
    }
    
    /*public double getDistance(Event e){
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
    }*/
    
    public String getMapArea(){
        String locArea;
          
        double xGrid=(double)1.0/(double)divX;
        int xIndex=(int)(x/xGrid);
        int xIndex_compl=(int)((((double)1)-x_compl)/xGrid);
        
        System.out.println("getMapArea: x="+x+", x_compl="+x_compl+", xIndex="+xIndex+", xIndex_compl="+(((double)1)-x_compl)+", xGrid="+xGrid);
        
        double yGrid=(double)1.0/(double)divY;
        int yIndex=(int)(y/yGrid);
        int yIndex_compl=(int)((((double)1)-y_compl)/yGrid);
         System.out.println("getMapArea: y="+y+", y_compl="+y_compl+", yIndex="+yIndex+", yIndex_compl="+yIndex_compl+", yGrid="+yGrid);
        
        double zGrid=(double)1.0/(double)divZ;
        int zIndex=(int)(z/zGrid);
        int zIndex_compl=(int)((((double)1)-z_compl)/zGrid);
         System.out.println("getMapArea: z="+z+", z_compl="+z_compl+", zIndex="+zIndex+", zIndex_compl="+zIndex_compl+", zGrid="+zGrid);
        
        if(Math.abs(x-(1.0-x_compl))<0.01||xIndex==xIndex_compl){
            locArea=xGridIndex[xIndex]+"_";
        }
        else{
            locArea="{"+xGridIndex[xIndex]+"-"+xGridIndex[xIndex_compl]+"}_";
        }
        if(Math.abs(y-(1.0-y_compl))<0.01||yIndex==yIndex_compl){
            locArea+=yGridIndex[yIndex]+"_";
        }
        else{
            locArea+="{"+yGridIndex[yIndex]+"-"+yGridIndex[yIndex_compl]+"}_";
        }
        if(Math.abs(z-(1.0-z_compl))<0.01||zIndex==zIndex_compl){
            locArea+=zIndex;
        }
        else{
            locArea+="{"+zIndex+"-"+zIndex_compl+"}";
        }
        return locArea;
    }
}
