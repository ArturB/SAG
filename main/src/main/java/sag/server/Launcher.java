package sag.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
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
        ActorSystem system = ActorSystem.create("ClassifiersSystem", ConfigFactory.load("launcher"));
        system.actorOf(sag.server.Agent.props());
    }

}
