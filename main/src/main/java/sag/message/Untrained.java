package sag.message;

import java.io.Serializable;

/**
 * Odpowiedź klasyfikatora na zadanie klasyfikacji, wysyłana, jeśli klasyfikator nie jest w stanie wykonać zadania z powodu nienauczenia.
 */
public class Untrained implements Serializable {
    String className;
    private static final long serialVersionUID = 134534554455965L;

    /**
     * @param className Nazwa klasy której się nie nauczono.
     */
    public Untrained(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
