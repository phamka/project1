import java.util.ArrayList;


public class RCB  {
	
	private String RID;
	private status status;
	private ArrayList<PCB> waitingList;
	private PCB holder;
	
	public enum status {
		FREE, 
		ALLOCATED
	}
	
	public void setHolder(PCB p) {
		holder = p;
	}
	
	public PCB getHolder(){
		return holder;
	}
	
	public RCB(String RID)
	{
		this.RID = RID;
		this.status = status.FREE;
		waitingList = new ArrayList<PCB>();
	}
	
	public String getRID() {
		return RID;
	}
	
	public void addPCB(PCB pcb) {
		waitingList.add(pcb);
	}
	
	public status getStatus() {
		return status;
	}
	
	public void setStatus(status s) {
		this.status = s;
	}
	
	public void removePCB(PCB p) {
		waitingList.remove(p);
	}
	
	public int getWaitingListSize() {
		return waitingList.size();
	}
	
	public void pop(){
		if (getWaitingListSize() > 0)
			waitingList.remove(0);
	}
}
