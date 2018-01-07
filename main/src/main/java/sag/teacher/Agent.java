package sag.teacher;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import sag.message.*;

/**
 * Agent zlecający klasyfikatorom zdania uczenia się.
 */
public class Agent extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        rbuilder.match(Teach.class, learn -> {
            getContext().actorSelection(learn.getAgentPath()).tell(learn.getLearnTask(), getSelf());
        });

        rbuilder.match(LearnReply.class, reply -> {
            if(reply.getSuccess()) {
                System.out.println("Agent " + getSender().path() + " successfully learned!");
            }
            else {
                System.out.println("Agent " + getSender().path() + " learning error: " + reply.getMsg());
            }

        });

        rbuilder.matchAny(o -> log.info("Unknown message type!"));

        return rbuilder.build();
    }

}
