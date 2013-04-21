import java.util.ArrayList;


public class PCB {
	
	private String PID;
	private ArrayList<RCB> resource_list;
	private status status_type;
	private statusList status_list;
	private PCB parent;
	private PCB child;
	private int priority;
	
	public enum status{
		ready,
		running,
		block
	}
	
	public enum statusList{
		RL,
		BL
	}
	
	public PCB(String PID_name, int priorityNum)
	{
		PID = PID_name;
		resource_list = new ArrayList<RCB>();
		status_type = status.ready;
		status_list = statusList.RL;
		parent = null;
		child = null;
		priority = priorityNum;
	}
	
	public void setParent(PCB parentProcess)
	{
		parent = parentProcess;
	}
	
	public void setChild(PCB childProcess)
	{
		child = childProcess;
	}
	
	public void changeStatus(status statusType)
	{
		status_type = statusType;
	}
	
	public void changeStatusList(statusList statusListType)
	{
		status_list = statusListType;
	}
	
	public void addResource(RCB resource)
	{
		resource_list.add(resource);
	}
	
	// all get methods for PCB
	public String getPID() { return PID;}
	public ArrayList<RCB> getResourceList() { return resource_list;}
	public status getStatus() { return status_type; }
	public statusList getStatusList() { return status_list;}
	public PCB getParent() { return parent; }
	public PCB getChild() { return child; }
	public int getPriority() {return priority; }

}
