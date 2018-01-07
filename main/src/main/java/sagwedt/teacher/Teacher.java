package sagwedt.teacher;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.commons.cli.*;
import sagwedt.message.Teach;

public class Teacher {
    public static void main(String[] args) {
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("id", true, "System name (identifier). The default name is SAG");
        opts.addOption("h", "help", false, "Show this help");
        try {
            String name = "SAG";
            CommandLine cmd = parser.parse(opts, args);
            if(cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "sag-launcher", opts, true );
                System.exit(0);
            }
            if(cmd.hasOption("id")) {
                name = cmd.getOptionValue("id");
            }
            else {
                System.out.println("No system name provided, defaults to SAG...");

            }
            ActorSystem system = ActorSystem.create(name);
            Props props = Props.create(Agent.class);
            ActorRef teacher = system.actorOf(props);

            while(true) {
                String[] input = System.console().readLine().split(" ");
                if(input.length < 2) {
                    System.out.println("Invalid input data!");
                }
                else {
                    //teacher.tell(new Teach())
                }
            }
        }
        catch(ParseException e) {
            System.out.println("Unknown option!");
        }
    }
}
