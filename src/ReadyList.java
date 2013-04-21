import java.util.ArrayList;


public class ReadyList {
	private ArrayList<PCB> list;
	
	public ReadyList()
	{
		list = new ArrayList<PCB>();
	}
	
	public void insert(PCB pcb) {
		list.add(pcb);
	}
	
	public void removePCB(PCB p) {
		list.remove(p);
	}
	
	public int getSize() {
		return list.size();
	}
	
	public PCB highestPriorityPCB() {
		return list.get(0);
	}
}
