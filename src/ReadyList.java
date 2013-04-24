import java.util.ArrayList;

/**
 * ReadyList will be stack in 3 layers represent the priority of the data structure
 */
public class ReadyList {
    private ArrayList<PCB> list;

    public ReadyList()
    {
        list = new ArrayList<PCB>();
    }

    public void insert(PCB pcb) {
        list.add(pcb);
    }

    public boolean removePCB(PCB p) {
        return list.remove(p);
    }

    public int getSize() {
        return list.size();
    }

    public PCB highestPriorityPCB() {
        return list.get(0);
    }

    public ArrayList<PCB> getList()
    {
        return list;
    }

    public void clear()
    {
        list.clear();
    }

    public PCB getPCB(int index) {
        return list.get(index);
    }
}
