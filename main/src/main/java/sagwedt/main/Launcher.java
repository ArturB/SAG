package sagwedt.main;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class Launcher extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    public Launcher() {
        Props props = Props.create(LearnAgent.class);
        for(int i = 0; i < 5; ++i) {
            ActorRef ref = getContext().actorOf(props);
            System.out.println(ref.path().toString());
        }

    }

    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();
        rbuilder.matchAny(o -> log.info("Unknown message type!"));
        return rbuilder.build();
    }

}
