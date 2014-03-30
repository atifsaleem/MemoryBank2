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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import java.util.Arrays;
import java.util.Date;
@WebServlet(urlPatterns = {"/EpisodicModel"})
public class EpisodicModel extends HttpServlet{
    protected FusionARTGEncoding seqLearner;
    protected FusionART eventLearner;
   
    //protected String filePath="C:\\Documents and Settings\\User\\Desktop\\My Dropbox\\wenwen\\em-modified\\";
    protected String filePath="/Users/atifsaleem/NetBeansProjects/Falcon_14_05_10_atif/data";
    private double[][] input_e1;
    //protected String filePath= System.getProperty("user.dir")+"\\";
    
    public EpisodicModel() throws IOException{
        seqLearner=new FusionARTGEncoding();
        eventLearner=new FusionART();
        /*
         * Location Encoding 
        
         */
        
        int[] le={2,6};
        double[] b={(double)1.0,(double)1.0};
        double[] a={(double)0.1,(double)0.1};
        double[] g={(double)0.5,(double)0.5};
        double[] r={(double)1.0,(double)1.0};
        int number = 0;
        this.initEventPara(2, le, b, a, g, r);
        
        this.initSeqPara(1.0,.1 , 1.0, 1.0);
        double[][] input_e1={{(double).04885,(double).00235},{(double).02,(double).02,(double).03, (double)0.1368217583, (double)0.0, (double) 0.1}};
        double[][] input_e2={{(double).04885,(double).00235},{(double).01,(double).02,(double).03, (double)0.1368217583, (double)0.0,(double) 0.2}};
        double[][] input_e3={{(double).04885,(double).00235},{(double).01,(double).02,(double).03, (double)0.1368217583, (double)0.0,(double) 0.3}};
        double[][] input_e4={{(double).04885,(double).00234},{(double).01,(double).02,(double).03, (double)0.1368217583, (double)0.0,(double) 0.4}};
        double[][] input_e5={{(double).04885,(double).00235},{(double).01,(double).02,(double).03, (double)0.1368217583, (double)0.0,(double) 0.5}};
        double[][] input_e6={{(double)0.07109307,(double)0.15218383},{(double).03,(double).04,(double).05,(double)0.1384370926,(double)0.3,(double) 0.1}};
        double[][] input_e7={{(double)0.07109307,(double)0.15218383},{(double).03,(double).04,(double).05,(double)0.1384374526,(double)0.3,(double) 0.2}};
        double[][] input_e8={{(double)0.07109307,(double)0.15218383},{(double).03,(double).04,(double).05,(double)0.1384378126,(double)0.3,(double) 0.3}};
        double[][] input_e9={{(double)0.07109307,(double)0.15218383},{(double).03,(double).04,(double).05,(double)0.1384381726,(double)0.3,(double) 0.4}};
        double[][] input_e10={{(double)0.0240355,(double)0.01537823},{(double)0.5,(double)0.6,(double)0.7,(double)1384385326,(double)0.2,(double) 0.1}};
        double[][] input_e11={{(double)0.0240355,(double)0.01537823},{(double)0.5,(double)0.6,(double)0.7,(double)1384388926,(double)0.2,(double) 0.2}};
        double[][] input_e12={{(double)0.0240355,(double)0.01537823},{(double)0.5,(double)0.6,(double)0.7,(double)(double)1384514926,(double)0.2,(double) 0.3}};

        System.out.println("learn_1");
        this.newEvent(input_e1);
        System.out.println("learn_2");
        this.newEvent(input_e2);
        System.out.println("learn_3");
        this.newEvent(input_e3);
        System.out.println("learn_4");
        this.newEvent(input_e4);
        System.out.println("learn_5");
        this.newTermination(input_e5);
        //this.seqLearner.print();
        this.seqLearner.resetCurSeq();
        this.seqLearner.resetActivityF1();
        this.seqLearner.resetBaseLineTho();
        
        System.out.println("learn_6");
        this.newEvent(input_e6);
        System.out.println("learn_7");
        this.newEvent(input_e7);
        System.out.println("learn_8");
        this.newEvent(input_e8);
        System.out.println("learn_9");
        this.newTermination(input_e9);
        //this.seqLearner.print();
        
        this.seqLearner.resetCurSeq();
        this.seqLearner.resetActivityF1();
        this.seqLearner.resetBaseLineTho();
        
        System.out.println("learn_10");
        this.newEvent(input_e10);
        System.out.println("learn_11");
        this.newEvent(input_e11);
        System.out.println("learn_12");
        this.newTermination(input_e12);
        //this.seqLearner.print();
        this.seqLearner.resetCurSeq();
        this.seqLearner.resetActivityF1();
        this.seqLearner.resetBaseLineTho();
        //this.getPlayBack();
        
        
        /* System.out.println("TEST FOR TRAINING");
        int[] learnSeq=this.newTrainEvent(input_e10,1);
        if(learnSeq==null){
            System.out.println("Cant learn sequence");
        }
        else{
            for(int i=0;i<learnSeq.length;i++){
                System.out.print(learnSeq[i]+"\t");
            }
            System.out.println("");
        }*/
        
        //this.printEposidic();
        
       /* System.out.println("train_event6...");
        int[] learnSeq=this.newTrainEvent(input_e11,1);
        if(learnSeq==null){
            System.out.println("Cant learn sequence");
        }
        else{
            for(int i=0;i<learnSeq.length;i++){
                System.out.print(learnSeq[i]+"\t");
            }
            System.out.println("");
        }*/
        
        //this.saveEpisodiceModel("event_learner.ext", "seq_learner.ext");
        
        //this.loadEpisodicModel("event_learner.ext", "seq_learner.ext");
        
        //this.printEposidic();

    }
    public String getMemory(String input)
    {
    if (input=="DUBAI - UNITED ARAB EMIRATES")
    {
    return "Fri, 10 May 2013 20:26:23 GMT - I was with Pierre, Enric and Federico - Dubai, United Arab Emirates - It was really good.";
    }
    else if (input=="SINGAPORE")
    {
    return "Wed, 13 Nov 2013 19:28:46 GMT - I was with Federico, Emilie and Danielle - Singapore, Singapore - It was bad.";
    }
    else if (input=="PARIS, FRANCE")
    {
    return "Wed, 15 Nov 2013 23:28:46 GMT - I was with Danielle, Kota and Tetsu in - Paris , France - It was not good or bad.";
    }
    else return "";
    }
    protected void initSeqPara(double b, double a, double g, double r){
        seqLearner.initParameters(b, a, g, r);
    }
    
    protected void initEventPara(int ns, int[] le, double b[], double[] a, double[] g, double[] r){
        eventLearner.initParameters(ns, le, b, a, g, r);   
    }
        /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


    protected void newEvent(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART); 
        
        if(winner<0){
            System.out.println("!!!error @ maximum capacity!!!");
            System.exit(0);
        }
        
        System.out.println("winner@event="+winner);
        boolean newEOccurred=eventLearner.newCode[winner];
        seqLearner.newEvent(winner);
        eventLearner.doLearn(winner, eventLearner.FUZZYART,0.5);
    }
    
    protected int newEventDebug(double[][] event){
        int winner=eventLearner.findWinner(event, eventLearner.FUZZYART);
        int debugM;
        
        if(winner<0){
            System.out.println("!!!error @ maximum capacity!!!");
            System.exit(0);
        }
        
        System.out.println("winner@event="+winner);
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
        
        System.out.println("winner@event="+winner);
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
        System.out.println("find_winner="+winner);
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
    
    public String testMethod(String input) throws IOException{
    
        double[][] input_e1={{(double).04885,(double).00235},{(double).02,(double).02,(double).03, (double)0.1368217583, (double)0.0, (double) 0.1}};

        System.out.println("learn_1");
        this.newEvent(input_e1);
        String storyString = null;

        this.seqLearner.seqToActivityF1();
        //this.printEposidic();
        //int swin = this.seqLearner.findWinnerFromF1();
       // this.seqLearner.activityF1Print();
        int swin = this.seqLearner.WinnerFromF1();
        //swin = this.seqLearner.findWinner(this.seqLearner.activityF1, 0);
        if(swin>=0){
            System.out.println("Winner is sequence:");
            this.seqLearner.readOutSeq(swin);
            this.seqLearner.activityF1ToCurSeq();
            System.out.println("curSeq: " + this.seqLearner.curSeq);
            System.out.println("curSeqWeight" + this.seqLearner.curSeqWeight);
            double stho = 1.1;
            int wn = -1;
            System.out.println("-----------------------");
            System.out.println("Recalling from partial episodes:");
            int index =-1;
            while(stho>0){
               
                //wn = this.getNextIdxSeq(stho);
                index++;
                wn=this.seqLearner.curSeq.get(index);
                stho = this.getNextMaxSeq(stho);
                System.out.println(stho);
                System.out.println("seq win: " + wn + " stho: " + stho);
                if(wn>=0){
                    String plist = "";
                    double lat = 0,lng = 0;
                    long timestamp = 0;
                    int emotional_state = 0;
                    int verb = 0;
                    double[][] ifield = this.eventLearner.getAllOutput(wn);
                    MemoryEvent e = new MemoryEvent(ifield,wn);
                    if (index==0)
                    storyString = e.getStory();
                    if (this.getNextMaxSeq(stho)==0.0)
                    {  System.out.println("-----------------------\n"+storyString+"\n-----------------------");
                        break;}
                }
            }

        } else {
            System.out.println("No Winner!");
        }
        return storyString;

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
    
   
    
    public static void main(String[] args) throws IOException{
        EpisodicModel m = new EpisodicModel();
    }
    
    protected Vector<Event> getSeqEvents(int[] seq){
        Vector<Event> eList=new Vector<Event>(0);
        
        for(int i=0;i<seq.length;i++){
            eList.add(Event.getEvent(eventLearner.weight[seq[i]]));
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
     private static StringBuilder inputStreamToString(InputStream is) throws IOException {
    String line = "";
    StringBuilder total = new StringBuilder();
    
    // Wrap a BufferedReader around the InputStream
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

    // Read response until the end
    while ((line = rd.readLine()) != null) { 
        total.append(line); 
    }
    
    // Return full string
    return total;
}
}