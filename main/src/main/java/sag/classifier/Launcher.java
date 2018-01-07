package sag.classifier;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.commons.cli.*;

/**
 * Program uruchamiający zadaną liczbę klasyfikatorów w systemie.
 */
public class Launcher {

    /**
     * Entry point.
     * @param args Command-line parametrs.
     */
    public static void main( String[] args )
    {
        // Lista obsługiwanych opcji CLI
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("n", "agents-number", true, "Number of agents to run");
        opts.addOption("id", true, "System name (identifier). The default name is SAG");
        opts.addOption("h", "help", false, "Show this help");

        // parsuj argumenty CLI
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

            // Jeśli wszystkie parametry są podane, uruchom odpowiednią liczbę klasyfikatorów.
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
