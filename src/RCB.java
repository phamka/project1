import java.util.ArrayList;

/**
 * RCB is a resource control block to help the data structure execute in the Manager class
 */

public class RCB  {

    private String RID;
    private resource_status status;
    private ArrayList<PCB> waitingList;
    private PCB holder;

    // resource status
    public enum resource_status {
        FREE,
        ALLOCATED
    }

    // constructor
    public RCB(String RID)
    {
        this.RID = RID;
        status = resource_status.FREE;
        waitingList = new ArrayList<PCB>();
        holder = null;
    }

    // clear the waiting resource list
    public void clear()
    {
        waitingList.clear();
    }

    // set pointer of the process acquired resource
    public void setHolder(PCB p) {
        holder = p;
    }

    // get the pointer of the process
    public PCB getHolder(){
        return holder;
    }

    // get the resource name
    public String getRID() {
        return RID;
    }

    // add process to its waiting list
    public void addPCBtoWaitingList(PCB pcb) {
        waitingList.add(pcb);
    }

    // check for the status
    public resource_status getStatus() {
        return status;
    }

    // set the status
    public void setStatus(resource_status rs) {
        status = rs;
    }

    // remmove process from waiting list
    public void removePCB(PCB p) {
        waitingList.remove(p);
    }

    // check the size fo the waiting list
    public int getWaitingListSize() {
        return waitingList.size();
    }

    // get the waiting list
    public ArrayList<PCB> getWaitingList() {
        return waitingList;
    }

    // return the first process waiting on its list
    public PCB pop(){
        PCB p = null;
        if (getWaitingListSize() > 0)
            p = waitingList.remove(0);

        return p;
    }
}
