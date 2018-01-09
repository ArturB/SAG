package sag.message;

import java.io.Serializable;

/**
 * Odpowiedź klasyfikatora za zadanie klasyfikacji tekstu. Zawiera wyznaczone przez klasyfikator prawdopodobieństwo należenia sklasyfikowanego tekstu do obsługiwanej przez klasyfikator klasy. Wysyłana przez klasyfikator bezpośrednio do requestera.
 */
public class Response implements Serializable {
    private double bayesProb;
    private double logisticProb;
    private String className;
    private static final long serialVersionUID = 68539058435850385L;

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
