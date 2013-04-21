import java.util.ArrayList;


public class Manager {
	
	private ReadyList[] readyList;
	private RCB[] resourceList;
	private PCB runningProcess = null;
	
	
	
	public Manager()
	{
		readyList = new ReadyList[3];
		for(int i = 0; i < readyList.length; i++)
		{
			readyList[i] = new ReadyList();
			//readyList[i].setPriority(i);
		}
		resourceList = new RCB[4];
		for(int i = 0; i < resourceList.length; i++)
		{
			resourceList[i] = new RCB("R" + Integer.toString(i+1));
		}
	}
	
	public void create_init()
	{
		PCB pcb = new PCB("init", 0);
		readyList[0].insert(pcb);
		runningProcess = pcb;
		System.out.println(runningProcess.getPID() + " is running.");
	}
	
	public void create_process(String PID, int priority)
	{
		
		// create new PCB
		PCB pcb = new PCB(PID, priority);
		
		// add to the ready list
		readyList[priority].insert(pcb);
		if(runningProcess != null)
		{
			pcb.setParent(runningProcess);
			runningProcess.setChild(pcb);
		}
		
		
		scheduler();
	}
	
	public void destroy_process(String PID)
	{
		PCB current_p = runningProcess;
		while(!current_p.getPID().equals(PID)) {
			current_p = current_p.getChild();
		}
		
		Kill_Tree(current_p);
		
		scheduler();
	}
	
	public void request_resource(String RID)
	{
		for (int i = 0; i < resourceList.length; ++i) {
			RCB rcb = resourceList[i];
			if (rcb.getRID().equals(RID)) {
				if (rcb.getStatus() == RCB.status.FREE) {
					rcb.setStatus(RCB.status.ALLOCATED);
					runningProcess.addResource(rcb);
					rcb.setHolder(runningProcess);
				} else {
					runningProcess.setStatus(PCB.status.block);
					rcb.addPCB(runningProcess);
					readyList[runningProcess.getPriority()].removePCB(runningProcess);
				}
				break;
			}
		}
	}
	
	public void release_resource(String RID)
	{
		for (int i = 0; i < resourceList.length; ++i) {
			RCB rcb = resourceList[i];
			if (rcb.getRID().equals(RID)) {
				rcb.setStatus(RCB.status.FREE);
				rcb.getHolder().removeResource(rcb);
				rcb.setHolder(null);
				break;
			}
		}
	}
	
	public void time_out()
	{
		int priority = runningProcess.getPriority();
		readyList[priority].removePCB(runningProcess);
		readyList[priority].insert(runningProcess);
		runningProcess.setStatus(PCB.status.ready);
		scheduler();
	}
	
	public void scheduler()
	{
		// find the highest priority process in the ready list
		for (int i = 2; i >= 0; --i) {
			if(readyList[i].getSize() != 0) {
				PCB p = readyList[i].highestPriorityPCB();
				runningProcess.setStatus(PCB.status.ready);
				p.setStatus(PCB.status.running);
				runningProcess = p;
				break;
			}
		}
		System.out.println(runningProcess.getPID() + " is running.");
	}
	
	public void Kill_Tree(PCB p) {
		// free resources
		ArrayList<RCB> rs = p.getResourceList();
		for (RCB rcb : rs) {
			rcb.setStatus(RCB.status.FREE);
		}
		
		// loop through R1 -> R4's waiting list
		for (RCB rcb : resourceList) {
			rcb.removePCB(p);
		}
		
		PCB p_child = p.getChild();
		if(p_child != null) Kill_Tree(p_child);
		
		
		for(int i = 2; i >= 0; i--)
		{
			readyList[i].removePCB(p);
		}
		
	}
	
	public void memory_dump()
	{
		System.out.println("Current runing process: " + runningProcess.getPID());
		for(int i = 2; i >= 0; i--)
		{
			if(readyList[i].getSize() == 0)
				System.out.println("ReadyList w/ priority " + i + " is empty.");
			else
			{
				System.out.println("ReadyList w/ priority " + i);
				for(PCB b : readyList[i].getList())
				{
					System.out.println("PCB: " + b.getPID() + "\n");
				}
			}
			
			
		}
	}

}
