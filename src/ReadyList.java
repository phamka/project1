import java.util.ArrayList;


public class ReadyList {
	
	private int list_priority;
	private ArrayList<PCB> list;
	
	public ReadyList(int priority)
	{
		list_priority = priority;
		list = new ArrayList<PCB>();
	}
	
	
	public void addPCB(PCB process)
	{
		list.add(process);
	}

}
