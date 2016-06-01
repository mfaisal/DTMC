
public class NeighborState {
	private State state = null;
	private double transProb = 0.0;
	private long transCounter = 0;
	
	public void setNeighborState(State state){
		this.state = state;
	}
	
	public void setTransitionProb(double tp){
		this.transProb = tp;
	}
	
	public void setTransCounter(long tc){
		this.transCounter = tc;
	}
	
	public State getNeighborState(){
		return this.state;
	}
	
	public double setTransitionProb(){
		return this.transProb;
	}
	
	public long getTransCounter(){
		return this.transCounter;
	}
}
