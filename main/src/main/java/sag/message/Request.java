package sag.message;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez Mastera do wszystkich klasyfikatorów, zawierający tekst do sklasyfikowania. Klasyfikator po otrzymaniu zlecenia wysyła odpowiedź albo {@link sag.message.Response} jeśli jest w stanie wykonać zadanie, albo {@link sag.message.Untrained} jeśli nie jest jeszcze nauczony.
 */
public class Request implements Serializable {
    private String textToClassify;
    private static final long serialVersionUID = 3294930483453252345L;

    /**
     * @param textToClassify Tekst do sklasyfikowania w formie prostego Stringa.
     */
    public Request(String textToClassify) throws Exception {
    }

    public String getTextToClassify() {
        return textToClassify;
    }

    public void setTextToClassify(String textToClassify) throws Exception{
        this.textToClassify = textToClassify;
    }

}
