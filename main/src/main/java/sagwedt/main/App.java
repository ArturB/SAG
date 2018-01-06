package sagwedt.main;

import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ActorSystem system = ActorSystem.create("SAG");
        Props props = Props.create(Launcher.class);
        system.actorOf(props);

    }
}
