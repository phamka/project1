

/*
   * Command usage examples:
    * init      //restore the system to its initial state

    * quit      //quit the shell, Terminate the execution of the system

    * cr        //cr <name> <priority>  ,Invoke function Create(), which creates a new process <name> at the <priority>;
    *           //<name> is a single character; <priority> can be 1 or 2 (0 is reserved for Init process).

    * de        //de <name> ,Invoke the function Destroy(), which destroy the process <name> and all of its descendants.

    * req       //req <name>   ,Invoke the function  Request(), which requests the resource <name>;
    *           //<name> can be R1, R2, R3, or R4.
                //When implementing the additional task 5.1 (page 490), the command is extended to:
                //req <name> <# of units>
                //where <# of units> gives the number of units of  resource <name> to be requested. The numbers of
                //units constituting each resource are as follows: 1 for R1, 2 for R2, 3 for R3, and 4 for R4.
    * rel       //rel <name>    ,Invoke the function  Release(), which release the resource <name>.
                //When implementing the additional task 5.1 (page 490), the command is extended to:
                //rel <name> <# of units>
                //where <# of units> gives the number of units of  resource <name> to be released.
    * to        //Invoke the function Timeout().
    * rio       //Invoke the function Request_IO().
    * ioc       //Invoke the function IO_completion().
    *
    * optional:
    * list all processes and their status
        list all resources and their status
        provide information about a given process
        provide information about a given resource
 */

import java.util.Arrays;
import java.util.Scanner;

public class PresentationShell
{
	private static Manager manager;
	
    PresentationShell() {
    	manager = new Manager();
    }

    public enum Command
    {
        INIT,   //INIT
        QUIT,   //QUIT
        CR,     //CREATE
        DE,     //DESTROY
        REQ,    //REQUEST
        TO      //TIMEOUT

    }

    public static void main(String[] args)
    {
    	
    	read_file("");
    	create_shell();
       
    }
    
    // function to read in a file and execute line by line
    public static void read_file(String filename)
    {
    	
    }
    
    // creating shell for process command manually
    public static void create_shell()
    {
    	Scanner consoleIn = new Scanner(System.in);
        Command opcode;


        do {
            System.out.print("shell> ");
            String cmd = consoleIn.nextLine();
            String[] cmds = cmd.split(" ");
            opcode = Command.valueOf(cmds[0].toUpperCase());

            switch (opcode) {
                case INIT:
                    manager.create_process("init", 0);
                    System.out.println("input is init");
                    break;

                case QUIT:
                    if (cmds.length != 1)
                    {
                        System.out.println("\nError: 'quit' format is wrong type quit");
                    }
                    System.out.println("*exit system");
                    break;
                case CR:    // cr A 1   //create <name> <priority>
                    System.out.println("*create process");

                    if(manager != null && cmds.length == 3)
                    {
                        String processName = cmds[1];
                        int priorityNum = Integer.parseInt(cmds[2]);
                        manager.createPCB(processName, priorityNum);
                    }
                    else if(cmds.length != 3)
                        System.out.println("");
                    break;

                case DE:
                    System.out.println("*destroy process");
                    break;

                case REQ:
                    System.out.println("*request resource");
                    break;

                case TO:
                    System.out.println("*process time-out");
                    break;

                default:
                    System.out.println("wrong command please enter correct input.");
            }

        }while(!(opcode == Command.QUIT));

        consoleIn.close();
    }
    
    public void process_line(String line)
    {
    	
    }
    
    
}