import java.util.ArrayList;


public class Manager {
	
	private ReadyList[] readyList;
	private PCB runningProcess;
	
	
	
	public Manager()
	{
		readyList = new ReadyList[3];
		for(int i = 0; i < readyList.length; i++)
		{
			readyList[i] = new ReadyList(i);
			//readyList[i].setPriority(i);
		}
	}
	
	
	public void create_process(String PID, int priority)
	{
		
	}
	
	public void destroy_process(String PID)
	{
		
	}
	
	public void request_resource(String RID)
	{
		
	}
	
	public void release_resource(String RID)
	{
		
	}
	
	public void time_out()
	{
		
	}
	
	public void scheduler()
	{
		
	}

}
