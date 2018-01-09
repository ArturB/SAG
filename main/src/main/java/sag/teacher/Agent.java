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
 * Agent programu teacher. Jego zadaniem jest tworzenie i uczenie klasyfikatorów na podstawie danych wczytanych przez Teacher z plikow. Klasyfikatory tworzone są zawsze na maszynie, na której uruchomiony został Teacher, a następnie rejestrowane przez sieć na serwerze. Tak utworzone klasyfikatory przyjmują następnie z sieci zlecenia, wykonują zapytania na lokalnej maszynie i odsyłają wyniki do requesterów.
 */
public class Agent extends AbstractActor {

    private String serverPath;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Simple constructor.
     * @param serverPath ścieżka do serwera systemu.
     */
    public Agent(String serverPath) {
        this.serverPath = serverPath;
    }

    /**
     * Generator of actor.
     * @param serverPath ścieżka do serwera systemu.
     * @return
     */
    static Props props(String serverPath) {
        return Props.create(sag.teacher.Agent.class, () -> new sag.teacher.Agent(serverPath));
    }

    /**
     * Prztwarzanie komunikatów:
     * {@link sag.message.Learn} - zleć określonemu klasyfikatorowi zadanie uczenia się.
     * {@link sag.message.LearnReply} - wypisz na stdout wynik uczenia.
     * @return Receiver komunikatów.
     */
    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        // LEARN
        rbuilder.match(Learn.class, learn -> {
            ActorRef classifier = getContext().actorOf(sag.classifier.Agent.props(learn.getClassName()));
            classifier.tell(learn, getSelf());
        });

        // LEARN-REPLY
        rbuilder.match(LearnReply.class, reply -> {
            if(reply.getSuccess()) {
                getContext().actorSelection(serverPath).tell(new NewClassifier(getSender().path().toStringWithoutAddress()), getSelf());
                log.info("[SUCCESS] Agent " + getSender().path() + " successfully created and learned!");
            }
            else {
                log.error("Agent " + getSender().path() + " learning error: " + reply.getMsg());
            }

        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }

}
