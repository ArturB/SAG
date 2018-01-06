package sagwedt.message;

/**
 * Odpowiedź klasyfikatora na zadanie klasyfikacji, wysyłana, jeśli klasyfikator nie jest w stanie wykonać zadania z powodu nienauczenia.
 */
public class Untrained {
    String className;
    private final String noClassException = "No class name!";

    private void checkClassName() throws Exception {
        if(className == null)
            throw new Exception(noClassException);
    }

    /**
     * Domyślny konstruktor.
     * @param className Nazwa klasy której się nie nauczono.
     * @throws Exception Jeśli podano null.
     */
    public Untrained(String className) throws Exception {
        this.className = className;
        checkClassName();
    }

    /**
     * Getter dla nazwy klasy.
     * @return Nazwa klasy.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sette dla nazwy klasy.
     * @param className Nazwa klasy.
     * @throws Exception Rzuca jeśli podano null.
     */
    public void setClassName(String className) throws Exception {
        this.className = className;
        checkClassName();
    }
}
