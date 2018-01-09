package sag.requester;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.cli.*;
import sag.message.Aggregate;
import sag.message.Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Program wysyłający do serwera zlecenia klasyfikacji CV.
 */
public class Requester {

    /**
     * Requester entry point.
     * @param args Parametry CLI.
     */
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

        opts.addOption("bayes", false, "Show only Bayes-based predictions. ");
        opts.addOption("logistic", false, "Show only logistic regression - based predictions. ");

        opts.addOption("h", "help", false, "Show this help");

        String serverIP = "127.0.0.1";
        String serverPort = "5150";
        String serverSystemName = "ClassifiersSystem";
        String serverAgent = "/user/$a";

        Config cfg2 = ConfigFactory.parseFile(new File("netty.conf"));
        if(cfg2.hasPath("akka.remote.netty.tcp.hostname")) {
            serverIP = cfg2.getString("akka.remote.netty.tcp.hostname");
        }
        if (cfg2.hasPath("akka.remote.netty.tcp.port")) {
            serverPort = cfg2.getString("akka.remote.netty.tcp.port");
        }
        if(cfg2.hasPath("akka.remote.netty.tcp.system-name")) {
            serverSystemName = cfg2.getString("akka.remote.netty.tcp.system-name");
        }
        if(cfg2.hasPath("akka.remote.netty.tcp.agent-name")) {
            serverAgent = cfg2.getString("akka.remote.netty.tcp.agent-name");
        }


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
            Boolean showBayes = true;
            Boolean showLogistic = true;
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
            if(cmd.hasOption("bayes") && !cmd.hasOption("logistic")) {
                showLogistic = false;
            }
            if(!cmd.hasOption("bayes") && cmd.hasOption("logistic")) {
                showBayes = false;
            }


            String remotePath = "akka.tcp://" + serverSystemName + "@" + serverIP + ":" + serverPort + serverAgent;

            String cv = new String(Files.readAllBytes(Paths.get(cmd.getOptionValue("cv"))));

            Config cfg = ConfigFactory.load("requester");
            ActorSystem system = ActorSystem.create("RequesterSystem", cfg);

            ActorRef requester = system.actorOf(sag.requester.Agent.props(timeout, remotePath, showBayes, showLogistic));
            requester.tell(new Request(cv, requester), null);

            Thread.sleep(timeout);
            requester.tell(new Aggregate(), null);
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
