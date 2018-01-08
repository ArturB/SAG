package sag;

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
        ActorSystem.create("ClassifiersSystem", ConfigFactory.load("launcher"));
        System.out.println("\n[SUCCESS] Started classifers system!");
    }

}
