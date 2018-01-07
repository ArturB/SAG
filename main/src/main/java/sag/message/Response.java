package sag.message;

/**
 * Odpowiedź klasyfikatora za zadanie klasyfikacji tekstu. Zawiera wyznaczone przez klasyfikator prawdopodobieństwo należenia sklasyfikowanego tekstu do obsługiwanej przez klasyfikator klasy. .
 * Wysyłany tylko przez klasyfikatory już nauczone oczekiwanej klasy.
 */
public class Response {
    private double bayesProb;
    private double logisticProb;
    private String className;

    /**
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
    }
}