package sag.message;

import java.io.Serializable;

/**
 * Komunikat wysyłany przez nauczyciela do klasyfikatora. Zawiera lokalizację danych, na podstawie których klasyfikator wykonuje uczenie.
 */
public class Learn implements Serializable {
    private String className;
    private String positiveDataPath;
    private String negativeDataPath;
    private int wordLimit;

    private static final long serialVersionUID = 74839472389478932L;

    /**
     * Konstruktor komunikatu.
     * @param positiveDataPath Ścieżka do przykładów należących do klasy.
     * @param negativeDataPath Ścieżka do przykładów nienależących do klasy.
     * @param wordLimit Limit liczby analizowanych słów.
     */
    public Learn(String className, String positiveDataPath, String negativeDataPath, int wordLimit) {
        this.className = className;
        this.positiveDataPath = positiveDataPath;
        this.negativeDataPath = negativeDataPath;
        this.wordLimit = wordLimit;
    }

    public String getPositiveDataPath() {
        return positiveDataPath;
    }

    public void setPositiveDataPath(String positiveDataPath) {
        this.positiveDataPath = positiveDataPath;
    }

    public String getNegativeDataPath() {
        return negativeDataPath;
    }

    public void setNegativeDataPath(String negativeDataPath) {
        this.negativeDataPath = negativeDataPath;
    }

    public int getWordLimit() {
        return wordLimit;
    }

    public void setWordLimit(int wordLimit) {
        this.wordLimit = wordLimit;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
