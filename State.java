import java.util.ArrayList;

public class State {
	private String data = null;
	private String type = null;
	private long elements = 0;
	private String id = null;
	private double fts = -1.0;
	private double lts = -1.0;
	private ArrayList<NeighborState> neighbors = null;
	
	public void setData(String data){
		this.data = data;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setNumOfElements(long elements){
		this.elements = elements;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setfts(double fts){
		this.fts = fts;
	}
	
	public void setlts(double lts){
		this.lts = lts;
	}
	
	public void addNeighbor(NeighborState neg){
		neighbors.add(neg);
	}
	
	public String getData(){
		return this.data;
	}
	
	public String getType(){
		return this.type;
	}
	
	public long getNumOfElements(){
		return this.elements;
	}
	
	public String getId(){
		return this.id;
	}
	
	public double getfts(){
		return this.fts;
	}
	
	public double getlts( ){
		return this.lts;
	}
	
}
