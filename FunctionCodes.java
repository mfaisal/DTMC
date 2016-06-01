import java.util.HashMap;

public class FunctionCodes {
	
	HashMap<Integer,String> fcodes = new HashMap<Integer,String>();
	
	public FunctionCodes(){
		fcodes.put(1,"Read Coil");
		fcodes.put(2,"Read Input");
		fcodes.put(3,"Read Holding Registers");
		fcodes.put(4,"Read Input Registers");
		fcodes.put(5,"Force/Write Single Coil");
		fcodes.put(6,"Write Single Holding Register");
		fcodes.put(7,"Read Exception Status");
		fcodes.put(8,"Diagnostic");
		fcodes.put(11,"Fetch Comm. Event Ctr");
		fcodes.put(12, "Fetch Comm. Event Log");
		fcodes.put(15,"Write/Force Multiple Coils");
		fcodes.put(16,"Write Multiple Holding Registers");
		fcodes.put(17,"Report Slave ID");
		fcodes.put(19,"Reset Comm. Link");
		fcodes.put(20,"Read File Recod");
		fcodes.put(21,"Write File Recod");
		fcodes.put(22,"Mask Write 4X Register");
		fcodes.put(23,"Read/Write 4X Registers");
		fcodes.put(24,"Read FIFO Queue");
	}
	
	public String getFCDes(int fc){
		return fcodes.get(fc);
	}

}
