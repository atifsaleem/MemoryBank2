/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
@WebServlet(urlPatterns = {"/EpisodicModel"})
public class EpisodicModel extends HttpServlet{
    protected FusionARTGEncoding seqLearner;
    protected FusionART eventLearner;
    protected final Geocoder geocoder = new Geocoder();
   
    //protected String filePath="C:\\Documents and Settings\\User\\Desktop\\My Dropbox\\wenwen\\em-modified\\";
    protected String filePath="/Users/atifsaleem/NetBeansProjects/Falcon_14_05_10_atif/data";
    private double[][] input_e1;
    //protected String filePath= System.getProperty("user.dir")+"\\";
    private int acitivityVal;
    
    public EpisodicModel() throws IOException, java.text.ParseException{
        seqLearner=new FusionARTGEncoding();
        eventLearner=new FusionART();
        /*
         * Location Encoding 
        
         */
        int[] le={4,14,6,2,2,22};
        double[] b={(double)0.2,(double)0.1,(double)0.1,(double)0.1,(double)0.2,(double)0.1};
        double[] a={(double)0.1,(double)0.1,(double)0.1,(double)0.1,(double)0.1,(double)0.1};
        double[] g={(double)1.0,(double)1.0,(double)1.0,(double)1.0,(double)1.0,(double)1.0};
        double[] r={(double)0.6,(double)1.0,(double)1.0,(double)1.0,(double)0.6,(double)1.0};
        int number = 0;
        this.initEventPara(6, le, b, a, g, r);
        
        this.initSeqPara(1.0, .1 , 1.0, 1.0);
        String filename = "/Applications/XAMPP/xamppfiles/htdocs/MemoryBank2/web/js/JSON.js";
        this.readFromFile(filename);
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
    @SuppressWarnings("empty-statement")
    protected void readFromFile(String filename)
    {
    	JSONParser parser = new JSONParser();
 
	try {
                Object obj = parser.parse(new FileReader(filename));
		JSONObject jsonObject = (JSONObject) obj;
               JSONArray memories = (JSONArray) jsonObject.get("memories");
               Iterator<String> iterator = memories.iterator();
		while (iterator.hasNext()) {                    
                System.out.println(iterator.next());
		String location = (String) jsonObject.get("location");
                String date = (String) jsonObject.get("date");
		String people = (String) jsonObject.get("people");
                String mood = (String) jsonObject.get("mood");
		String activity = (String) jsonObject.get("activity");
                String imagery = (String) jsonObject.get("path");
                GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("en").getGeocoderRequest();
                GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
                List<GeocoderResult> results = geocoderResponse.getResults();
                float latitude = results.get(0).getGeometry().getLocation().getLat().floatValue();
                float longitude = results.get(0).getGeometry().getLocation().getLng().floatValue();
                latitude = normalize(latitude);
                float latitudeComp = 1 - latitude;
                longitude = normalize(longitude);
                float longitudeComp = 1 - longitude;
                Date lFromDate1 = new Date();
                try{
                SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                lFromDate1 = datetimeFormatter1.parse(date);
                }
                catch (java.text.ParseException ex) {
                    Logger.getLogger(EpisodicModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally{
                try {
                    SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("MMM");
                    lFromDate1 = datetimeFormatter1.parse(date);
                }   catch (java.text.ParseException ex) {
                        Logger.getLogger(EpisodicModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                finally {
                                try {
                    SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy");
                    lFromDate1 = datetimeFormatter1.parse(date);
                }   catch (java.text.ParseException ex) {
                        Logger.getLogger(EpisodicModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 finally {
                    try {
                    SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd");
                    lFromDate1 = datetimeFormatter1.parse(date);
                    }
                    catch (java.text.ParseException ex)
                    {
                    Logger.getLogger(EpisodicModel.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                }
                }
                }
                
                Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
                long timestamp = fromTS1.getTime();
                float time  = normalize((float)timestamp);
                float timeComp = 1 - time;
                people = people.toUpperCase();
                String[] peopleSplit = people.split(",");
                mood = mood.toUpperCase();
               String[] moodSplit = mood.split(",");
                activity = activity.toUpperCase();
                 String[] activitySplit = activity.split(",");
                Set<Integer> keys = MemoryEvent.getKeysByValue(MemoryEvent.symlinks, imagery);
                int key = new ArrayList<Integer>(keys).get(0);
                float img = (float) key;
                img = normalize(img);
                float imgComp = 1-img;
                for (int i =0; i < peopleSplit.length;i++)
                {
                    double[][] input = {{latitude,latitudeComp,longitude,longitudeComp},{0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0},{0.0,1.0,0.0,1.0,0.0,1.0},{img,imgComp},{time,timeComp},{0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0}};
                    String ppl = peopleSplit[i];
                    int peopleVal = MemoryEvent.peopleList.valueOf(ppl).ordinal();
                    input[1][peopleVal]=1.0;
                    input[1][peopleVal+1]=0.0;
                    String md = moodSplit[i];
                    int moodVal = MemoryEvent.moodList.valueOf(md).ordinal();
                    input[2][moodVal]=1.0;
                    input[2][moodVal+1]=0.0;
                    String act = activitySplit[i];
                    int activityVal = MemoryEvent.activityList.valueOf(activity).ordinal();
                    input[5][activityVal]=1.0;
                    input[5][activityVal+1]=0.0;
                    if (i!=peopleSplit.length-1)
                    this.newEvent(input);
                    else 
                    {
                    this.newTermination(input);
                    this.seqLearner.resetCurSeq();
                    this.seqLearner.resetActivityF1();
                    this.seqLearner.resetBaseLineTho();
                    }
                }
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
 
     }
    public String searchAndRetrieve(String q, int[] weights, int wander) throws java.text.ParseException{
    JSONObject query = this.stringToQuery(q);
    JSONArray memories = new JSONArray();
    ArrayList<MemoryEvent> wanderList = new ArrayList();
    String location = (String) query.get("location");
    String date = (String) query.get("date");
    String people = (String) query.get("people");
    String mood = (String) query.get("mood");
    String activity = (String) query.get("activity");
    
    GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("en").getGeocoderRequest();
    GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
    List<GeocoderResult> results = geocoderResponse.getResults();
    float latitude = results.get(0).getGeometry().getLocation().getLat().floatValue();
    float longitude = results.get(0).getGeometry().getLocation().getLng().floatValue();
    latitude = normalize(latitude);
    longitude = normalize(longitude);
    SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd-MMM-yyyy");
    Date lFromDate1 = datetimeFormatter1.parse(date);
    Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
    MemoryEvent cue = new MemoryEvent(latitude,longitude,fromTS1,people,mood,activity); 
    double[] gamma = new double[weights.length];
    for(int i=0; i<weights.length; i++) {
    gamma[i] = weights[i]/100;
    }
    this.eventLearner.setContribution(gamma);
    getMemories(cue,memories,wanderList);
    if (wander==2)
    {
    wander--;
    getMemories(wanderList.get(0),memories,wanderList);
    }
    if (wander==1)
    {
    wander--;
    getMemories(wanderList.get(1),memories,wanderList);
    }
    return memories.toJSONString();
    }
    
    protected JSONObject stringToQuery(String q){
    String[] s = q.split(",");
    JSONObject temp = new JSONObject();
    for (int i=0;i<s.length;i++)
    {
    String[] keyvalue = s[i].split(":");
    String key = keyvalue[0];
    String value = keyvalue[1];
    temp.put(key, value);
    }
    return temp;
    }
    protected void getMemories(MemoryEvent event,JSONArray memories,ArrayList<MemoryEvent> wanderList)
    {
    double[][] input = encodeMemory(event);
    JSONObject single = new JSONObject();
    boolean first = true;
    this.newEvent(input);
    LinkedHashMap<Integer,Double> winners = this.seqLearner.WinnerFromF1New();
    Iterator<Integer> it = winners.keySet().iterator();
    while (it.hasNext())
    {
    int swin = it.next().intValue();
    this.seqLearner.setActivityF2(swin,0);
    this.seqLearner.seqToActivityF1(); 
            if(swin!=0){
            this.seqLearner.readOutSeq(swin);
            this.seqLearner.activityF1ToCurSeq();
            double stho = 1.1;
            int wn = -1;
            int index =-1;
            while(stho>0){
            
                //wn = this.getNextIdxSeq(stho);
                index++;
                wn=this.seqLearner.curSeq.get(index);
                stho = this.getNextMaxSeq(stho);
                if(wn>=0){
                    double[][] ifield = this.eventLearner.getAllOutput(wn);
                    MemoryEvent e = decodeMemory(ifield); 
                    if (first)
                    {
                    wanderList.add(e);
                    first=false;
                    }
                    int imagery = (int) ifield[3][0];
                    Integer img = imagery;
                    String path = MemoryEvent.symlinks.get(img);
                    single.put("location", e.getLocationString());
                    single.put("date", e.getFormattedTime());
                    single.put("people",e.getPeople());
                    single.put("mood",e.getMood());
                    single.put("activity",e.getActivity());
                    single.put("path",path);
                   }
            }
     memories.add(index, event);
    }
    }
    }
    protected double[][] encodeMemory(MemoryEvent event)
    {
                double latitude = event.getCurLat();
                double longitude = event.getCurLong();
                latitude = normalize((float) latitude);
                float latitudeComp = (float) (1 - latitude);
                longitude = normalize((float) longitude);
                float longitudeComp = (float) (1 - longitude);
                long timestamp = event.getCurTime().getTime();
                float time  = normalize((float)timestamp);
                float timeComp = 1 - time;
                String people = event.getPeople();
                people = people.toUpperCase();
                String mood = event.getMood();
                mood = mood.toUpperCase();
                String activity = event.getActivity();
                activity = activity.toUpperCase();
                double[][] input = {{latitude,latitudeComp,longitude,longitudeComp},{0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0},{0.0,1.0,0.0,1.0,0.0,1.0},{0.0,1.0},{time,timeComp},{0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0,0.0,1.0}};
                int peopleVal = MemoryEvent.peopleList.valueOf(people).ordinal();
                int moodVal = MemoryEvent.moodList.valueOf(mood).ordinal();
                int activityVal = MemoryEvent.activityList.valueOf(activity).ordinal();
                input[1][peopleVal]=1.0;
                input[1][peopleVal+1]=0.0;
                input[2][moodVal]=1.0;
                input[2][moodVal+1]=0.0;
                input[5][activityVal]=1.0;
                input[5][activityVal+1]=0.0;
                return input;
    }
    protected MemoryEvent decodeMemory(double[][] input)
    {
    double normLat=input[0][1];
    double normLong=input[0][3];
    double latitude = inverse((float) normLat);
    double longitude = inverse((float) normLat);
    double timestamp = inverse((float) input[3][0]);
    Timestamp t = new Timestamp((long) timestamp);
    String people="";
    for (int i=0;i<14;i+=2)
    {
    if (input[1][i]==1.0)
    {
    people+=MemoryEvent.peopleList.values()[i];
    people+=",";
    }
    }
    String mood="";
    for (int i=0;i<6;i+=2)
    {
    if (input[2][i]==1.0)
    {
    mood+=MemoryEvent.moodList.values()[i];
    }
    }
    String activity="";
    for (int i=0;i<22;i+=2)
    {
    if (input[2][i]==1.0)
    {
    activity+=MemoryEvent.activityList.values()[i];
    }
    }
    MemoryEvent m = new MemoryEvent(latitude, longitude, t, people, mood, activity);
    return m;
    }
    private float normalize(float x)
    {
    return (float) (1 / (1 + Math.exp(-x)));
    }
    public float inverse(float x){

   return (float) (1/(1+(Math.exp(-x))));
   
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
    
   
    
    public static void main(String[] args) throws IOException, java.text.ParseException{
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