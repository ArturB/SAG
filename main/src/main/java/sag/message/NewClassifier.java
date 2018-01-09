package sag.message;

import akka.actor.ActorRef;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez teachera po utworzeniu i poprawnym nauczeniu klasyfikatora. Wysyłany jest na serwer i zawiera adres utworzonego klasyfikatora. Serwer zapisuje adres nowoutworzonego klasyfikatora do swojej listy.
 */
public class NewClassifier implements Serializable {

    private ActorRef path;
    private static final long serialVersionUID = 483094839800L;

    /**
     * Simple constructor.
     * @param path Ścieżka (URI) nowoutworzonego klasyfikatora.
     */
    public NewClassifier(ActorRef path) {
        this.path = path;
    }

    public ActorRef getPath() {
        return path;
    }

    public void setPath(ActorRef path) {
        this.path = path;
    }
}
