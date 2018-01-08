package sag.requester;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.cli.*;
import sag.message.Request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Requester {

    public static void main(String[] args) {
        // Lista obsługiwanych parametrów CLI
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("s", "server-address", true, "IP Address of the system server. Dafaults to loopback 127.0.0.1. ");
        opts.addOption("p", "server-port", true, "Listening port of system server. Defaults to 5150. ");
        opts.addOption("n", "server-system-name", true, "Name of remote ActorSystem of the system server. Defaults to ClassifiersSystem");
        opts.addOption("g", "server-agent-name", true, "Server agent name. Defaults to /user/$a. ");
        opts.addOption("cv", true, "Path to text file with CV to classify.");
        opts.addOption("t", "timeout", true, "Time to wait for classifiers responses, in milliseconds. Defaults to 2000 (2s). ");
        opts.addOption("h", "help", false, "Show this help");

        // Parsuj argumenty CLI
        try {
            CommandLine cmd = parser.parse(opts, args);
            // PRINT HELP
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("teacher", opts, true);
                System.exit(0);
            }
            if (!cmd.hasOption("cv")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("teacher", opts, true);
                System.exit(-1);
            }
            String serverIP = "127.0.0.1";
            String serverPort = "5150";
            String serverSystemName = "ClassifiersSystem";
            String serverAgent = "/user/$a";
            int timeout = 2000;
            if (cmd.hasOption("s")) {
                serverIP = cmd.getOptionValue("s");
            }
            if (cmd.hasOption("p")) {
                serverPort = cmd.getOptionValue("p");
            }
            if (cmd.hasOption("n")) {
                serverSystemName = cmd.getOptionValue("n");
            }
            if (cmd.hasOption("g")) {
                serverAgent = cmd.getOptionValue("g");
            }
            if(cmd.hasOption("t")) {
                timeout = Integer.parseInt(cmd.getOptionValue("t"));
            }
            String remotePath = "akka.tcp://" + serverSystemName + "@" + serverIP + ":" + serverPort + serverAgent;

            String cv = new String(Files.readAllBytes(Paths.get(cmd.getOptionValue("cv"))));

            ActorSystem system = ActorSystem.create("RequesterSystem", ConfigFactory.load("requester"));
            ActorRef requester = system.actorOf(sag.requester.Agent.props(remotePath));
            requester.tell(new Request(cv, requester), null);

            Thread.sleep(timeout);
            system.terminate();



        } catch(ParseException pe) {
            System.out.println("Unknown option!");
        }
        catch(IOException ioe) {
            System.out.println("Invalid CV file path!");
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }


    }
}
