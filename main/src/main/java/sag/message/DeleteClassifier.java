package sag.message;

import akka.actor.ActorRef;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez teachera do klasyfikatora. Klasyfikator po otrzymaniu komunikatu odsyła go do serwera a następnie zamyka się. Serwer po otzymaniu komunikatu usuwa klasyfikator ze swojej listy.
 */
public class DeleteClassifier implements Serializable {

    private static final long serialVersionUID = 749472389478932L;

    private ActorRef classifier;

    public DeleteClassifier(ActorRef classifier) {
        this.classifier = classifier;
    }

    public ActorRef getClassifier() {
        return classifier;
    }

    public void setClassifier(ActorRef classifier) {
        this.classifier = classifier;
    }
}

