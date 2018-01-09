package sag.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.cli.*;

import java.io.File;

/**
 * Program uruchamiający serwer systemu SAG. Serwer zbiera informacje nt. uruchomionych w systemie klasyfikatorów oraz przyjmuje zapytania od requesterów i rozsyła je do klasyfikatorów.
 */
public class Server {

    /**
     * Server entry point.
     * @param args Parametry CLI.
     */
    public static void main( String[] args )
    {
        Config cfg = ConfigFactory.load("launcher");
        Config cfg2 = ConfigFactory.parseFile(new File("server.conf")).withFallback(cfg);
        ActorSystem system = ActorSystem.create("ClassifiersSystem", cfg2);

        system.actorOf(sag.server.Agent.props());
    }

}
