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

public class EventDistance {
        public double x_dis;
        public double z_dis;
    
         public double health_dis;
         public int hasAmmo_dis;
         public double emeDistance_dis; //if not seen, set 1
         public int reachableItem_dis;
    
         public int behave1_dis;
         public int behave2_dis;
         public int behave3_dis;
         public int behave4_dis;
    
         public double reward_dis;
         
         public static NumberFormat numForm_Double=new DecimalFormat("0.00");
         public static NumberFormat numForm_Int=new DecimalFormat("0000");
         public static NumberFormat numForm_Heal=new DecimalFormat("000");
   
    public static EventDistance substract(Event e_cur, Event e_p){
        EventDistance e=new EventDistance();
        
        e.x_dis=e_cur.x-e_p.x;
        e.z_dis=e_cur.z-e_p.z;
        
        e.health_dis=e_cur.curHealth-e_p.curHealth;
        e.hasAmmo_dis=calDis(e_cur.hasAmmo, e_p.hasAmmo);
        e.emeDistance_dis=e_cur.emeDistance-e_p.emeDistance;
        e.reachableItem_dis=calDis(e_cur.reachableItem, e_p.reachableItem);
        
        e.behave1_dis=calDis(e_cur.behave1, e_p.behave1);
        e.behave2_dis=calDis(e_cur.behave2, e_p.behave2);
        e.behave3_dis=calDis(e_cur.behave3, e_p.behave3);
        e.behave4_dis=calDis(e_cur.behave4, e_p.behave4);
        
        e.reward_dis=e_cur.reward-e_p.reward;
        
        return e;
    }
    
    public static Vector<String> printEventDist(EventDistance e_dis, Event cur_e){
        Vector<String> changes=new Vector<String>(0);
        
        if(printLoc(e_dis.x_dis, e_dis.z_dis, cur_e.x, cur_e.z)==null){
        }
        else{
            String loc_str=""+printLoc(e_dis.x_dis, e_dis.z_dis, cur_e.x, cur_e.z)+";\n";
            changes.add(loc_str);
        }
        
        if(e_dis.health_dis!=0){
            String heal_str= "Health level "+printHeal(e_dis.health_dis, cur_e.curHealth)+";\n";
            changes.add(heal_str);
        }
        
        if(e_dis.hasAmmo_dis==1){
            String ammo_str= "Ammo "+printBoolean(e_dis.hasAmmo_dis)+";\n";
            changes.add(ammo_str);
        }
        
        if(e_dis.emeDistance_dis!=0){
            String heal_str= "Enemy "+printEme(e_dis.emeDistance_dis,cur_e.emeDistance)+";\n";
            changes.add(heal_str);
        }
        
        if(e_dis.reachableItem_dis==1){
            String heal_str= "Some reachable items "+printBoolean(e_dis.reachableItem_dis)+";\n";
            changes.add(heal_str);
        }
        
        String state_str=printBehave(e_dis);
        if(state_str!=null){
            state_str+=";\n";
            changes.add(state_str);
        }
        
        String reward_str=printConsequence(cur_e);
        if(reward_str!=null){
            reward_str+=";\n";
            changes.add(reward_str+"\n");
        }
        
        changes.add("\n");
        
        return changes;
    }
    
    //no change---0; false->true----1; true->false----(-1)
    private static int calDis(boolean b_cur,boolean b_pre){
        if(b_cur==b_pre){
            return 0;
        }
        
        if(b_cur==true&&b_pre==false){
            return 1;
        }
        
            return -1;
    }
    
    private static String printDouble(double dis, double cur){
        String print="";
        
        if(dis>0){
            return "increase by "+dis+" to "+cur;
        }
        
        if(dis<0){
            return "decrease by "+dis+" to "+cur;
        }
        
        return null;
    }
    
    private static String printBoolean(int change){
        if(change==1){
            return "appears";
        }
        
        return null;
    }
    
    private static String printLoc(double dis_x, double dis_z, double cur_x, double cur_z){
        String print="";
        
        if(dis_x==0&&dis_z==0){
            return null;
        }
        
        String dis_x_str=numForm_Int.format(convertLocDis(dis_x));
        String dis_z_str=numForm_Int.format(convertLocDis(dis_z));
        String cur_x_str=numForm_Int.format(convertLoc(cur_x));
        String cur_z_str=numForm_Int.format(convertLoc(cur_z));
        
        return "moves by ("+dis_x_str+", "+dis_z_str+") to ("+cur_x_str+", "+cur_z_str+")";      
    }
    
    private static int convertLoc(double x){
        return (int) (x*10000-5000);
    }
    
    private static int convertLocDis(double x){
        return (int) (x*10000);
    }
    
    private static String printBehave(EventDistance e){
        int prev_state=-1;
        int cur_state=-1;
        
        if(e.behave1_dis>0){
            cur_state=1;
        }
        else if(e.behave1_dis<0){
            prev_state=1;
        }
        
        if(e.behave2_dis>0){
            cur_state=2;
        }
        else if(e.behave2_dis<0){
            prev_state=2;
        }
        
        if(e.behave3_dis>0){
            cur_state=3;
        }
        else if(e.behave3_dis<0){
            prev_state=3;
        }
        
        if(e.behave4_dis>0){
            cur_state=4;
        }
        else if(e.behave4_dis<0){
            prev_state=4;
        }
        
        if(cur_state==-1&&prev_state==-1){
            return null;
        }
        
        return "State changes from "+printStateName(prev_state)+" to "+printStateName(cur_state);
    }
    
    private static String printStateName(int i){
        switch(i){
            case 1: return "Running Around"; 
            case 2: return "Collecting Item"; 
            case 3: return "Escaping From Battle"; 
            case 4: return "Engaging Fire"; 
        }    
        return null;
    }
    
    private static String printConsequence(Event e){
        if(e.reward==0){
            return "Is killed. The battle is over...";
        }
        
        if(e.reward==0.25){
            return "Hitten by others.";
        }
        
        if(e.reward==0.75){
            return "Hits others.";
        }
        
        if(e.reward==1){
            return "Kills enemy. The battle is over...";
        }
        
        return null;
    }
    
    private static String printHeal(double dis, double cur){
        String print="";
        
        if(dis>0){
            return "increase by "+numForm_Heal.format(convertHeal(dis))+" to "+numForm_Heal.format(convertHeal(cur));
        }
        
        if(dis<0){
            return "decrease by "+numForm_Heal.format(-convertHeal(dis))+" to "+numForm_Heal.format(convertHeal(cur));
        }
        
        return null;
    }
    
    private static int convertHeal(double x){
        return (int) (x*10*15);
    }
    
    private static String printEme(double dis, double cur){
        String print="";
        
        if(dis>0){
            return "gets far from the agent by "+numForm_Int.format(convertEme(dis))+" to "+numForm_Heal.format(convertEme(cur));
        }
        
        if(dis<0){
            return "gets closer from by "+numForm_Int.format(-convertEme(dis))+" to "+numForm_Heal.format(convertEme(cur));
        }
        
        return null;
    }
    
    private static int convertEme(double x){
        return (int) (x*300*10);
    }
    
    //code added by WWW after 02/12/09
    
    public static Vector<String> incEDiscption(Event e_pre, Event e_cur){
        Vector<String> story=new Vector<String>(0);
        
        if(!e_cur.getMapArea().equalsIgnoreCase(e_pre.getMapArea())){
            story.add("Moved to Location Area "+e_cur.getMapArea()+" from "+ e_pre.getMapArea()+".\n");
        }
        
        if(e_pre.emeDistance==1&&e_cur.emeDistance<1){
            story.add("An enemy is appearing with a distance of "+e_cur.emeDistance*10*300+".\n");
        }
        else if(e_cur.emeDistance==1&&e_pre.emeDistance<1){
            story.add("An enemy is disappearing.\n");
        }
        else if(e_cur.emeDistance>e_pre.emeDistance){
            story.add("Moving further away from enemy and the distance is "+e_cur.emeDistance*10*300+".\n");
        }
        else if(e_cur.emeDistance<e_pre.emeDistance){
            story.add("Moving closer his enemy and the distanc is "+e_cur.emeDistance*10*300+".\n");
        }
        
        if(e_pre.reachableItem==true&&e_cur.reachableItem==false){
            story.add("Moving away from the nearby collectable items.\n");
        }
        
        if(e_pre.reachableItem==false&&e_cur.reachableItem==true){
            story.add("Moving towards a area with collectable items.\n");
        }
        
        if(e_pre.hasAmmo==false&&e_cur.hasAmmo==true){
            story.add("Out of Ammo.\n");
        }
        
        if(e_pre.hasAmmo==true&&e_cur.hasAmmo==false){
            story.add("Getting more Ammo.\n");
        }
        
         if((e_pre.convertState()!=null)&&(e_cur.convertState()!=null)){
            if(!e_pre.convertState().equalsIgnoreCase(e_cur.convertState())){
                story.add(""+e_cur.convertState()+".\n");
            }
        }
        
        if(e_cur.getHealth&&e_cur.getWeapon){
            story.add("Getting both more health and a weapon.\n");
        }
        else if((!e_cur.getHealth)&&e_cur.getWeapon){
            story.add("Getting a weapon.\n");
        }
        else if(e_cur.getHealth&&(!e_cur.getWeapon)){
            story.add("Getting more health.\n");
        }
        
        if(e_cur.isShooting&&(!e_pre.isShooting)){
            story.add("Starting shooting.\n");
        }
        else if((!e_cur.isShooting)&&e_pre.isShooting){
            story.add("Stopped shooting.\n");
        }
        
        if(e_cur.changeWeapon){
            story.add("Changing weapon.\n");
        }
        
        if(e_pre.curHealth>e_cur.curHealth){
            story.add("Get hitten and cosumed health level by "+(int)((e_pre.curHealth-e_cur.curHealth)*100)+"% resulting a health level of "+(int)(e_cur.curHealth*100)+"%.\n");
            
        }
        
        if(e_pre.curHealth<e_cur.curHealth){
            story.add("Health level is raised by "+(int)((e_cur.curHealth-e_pre.curHealth)*100)+"% resulting a health level of "+(int)(e_cur.curHealth*100)+"%.\n");
        }
        
       
        
        if(e_cur.reward!=e_pre.reward&&e_cur.reward!=0.5){
            story.add(printConsequence(e_cur)+".\n");
        }
        
        if(story.size()!=0){
            story.add("\n");
        }
        
        return story;
    }
}
