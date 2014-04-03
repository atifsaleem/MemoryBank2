//@author: Wang Di

package falcon;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.text.NumberFormat;
import java.util.Arrays;

public class FusionART {
	protected NumberFormat df = NumberFormat.getInstance();
	
	protected int capacity;
	protected int numSpace;
	protected int [] len;
	protected int numCode;
	protected double[][] activityF1;
	protected double[]   activityF2;
	protected double[][][] weight;
	protected double beta[];
	protected double alpha[];
	protected double gamma[];
	protected double rho[];			
	
	protected boolean[] newCode;
	protected double[] confidence;
	protected double initConfidence;
	protected double reinforce_rate;
	protected double penalize_rate;
	protected double decay_rate;
	protected double threshold;
		
	final static int FUZZYART = 0;
	final static int ART2 = 1;
	
	final static double FRACTION = 0.00001;
	
	public FusionART () { //after initialization, must set parameters
		df.setMaximumFractionDigits (1);
		capacity = 9999999;
		numCode = 1;
		initConfidence = 0.5;
		reinforce_rate = 0.5;
		penalize_rate = 0.2;
		decay_rate = 0.001;
		threshold = 0.01;
		newCode = new boolean[numCode];
		newCode[0] = true;
		confidence = new double[numCode];
		confidence[0] = initConfidence;
	}
	public void printt(){System.out.println("All OK");}
	public void initParameters (int ns, int[] le, double b[], double[] a, double[] g, double[] r) {
		numSpace = ns;
		len = new int[le.length];
		copyArray(le, len);
		
		activityF1 = new double[numSpace][];
		for (int k=0; k<numSpace; k++) {
			activityF1[k] = new double [len[k]];
		}//empty array
		activityF2 = new double[numCode];//empty array
		weight = new double[numCode][][];
		for (int j=0; j<numCode; j++) {
			weight[j] = new double[numSpace][];
			for (int k=0; k<numSpace; k++) {
				weight[j][k] = new double[len[k]];
				for (int i=0; i<len[k]; i++) {
					weight[j][k][i] = 1;
				}
			}
		}
				
		beta = new double[b.length];
                copyArray(b,beta);
		alpha = new double[a.length];
		copyArray(a,alpha);
		gamma = new double[g.length];
		copyArray(g,gamma);
		rho = new double[r.length];
		copyArray(r,rho);
	}
        public void setContribution(double[] g)
        {
            this.gamma=g;
        }
	
	public void overWritePara (double ic, double rr, double pr, double dr, double t) {
		initConfidence = ic;
		confidence[numCode-1]=initConfidence;
		reinforce_rate = rr;
		penalize_rate = pr;
		decay_rate = dr;
		threshold = t;		
	} 	
	
	public void setCapacity (int c) {
		capacity = c;
	}
	
	public void copyArray (int[] from, int[] to) {
		for (int i=0; i<from.length; i++)
			to[i] = from[i];
	}
	
	public void copyArray (double[] from, double[] to) {
		for (int i=0; i<from.length; i++)
			to[i] = from[i];
	}
	
	public void displayVector (String s, double[] x, int n) {		
        System.out.print(s+ " : ");
        for (int i=0; i<n-1; i++)
            System.out.print (df.format(x[i])+", ");
        System.out.println (df.format(x[n-1]));
    }
    
    public boolean isNull (double[] x) {
    	for (int i=0; i<x.length; i++) 
    		if (x[i] != 0) return (false);
    	return (true);
 
    }
    
    public void setActivityF1 (double[][] input) {
    	for (int k=0; k<numSpace; k++) 
    		for (int i=0; i<len[k]; i++) 
    			activityF1[k][i] = input[k][i];
    }
    
    public void setActivityF1 (int k, int i, double value) {
    	activityF1[k][i] = value;
    }
    
    public void setActivityF2 (int j, double value){
    	activityF2[j] = value;
    }
    
    public void setWeight (int j, int k, int i, double value){
        weight[j][k][i] = value;
    }
    
    public void doOverwrite (int j) {
    	for (int k=0; k<numSpace; k++) 
    		for (int i=0; i<len[k]; i++) 
    			weight[j][k][i] = activityF1[k][i];
    }
    
    public void displayActivityF1 (int k) {
        System.out.print ("Space " + k + " : ");
        System.out.print(df.format(activityF1[k][0]));
        for (int i=1; i<len[k]; i++)
            System.out.print ("," + df.format(activityF1[k][i]));
        System.out.println("");
    }
    
    public void displayOutput (int j, int k) {
    	displayVector("weight[" + j + "]" + "[" + k + "]", weight[j][k], len[k]);
    }
    
    public double[][] getAllOutput(int j){
    //by Budhitama Subagdja
        
        return weight[j];
    }
    
    public double[] getOutput (int j, int k){
    	return weight[j][k];
    }
    
    public double[] getActivityF1 (int k){
    	return activityF1[k];
    }
    
    public double[] getActivityF2 (){
    	return activityF2;
    }
    
    public double getActivityF2 (int j){
    	return activityF2[j];
    }
    
    public int getNumCode () {
    	return numCode;
    }
    
    public double getConfidence (int j) {
    	return confidence[j];
    }
    
    public void saveCode (FileWriter fw) {
    	try{
    		String str;
    		str=NumberFormat.getInstance().format(numCode);
    		fw.write(str+"\n");
    		for (int j=0; j<numCode; j++) {
    			str="";
    			for (int k=0; k<numSpace; k++) {
    				for (int i=0; i<len[k]; i++) {
    					str=str+NumberFormat.getInstance().format(weight[j][k][i])+" ";
    				}
    			}
    			str = str + "\n" + NumberFormat.getInstance().format(confidence[j]) + " ";
    			str = str + newCode[j] + " ";
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
    		newCode = new boolean [numCode];
    		for (int j=0; j<numCode; j++) {
                        //System.out.println("read rule "+j);
    			weight[j]=new double [numSpace][];
    			for (int k=0; k<numSpace; k++) {
    				weight[j][k]=new double[len[k]];
    				for (int i=0; i<len[k];i++) {
    					weight[j][k][i]=sc.nextDouble();
    				}
    			}
    			confidence[j] = sc.nextDouble();
    			newCode[j] = sc.nextBoolean();
    		}
    	}catch (Exception e){
                e.printStackTrace();
    		System.out.println("Cannot load!!!");
    	}    	
    }
    
    public void reinforce (int j) {
    	confidence[j] += (1-confidence[j])*reinforce_rate;
    }
    
    public void reinforce (int j, double rate) {
    	confidence[j] += (1-confidence[j])*rate;
    }
    
    public void penalize (int j) {
    	confidence[j] -= confidence[j]*penalize_rate;
    }
    
    public void decay () {
    	for (int j=0; j<numCode-1; j++)
    		confidence[j] -= confidence[j]*decay_rate;
    }
    
    public void slowerDecay(int j, double slow_rate){
        confidence[j] -= confidence[j]*slow_rate;
    }
    
    public void fasterDecay(int j, double fast_rate){
        confidence[j] -= confidence[j]*fast_rate;
    }
    
    public void prune () {
    	for (int j=0; j<numCode-1; j++)
    		if (confidence[j] < threshold)
    			newCode[j] = true;
    }
    
    public int[] purge () {
    	int numPurge = 0;
    	for (int j=0; j<numCode-1; j++){
    		if (newCode[j])
    			numPurge++;
        }        
        
    	int purged[]=new int[numPurge];
    	if (numPurge >0) {
    		double[][][] new_weight = new double[numCode-numPurge][][];
    		boolean[] new_newCode = new boolean[numCode-numPurge];
    		double[] new_confidence = new double[numCode-numPurge];
    		
    		int k=0;
    		for (int j=0, p=0; j<numCode-1; j++) {
    			if (newCode[j] == false) {
    				new_weight[k] = weight[j];
    				new_newCode[k] = newCode[j];
    				new_confidence[k] = confidence[j];
    				k++;
    			}
                        else{
                            purged[p]=j;
                            p++;
                        }
    		}
    		new_weight[numCode-numPurge-1] = weight[numCode-1];
    		new_newCode[numCode-numPurge-1] = newCode[numCode-1];
    		new_confidence[numCode-numPurge-1] = confidence[numCode-1];
    		
    		weight = new_weight;
    		newCode = new_newCode;
    		confidence = new_confidence;
    		numCode = numCode-numPurge;
    		activityF2 = new double[numCode];    		
    	}
        
        return purged;
    }
    
    public void expand (double ini_conf) {
    	numCode++;
    	double[][][] new_weight = new double[numCode][][];
    	boolean[] new_newCode = new boolean[numCode];
    	double[] new_confidence = new double[numCode];
    	
    	for (int j=0; j<numCode-1; j++) {
    		new_weight[j] = weight[j];
    		new_newCode[j] = newCode[j];
    		new_confidence[j] = confidence[j]; 
    	}
    	new_weight[numCode-1] = new double[numSpace][];
    	for (int k=0; k<numSpace; k++) {
    		new_weight[numCode-1][k] = new double[len[k]];
    		for (int i=0; i<len[k]; i++)
    			new_weight[numCode-1][k][i] = 1;
    	}
    	new_newCode[numCode-1] = true;
    	new_confidence[numCode-1] = ini_conf;
    	
    	weight = new_weight;
    	newCode = new_newCode;
    	confidence = new_confidence;
    	activityF2 = new double[numCode];
    }
    
    public void computeChoice (int ART_MODE) {
    	double top, bottom;
    	
    	if (ART_MODE == FUZZYART) {
    		for (int j=0; j<numCode; j++) {
    			activityF2[j] = 0;
    			for (int k=0; k<numSpace; k++) {
    				top = 0;
    				bottom = alpha[k];
    				for (int i=0; i<len[k]; i++) {
    					top += Math.min(activityF1[k][i],weight[j][k][i]);
    					bottom += weight[j][k][i];
    				}
    				activityF2[j] += gamma[k] * (top/bottom);
    			}
    		}
    	}
    	
    	else if (ART_MODE == ART2) {
    		for (int j=0; j<numCode; j++) {
    			activityF2[j] = 0;
    			for (int k=0; k<numSpace; k++) {
    				top = 0;
    				for (int i=0; i<len[k]; i++)
    					top += activityF1[k][i] * weight[j][k][i];
    				top /= len[k];
    				activityF2[j] += top;
    			}
                        
                        System.out.print("act="+activityF2[j]);
    		}
                System.out.println("");
    	}
    }
    
    public void computeChoice (int ART_MODE, int except_space) {
    	double top, bottom;
    	
    	if (ART_MODE == FUZZYART) {
    		for (int j=0; j<numCode; j++) {
    			activityF2[j] = 0;
    			for (int k=0; k<numSpace; k++) {
    				if (k == except_space)
    					continue;
    				top = 0;
    				bottom = alpha[k];
    				for (int i=0; i<len[k]; i++) {
    					top += Math.min(activityF1[k][i],weight[j][k][i]);
    					bottom += weight[j][k][i];
    				}
    				activityF2[j] += gamma[k] * (top/bottom);
    			}
    		}
    	}
    	
    	else if (ART_MODE == ART2) {
    		for (int j=0; j<numCode; j++) {
    			activityF2[j] = 0;
    			for (int k=0; k<numSpace; k++) {
    				if (k == except_space)
    					continue;
    				top = 0;
    				for (int i=0; i<len[k]; i++)
    					top += activityF1[k][i] * weight[j][k][i];
    				top /= len[k];
    				activityF2[j] += top;
    			}
    		}
    	}
    }
    
    public int codeCompetition () {
    	double max_act = -1;
    	int c = -1;
    	
    	for (int j=0; j<numCode; j++) {
    		if (activityF2[j] > max_act) {
    			max_act = activityF2[j];
    			c = j;
    		}
    	}
    	
    	return c;
    }
    
    public int[] codeCompetitionK (double thresh) {
        Vector<Integer> max_code = new Vector<Integer>(0);
    	double  max_value = -1;
        int[] winners;
    	
    	for (int j=0; j<numCode; j++) {
    		if (activityF2[j] > max_value) {
    			max_value = activityF2[j];
                        max_code.removeAllElements();
                        max_code.add(new Integer(j));
    		}
                
                else if(activityF2[j]==max_value){
                    max_code.add(new Integer(j));
                }
    	}
        
        if(max_code.size()==0){
            return null;
        }
        
        winners=new int[max_code.size()];
        for(int m=0;m<max_code.size();m++){
            winners[m]=max_code.elementAt(m).intValue();
        }
        
        System.out.println("code competitionK maxact="+max_value);
        //if(max_value<0.18) return null;
        if(max_value<thresh) return null;
    	
    	return winners;
    }
    
    public double computeMatch (int j, int k) {
    	double m = 0;
    	double denominator = 0;
    	
    	//if (isNull(activityF1[k]))
    	//	return 1;
    	//System.out.println("compute_match@code_"+j+",channel_"+k);
    	for (int i=0; i<len[k]; i++) {
                //System.out.print("weight_"+i+"="+weight[j][k][i]);
                //System.out.println("input_"+i+"="+activityF1[k][i]);
    		m += Math.min(activityF1[k][i],weight[j][k][i]);
    		denominator += activityF1[k][i];
    	} 	
    		
    	if (denominator == 0)
    		return 1;
        //System.out.println("match_computed="+m/denominator);
    	return (m/denominator);
    }
    
    public boolean resonanceOccur (int j, double[] temp_rho) {
    	boolean violated = false;
    	
    	for (int k=0; k<numSpace; k++) {
    		if (computeMatch(j,k) < temp_rho[k]) {
    			//temp_rho[k] = Math.min(computeMatch(j,k) + FRACTION, 1);
    			violated = true;
    		}
    	}
        if(violated){
            boolean end=false;
            while(end){
                if(temp_rho[0]==1){
                     end=true;
                     break;
                 }
                for (int k=0; k<numSpace; k++) {    		     
    		     temp_rho[k] = Math.min(temp_rho[k] + FRACTION, 1);
    	         }
                for (int k=0; k<numSpace; k++) {    		     
    		     if(computeMatch(j,k) >= temp_rho[k]){
                         end=false;
                     }
    	         }
            }
        }   	
    	return violated;
        /*
        boolean violated = false;
    	
    	for (int k=0; k<numSpace; k++) {
    		if ( computeMatch(j,k) < temp_rho[k]) {
    			temp_rho[k] = Math.min(computeMatch(j,k) + FRACTION, 1);
    			violated = true;
    		}
    	}  
    	return violated;*/
    }
    
    public boolean resonanceOccur (int j, double[] temp_rho, int except_space) {
    	boolean violated = false;
    	
    	for (int k=0; k<numSpace; k++) {
    		if (k != except_space && computeMatch(j,k) < temp_rho[k]) {
    			temp_rho[k] = Math.min(computeMatch(j,k) + FRACTION, 1);
    			violated = true;
    		}
    	}    	
    	return violated;
    }
    
    public void doLearn (int j, int ART_MODE, double ini_conf) {
    	double rate;
    	
    	if (!newCode[j] || numCode < capacity+1) {
    		for (int k=0; k<numSpace; k++) {
                    if (newCode[j]) rate = 1;
                    else rate = beta[k];
    			for (int i=0; i<len[k]; i++) {
    				if (ART_MODE == FUZZYART)
    					weight[j][k][i] = (1-rate)*weight[j][k][i]+rate*Math.min(weight[j][k][i],activityF1[k][i]);
    				else if (ART_MODE == ART2)
    					weight[j][k][i] = (1-rate)*weight[j][k][i]+rate*activityF1[k][i];
    			}
    		}
    		
    		if (newCode[j]) {
    			newCode[j] = false;
    			expand(ini_conf);
    		}
    	}
    }
    
    public void doLearn (int j, int ART_MODE, int except_space, int ini_conf) {
    	double rate;
    	
    	if (!newCode[j] || numCode < capacity+1) {
    		for (int k=0; k<numSpace; k++) {
                    if (newCode[j]) rate = 1;
                    else rate = beta[k];
    			if (k == except_space) continue;
    			for (int i=0; i<len[k]; i++) {
    				if (ART_MODE == FUZZYART)
    					weight[j][k][i] = (1-rate)*weight[j][k][i]+rate*Math.min(weight[j][k][i],activityF1[k][i]);
    				else if (ART_MODE == ART2)
    					weight[j][k][i] = (1-rate)*weight[j][k][i]+rate*activityF1[k][i];
    			}
    		}
    		
    		if (newCode[j]) {
    			newCode[j] = false;
    			expand(ini_conf);
    		}
    	}
    }
    
    public int findWinner (double[][] input, int ART_MODE) {
    	int winner = -1;
    	double [] temp_rho = new double[numSpace];
        for (int k=0; k<numSpace; k++) {
            temp_rho[k] = rho[k];
        }
    	
    	this.setActivityF1(input);
    	this.computeChoice(ART_MODE);
    	
    	do {
    		winner = this.codeCompetition();
    		if (winner>=numCode-1) break;    		
    		
    		if (this.resonanceOccur(winner,temp_rho)) {
    			this.setActivityF2(winner,0);
    		}
    		else{
                    //System.out.println("event learner find a winnner @"+winner+"with act="+activityF2[winner]+", rho="+temp_rho);
    			break;
                }        
    	}while (true);
    	
    	return winner;
    }
    
    public int findWinner (double[][] input, int ART_MODE, int except_space) {
    	int winner = -1;
    	double [] temp_rho = new double[numSpace];
        for (int k=0; k<numSpace; k++) {
            temp_rho[k] = rho[k];
        }
    	
    	this.setActivityF1(input);
    	this.computeChoice(ART_MODE, except_space);
    	
    	do {
    		winner = this.codeCompetition();
    		if (winner>=numCode-1) break;    		
    		
    		if (this.resonanceOccur(winner, temp_rho, except_space)) {
    			this.setActivityF2(winner,0);
    		}
    		else{
                    //System.out.println("event learner find a winnner @"+winner+"with act="+activityF2[winner]);
    			break;
                }        
    	}while (true);
    	
    	return winner;
    }
    
    public int[] findWinnerK (double[][] input, int ART_MODE, int except_space, double thresh) {
    	int winner[];
    	double [] temp_rho = new double[numSpace];
        for (int k=0; k<numSpace; k++) {
            temp_rho[k] = rho[k];
        }
    	
    	this.setActivityF1(input);
    	this.computeChoice(ART_MODE, except_space);
        
        boolean incomplete=true;
    	
    	
    		winner = this.codeCompetitionK(thresh);
                
                if(winner==null)
                    return null;
                
                if(winner.length==0){
                    return null;
                }
                
               // for(int i=0;i<winner.length;i++){
    		 //    if (winner[i]>=numCode-1) break;
                //}
    		
                /*for(int i=0;i<winner.length;i++){
    		     if (this.resonanceOccur(winner[i], temp_rho, except_space)) {
    			    this.setActivityF2(winner[i],0);
    		     }
    		     else{
                    //System.out.println("event learner find a winnner @"+winner+"with act="+activityF2[winner]);
    			break;
                     }  
                }*/
                
    	
    	
    	return winner;
    }
    
    public int findWinner (double[][] input, int ART_MODE, Vector seq) {
    	int winner = -1;
    	double [] temp_rho = new double[numSpace];
        for (int k=0; k<numSpace; k++) {
            temp_rho[k] = rho[k];
        }
    	
    	this.setActivityF1(input);
    	this.computeChoice(ART_MODE);
    	
    	do {
    		winner = this.codeCompetition();
                //System.out.println("Check winner: " + winner);
    		if (winner>=numCode-1) break;    		
    		
                if ((this.resonanceOccur(winner,temp_rho))||(seq.contains(winner))) {
    			this.setActivityF2(winner,0);
    		}
    		else{
                    //System.out.println("event learner find a winnner @"+winner+"with act="+activityF2[winner]+", rho="+temp_rho);
    			break;
                }        
    	}while (true);
    	
    	return winner;
    }
    
    
   /* public int findWinner (double[][] input, int ART_MODE, int except_space) {
    	int winner = -1;
    	double [] temp_rho = new double[numSpace];
        for (int k=0; k<numSpace; k++) {
            temp_rho[k] = rho[k];
        }
    	
    	this.setActivityF1(input);
    	this.computeChoice(ART_MODE, except_space);
    	
    	do {
    		winner = this.codeCompetition();
    		if (newCode[winner]) break;    		
    		
    		if (this.resonanceOccur(winner,temp_rho, except_space)) {
    			this.setActivityF2(winner,0);
    		}
    		else
    			break;
    	}while (true);
    	
    	return winner;
    }*/
    
    public double[] retrieveKnowledge(int winner, int k) {
    	if (newCode[winner]) return null;
    	else return getOutput(winner, k);
    }
    
    public void print(){
        System.out.println("================");
        System.out.println("Event learner print");
        System.out.println("=================");

        System.out.println("Numeber of events recognized="+this.newCode);
        
        for(int i=0;i<weight.length;i++){
            for(int j=0;j<weight[i].length;j++){
                for(int k=0;k<weight[i][j].length;k++){
                    System.out.print(weight[i][j][k]+"\t");
                }
            }
            System.out.println("");
        }
    }
}