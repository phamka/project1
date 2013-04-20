import java.util.ArrayList;
import java.util.HashMap;


public class ReadyList {
	
	private int priority;
	private HashMap<String, PCB> list;
	
	public ReadyList(int priority)
	{
		this.priority = priority;
		list = new HashMap<String, PCB>();
	}
	
	public void insert(PCB pcb) {
		list.put(pcb.getPID(), pcb);
	}
	
	public void removePCB(String PID) {
		list.remove(PID);
	}
}
