package sagwedt.message;

/**
 * Odpowiedź klasyfikatora za zadanie klasyfikacji tekstu. Zawiera prawdopodobieństwo danej klasy.
 * Wysyłany tylko przez klasyfikatory już nauczone oczekiwanej klasy.
 */
public class Response {
    private double bayesProb;
    private double logisticProb;
    private String className;

    private void checkClassName() throws Exception {
        String noClassException = "No class name!";
        if(className == null)
            throw new Exception(noClassException);
    }

    /**
     * Domyślny konstruktor.
     * @param bayesProb Prawdopodobieństwo obliczone metodą Bayesa.
     * @param logisticProb Prawdopodobieństwo obliczone metodą logistyczną.
     * @param className Nazwa analizowanej klasy.
     */
    public Response(double bayesProb, double logisticProb, String className) {
        this.bayesProb = bayesProb;
        this.logisticProb = logisticProb;
        this.className = className;
    }

    public double getBayesProb() {
        return bayesProb;
    }

    public void setBayesProb(double bayesProb) {
        this.bayesProb = bayesProb;
    }

    public double getLogisticProb() {
        return logisticProb;
    }

    public void setLogisticProb(double logisticProb) {
        this.logisticProb = logisticProb;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) throws Exception {
        this.className = className;
        checkClassName();
    }
}
