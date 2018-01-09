package sag.server;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import sag.message.*;

import java.util.LinkedList;

/**
 * Agent serwera systemu. Zbiera informacje nt. uruchomionych w systemie klasyfikatorów oraz przyjmuje zapytania od requesterów i rozsyła je do klasyfikatorów.
 */
public class Agent extends AbstractActor {


    LinkedList<ActorRef> classifiers;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Generator agenta jako aktora Akki {@link Props}.
     * @return
     */
    static Props props() {
        return Props.create(sag.server.Agent.class, () -> new sag.server.Agent());
    }

    /**
     * Default constructor.
     */
    public Agent() {
        classifiers = new LinkedList<>();
        log.info("[SUCCESS] Started classifiers server!");
    }

    /**
     * Prztwarzanie komunikatów:
     * {@link sag.message.NewClassifier} - dopisz nowego klasyfikatora do listy. .
     * {@link sag.message.Request} - zadaj wszystkim znanym klasyfikatorom zadanie sklasyfikowania CV.
     * @return Receiver komunikatów.
     */
    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        rbuilder.match(NewClassifier.class, classifier -> {
            classifiers.add(classifier.getPath());
            log.info("New classifier added: " + classifier.getPath());
        });

        rbuilder.match(Request.class, request -> {
            if(classifiers.isEmpty()) {
                getSender().tell(new NoAgents(), getSelf());
            }
            else {
                for(ActorRef cp : classifiers) {
                    cp.tell(request, getSelf());
                }
            }
        });

        rbuilder.match(Response.class, response -> {
           response.getRequester().tell(response, getSelf());
        });

        rbuilder.match(DeleteClassifier.class, delete -> {
           classifiers.remove(delete.getClassifier());
           System.out.println("Removing classifier " + delete.getClassifier().path());
        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }

}
