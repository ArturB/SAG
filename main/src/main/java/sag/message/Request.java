package sag.message;

import akka.actor.ActorRef;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez Mastera do wszystkich klasyfikatorów, zawierający tekst do sklasyfikowania. Klasyfikator po otrzymaniu zlecenia wysyła odpowiedź albo {@link sag.message.Response} jeśli jest w stanie wykonać zadanie, albo {@link sag.message.Untrained} jeśli nie jest jeszcze nauczony.
 */
public class Request implements Serializable {
    private String textToClassify;
    private ActorRef requester;
    private static final long serialVersionUID = 3294930483453252345L;

    /**
     * Simple constructor.
     * @param textToClassify CV do sklasyfikowania w formie prostego stringa.
     * @param requester Requester. Tu zostanie wysłana odpowiedź na zapytanie.
     */
    public Request(String textToClassify, ActorRef requester) {
        this.textToClassify = textToClassify;
        this.requester = requester;
    }

    public String getTextToClassify() {
        return textToClassify;
    }

    public void setTextToClassify(String textToClassify) throws Exception{
        this.textToClassify = textToClassify;
    }

    public ActorRef getRequester() {
        return requester;
    }

    public void setRequester(ActorRef requester) {
        this.requester = requester;
    }
}
