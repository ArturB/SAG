package sag.message;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez teachera po utworzeniu i poprawnym nauczeniu klasyfikatora. Wysyłany jest na serwer i zawiera adres utworzonego klasyfikatora. Serwer zapisuje adres nowoutworzonego klasyfikatora do swojej listy.
 */
public class NewClassifier implements Serializable {

    private String path;
    private static final long serialVersionUID = 483094839800L;

    /**
     * Simple constructor.
     * @param path Ścieżka (URI) nowoutworzonego klasyfikatora.
     */
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
