package sag.message;

import java.io.Serializable;

/**
 * Komunikato wysyłany przez teachera po utworzeniu klasyfikatora. Wysyłany jest na serwer i zawiera adres utworzonego klasyfikatora.
 */
public class NewClassifier implements Serializable {


    private String path;
    private static final long serialVersionUID = 483094839800L;

    public NewClassifier(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
