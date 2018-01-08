package sag.teacher;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import sag.message.*;

/**
 * Agent zlecający klasyfikatorom zdania uczenia się.
 */
public class Agent extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Generator of actor.
     * @return
     */
    static Props props() {
        return Props.create(sag.teacher.Agent.class, () -> new sag.teacher.Agent());
    }

    /**
     * Prztwarzanie komunikatów:
     * {@link sag.message.Teach} - zleć określonemu klasyfikatorowi zadanie uczenia się.
     * {@link sag.message.LearnReply} - wypisz na stdout wynik uczenia.
     * @return Receiver komunikatów.
     */
    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        // TEACH
        rbuilder.match(Learn.class, learn -> {
            ActorRef classifier = getContext().actorOf(sag.classifier.Agent.props(learn.getClassName()));
            classifier.tell(learn, getSelf());
            System.out.println("Created classifier " + classifier.path().toString());
        });

        // LEARN-REPLY
        rbuilder.match(LearnReply.class, reply -> {
            if(reply.getSuccess()) {
                System.out.println("Agent " + getSender().path() + " successfully learned!");
                getContext().stop(getSelf());
            }
            else {
                System.out.println("Agent " + getSender().path() + " learning error: " + reply.getMsg());
            }

        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info("Unknown message type!"));

        return rbuilder.build();
    }

}
