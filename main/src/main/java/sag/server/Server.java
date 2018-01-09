package sag.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.cli.*;

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
        ActorSystem system = ActorSystem.create("ClassifiersSystem", ConfigFactory.load("launcher"));
        system.actorOf(sag.server.Agent.props());
    }

}
