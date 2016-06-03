import java.util.HashMap;
import java.util.TreeMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author mustafa
 *
 */
public class ModelingDTMC {

	TreeMap<String,State> states = new TreeMap<String,State>();
	TreeMap<String,Transition> transitions = new TreeMap<String,Transition>();
	HashMap<Integer,String> tids = new HashMap<Integer,String>();
	ArrayList<Integer> tidwreq = new ArrayList<Integer>();
	boolean hasRequest = false;
	//int rtid = -1;
	/**
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	
	
	public static void main(String[] args) throws IOException{
		
		ModelingDTMC mdl = new ModelingDTMC();
		
		String fln = "/home/mustafa/Spring2016/Research/PCAPAnalysis/SigChannels/172.16.11.142._172.16.2.34._254.txt";
		mdl.generateDTMC(fln);
		//System.out.println(mdl.tidwreq.size());
		System.out.println(mdl.states.size()+" " + mdl.transitions.size());
		
		/*for(State st:mdl.states.values()){
			
			System.out.println(st.getId());
			System.out.println(st.getData());
			System.out.println(st.getfts());
			System.out.println(st.getlts());
			System.out.println(st.getNumOfElements());
			System.out.println(st.getType());
			
			System.out.println("===================================");
		}*/
		
		for(Transition trans:mdl.transitions.values()){
			System.out.println(trans.getID());
			System.out.println(trans.getProbability());
			System.out.println(trans.getnumJumps());
			System.out.println(trans.getFirstJump());
			System.out.println(trans.getLastJump());
			System.out.println(trans.getATE());
			System.out.println(trans.getSTDATE());
			
			
			System.out.println("*************************************");
		}
		
		
	}
	
	public void generateDTMC(String fileName) throws FileNotFoundException{
		FunctionCodes fs = new FunctionCodes();
		Scanner sc = new Scanner(new File(fileName));
		String event="";
		State st = new State();
		State prevSt = new State();
		String transId = null;
		
		int count = 0;
		while(sc.hasNext()){
			event = sc.next(); 
			
			st = getState(event,fs); // extract state and add if it is new
			
			if(!hasRequest){ 
				count++;
				continue;
			}
			
			if(st==null){
				st = updateS(event); // update state
			}
			
			if(prevSt.getId()==null){ // first packet in trace without any previous state
				prevSt = st; // updating the previous state
				continue;
			}
			
			transId = prevSt.getId() + "_to_" + st.getId();
			//System.out.println(transId);
			
			if(transitions.containsKey(transId)){
				updateTransition(prevSt,st,transId); // update the existing transition
			}else{
				addTransition(prevSt, st, transId); // add new transition
			}
			
			prevSt = st; // updating the previous state
		}
		//System.out.println(count);
		sc.close();
	}
	/**
	 * 
	 * @param event
	 * @param fs
	 * @return
	 */
	public State getState(String event, FunctionCodes fs){
		
		
		String[] flds = event.split(",");
		
		double fsls = Double.parseDouble(flds[0]);
		int tid = Integer.parseInt(flds[1]);
		int rq = Integer.parseInt(flds[2]);
		int fc = Integer.parseInt(flds[3]);
		int rn = Integer.parseInt(flds[4]);
		int bwc = Integer.parseInt(flds[5]);
		String[] rn_bwc;
		
		if(!tids.containsKey(tid)&&rq==1){
			tids.put(tid,rn+","+bwc);
		}else if(tids.containsKey(tid)&&rq==0){
			rn_bwc = tids.get(tid).split(","); // retrieve reference number
			rn = Integer.parseInt(rn_bwc[0]);
			bwc = Integer.parseInt(rn_bwc[1]);
			//tids.remove(tid);
		}else{
			tidwreq.add(tid);
		}
		
		if(rn==0&&rq==0){
			hasRequest = false;
			tids.remove(tid);
			return null;
		}
		else hasRequest = true;
		
		//id = "fc_rn_bwc"
		
		String id = fc+"_"+rn+"_"+bwc; // reference number is 0 for response
		
		if(states.containsKey(id)){
			return null;
		}
		
		
		//System.out.println(id);
		State st = new State();
		String data = fs.getFCDes(fc)+" from " + rn + " by " + bwc;
		st.setId(id);
		st.setData(data);
		st.setNumOfElements(1);
		st.setfts(fsls);
		st.setlts(fsls);
		if(rq==1)
			st.setType("request");
		else{
			System.out.println(states.keySet());
			//System.out.println(event +"\n" + id);
			st.setType("response");
		}
		states.put(id,st);
		
		return st;
	}
	/**
	 * Update State as it is already exist in DTMC
	 * @param event
	 */
	public State updateS(String event){
		
		String[] flds = event.split(",");
		
		double fsls = Double.parseDouble(flds[0]);
		int rq = Integer.parseInt(flds[2]);
		int tid = Integer.parseInt(flds[1]);
		int fc = Integer.parseInt(flds[3]);
		int rn = Integer.parseInt(flds[4]);
		int bwc = Integer.parseInt(flds[5]);
		String[] rn_bwc;
		
		if(tids.containsKey(tid)&&rq==0){
			rn_bwc = tids.get(tid).split(","); // retrieve reference number
			rn = Integer.parseInt(rn_bwc[0]);
			bwc = Integer.parseInt(rn_bwc[1]);
			tids.remove(tid);
		}
		
		String id = fc+"_"+rn+"_"+bwc; // state ID = Function code_ReferNumber_B/W
		
		State st = states.get(id);
		st.setNumOfElements(st.getNumOfElements()+1);
		st.setlts(fsls);
		
		//System.out.println(rq + " "+ st.getType()+" " +id.equals("3_512_1"));
		//System.out.println(st.getType() + "," + rq);
		if(st.getType().equals("request")&&rq==0){
			//System.out.println("Hi");
			st.setType("request/response");
		}
		
		states.put(id, st);
		return st;
	}
	/**
	 * 
	 * @param prevSt
	 * @param st
	 * @param transId
	 */
	public void addTransition(State prevSt, State st, String transId){
		Transition ts = new Transition();
		
		ts.setSourceState(prevSt);
		ts.setDestState(st);
		
		
		ts.setATE(st.getlts()-prevSt.getlts());
		ts.setSTDATE(0.0);
		ts.setFirstJump(st.getlts());
		ts.setLastJump(st.getlts());
		ts.setProbability(1.0);
		ts.setnumJumps(1);
		
		ts.setID(transId);
		
		transitions.put(transId,ts);
	}
	
	public void updateTransition(State prevSt, State st, String transId){
		Transition ts = transitions.get(transId);
		
		ts.setSourceState(prevSt);
		ts.setDestState(st);
		
		
		
		ts.setnumJumps(ts.getnumJumps()+1);
		double prevate = ts.getATE();
		double tdiff = st.getlts() - ts.getLastJump();
		double ate = (ts.getnumJumps()-1)*prevate +tdiff;
		ate = ate/(double)ts.getnumJumps();
		double sate = ts.getSTDATE()+Math.pow(tdiff,2);
		
		ts.setATE(ate);
		
		ts.setSTDATE(sate);
		ts.setLastJump(st.getlts());
		ts.setProbability((double)ts.getnumJumps()/(double)prevSt.getNumOfElements());
		
		
		ts.setID(transId);
		
		transitions.put(transId,ts);
	}
	
}
