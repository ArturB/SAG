package sag.message;

/**
 * Odpowiedź klasyfikatora na zadanie klasyfikacji, wysyłana, jeśli klasyfikator nie jest w stanie wykonać zadania z powodu nienauczenia.
 */
public class Untrained {
    String className;

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
