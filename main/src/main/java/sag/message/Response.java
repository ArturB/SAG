package sag.message;

import akka.actor.ActorRef;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Odpowiedź klasyfikatora za zadanie klasyfikacji tekstu. Zawiera wyznaczone przez klasyfikator prawdopodobieństwo należenia sklasyfikowanego tekstu do obsługiwanej przez klasyfikator klasy. Wysyłana przez klasyfikator do serwera i stamtąd z powrotem do requestera.
 */
public class Response implements Serializable {
    private ActorRef requester;
    private BigDecimal bayesProb;
    private BigDecimal logisticProb;
    private String className;

    private static final long serialVersionUID = 68539058435850385L;

    /**
     * Simple constructor.
     * @param requester Adres requestera. Tu serwer systemu przekaże odpowiedź.
     * @param bayesProb Prawdopodobieństwo obliczone metodą Bayesa.
     * @param logisticProb Prawdopodobieństwo obliczone metodą logistyczną.
     * @param className Nazwa analizowanej klasy.
     */
    public Response(ActorRef requester, BigDecimal bayesProb, BigDecimal logisticProb, String className) {
        this.requester = requester;
        this.bayesProb = bayesProb;
        this.logisticProb = logisticProb;
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

    public ActorRef getRequester() {
        return requester;
    }

    public void setRequester(ActorRef requester) {
        this.requester = requester;
    }

    /**
     *
     * @return Zwraca średnią arytmetyczną prawdopodobieństwa obliczonych dwoma metodami.
     */
    public BigDecimal getAverage() {
        return (bayesProb.add(logisticProb)).divide(new BigDecimal(2));
    }
}
