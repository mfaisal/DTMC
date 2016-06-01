
public class Transition {
	private State src = null;
	private State dst = null;
	private double prob = 0.0;
	private long numJumps = 0;
	private double firstJump = 0.0;
	private double lastJump = 0.0;
	private double ate = 0.0;
	private double stdate = 0.0;
	private String id = null;
	
	public void setSourceState(State srcState){
		this.src = srcState;
	}
	
	public void setDestState(State dstState){
		this.dst = dstState;
	}
	
	public void setProbability(double prob){
		this.prob = prob;
	}
	
	public void setnumJumps(long jums){
		this.numJumps = jums;
	}
	
	public void setFirstJump(double fj){
		this.firstJump = fj;
	}
	
	public void setLastJump(double lj){
		this.lastJump = lj;
	}
	
	public void setATE(double ate){
		this.ate = ate;
	}
	
	public void setSTDATE(double stdATE){
		this.stdate = stdATE;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	
	public State getSourceState(){
		return this.src;
	}
	
	public State getDestState( ){
		return this.dst;
	}
	
	public double getProbability(){
		return this.prob;
	}
	
	public long getnumJumps(){
		return this.numJumps;
	}
	
	public double getFirstJump(){
		return this.firstJump;
	}
	
	public double getLastJump(){
		return this.lastJump;
	}
	
	public double getATE(){
		return this.ate;
	}
	
	public double getSTDATE(){
		return this.stdate;
	}
	
	public String getID(){
		return this.id;
	}
	
	
	
}
