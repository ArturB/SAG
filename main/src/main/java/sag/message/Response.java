package sag.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Odpowiedź klasyfikatora za zadanie klasyfikacji tekstu. Zawiera wyznaczone przez klasyfikator prawdopodobieństwo należenia sklasyfikowanego tekstu do obsługiwanej przez klasyfikator klasy. Wysyłana przez klasyfikator bezpośrednio do requestera.
 */
public class Response implements Serializable {
    private BigDecimal bayesProb;
    private BigDecimal logisticProb;
    private String className;

    private static final long serialVersionUID = 68539058435850385L;

    /**
     * @param bayesProb Prawdopodobieństwo obliczone metodą Bayesa.
     * @param logisticProb Prawdopodobieństwo obliczone metodą logistyczną.
     * @param className Nazwa analizowanej klasy.
     */
    public Response(double bayesProb, double logisticProb, String className) {
        MathContext mc = new MathContext(4, RoundingMode.HALF_EVEN);
        this.bayesProb = new BigDecimal(bayesProb, mc);
        this.logisticProb = new BigDecimal(logisticProb, mc);
        this.className = className;
    }

    public BigDecimal getBayesProb() {
        return bayesProb;
    }

    public void setBayesProb(BigDecimal bayesProb) {
        this.bayesProb = bayesProb;
    }

    public BigDecimal getLogisticProb() {
        return logisticProb;
    }

    public void setLogisticProb(BigDecimal logisticProb) {
        this.logisticProb = logisticProb;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) throws Exception {
        this.className = className;
    }

    /**
     *
     * @return Zwraca średnią arytmetyczną prawdopodobieństwa obliczonych dwoma metodami.
     */
    public BigDecimal getAverage() {
        return (bayesProb.add(logisticProb)).divide(new BigDecimal(2));
    }
}
