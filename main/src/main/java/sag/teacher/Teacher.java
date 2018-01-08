package sag.teacher;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.cli.*;
import sag.message.Learn;

/**
 * Program pozwalający zlecać klasyfikatorom zdania uczenia się.
 */
public class Teacher {
    public static void main(String[] args) {
        // Lista obsługiwanych parametrów CLI
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("c", "class-name", true, "SID of agent to teach");
        opts.addOption("w", "word-limit", true, "SID of agent to teach");
        opts.addOption("p", "positive-examples", true, "Path to directory including POSITIVE class examples");
        opts.addOption("n", "megative-examples", true, "Path to directory including NEGATIVE class examples");
        opts.addOption("h", "help", false, "Show this help");

        // Parsuj argumenty CLI
        try {
            CommandLine cmd = parser.parse(opts, args);
            // PRINT HELP
            if(cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "sag-launcher", opts, true );
                System.exit(0);
            }
            // CHECK IF ALL NECESSARY PARAMETERS ARE PASSED
            if(!cmd.hasOption("c") ||
               !cmd.hasOption("w") ||
               !cmd.hasOption("p") ||
               !cmd.hasOption("n")
                    ) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "sag-launcher", opts, true );
                System.exit(-1);
            }

            // utwórz nauczyciela i wyślij zlecenie uczenia
            ActorSystem system = ActorSystem.create("TeacherSystem", ConfigFactory.load("teacher"));
            ActorRef teacher = system.actorOf(sag.teacher.Agent.props(), "teacher");
            teacher.tell(new Learn(
                    cmd.getOptionValue("c"),
                    cmd.getOptionValue("p"),
                    cmd.getOptionValue("n"),
                    Integer.parseInt(cmd.getOptionValue("w"))
            ), null);

        }
        catch(ParseException e) {
            System.out.println("Unknown option!");
        }
    }
}
