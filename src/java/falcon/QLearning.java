//@author Wang Di

package falcon;

public class QLearning {
	private double alpha;
	private double gamma;
	
	public QLearning (double a, double g) {
		alpha = a;
		gamma = g;		
	}
	
	public double computeQ (boolean BQ, double immediateReward, double maxNextQ, double thisQ) {
		double Q = -1;
		double TDError = immediateReward + gamma*maxNextQ - thisQ;
		
		if (BQ)
			Q = thisQ + alpha*TDError*(1-thisQ); 
		else
			Q = thisQ + alpha*TDError; 
		
		if (Q<0)
			Q=0;
		else if (Q>1)
			Q=1;
		
		return Q;
	}
}