

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
import java.io.*;
import java.util.Scanner;

public class PresentationShell
{
    private static Manager manager;
    private static BufferedWriter out;
    // test file on desktop
    private static String outputFilename = "C:\\Users\\Khoa\\Desktop\\76218534.txt";
    private static String inputFilename = "C:\\Users\\Khoa\\Desktop\\input.txt";
    // test file on flash drive
    //private static String inputFilename = "D:\\input.txt";
    //private static String outputFilename = "D:\\76218534.txt";

    PresentationShell() { }

    // command opcode predefined
    public enum Command
    {
        INIT,   //INIT
        QUIT,   //QUIT
        CR,     //CREATE
        DE,     //DESTROY
        REQ,    //REQUEST
        REL,    //RELEASE
        TO,     //TIMEOUT
        DUMP    //DUMP
    }

    public static void main(String[] args) throws IOException {
        simulation();
        //create_shell();
    }

    // function to read in a file and execute line by line
    public static void simulation()
    {
        try {
            out = new BufferedWriter(new FileWriter(outputFilename));
            manager = new Manager(out);

            File file = new File(inputFilename);

            Scanner scanner = new Scanner(file);

            Command opcode;

            while (scanner.hasNextLine()) {
                String cmd = scanner.nextLine();
                System.out.println(cmd);
                if (cmd.equals(""))
                {
                    continue;
                }
                String[] cmds = cmd.split(" ");
                opcode = Command.valueOf(cmds[0].toUpperCase());

                switch (opcode) {

                    case INIT: // init
                        System.out.println("init");
                        manager.create_init();
                        break;

                    case QUIT: // quit
                        System.out.println("*exit system.");
                        out.write("process terminated");
                        break;

                    case CR:    // cr A 1   //create <name> <priority>
                        if(manager != null && cmds.length == 3)
                        {
                            String PID = cmds[1];
                            int priorityNum = Integer.parseInt(cmds[2]);
                            manager.create_process(PID, priorityNum);
                        }
                        else if(cmds.length != 3)
                            System.out.println("error format");
                        break;

                    case DE: // de <name>
                        if(manager != null && cmds.length == 2)
                        {
                            String PID = cmds[1];
                            manager.destroy_process(PID);
                            System.out.println("*destroy process");
                        }
                        else if(cmds.length != 2)
                        {
                            System.out.println("error format");
                        }
                        break;

                    case REQ: // req <name>
                        if(manager != null && cmds.length == 2)
                        {
                            String RID = cmds[1];
                            manager.request_resource(RID);
                            System.out.println("*request process");
                        }
                        else if(cmds.length != 2)
                        {
                            System.out.println("error format");
                        }
                        break;

                    case REL: // rel <name>
                        if(manager != null && cmds.length == 2)
                        {
                            String RID1 = cmds[1];
                            manager.release_resource(RID1);
                            System.out.println("*release process");
                        }
                        else if(cmds.length != 2)
                        {
                            System.out.println("error format");
                        }

                        break;

                    case TO: // to
                        manager.time_out();
                        break;

                    case DUMP: // dump
                        manager.memory_dump();
                        break;
                }

            }
            scanner.close();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // creating shell for process command manually
    public static void create_shell() throws IOException {
        out = new BufferedWriter(new FileWriter(outputFilename));
        manager = new Manager(out);
        Scanner consoleIn = new Scanner(System.in);
        Command opcode;


        do {
            System.out.print("shell> ");
            String cmd = consoleIn.nextLine();
            String[] cmds = cmd.split(" ");
            opcode = Command.valueOf(cmds[0].toUpperCase());

            switch (opcode) {
                case INIT: // init
                    manager.create_init();
                    break;

                case QUIT: // quit
                    if (cmds.length != 1)
                    {
                        System.out.println("\nError: 'quit' format is wrong type quit");
                    }
                    System.out.println("*exit system.");
                    out.write("process terminated.");
                    break;
                case CR:    // cr A 1   //create <name> <priority>

                    if(manager != null && cmds.length == 3)
                    {
                        String PID = cmds[1];
                        int priorityNum = Integer.parseInt(cmds[2]);
                        manager.create_process(PID, priorityNum);
                    }
                    else if(cmds.length != 3)
                        System.out.println("error format");
                    break;

                case DE: // de <name>


                    if(manager != null && cmds.length == 2)
                    {
                        String PID = cmds[1];
                        manager.destroy_process(PID);
                        System.out.println("*destroy process");
                    }
                    else if(cmds.length != 2)
                    {
                        System.out.println("error format");
                    }

                    break;

                case REQ: // req <name>
                    String RID = cmds[1];
                    manager.request_resource(RID);
                    break;

                case REL: // rel <name>
                    String RID1 = cmds[1];
                    manager.release_resource(RID1);
                    break;

                case TO: // to
                    manager.time_out();
                    break;

                case DUMP: // dump
                    manager.memory_dump();
                    break;
            }

        }while(!(opcode == Command.QUIT));

        consoleIn.close();
        out.close();
    }
}