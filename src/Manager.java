import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Manager class manage all the function of create/destroy/request/release resource from the presentation shell
 */
public class Manager {

    private ReadyList[] readyList;
    private RCB[] resourceList;
    private PCB runningProcess = null;
    private BufferedWriter writer;

    // constructor
    public Manager(BufferedWriter writer) throws IOException {
        this.writer = writer;
        // creating 3 layers priority levels ReadyList
        readyList = new ReadyList[3];
        for(int i = 0; i < readyList.length; i++)
        {
            readyList[i] = new ReadyList();
        }

        // creating 4 layers resource R1-R4
        resourceList = new RCB[4];
        for(int i = 0; i < resourceList.length; i++)
        {
            resourceList[i] = new RCB("R" + Integer.toString(i+1));
        }

        create_init();
    }

    // function specific to 'init' command from the shell
    public void create_init() throws IOException {
        System.out.println();
        writer.write("\n");
        for (ReadyList rl : readyList) {
            rl.clear();
        }

        for (RCB rcb : resourceList) {
            rcb.clear();
            rcb.setStatus(RCB.resource_status.FREE);
        }

        PCB pcb = new PCB("Init", 0);
        pcb.setStatus(PCB.status.READY);
        pcb.setParent(runningProcess);
        insert(readyList, pcb);


        scheduler();
    }

    // create a process is performed by parent process
    // Create(initialization parameters)
    // create PCB data structure
    // initialize PCB using parameters
    // link PCB to creation tree
    // insert(RL, PCB)
    // Scheduler()
    public void create_process(String PID, int priority)
    {
        // create new PCB
        PCB pcb = new PCB(PID, priority);
        pcb.setStatus(PCB.status.READY);
        pcb.setParent(runningProcess);
        if(runningProcess != null)
        {
            runningProcess.addChild(pcb);
        }
        insert(readyList, pcb);
        scheduler();
    }

    // destroy a process is performed by parent process
    public void destroy_process(String PID)
    {
        PCB pid = find_pid(PID);
        if(pid == null)
            System.out.println("cannot find " + PID + " process in creation tree.");

        Kill_Tree(pid);

        scheduler();
    }

    /**
     * Kill_Tree(p) {
     *     for all child processes q Kill_Tree(q)
     *     free resources
     *     delete PCB and update all pointers
     * }
     * @param p - process p to be destroy/kill
     */
    public void Kill_Tree(PCB p) {
            // remove all the children until the children list is empty
            while(!p.isChildrenListEmpty()) {
                Kill_Tree(p.pop());
            }
            // then remove itself
            remove_p(p);
    }

    // destroy the process that is call from the presentation shell
    private void remove_p(PCB p) {
        // remove p from its parent
        PCB parent = p.getParent();
        if (parent != null)
            parent.getChildrenList().remove(p);

        // detach p from parent
        p.setParent(null);

        // set status to destroyed
        p.setStatus(PCB.status.DESTROYED);

        // if other resource_list_is_not_empty
        if (!p.isResourceListEmpty()) {
            // remove resource holder
            for (RCB rcb : p.getResourceList()) {

                // if there are no process waiting for the resource, set its status to FREE
                if (rcb.getWaitingListSize() == 0) {
                    rcb.setStatus(RCB.resource_status.FREE);
                    rcb.setHolder(null);
                } else { // else assign the resource to the first process waiting in resource waiting list
                    PCB waitingProcess = rcb.pop();
                    waitingProcess.setStatus(PCB.status.READY);
                    waitingProcess.setStatusList(PCB.statusList.READYLIST);
                    insertResource(waitingProcess, rcb);
                    rcb.setHolder(waitingProcess);
                    insert(readyList, waitingProcess);
                }
            }

            // clear resource list
            p.getResourceList().clear();
        }

        // remove p from the readyList
        if (p.getStatusList() == PCB.statusList.READYLIST) {
            remove(readyList, p);
        } else { // remove p from any resource waiting list
            for (RCB rcb : resourceList) {
                rcb.removePCB(p);
            }
        }
    }

    // request if performed by current process
    public void request_resource(String RID)
    {
        int resourceID = Integer.parseInt(RID.substring(1));
        RCB r = resourceList[resourceID-1];

        // if resource status is free assign requested resource to the current running process
        if (r.getStatus() == RCB.resource_status.FREE) {
            r.setStatus(RCB.resource_status.ALLOCATED);
            r.setHolder(runningProcess);
            insertResource(runningProcess, r);
        }
        else // else set running process to blocked and add it to requested resource waiting list
        {
            runningProcess.setStatus(PCB.status.BLOCKED);
            runningProcess.setStatusList(PCB.statusList.WAITINGLIST);
            readyList[runningProcess.getPriority()].removePCB(runningProcess);
            r.addPCBtoWaitingList(runningProcess);
        }

        scheduler();
    }

    // performed by some other process
    public void release_resource(String RID)
    {
        int resourceID = Integer.parseInt(RID.substring(1));
        RCB r = resourceList[resourceID];

        // remove resource from the whichever process holding it
        if(r.getStatus() == RCB.resource_status.ALLOCATED) {
            PCB holder = r.getHolder();
            //holder.getResourceList().remove(r);
            holder.removeResource(r);
            r.setHolder(null);
        }

        // if there are no process waiting for the resource RID, set its status to FREE
        if (r.getWaitingListSize() == 0) {
            r.setStatus(RCB.resource_status.FREE);
            r.setHolder(null);
        }
        else { // else assign the resource to the first process waiting in resource waiting list
            PCB waitingProcess = r.pop();
            waitingProcess.setStatus(PCB.status.READY);
            waitingProcess.setStatusList(PCB.statusList.READYLIST);
            insertResource(waitingProcess, r);
            r.setHolder(waitingProcess);
            insert(readyList, waitingProcess);
        }

        scheduler();
    }

    /**
     * performed by hardware
     * Time_out() {
     *     find running process q;
     *     remove(RL, q);
     *     q->Status.Type = 'ready';
     *     insert(RL, q);
     *     Scheduler();
     * }
     */
    public void time_out()
    {
        remove(readyList, runningProcess);
        runningProcess.setStatus(PCB.status.READY);
        insert(readyList, runningProcess);
        scheduler();
    }

    /**
     * invoked at end of each operation
     * Scheduler() {
     *     find highest priority process p
     *     if(self->priority < p->priority || self->Status.Type != 'running' || self == Null)
     *     preempt(p, self)
     * }
     * condition(3): called from create or release
     * condition(4): called from request or time-out
     * condition(5): called from destroy
     * Preemption:
     *   Change status of p to running (status of self already changed to ready/blocked)
     *   Context switch -- output name of running process
     */

    private void scheduler()
    {
        PCB highestProcess = find_highest_priorityProcess();
        if(runningProcess == null ||
                runningProcess.getPriority() < highestProcess.getPriority() ||
                runningProcess.getStatus() != PCB.status.RUNNING)
        {
            preempt(highestProcess, runningProcess);
        }
        else
        {
            System.out.println(runningProcess.getPID() + " is running.");
            try {
                writer.write(runningProcess.getPID() + " is running\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // switching context from one process to the next process
    private void preempt(PCB p, PCB cr)
    {
        if(cr != null)
        {
            cr.setStatus(PCB.status.READY);
        }
        p.setStatus(PCB.status.RUNNING);
        runningProcess = p;

        System.out.println(runningProcess.getPID() + " is running.");
        try {
            writer.write(runningProcess.getPID() + " is running\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // function to see all the processes in the ready list
    public void memory_dump()
    {
        System.out.println("Current running process: " + runningProcess.getPID());
        for(int i = 2; i >= 0; i--)
        {
            if(readyList[i].getSize() == 0)
                System.out.println("ReadyList w/ priority " + i + " is empty.");
            else
            {
                System.out.println("ReadyList w/ priority " + i);
                for(PCB b : readyList[i].getList())
                {
                    System.out.println("PCB: " + b.getPID());
                }
            }
        }
    }

    // helper insert process function to the ready list
    private void insert(ReadyList[] RL, PCB p)
    {
        RL[p.getPriority()].insert(p);
    }

    // helper remove process from the ready list
    private boolean remove(ReadyList[] RL, PCB p)
    {
        return RL[p.getPriority()].removePCB(p);
    }

    // finding the highest priority process in readylist
    private PCB find_highest_priorityProcess()
    {
        PCB p = null;
        for (int i = 2; i >= 0; --i) {
            if(readyList[i].getSize() != 0) {
                p = readyList[i].highestPriorityPCB();
                break;
            }
        }
        return p;
    }

    // fidn where the PID locate
    private PCB find_pid(String PID)
    {
        PCB pid = null;
        for(int i = 2; i >= 0; i--)
        {
            if(readyList[i].getSize() != 0)
            {
                for(int j = 0; j < readyList[i].getSize(); j++)
                {
                    if(readyList[i].getList().get(j).getPID().equals(PID))
                    {
                        pid = readyList[i].getList().get(j);
                        return pid;
                    }
                }

            }
        }
        for(RCB rcb : resourceList) {
            if (rcb.getWaitingListSize() != 0) {
                for (PCB p : rcb.getWaitingList()) {
                    if (p.getPID().equals(PID)) {
                        pid = p;
                        return  pid;
                    }
                }
            }
        }

        return pid;
    }

    // add resource to the process
    private void insertResource(PCB self, RCB r)
    {
        self.addResource(r);
    }

}
