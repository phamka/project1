import java.util.ArrayList;


public class RCB  {
	
	private String RID;
	private int status;
	private ArrayList<PCB> waitingList;
	
	public RCB(String RID)
	{
		this.RID = RID;
		status = 0;
		waitingList = new ArrayList<PCB>();
	}
	
	public String getRID() {
		return RID;
	}
	
	public void addPCB(PCB pcb) {
		waitingList.add(pcb);
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getWaitingListSize() {
		return waitingList.size();
	}
	
	public void pop(){
		if (getWaitingListSize() > 0)
			waitingList.remove(0);
	}
}
