package sag.teacher;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import org.apache.commons.cli.*;
import sag.message.DeleteClassifier;
import sag.message.Learn;

import java.io.File;

/**
 * Program tworzący i uczący klasyfikatory.
 */
public class Teacher {
    public static void main(String[] args) {
        ActorRef teacher;

        // Lista obsługiwanych parametrów CLI
        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("s", "server-address", true, "IP Address of the system server. Dafaults to loopback 127.0.0.1. ");
        opts.addOption("p", "server-port", true, "Listening port of system server. Defaults to 5150. ");
        opts.addOption("n", "server-system-name", true, "Name of remote ActorSystem of the system server. Defaults to ClassifiersSystem");
        opts.addOption("g", "server-agent-name", true, "Server agent name. Defaults to /user/$a. ");
        opts.addOption("l", "classifiers-list", true, "Path to file with list of classifiers.");
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
            if(cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "teacher", opts, true );
                System.exit(0);
            }
            if(!cmd.hasOption("l")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "teacher", opts, true );
                System.exit(-1);
            }
            if(cmd.hasOption("s")) {
                serverIP = cmd.getOptionValue("s");
            }
            if(cmd.hasOption("p")) {
                serverPort = cmd.getOptionValue("p");
            }
            if(cmd.hasOption("n")) {
                serverSystemName = cmd.getOptionValue("n");
            }
            if(cmd.hasOption("g")) {
                serverAgent = cmd.getOptionValue("g");
            }
            String remotePath = "akka.tcp://" + serverSystemName + "@" + serverIP + ":" + serverPort + serverAgent;

            // parse HOCON classifiers list file
            Config list = ConfigFactory.parseFile(new File(cmd.getOptionValue("l"))).resolve();
            Config cfg = ConfigFactory.load("teacher");
            Config cfg3 = cfg2.withFallback(cfg);

            //System.out.println(cfg3.getString("akka.remote.netty.tcp.bind-hostname"));
            //System.out.println(cfg3.getString("akka.remote.netty.tcp.bind-port"));

            ActorSystem system = ActorSystem.create("TeacherSystem", cfg3);

            teacher = system.actorOf(sag.teacher.Agent.props(remotePath), "teacher");
            for(ConfigObject o : list.getObjectList("classifiers.list")) {
                Config classifier = o.toConfig().resolveWith(list);
                teacher.tell(new Learn(
                        classifier.getString("class"),
                        classifier.getString("positive-examples"),
                        classifier.getString("negative-examples"),
                        classifier.getInt("word-limit")
                ), null);
            }

            //Ctrl+C Handler
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.out.println("\n\nShutting system down...");
                    teacher.tell(new DeleteClassifier(null), null);
                    try {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    system.terminate();
                }
            });

        }
        catch(ParseException e) {
            System.out.println("Unknown option!");
        }

    }



}
