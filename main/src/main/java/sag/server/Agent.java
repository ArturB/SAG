package sag.server;

import akka.actor.AbstractActor;
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


    LinkedList<String> classifiers;
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
            String classifierPath = getSender().path().address().toString() + classifier.getPath();
            classifiers.add(classifierPath);
            log.info("New classifier added: " + classifierPath);
        });

        rbuilder.match(Request.class, request -> {
           for(String classpath : classifiers) {
               getContext().actorSelection(classpath).tell(request, getSelf());
           }

        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }

}
