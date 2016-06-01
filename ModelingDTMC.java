import java.util.HashMap;
import java.util.TreeMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * @author mustafa
 *
 */
public class ModelingDTMC {

	TreeMap<String,State> states = new TreeMap<String,State>();
	TreeMap<String,Transition> transitions = new TreeMap<String,Transition>();
	HashMap<Integer,Integer> tids = new HashMap<Integer,Integer>();
	ArrayList<Integer> tidwreq = new ArrayList<Integer>();
	boolean hasState;
	/**
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void generateDTMC(String fileName) throws FileNotFoundException{
		FunctionCodes fs = new FunctionCodes();
		Scanner sc = new Scanner(new File(fileName));
		String event;
		State st = new State();
		State prevSt = new State();
		String transId = null;
		String prevTransId = "";
		
		while(sc.hasNext()){
			event = sc.next(); 
			st = getState(event,fs); // extract state and add if it is new
			
			if(st.equals(null)||st==null){
				st = updateS(event); // update state
			}
			
			if(prevSt.getId()==null||prevSt.getId().equals(null)){ // first packet in trace without any previous state
				continue;
			}
			
			transId = st.getId() + "_to_" + prevTransId;
			
			if(transitions.containsKey(transId)){
				updateTransition(prevSt,st,transId); // update the existing transition
			}else{
				addTransition(prevSt, st, transId); // add new transition
			}
			
			prevSt = st; // updating the previous state
		}
		
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
		
		
		if(!tids.containsKey(tid)&&rq==1){
			tids.put(tid,rn);
		}else if(tids.containsKey(tid)&&rq==0){
			rn = tids.get(tid); // retrieve reference number
			tids.remove(tid);
		}else{
			tidwreq.add(tid);
		}
		
		
		
		//id = "fc_rn_bwc"
		
		String id = fc+"_"+rn+"_"+bwc; // reference number is 0 for response
		
		if(states.containsKey(id)){
			return null;
		}
		
		State st = new State();
		String data = fs.getFCDes(fc)+" from " + rn + " by " + bwc;
		st.setId(id);
		st.setData(data);
		st.setNumOfElements(1);
		st.setfts(fsls);
		st.setlts(fsls);
		if(rq==1)
			st.setType("request");
		else
			st.setType("response");
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
		int fc = Integer.parseInt(flds[3]);
		int rn = Integer.parseInt(flds[4]);
		int bwc = Integer.parseInt(flds[5]);
		
		String id = fc+"_"+rn+"_"+bwc; // state ID = Function code_ReferNumber_B/W
		
		State st = states.get(id);
		st.setNumOfElements(st.getNumOfElements()+1);
		st.setlts(fsls);
		if(st.getType().equals("request")&&rq==0){
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
		double tdiff = st.getfts() - ts.getLastJump();
		double ate = (ts.getnumJumps()-1)*prevate +tdiff;
		ate = ate/ts.getnumJumps();
		double sate = ts.getSTDATE()+Math.pow(tdiff,2);
		
		ts.setATE(ate);
		
		ts.setSTDATE(sate);
		ts.setLastJump(st.getlts());
		ts.setProbability(ts.getnumJumps()/prevSt.getNumOfElements());
		
		
		ts.setID(transId);
		
		transitions.put(transId,ts);
	}
	
}
