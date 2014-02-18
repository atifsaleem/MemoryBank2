//@author Wang Di

package falcon;

public class SelectAction {
	private double Epsilon;
	private double decay;
	
	public SelectAction (double e, double d) {
		Epsilon = e;
		decay = d;
	}

	public boolean actionSelection () {
		if (Math.random() < Epsilon)
			return false;
		else
			return true;
	}
	
	public void moveOn () {
            if (Epsilon >= decay)
		Epsilon -= decay;
	}
	
	public double getEpsilon () {
		return Epsilon;
	}
	
	//public void setEpsilon (double e) {
	//	Epsilon = e;
	//}
	
	public double getDecay () {
		return decay;
	}
	
	public void setDecay (double d) {
		decay = d;
	}
}