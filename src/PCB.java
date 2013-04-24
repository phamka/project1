import java.util.ArrayList;
import java.util.List;

/**
 * PCB class represetn a process control block which contains all the data necessary for the Manager class
 * to execute
 */

public class PCB {

    private String PID;
    private ArrayList<RCB> resource_list;
    private status status_type;
    private statusList status_list;
    private PCB parent;
    private ArrayList<PCB> children;
    private int priority;

    // status state
    public enum status{
        READY,
        RUNNING,
        BLOCKED,
        DESTROYED
    }

    // status list
    public enum statusList{
        READYLIST,
        WAITINGLIST
    }

    // constructor
    public PCB(String PID_name, int priorityNum)
    {
        PID = PID_name;
        resource_list = new ArrayList<RCB>();
        status_type = status.READY;
        status_list = statusList.READYLIST;
        parent = null;
        children = new ArrayList<PCB>();
        priority = priorityNum;
    }

    // set the parent pointer
    public void setParent(PCB parentProcess)
    {
        parent = parentProcess;
    }

    // add child to its list
    public void addChild(PCB childProcess)
    {
        children.add(childProcess);
    }

    // check if the children list is empty
    public boolean isChildrenListEmpty() {
        return children.isEmpty();
    }

    // set process status
    public void setStatus(status statusType)
    {
        status_type = statusType;
    }

    // set process list
    public void setStatusList(statusList statusListType)
    {
        status_list = statusListType;
    }

    // add resource to process list
    public void addResource(RCB resource)
    {
        resource_list.add(resource);
    }

    // remove resource from process list
    public void removeResource(RCB resource) {
        resource_list.remove(resource);
    }

    // remove first children from the list
    public PCB pop(){
        PCB p = null;
        if (children.size() > 0)
            p = children.remove(0);

        return p;
    }

    // all get methods for PCB
    public String getPID() { return PID;}
    public ArrayList<RCB> getResourceList() { return resource_list;}
    public boolean isResourceListEmpty() { return resource_list.isEmpty();}
    public status getStatus() { return status_type; }
    public statusList getStatusList() { return status_list;}
    public PCB getParent() { return parent; }
    public ArrayList<PCB> getChildrenList() { return children; }
    public int getPriority() {return priority; }

}
