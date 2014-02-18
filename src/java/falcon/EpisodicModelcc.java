/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package falcon;

/**
 *
 * @author WA0003EN
 */
import java.io.*;
import java.io.IOException;
import java.util.Vector;

public class EpisodicModelcc {
    protected FusionARTGEncodingcc seqLearner;
    protected FusionART eventLearner;
    //protected String filePath="C:\\Documents and Settings\\User\\Desktop\\My Dropbox\\wenwen\\em-modified\\";
    protected String filePath="/Users/atifsaleem/NetBeansProjects/MemoryBank2/data";
    //protected String filePath= System.getProperty("user.dir")+"\\";
     public double iniConf1=0.75;
     public double iniConf2=1.0;
    
    public EpisodicModelcc(){
        seqLearner=new FusionARTGEncodingcc();
        eventLearner=new FusionART();
    }
    
    protected void initSeqPara(double b, double a, double g, double r){
        seqLearner.initParameters(b, a, g, r);
    }
    
    protected void initEventPara(int ns, int[] le, double b[], double[] a, double[] g, double[] r){
        eventLearner.initParameters(ns, le, b, a, g, r);   
    }
    
    protected void newEvent(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART,seqLearner.curSeq); 
        
        if(winner<0){
            System.out.println("!!!error @ maximum capacity!!!");
            System.exit(0);
        }
        
        //System.out.println("winner@event="+winner);
        boolean newEOccurred=eventLearner.newCode[winner];
        seqLearner.newEvent(winner);
        
        if(event[3][0]==0.5){
            eventLearner.doLearn(winner, eventLearner.FUZZYART,0.5);
        }
        else if(event[3][0]==1||event[3][0]==0){
            eventLearner.doLearn(winner, eventLearner.FUZZYART,this.iniConf2);
        }
        else{
            eventLearner.doLearn(winner, eventLearner.FUZZYART, this.iniConf1);
        }
        /*if(event[3][0]==1.0||event[3][0]==0){
           eventLearner.reinforce(winner,1.0);
        }
        else if(event[3][0]!=0.5){
           eventLearner.reinforce(winner,0.7);
        }
        else{
            eventLearner.reinforce(winner);
        }*/
        eventLearner.reinforce(winner);
    }
    
    
     protected int newEventWinner(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART,seqLearner.curSeq); 
        
        if(winner<0){
            System.out.println("!!!error @ maximum capacity!!!");
            System.exit(0);
        }
        
        //System.out.println("winner@event="+winner);
        boolean newEOccurred=eventLearner.newCode[winner];
        seqLearner.newEvent(winner);
        eventLearner.doLearn(winner, eventLearner.FUZZYART,0.5);
        
        return winner;
    }
   
    protected int newEventDebug(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART);
        int debugM;
        
        if(winner<0){
            System.out.println("!!!error @ maximum capacity!!!");
            System.exit(0);
        }
        
       // System.out.println("winner@event="+winner);
        boolean newEOccurred=eventLearner.newCode[winner];
        
        if(newEOccurred){
            seqLearner.newEvent(winner);
            eventLearner.doLearn(winner, eventLearner.FUZZYART,0.5);
            return 0;
        }
        
        seqLearner.newEvent(winner);
        
        //Event curE=new Event();
        //Event preE=new Event();
        Event curE=Event.getEvent(event);
        Event preE=Event.getEvent(eventLearner.weight[winner]);
        
        if(curE.isExactEqual(preE)){
            debugM=1;
        }
        else{
            debugM=2;
        }
        
        eventLearner.doLearn(winner, eventLearner.FUZZYART,0.5);
        return debugM;
    }
    
    protected void newTermination(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART); 
        
        if(winner<0){
            System.out.println("!!!error @ maximum capacity!!!");
            System.exit(0);
        }
        
        //System.out.println("winner@event="+winner);
        boolean newEOccurred=eventLearner.newCode[winner];
        seqLearner.newEvent(winner);
        eventLearner.doLearn(winner, eventLearner.ART2,0.5);
        seqLearner.sequenceEnd();
    }
    
    protected boolean newTrainEvent(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART);
        if(winner<0||winner>=eventLearner.numCode-1){
            System.out.println("!!!error@testing_data:  unexperienced event!!!----winner="+winner);
            this.eventLearner.print();
            this.seqLearner.emptyAccumulated();
            return false;
        }
        //System.out.println("find_winner="+winner);
        return seqLearner.oneTrainAccumulated(winner);
    }
    
    protected void printEposidic(){
       for(int i=0;i<eventLearner.weight.length;i++){
            for(int j=0;j<eventLearner.weight[i].length;j++){
                for(int k=0;k<eventLearner.weight[i][j].length;k++){
                    System.out.print(eventLearner.weight[i][j][k]+"\t");
                }
                System.out.print("||");
            }
            System.out.println("");
        }
       
       System.out.println("=========================");
       
       for(int i=0;i<seqLearner.weight.length;i++){
            for(int j=0;j<seqLearner.weight[i].length;j++){
                for(int k=0;k<seqLearner.weight[i][j].length;k++){
                    System.out.print(seqLearner.weight[i][j][k]+"\t");
                }
                System.out.println("");
            }
        }
    }
    
    
    public double getNextMaxSeq(double startTho){
        double maxv = 0.0;
        //System.out.println("curSeq " + seqLearner.curSeq);
        for(int i=0;i<seqLearner.curSeq.size();i++){
            if((seqLearner.curSeqWeight.get(i)>maxv)&&(seqLearner.curSeqWeight.get(i)<startTho)){
                maxv = seqLearner.curSeqWeight.get(i);
            }
        }
        return maxv;
    }

    public int  getNextIdxSeq(double startTho){
        double maxv = 0.0;
        int idx = -1;
        //System.out.println("Capacity: " + seqLearner.curSeq.size());
        for(int i=0;i<seqLearner.curSeq.size();i++){
            if((seqLearner.curSeqWeight.get(i)>maxv)&&(seqLearner.curSeqWeight.get(i)<startTho)){
                maxv = seqLearner.curSeqWeight.get(i);
                idx = i;
                //System.out.println("maxidx: " + maxv + " idx: " + idx);
            } 
        }
        return idx;
    }
    
    
    
    public static void main(String[] args){
        EpisodicModelcc em=new EpisodicModelcc(); 
        int[] le={3,3};
        double[] b={(double)1.0,(double)1.0};
        double[] a={(double)0.1,(double)0.1};
        double[] g={(double)0.5,(double)0.5};
        double[] r={(double)1.0,(double)1.0};
        em.initEventPara(2, le, b, a, g, r);
        
        em.initSeqPara(1.0,.1 , 1.0, 1.0);
    
    
        double[][] input_e1={{(double).1,(double).1,(double).1},{(double).1,(double).1,(double).1}};
        double[][] input_e2={{(double).2,(double).2,(double).2},{(double).2,(double).2,(double).2}};
        double[][] input_e3={{(double).3,(double).3,(double).3},{(double).3,(double).3,(double).3}};
        double[][] input_e4={{(double).4,(double).4,(double).4},{(double).4,(double).4,(double).4}};
        double[][] input_e5={{(double).5,(double).5,(double).5},{(double).5,(double).5,(double).5}};
        double[][] input_e6={{(double).7,(double).7,(double).6},{(double).6,(double).6,(double).6}};
        double[][] input_e7={{(double).8,(double).8,(double).8},{(double).8,(double).8,(double).8}};
        double[][] input_e8={{(double).9,(double).9,(double).9},{(double).9,(double).9,(double).9}};
        double[][] input_e9={{(double).9,(double).9,(double).9},{(double).1,(double).1,(double).1}};
        double[][] input_e10={{(double).8,(double).8,(double).8},{(double).2,(double).2,(double).2}};
        double[][] input_e11={{(double).2,(double).2,(double).2},{(double).8,(double).8,(double).8}};
        double[][] input_e12={{(double).1,(double).1,(double).1},{(double).9,(double).9,(double).9}};
       
        
        System.out.println("learn_1");
        em.newEvent(input_e1);
        System.out.println("learn_2");
        em.newEvent(input_e2);
        System.out.println("learn_3");
        em.newEvent(input_e3);
        System.out.println("learn_4");
        em.newEvent(input_e4);
        System.out.println("learn_5");
        em.newTermination(input_e5);
        em.seqLearner.print();
        em.seqLearner.resetCurSeq();
        em.seqLearner.resetActivityF1();
        em.seqLearner.resetBaseLineTho();
        
        
        System.out.println("learn_6");
        em.newEvent(input_e6);
        System.out.println("learn_7");
        em.newEvent(input_e7);
        System.out.println("learn_8");
        em.newEvent(input_e8);
        System.out.println("learn_9");
        em.newTermination(input_e9);
        em.seqLearner.print();
        em.seqLearner.resetCurSeq();
        em.seqLearner.resetActivityF1();
        em.seqLearner.resetBaseLineTho();
        
        /*
        System.out.println("learn_10");
        em.newEvent(input_e10);
        System.out.println("learn_11");
        em.newEvent(input_e11);
        System.out.println("learn_12");
        em.newTermination(input_e12);
        em.seqLearner.print();
        em.seqLearner.resetCurSeq();
        em.seqLearner.resetActivityF1();
*/
        
        
        System.out.println("learn_1");
        em.newEvent(input_e1);
        System.out.println("learn_2");
        em.newEvent(input_e2);
        System.out.println("learn_3");
        em.newEvent(input_e3);
        System.out.println("learn_1");
        em.newEvent(input_e1);
        System.out.println("learn_1");
        em.newEvent(input_e1);       
        em.seqLearner.seqToActivityF1();
        System.out.println("curSeq: " + em.seqLearner.curSeq);
        System.out.println("curSeqWeight" + em.seqLearner.curSeqWeight);
        
        //int swin = em.seqLearner.findWinnerFromF1();
        em.seqLearner.activityF1Print();
        int swin = em.seqLearner.WinnerFromF1();
        //swin = em.seqLearner.findWinner(em.seqLearner.activityF1, 0);
        if(swin>=0){
            System.out.println("Winner index is: " + swin);
            System.out.println("Winner is sequence:");
            em.seqLearner.readOutSeq(swin);
            em.seqLearner.activityF1Print();
            em.seqLearner.activityF1ToCurSeq();
            System.out.println("curSeq: " + em.seqLearner.curSeq);
            System.out.println("curSeqWeight" + em.seqLearner.curSeqWeight);
            double stho = 1.1;
            int wn = -1;
            System.out.println("Recalling from partial episodes:");
            while(stho>0){
                wn = em.getNextIdxSeq(stho);
                stho = em.getNextMaxSeq(stho);
                System.out.println("seq win: " + wn + " stho: " + stho);
                if(wn>=0){
                    String plist = "";
                    double[][] ifield = em.eventLearner.getAllOutput(wn);
                    for(int k=0;k<ifield.length;k++){
                        for(int i=0;i<ifield[k].length;i++){
                            plist = plist + "," + ifield[k][i];
                        }
                    }
                    System.out.println(plist);
                }
            }

        } else {
            System.out.println("No Winner!");
        }
        
        /* System.out.println("TEST FOR TRAINING");
        int[] learnSeq=em.newTrainEvent(input_e10,1);
        if(learnSeq==null){
            System.out.println("Cant learn sequence");
        }
        else{
            for(int i=0;i<learnSeq.length;i++){
                System.out.print(learnSeq[i]+"\t");
            }
            System.out.println("");
        }*/
        
        //em.printEposidic();
        
       /* System.out.println("train_event6...");
        int[] learnSeq=em.newTrainEvent(input_e11,1);
        if(learnSeq==null){
            System.out.println("Cant learn sequence");
        }
        else{
            for(int i=0;i<learnSeq.length;i++){
                System.out.print(learnSeq[i]+"\t");
            }
            System.out.println("");
        }*/
        
        //em.saveEpisodiceModel("event_learner.ext", "seq_learner.ext");
        
        //em.loadEpisodicModel("event_learner.ext", "seq_learner.ext");
        
        //em.printEposidic();
    }
    
    protected Vector<Event> getSeqEvents(int[] seq){
        Vector<Event> eList=new Vector<Event>(0);
        
        for(int i=0;i<seq.length;i++){
            eList.add(Event.getEvent(eventLearner.weight[seq[i]]));
        }
        
        return eList;
    }
    
    protected Vector<EventPattern> getSeqEventPatterns(int[] seq){
        Vector<EventPattern> eList=new Vector<EventPattern>(0);
        
        for(int i=0;i<seq.length;i++){
            eList.add(EventPattern.getEventPattern(eventLearner.weight[seq[i]]));
        }
        
        return eList;
    }
    
    protected void saveEpisodiceModel(String fileEvent, String fileSeq){
        if(fileEvent.equalsIgnoreCase(fileSeq)){
            System.out.println("!!!error@identitical_file_paths!!!");
            return;
        }
        
        try {
               FileWriter fw1 = new FileWriter(filePath + fileEvent);
               eventLearner.saveCode(fw1);
            }
         catch (Exception e){
    	       System.out.println("Cannot save FusionART EpisodicModel.eventLearn!"+(filePath + fileEvent));
         }
        try {
               FileWriter fw2 = new FileWriter(filePath + fileSeq);
               seqLearner.saveCode(fw2);
            }
         catch (Exception e){
    	       System.out.println("Cannot save FusionART EpisodicModel.seqLearn!"+(filePath + fileSeq));
         }
    }
    
    protected void loadEpisodicModel(String fileEvent, String fileSeq){
        if(fileEvent.equalsIgnoreCase(fileSeq)){
            System.out.println("!!!error@identitical_file_paths!!!");
            return;
        }
        
        try {
               FileReader fw1 = new FileReader(filePath + fileEvent);
               eventLearner.loadCode(fw1);
            }
         catch (Exception e){
    	       System.out.println("Cannot save FusionART EpisodicModel.eventLearn!..."+(filePath + fileEvent));
         }
        try {
               FileReader fw2 = new FileReader(filePath + fileSeq);
               seqLearner.loadCode(fw2);
            }
         catch (Exception e){
    	       System.out.println("Cannot save FusionART EpisodicModel.seqLearn!"+(filePath + fileSeq));
         }
    }
    
    protected Vector<Vector<Event>> getPlayBack(){
        Vector<Vector <Integer>> seqByIndex=new Vector<Vector <Integer>>();
        Vector<Vector <Event>> seqByEvent=new Vector<Vector <Event>>();
        
        for(int k=0;k<this.seqLearner.numCode;k++){
            double[][] winnerCode=new double[seqLearner.numF1][seqLearner.maxIdEvents];
            for(int i=0;i<seqLearner.numF1;i++){
                 System.arraycopy(seqLearner.weight[k][i],0, winnerCode[i], 0, seqLearner.maxIdEvents);
            }
           
            seqByIndex.add(new Vector<Integer> ());
            double maxWeight=0;
            int maxEvent_1=-1;
            int maxEvent_2=-1;
            do{
                for(int i=0;i<seqLearner.numF1;i++){
                    for(int j=0;j<seqLearner.maxIdEvents;j++){
                       if(maxWeight<winnerCode[i][j]){
                           maxWeight=winnerCode[i][j];
                           maxEvent_1=i;
                           maxEvent_2=j;
                       } 
                   }
               }
               if(maxWeight>0){
                    seqByIndex.get(k).add(new Integer(maxEvent_1));
                    winnerCode[maxEvent_1][maxEvent_2]=0;
                    maxWeight=0;
                    maxEvent_1=-1;
                    maxEvent_2=-1;
                }
                else{
                    break;
                }
            }while(true);
        }
        
        for(int i=0;i<seqByIndex.size();i++){
            seqByEvent.add(new Vector<Event>());
            
            for(int j=0;j<seqByIndex.get(i).size();j++){
                seqByEvent.get(i).add(Event.getEvent(eventLearner.weight[seqByIndex.get(i).get(j)]));
            }
        }
        
        System.out.println("print playback function");
        for(int i=0;i<seqByIndex.size();i++){
            System.out.println(""+seqByIndex.get(i).size());
        }
        
        System.out.println("print playback function");
        for(int i=0;i<seqByIndex.size();i++){
            System.out.println(""+seqByEvent.get(i).size());
        }
        
        return seqByEvent;
    }
}
