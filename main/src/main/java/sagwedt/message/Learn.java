package sagwedt.message;

public class Learn {
    private String positiveDataPath;
    private String negativeDataPath;
    private int wordLimit;

    public Learn(String positiveDataPath, String negativeDataPath, int wordLimit) {
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
}
