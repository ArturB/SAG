package sagwedt.classifier;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.commons.cli.*;

public class Launcher {

    public static void main( String[] args )
    {
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("n", "agents-number", true, "Number of agents to run");
        opts.addOption("id", true, "System name (identifier). The default name is SAG");
        opts.addOption("h", "help", false, "Show this help");

        try {
            CommandLine cmd = parser.parse(opts, args);
            int n = -1;
            if(cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "sag-launcher", opts, true );
                System.exit(0);
            }
            if(cmd.hasOption("n")) {
                n = Integer.parseInt(cmd.getOptionValue("n"));
                System.out.println("No system name provided, defaults to SAG...");
            }
            else {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "sag-launcher", opts, true );
                System.exit(-1);
            }
            String name = "SAG";
            if(cmd.hasOption("name")) {
                name = cmd.getOptionValue("name");
            }

            ActorSystem system = ActorSystem.create(name);
            Props props = Props.create(Agent.class);
            System.out.println("Running " + String.valueOf(n) + " agents. Agents refs:");
            for(int i = 0; i < n; ++i) {
                ActorRef ref = system.actorOf(props);
                System.out.println("  " + ref.path().toString());
            }
        }
        catch(ParseException e) {
            System.out.println("Unknown option!");
        }


    }

}
