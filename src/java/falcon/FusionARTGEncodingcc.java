/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package falcon;

/**
 *
 * @author WA0003EN
 */
import java.util.Vector;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import java.text.NumberFormat;

public class FusionARTGEncodingcc{
    protected NumberFormat df = NumberFormat.getInstance();
	
    protected int capacity;
    //protected int numSpace;
    //protected int [] len;
    protected int numCode; //number of code on F2 (category) field
    protected double[][] activityF1;
    protected double[]   activityF2;
    protected double[][][] weight;
    protected double beta;
    protected double alpha;
    protected double gamma;
    protected double rho;			
	
    //protected boolean[] newCode;
    protected double[] confidence;
    protected double initConfidence;
    protected double reinforce_rate;
    protected double penalize_rate;
    protected double decay_rate;
    protected double threshold;
		
    final static int FUZZYART = 0;
    final static int ART2 = 1;
	
    final static double FRACTION = 0.00001;
    //final static double FRACTION = 0.01;
    final static double BASELINETHO = 1.0;
    
    protected double tho=BASELINETHO;
    protected int numF1=0; //number of nodes on F1 (input) field
    protected Vector<Double> curSeqWeight=new Vector<Double>(0);
    protected Vector<Integer> curSeq=new Vector<Integer>(0);
    //final static double v=0.005;
    final static double v=0.05;
    //final static int maxIdEvents=(int)((1/v)/5);
    final static int maxIdEvents = 2;
    
    final static boolean complcoding = false;
    
   public FusionARTGEncodingcc(){
        df.setMaximumFractionDigits (1);
	capacity = 99999;
	numCode = 1;
	initConfidence = 0.5;
	reinforce_rate = 0.5;
	penalize_rate = 0.2;
	decay_rate = 0.003;
	threshold = 0.01;
	confidence = new double[numCode];
        numCode = 0;
        
   }
    
    public void initParameters (  double b, double a, double g, double r){
        beta=b;
	alpha=a;
	gamma=g;
	rho=r;
        //len=new int[1];
    }
    
    public void newF1Code(){
        if(numCode==0){
            for(int i=0;i<curSeqWeight.size();i++){
                double decayedValue=curSeqWeight.get(i).doubleValue()*(1-v);
                curSeqWeight.remove(i);
                curSeqWeight.add(i, new Double(decayedValue));
            }
            
            curSeqWeight.add(new Double(1.0));
            curSeq.add(new Integer(numF1));
           // tho-=v;
            //if(tho<0) tho = 0;
            numF1++;

        }
        else{
            for(int i=0;i<curSeqWeight.size();i++){
                double decayedValue=curSeqWeight.get(i).doubleValue()*(1-v);
                curSeqWeight.remove(i);
                curSeqWeight.add(i, new Double(decayedValue));
            }
            
            curSeqWeight.add(new Double(1.0));
            curSeq.add(new Integer(numF1));
            //tho-=v;
            //if(tho<0) tho = 0;
            numF1++;
            
            double[][][] new_weight=new double[weight.length][][];           
            for(int i=0;i<weight.length;i++){
                new_weight[i]=new double[numF1][maxIdEvents];
                for(int j=0;j<weight[i].length;j++){       
                    new_weight[i][j]=new double[weight[i][j].length];
                    System.arraycopy(weight[i][j], 0, new_weight[i][j], 0, weight[i][j].length);
                }
                double[] new_empty=new double[maxIdEvents];
                //for(int j=0;j<maxIdEvents;j++){                    
                //    new_empty[j]=0;
                //}
                new_empty[0]=0;
                new_empty[1]=0;
                if(complcoding)
                    new_empty[1]=1-new_empty[0];
                System.arraycopy(new_empty, 0, new_weight[i][numF1-1], 0, new_empty.length);
            }
            weight=new_weight;
        }
    }
    
    public void newEvent(int eventIndex){
        if(eventIndex<numF1){
            for(int i=0;i<curSeqWeight.size();i++){
                double decayedValue=curSeqWeight.get(i).doubleValue()*(1-v);
                curSeqWeight.remove(i);
                curSeqWeight.add(i, new Double(decayedValue));
            }
            
            curSeqWeight.add(new Double(1.0));
            curSeq.add(new Integer(eventIndex));
            //tho-=v;
            //if(tho<0) tho = 0;
        }
        else{
            newF1Code();
        }
    }
    
    
    public void readOutSeq(int selCode){ //readout from F2 code to F1 activation
    //by Budhitama Subagdja
        resetActivityF1();
        if((selCode>=0)&&(selCode<weight.length)){
            for(int k=0;k<weight[selCode].length;k++){
                for(int i=0;i<weight[selCode][k].length;i++){
                    activityF1[k][i] = weight[selCode][k][i];
                }
            }
        }
    }
    
    public void activityF1ToCurSeq(){ //transfer values of activityF1 to curSeq
    //by Budhitama Subagdja
        resetCurSeq();
        tho = BASELINETHO;
        while(tho>0){
            addF1ValueToCurSeq(tho);
            tho -= v;
            if(tho<0) tho = 0;
        }
    }
    
    
    public void addF1ValueToCurSeq(double ltho){ //add the maximum value of activityF1 to curSeq
        double maxval = 0.0;
        int maxidx = -1;
        for(int i=0;i<numF1;i++){
            /*
            for(int j=0;j<maxIdEvents;j++){
                if((activityF1[i][j]>maxval)&&(activityF1[i][j]<=ltho)){
                    maxval = activityF1[i][j];
                    maxidx = i;
                }
            }
             */
            if((activityF1[i][0]>maxval)&&(activityF1[i][0]<=ltho)){
                maxval = activityF1[i][0];
                maxidx = i;
            }
        }      
        if(maxidx >= 0){
            curSeqWeight.add(maxval);
            curSeq.add(maxidx);
        }
    }

    public void resetBaseLineTho(){
    //by Budhitama Subagdja
        tho = BASELINETHO;
    }
    
    public void setBaseLineTho(double th){
    //by Budhitama Subagdja
        tho = th;
    }

    
    
    public void resetCurSeq(){ //reset current input sequence
    //by Budhitama Subagdja
        curSeqWeight=new Vector<Double>(0);
        curSeq=new Vector<Integer>(0);
    }
    
    public void resetActivityF1(){ //reset activityF1
    //by Budhitama Subagdja
        activityF1=new double[numF1][maxIdEvents];
        for(int i=0;i<numF1;i++){
            /*for(int j=0;j<maxIdEvents;j++){
                activityF1[i][j]=0.0;
            }*/
            activityF1[i][0]=0.0;
            activityF1[i][1]=0.0;
            if(complcoding)
                activityF1[i][1]=1-activityF1[i][0];
        }
    }
    
    

    public void activityF1Print(){ //printing the values of activityF1
    //by Budhitama Subagdja    
        System.out.println("Activity at F1");
        for(int i=0;i<activityF1.length;i++){
          for(int j=0;j<activityF1[i].length;j++){
            System.out.print(activityF1[i][j]+"\t");
          }
          System.out.println("");
        }
    }
    
    
    public void seqToActivityF1(){ //transfering the sequence (curSeq) to F1 field
    //by Budhitama Subagdja    
        resetActivityF1();
        int[] curLoc=new int[numF1];
        for(int i=0;i<curLoc.length;i++){
            curLoc[0]=0;
        }

        for(int i=0; i<curSeq.size(); i++){
            int curEvent=curSeq.get(i).intValue();
            //activityF1[curEvent][curLoc[curEvent]]=curSeqWeight.get(i).doubleValue();
            //curLoc[curEvent]++;
            activityF1[curEvent][0]=curSeqWeight.get(i).doubleValue();
            activityF1[curEvent][1] = 0;
            
           // System.out.print("("+curEvent+", "+curSeqWeight.get(i).doubleValue()+") ");
            
            if(complcoding)
                activityF1[curEvent][1]=1-activityF1[curEvent][0];
        }
       // System.out.println("");
        
    }
    
    public int sequenceEndWinner(){
    //by Budhitama Subagdja
        if(numCode>0){
            seqToActivityF1();
            
            activityF2=new double[numCode];
            
            int winner=findWinner(activityF1,FUZZYART);
            if(winner>=0){
                System.out.println("winner="+winner);
                doLearn(winner, FUZZYART,false);
                return winner;
            }
            else{
                //System.out.println("New sequence 1");
                newSequence();
                return numCode-1;
            }
        }
        else{
            //System.out.println("New sequence 2");
            newSequence();
            return numCode-1;
        }        
    }

    public int sequenceRecWinner(){
    //by Budhitama Subagdja
        if(numCode>0){
            seqToActivityF1();
            
            activityF2=new double[numCode];
            
            int winner=findWinner(activityF1,FUZZYART);
            if(winner>=0){
                //System.out.println("winner="+winner);
                //doLearn(winner, FUZZYART,false);
                return winner;
            }
            else{
                //newSequence();
                //return numCode;
                return -1;
            }
        }
        else{
            //newSequence();
            //return numCode;
            return -1;
        }        
    }

    
    
    
    public void sequenceEnd(){
    //modified by Budhitama Subagdja
        //System.out.println("one seq end...");
        if(numCode>0){
            seqToActivityF1();
            
            activityF2=new double[numCode];
            
            int winner=findWinner(activityF1,FUZZYART);
            if(winner>=0){
                System.out.println("winner="+winner);
                doLearn(winner, FUZZYART,false);
            }
            else{
                newSequence();
            }
        }
        else{
            newSequence();
        }
    }
    
    public void newSequence(){
        numCode++;
        if(numCode>1){
            double[][][] new_weight=new double[numCode][][];
            
            //get new code
            double[][] input=new double[numF1][maxIdEvents];
            for(int i=0;i<numF1;i++){
                /*for(int j=0;j<maxIdEvents;j++){
                    input[i][j]=0.0;
                }*/
                input[i][0]=0.0;
                input[i][1]=0.0;
                if(complcoding)
                    input[i][1]=1-input[i][0];
            }
            /*int[] curLoc=new int[numF1];
            for(int i=0;i<curLoc.length;i++){
                curLoc[0]=0;
            }*/
            
            for(int i=0; i<curSeq.size(); i++){
                int curEvent=curSeq.get(i).intValue();
                input[curEvent][0]=curSeqWeight.get(i).doubleValue();
                input[curEvent][1]=0.0;
                if(complcoding)
                    input[curEvent][1]=1-input[curEvent][0];
                //curLoc[curEvent]++;
            }
            
            //expand weights
            for(int i=0;i<weight.length;i++){
                new_weight[i]=new double[numF1][];
                for(int j=0;j<weight[i].length;j++){
                    new_weight[i][j]=new double[maxIdEvents];
                    System.arraycopy(weight[i][j], 0, new_weight[i][j], 0, weight[i][j].length);
                }
            }
            new_weight[numCode-1]=new double[numF1][maxIdEvents];
            for(int i=0;i<numF1;i++){
                System.arraycopy(input[i], 0, new_weight[numCode-1][i], 0, input[i].length);
            }
            weight=new_weight;
        
            double[] new_confidence=new double[numCode];
        
            System.arraycopy(confidence, 0, new_confidence, 0, confidence.length);
            new_confidence[confidence.length]=initConfidence;
    	    confidence = new_confidence;
        }
        else{
            weight=new double[1][numF1][maxIdEvents];
            confidence=new double[1];
            
            double[][] input=new double[numF1][maxIdEvents];
            for(int i=0;i<numF1;i++){
                /*for(int j=0;j<maxIdEvents;j++){
                    input[i][j]=0.0;
                }*/
                input[i][0]=0.0;
                input[i][1]=0.0;
                if(complcoding)
                    input[i][1]=1-input[i][0];
            }
            int[] curLoc=new int[numF1];
            for(int i=0;i<curLoc.length;i++){
                curLoc[0]=0;
            }
            
            for(int i=0; i<curSeq.size(); i++){
                int curEvent=curSeq.get(i).intValue();
                //input[curEvent][curLoc[curEvent]]=curSeqWeight.get(i).doubleValue();
                //curLoc[curEvent]++;
                input[curEvent][0]=curSeqWeight.get(i).doubleValue();
                input[curEvent][1]=0.0;
                if(complcoding)
                    input[curEvent][1]=1-input[curEvent][0];
            }
            
            for(int i=0;i<numF1;i++){
                System.arraycopy(input[i], 0, weight[0][i], 0, input[i].length);
            }
            
            //System.arraycopy(input[0], 0, weight[0][0], 0, input[0].length);
            confidence[0]=initConfidence;
        }
    }
    
    public void computeChoice (int ART_MODE) {
    	double top, bottom;
    	
    	if (ART_MODE == FUZZYART) {
           // System.out.println("Phase code T computationation FUZZYART");
    		for (int j=0; j<numCode; j++) {
    			activityF2[j] = 0;
                        top = 0;
    			bottom = alpha;
    			for (int k=0; k<numF1; k++) {
    				for (int i=0; i<maxIdEvents; i++) {
    					top += Math.min(activityF1[k][i],weight[j][k][i]);
    					bottom += weight[j][k][i];
    				}
    				
    			}
                        activityF2[j] += gamma * (top/bottom);
                       // System.out.println("Code"+j+"_T="+activityF2[j]);
                        
    		}
    	}
    	
    	else if (ART_MODE == ART2) {
           // System.out.println("Phase code T computationation ART2");
    		for (int j=0; j<numCode; j++) {
    			activityF2[j] = 0;
                        top = 0;
    			for (int k=0; k<numF1; k++) {
    				
    				for (int i=0; i<maxIdEvents; i++)
    					top += activityF1[k][i] * weight[j][k][i];
    				
    			}
                        top /= ((double)(maxIdEvents*numF1));
    			activityF2[j] += top;
                        //System.out.println("Code"+j+"_T="+activityF2[j]);
    		}
    	}
    }
    
    public int codeCompetition () {
    	double max_act = 0;
    	int c = -1;
    	
        //System.out.println("Phase code competition");
    	for (int j=0; j<numCode; j++) {
    		if (activityF2[j] > max_act) {
    			max_act = activityF2[j];
    			c = j;
    		}
    	}
    	
        //System.out.println("Result ="+c);
        
    	return c;
    }
    
    public Vector<Integer> codeCompetitionPartial () {
    	double max_act = 0;
        Vector<Integer> maxList=new Vector<Integer>(0);
    	//int c = -1;
    	
        //System.out.println("Phase code competition");
    	for (int j=0; j<numCode; j++) {
    		if (activityF2[j] > max_act) {
                       maxList.removeAllElements();
    			max_act = activityF2[j];
                        maxList.add(new Integer(j));
    		}
                else if (activityF2[j] == max_act) {
                        maxList.add(new Integer(j));
    		}
    	}
    	
        //System.out.println("Result ="+c);
        
    	return maxList;
    }
    
    public double computeMatch (int j) {
    	double m = 0;
    	double denominator = 0;
    	
    	for(int k=0;k<numF1;k++){
    	   for (int i=0; i<maxIdEvents; i++) {
                //System.out.print("weight_"+i+"="+weight[j][k][i]);
                //System.out.println("input_"+i+"="+activityF1[k][i]);
    		m += Math.min(activityF1[k][i],weight[j][k][i]);
    		denominator += activityF1[k][i];
    	    } 
        }
    		
    	if (denominator == 0)
    		return 1;
        //System.out.println("Phase Code Mathing");
        //System.out.println("match_computed="+m/denominator);
    	return (m/denominator);
    }
    
    public boolean resonanceOccur (int j, double temp_rho) {
      boolean violated = false;
    	
    		if (computeMatch(j) < temp_rho) {
    			//temp_rho[k] = Math.min(computeMatch(j,k) + FRACTION, 1);
    			violated = true;
    		}
        
        if(violated){
            boolean end=false;
            while(end){  
                if(temp_rho==1){
                     end=true;
                     break;
                 }  
    		     temp_rho = Math.min(temp_rho + FRACTION, 1);
    	         
                 		     
    		     if(computeMatch(j) >= temp_rho){
                         end=false;
                     }
            }
        }   	
    	return !violated;
        
        /*boolean violated = false;
    	
    		if ( computeMatch(j) < temp_rho) {
    			temp_rho = Math.min(computeMatch(j) + FRACTION, 1);
    			violated = true;
                }
    	return violated;*/
    }
    
    public void doLearn (int j, int ART_MODE, boolean newCreat) {
    	double rate;
    	
    	if ( numCode < capacity+1) {
    		for (int k=0; k<numF1; k++) {
                    if (newCreat) rate = 1;
                    else rate = beta;
    			for (int i=0; i<maxIdEvents; i++) {
    				if (ART_MODE == FUZZYART)
    					weight[j][k][i] = (1-rate)*weight[j][k][i]+rate*Math.min(weight[j][k][i],activityF1[k][i]);
    				else if (ART_MODE == ART2)
    					weight[j][k][i] = (1-rate)*weight[j][k][i]+rate*activityF1[k][i];
    			}
    		}
    	}
    }
    
    
    public int WinnerFromF1 () {
        int winner = -1;
        if(numCode>0){
            seqToActivityF1();
            activityF2=new double[numCode];
            
            winner=findWinner(activityF1,FUZZYART);
        }
        return winner;
    }    
    
    public int findWinner (double[][] input, int ART_MODE) {
    	int winner = -1;
    	double temp_rho;
        temp_rho = rho;
    	
    	this.setActivityF1(input);
    	this.computeChoice(ART_MODE);
    	
    	do {
    		winner = this.codeCompetition();
    		if (winner<0) break;    		
    		
    		if (!this.resonanceOccur(winner,temp_rho)) {
    		    //System.out.println("!!!Resonance Occur----Loop again");	
                    this.setActivityF2(winner,0);
    		}
    		else
    			break;
    	}while (true);
    	
    	return winner;
    }
    
    public int findWinnerPartial () {
    	int winner = -1;
    	double temp_rho =rho;
    	
        double[][] input=new double[numF1][maxIdEvents];
            for(int i=0;i<numF1;i++){
                for(int j=0;j<maxIdEvents;j++){
                    input[i][j]=0.0;
                }
            }
            int[] curLoc=new int[numF1];
            for(int i=0;i<curLoc.length;i++){
                curLoc[i]=0;
            }
            
            //System.out.println("print the curSeqWeight");
            
            for(int i=0; i<curSeq.size(); i++){
                //System.out.println("+++++++Sequence size: "+curSeq.size());
                int curEvent=curSeq.get(i).intValue();
                input[curEvent][curLoc[curEvent]]=curSeqWeight.get(i).doubleValue();
                curLoc[curEvent]++;
                //System.out.print(curSeqWeight.get(i).doubleValue()+"\t");
            }
            //System.out.println("");
            
            //System.out.println("print the curSeq");
            
            //for(int i=0; i<curSeq.size(); i++){
              //  System.out.print(curSeq.get(i).intValue()+"\t");
            //}
            /*System.out.println("");
            
            System.out.println("Print train sequence");
            for(int i=0;i<input.length;i++){
                for(int j=0;j<input[i].length;j++){
                    System.out.print(input[i][j]+"\t");
                }
                System.out.println("");
            }
            System.out.println("");*/
            
    	this.setActivityF1(input);
    	this.computeChoicePartial();
        
        int loop=0;
    	
    	do {
                loop++;
                
                Vector<Integer> winners=this.codeCompetitionPartial();
                
                System.out.println("One Code Competition Partial # of winner="+winners.size());
                
                if(winners.size()>1) return -2;
                
                if(winners.size()==0) return -1;
                
    		winner = winners.firstElement().intValue();
                
    		if (loop>numCode||winner<0) { return -1;}    	
                
                /*System.out.println("Print winner sequence");
                for(int i=0;i<weight[winner].length;i++){
                    for(int j=0;j<this.maxIdEvents;j++){
                        System.out.print(input[i][j]+"\t");
                    }
                    System.out.println("");
                }
                System.out.println("");*/
    		
    		if (this.resonanceOccur(winner,temp_rho)) {
    			this.setActivityF2(winner,0);
    		}
    		else
    			break;
    	}while (true);
    	
    	return winner;
    }
    
    public void computeChoicePartial () {
    	double top, bottom;
        activityF2=new double[numCode];

        for (int j=0; j<numCode; j++) {
    		activityF2[j] = 0;
                top = 0;
    		bottom = alpha;
                double[][] eventOrder=findPartialSeq(j);
                
                //System.out.println("Get partial seq pattern for rule "+j);
                if(eventOrder==null){
                    //System.out.println("Recorded sequ is shorter than cur event order");
                    activityF2[j]=0;
                    continue;
                }
                else{
                    /*for(int m=0;m<eventOrder.length;m++){
                        for(int n=0;n<eventOrder[m].length;n++){
                            System.out.print(eventOrder[m][n]);
                        }
                        System.out.println("");
                    }
                    System.out.println("");*/
                }
                
    		    for (int k=0; k<numF1; k++) {
                        
                        
    			for (int i=0; i<this.maxIdEvents; i++) {
    				top += Math.min(activityF1[k][i],eventOrder[k][i]);
    				bottom += eventOrder[k][i];
    			}
    			
    		    }
                    activityF2[j] += gamma * (top/bottom);
    		}
    }
    
    
   
    
    public double[][] findPartialSeq(int codeId){
        int pSize=curSeq.size();
        double[][] curCode=new double[numF1][];
        for(int i=0;i<curCode.length;i++){
            curCode[i]=new double[this.maxIdEvents];
            System.arraycopy(weight[codeId][i], 0, curCode[i], 0, this.maxIdEvents);
        }
        //System.arraycopy(weight[codeId][0], 0, curCode, 0, weight[codeId][0].length);
        double[][] eventOrder=new double[numF1][this.maxIdEvents];
        
        double maxWeight=0;
        int maxEvent_1=-1;
        int maxEvent_2=-1;
        for(int i=0;i<pSize;i++){
            for(int j=0;j<curCode.length;j++){
                for(int k=0;k<maxIdEvents;k++){
                    if(maxWeight<curCode[j][k]){
                       maxEvent_1=j;
                       maxEvent_2=k;
                       maxWeight=curCode[j][k];
                    }
                }
            }
            if(maxEvent_1<0){
                return null;
            }
            eventOrder[maxEvent_1][maxEvent_2]=maxWeight;
            curCode[maxEvent_1][maxEvent_2]=0;
            maxEvent_1=-1;
            maxEvent_2=-1;
            maxWeight=0;
        }
        return eventOrder;
    }
    
    public int[] oneTrainIn(){
        /*if(eventIndex>numF1){
            //System.out.println("!!!Error @ an unexperienced event RECORDED BY SEQUENCE LEARNER!!!");
            return null;
        }
        
        curSeqWeight.add(new Double(tho));
        curSeq.add(new Integer(eventIndex));
        tho-=v;*/
        
        /*if(curSeq.size()<startIndex){
            return null;
        }*/
        
        int winner=findWinnerPartial();
        
        if(winner==-2){
            return new int[]{-1};
        }    
        
        if(winner==-1){
            this.emptyAccumulated();
            return null;
        }
                
        double[][] winnerCode=new double[numF1][maxIdEvents];
        for(int i=0;i<numF1;i++){
             System.arraycopy(weight[winner][i],0, winnerCode[i], 0, maxIdEvents);
        }
        
       /* System.out.println("Print won weights---"+winner);
        for(int i=0;i<weight[winner].length;i++){
            for(int j=0;j<weight[winner][i].length;j++){
                System.out.print(weight[winner][i][j]+"\t");
            }
            System.out.println("");
        }
        System.out.println("");*/
        
        Vector<Integer> winnerSeq=new Vector<Integer>(0);
        double maxWeight=0;
        int maxEvent_1=-1;
        int maxEvent_2=-1;
        do{
            for(int i=0;i<numF1;i++){
                for(int j=0;j<this.maxIdEvents;j++){
                   if(maxWeight<winnerCode[i][j]){
                       maxWeight=winnerCode[i][j];
                       maxEvent_1=i;
                       maxEvent_2=j;
                   } 
                }
            }
            if(maxWeight>0){
                winnerSeq.add(new Integer(maxEvent_1));
                winnerCode[maxEvent_1][maxEvent_2]=0;
                maxWeight=0;
                maxEvent_1=-1;
                maxEvent_2=-1;
            }
            else{
                break;
            }
        }while(true);
        
        int[] learnSeq=new int[winnerSeq.size()];
        for(int i=0;i<learnSeq.length;i++){
            learnSeq[i]=winnerSeq.get(i).intValue();
        }
        emptyAccumulated();
        return learnSeq;
    }
    
    public void emptyAccumulated(){
        curSeqWeight=new Vector<Double>(0);
        curSeq=new Vector<Integer>(0);
        tho=1;
        activityF1=new double[numF1][maxIdEvents];
    }
    
    public boolean oneTrainAccumulated(int eventIndex){
        if(eventIndex>numF1){
            System.out.println("!!!Error @ an unexperienced event RECORDED BY SEQUENCE LEARNER!!!");
            return false;
        }
        
        for(int i=0;i<curSeqWeight.size();i++){
                double decayedValue=curSeqWeight.get(i).doubleValue()*(1-v);
                curSeqWeight.remove(i);
                curSeqWeight.add(i, new Double(decayedValue));
         }
        
        curSeqWeight.add(new Double(1.0));
        curSeq.add(new Integer(eventIndex));
        //tho-=v;
        if(tho<0) tho = 0;
        return true;
    }
    
    public void saveCode (FileWriter fw) {
    	try{
    		String str;
    		str=NumberFormat.getInstance().format(numCode);
    		fw.write(str+"\n");
                String str2;
    		str2=NumberFormat.getInstance().format(numF1);
    		fw.write(str2+"\n");
    		for (int j=0; j<numCode; j++) {
    			str="";
    			for (int k=0; k<numF1; k++) {
    				for (int i=0; i<this.maxIdEvents; i++) {
    					str=str+NumberFormat.getInstance().format(weight[j][k][i])+" ";
    				}
                                str = str + "\n";
    			}
                        str = str + "\n";
    			str = str  + NumberFormat.getInstance().format(confidence[j]) + " ";
    			fw.write(str+"\n");
    		}
    		fw.close();
    	}
    	catch (Exception e){
    		System.out.println("Cannot save!!!");
    	}
    }
    
     public void loadCode (FileReader fr) {
    	try{
    		Scanner sc=new Scanner(fr);
    		numCode=sc.nextInt();
    		weight=new double [numCode][][];
    		activityF2 = new double [numCode];
                
    		confidence = new double [numCode];
                numF1=sc.nextInt();
                activityF1 = new double [numF1][this.maxIdEvents];
    		for (int j=0; j<numCode; j++) {
    			weight[j]=new double [numF1][];
    			for (int k=0; k<numF1; k++) {
    				weight[j][k]=new double[this.maxIdEvents];
    				for (int i=0; i<this.maxIdEvents;i++) {
    					weight[j][k][i]=sc.nextDouble();
    				}
    			}
    			confidence[j] = sc.nextDouble();
    		}
    	}catch (Exception e){
                e.printStackTrace();
    		System.out.println("Cannot load!!!");
    	}    	
    }
    
   public void setActivityF1 (double[][] input) {
    	for (int k=0; k<numF1; k++) 
    		for (int i=0; i<maxIdEvents; i++) 
    			activityF1[k][i] = input[k][i];
    }
    
    public void setActivityF2 (int j, double value){
    	activityF2[j] = value;
    }
    
    
    public static void main(String[] args){
        FusionARTGEncodingcc f=new FusionARTGEncodingcc();
        f.initParameters(1.0,0.1 , 1.0, 0.6);
        
        /*f.weight=new double[2][1][5];
        for(int i=0;i<f.weight.length;i++){
            for(int j=0;j<f.weight[i].length;j++){
                for(int k=0;k<f.weight[i][j].length;k++){
                    f.weight[i][j][k]=0.5;
                }
            }
        }
        f.numCode=2;
        
        f.newF1Code();
        f.newF1Code();
        for(int i=0;i<f.weight.length;i++){
            for(int j=0;j<f.weight[i].length;j++){
                for(int k=0;k<f.weight[i][j].length;k++){
                    System.out.print(f.weight[i][j][k]+"\t");
                }
                System.out.println("");
            }
        }*/
        
        f.newEvent(0);
        f.newEvent(1);
        f.newEvent(2);
        f.newEvent(3);
        f.newEvent(4);
        f.seqToActivityF1();
        f.activityF1Print();
        f.sequenceEnd();
        f.print();
        System.out.println("Contain testing:");
        int tn = 7;
        if(f.curSeq.contains(tn)){
            System.out.println("curSeq contains " + tn);
        } else {
            System.out.println("curSeq doesn't contain " + tn);
        }
        f.resetCurSeq();
        f.resetActivityF1();
        f.tho=1;


        f.newEvent(5);
        f.newEvent(6);
        f.newEvent(7);
        f.newEvent(8);
        f.newEvent(9);
        f.seqToActivityF1();
        f.activityF1Print();      
        f.sequenceEnd();
        f.print();
        f.resetCurSeq();
        f.resetActivityF1();
        f.tho=1;
        
        /*
        f.newEvent(0);
        f.newEvent(4);
        f.newEvent(2);
        f.newEvent(3);
        f.newEvent(4);
        f.seqToActivityF1();
        f.activityF1Print();      
        f.sequenceEnd();
        f.print();
        f.resetCurSeq();
        f.resetActivityF1();
        f.tho=1;
        */
        
        /*
        f.newEvent(0);
        f.newEvent(1);
        f.newEvent(2);
        f.newEvent(3);
        f.newEvent(4);
        f.seqToActivityF1();
        f.activityF1Print();      
        f.sequenceEnd();
        f.print();
        f.resetCurSeq();
        f.resetActivityF1();
        f.tho=1;
        */
        
        /*
        f.newEvent(2);
        f.newEvent(2);
        f.newEvent(2);
        f.newEvent(2);
        f.newEvent(2);
        f.seqToActivityF1();
        f.activityF1Print();     
        f.sequenceEnd();
        f.print();
        f.resetCurSeq();
        f.resetActivityF1();
        f.tho=1;
        */
    
        f.newEvent(7);
        //f.newEvent(3);
        //f.newEvent(4);
        //f.newEvent(8);
        //f.newEvent(4);
        
        f.seqToActivityF1();
        f.activityF1Print();
        f.sequenceEnd();
        f.print();
        System.out.println("The winner is: " + f.WinnerFromF1());
        
        
        /*
        f.readOutSeq(0);
        System.out.println("Read out result, code 0:");
        f.activityF1Print();

        f.readOutSeq(1);
        System.out.println("Read out result, code 1:");
        f.activityF1Print();

        f.readOutSeq(2);
        System.out.println("Read out result, code 2:");
        f.activityF1Print();
        f.activityF1ToCurSeq();
        System.out.println("curSeq: " + f.curSeq);
        System.out.println("curSeqWeight" + f.curSeqWeight);
        */
        
                
        
        
        /*
        f.newEvent(0);
        f.newEvent(1);
        f.newEvent(2);
        f.newEvent(3);
        f.newEvent(4);
        f.sequenceEnd();
        f.print();
        
        f.newEvent(5);
        f.newEvent(6);
        f.newEvent(7);
        f.newEvent(8);
        f.sequenceEnd();
        f.print();
        
        f.newEvent(0);
        f.newEvent(4);
        f.newEvent(2);
        f.newEvent(3);
        f.newEvent(4);
        f.sequenceEnd();
        f.print();
        
        f.newEvent(2);
        f.newEvent(2);
        f.newEvent(2);
        f.newEvent(2);
        f.newEvent(2);
        f.sequenceEnd();
        f.print();
        */

        
        /*int[] seq=f.oneTrainIn(2,4);
        seq=f.oneTrainIn(2,4);
        seq=f.oneTrainIn(2,4);
        seq=f.oneTrainIn(2,4);*/
        
        /*if(seq==null){
            System.out.println("not recogonized!!!");
        }
        else{
            for(int i=0;i<seq.length;i++){
                System.out.print(seq[i]+"\t");
            }
            System.out.println("");
        }*/
        
        /*f.newEvent(0);
        f.newEvent(1);
        f.newEvent(2);
        f.newEvent(3);
        f.newEvent(4);
        f.sequenceEnd();
        
        
        f.newEvent(5);
        f.newEvent(6);
        f.newEvent(7);
        f.newEvent(8);
        f.sequenceEnd();
        
        f.newEvent(9);
        f.newEvent(10);
        f.newEvent(7);
        f.newEvent(8);
        f.sequenceEnd();
        
        int[] seq=f.oneTrainIn(10,1);
        
        if(seq==null){
            System.out.println("not recogonized!!!");
        }
        else{
            for(int i=0;i<seq.length;i++){
                System.out.print(seq[i]+"\t");
            }
            System.out.println("");
        }*/
    }
    
    public void print(){
        System.out.println("================");
        System.out.println("Sequence learner print");
        System.out.println("=================");

        System.out.println("Numeber of events recognized="+this.numF1);
        System.out.println("Number of sequences learnt="+this.numCode);
        
        for(int i=0;i<weight.length;i++){
            for(int j=0;j<weight[i].length;j++){
                for(int k=0;k<weight[i][j].length;k++){
                    System.out.print(weight[i][j][k]+"\t");
                }
                System.out.println("");
            }
            System.out.println("");
        }
    }
    
    //*****Code added by WWW after 02/12/09
    protected int[] getOneSeq(int node_index){
        double[][] winnerCode=new double[numF1][maxIdEvents];
        for(int i=0;i<numF1;i++){
             System.arraycopy(weight[node_index][i],0, winnerCode[i], 0, maxIdEvents);
        }
        
       /* for(int i=0;i<weight[node_index].length;i++){
            for(int j=0;j<weight[node_index][i].length;j++){
                System.out.print(weight[node_index][i][j]+"\t");
            }
            System.out.println("");
        }
        System.out.println("");*/
        
        Vector<Integer> winnerSeq=new Vector<Integer>(0);
        double maxWeight=0;
        int maxEvent_1=-1;
        int maxEvent_2=-1;
        do{
            for(int i=0;i<numF1;i++){
                for(int j=0;j<this.maxIdEvents;j++){
                   if(maxWeight<winnerCode[i][j]){
                       maxWeight=winnerCode[i][j];
                       maxEvent_1=i;
                       maxEvent_2=j;
                   } 
                }
            }
            if(maxWeight>0){
                winnerSeq.add(0,new Integer(maxEvent_1));
                winnerCode[maxEvent_1][maxEvent_2]=0;
                maxWeight=0;
                maxEvent_1=-1;
                maxEvent_2=-1;
            }
            else{
                break;
            }
        }while(true);
        
        int[] learnSeq=new int[winnerSeq.size()];
        for(int i=0;i<learnSeq.length;i++){
            learnSeq[i]=winnerSeq.get(i).intValue();
        }
        //emptyAccumulated();
        return learnSeq;
    }
    
    public void purgeEvent(int[] idx){
        if(idx.length==0){
            return;
        }
        
        System.out.println("PURGE: "+numF1+", "+idx.length);
        this.numF1=this.numF1-idx.length;
        double[][][] new_weight = new double[numCode][numF1][];
        
        for(int i=0;i<idx.length;i++){
            System.out.print(""+idx[i]+"\t");
        }
        System.out.println("");
        System.out.println("("+weight.length+","+weight[0].length+","+weight[0][0].length+")");
        
        /*for(int i=0;i<2;i++){
            for(int j=0;j<weight[i].length;j++){
                for(int k=0;k<weight[i][j].length;k++){
                    System.out.print(""+weight[i][j][k]+"\t");
                }
                //System.out.println("");
            }
            System.out.println("");
        }*/
        
        for(int i=0; i<numCode; i++){
                int k=0;
    		for (int j=0; j<weight[i].length; j++) {
                    boolean purged=false;
                    for(int x=0;x<idx.length;x++){
                        if(j==idx[x]){
                            purged=true;
                            break;
                        }
                    }
                    
    			if (!purged) {
    				new_weight[i][k] = weight[i][j];
    				k++;
    			}
    		}
        }        
        weight = new_weight;
        activityF1=new double[numF1][maxIdEvents];
        
        System.out.println("AFTER PURGE*******");
        
         System.out.println("("+weight.length+","+weight[0].length+","+weight[0][0].length+")");
        /*for(int i=0;i<2;i++){
            for(int j=0;j<weight[i].length;j++){
                for(int k=0;k<weight[i][j].length;k++){
                    System.out.print(""+weight[i][j][k]+"\t");
                }
                //System.out.println("");
            }
            System.out.println("");
        }*/
    }
    
}
