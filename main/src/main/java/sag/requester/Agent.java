package sag.requester;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import sag.message.Aggregate;
import sag.message.NoAgents;
import sag.message.Request;
import sag.message.Response;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Agent programu Requester, zlecający do serwera zadania klasyfikacji tekstu i agregujący otrzymane odpowiedzi.
 */
public class Agent extends AbstractActor {

    private int timeout;
    private String serverPath;
    private Boolean showBayes, showLogistics;

    private LinkedList<String> maximumLikelihoodClass;
    private BigDecimal maximumProb;
    private MathContext mc;
    private DecimalFormat df;

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Konstruktor agenta requestera jako aktora Akki {@link Props}.
     * @param timeout Czas oczekiwania na odpowiedzi od klasyfikatorów.
     * @param serverPath Adres serwera systemu.
     * @param showBayes Jeśli TRUE, to system wyświetla prawdopodobieństwa obliczone metodą Bayesa.
     * @param showLogistics Jeśli TRUE, to system wyświetla prawdopodobieństwa obliczone metodą regresji logistycznej.
     * @return
     */
    static Props props(
            int timeout,
            String serverPath,
            Boolean showBayes,
            Boolean showLogistics) {
        return Props.create(sag.requester.Agent.class, () ->
                new sag.requester.Agent(
                timeout,
                serverPath,
                showBayes,
                showLogistics));
    }

    public Agent(int timeout, String serverPath, Boolean showBayes, Boolean showLogistics) {
        this.timeout = timeout;
        this.serverPath = serverPath;
        this.showBayes = showBayes;
        this.showLogistics = showLogistics;
        this.maximumLikelihoodClass = new LinkedList<>();
        this.mc = new MathContext(4, RoundingMode.HALF_EVEN);
        this.maximumProb = new BigDecimal(0, mc);
        this.df = new DecimalFormat("0.0000");
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public LinkedList<String> getMaximumLikelihoodClass() {
        return maximumLikelihoodClass;
    }

    public void setMaximumLikelihoodClass(LinkedList<String> maximumLikelihoodClass) {
        this.maximumLikelihoodClass = maximumLikelihoodClass;
    }

    public BigDecimal getMaximumProb() {
        return maximumProb;
    }

    public void setMaximumProb(BigDecimal maximumProb) {
        this.maximumProb = maximumProb;
    }

    /**
     * Odbieranie komunikatów.
     *     - {@link Request} - po odebraniu zlecenia prześlij je do serwera, odczekaj oznaczony przez timeout okres czasu na odpowiedzi od klasyfikatorów a na koniec wyświetl zagregowane wyniki.
     *     - {@link Response} - po otrzymaniu odpowiedzi od określonego klasyfikatora, wypisz otrzymane dane na wyjście.
     * @return Receiver.
     */
    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        rbuilder.match(Request.class, request -> {
            getContext().actorSelection(serverPath).tell(request, getSelf());
            maximumLikelihoodClass.clear();
            maximumProb = new BigDecimal(0, mc);
        });

        rbuilder.match(Response.class, response -> {
            BigDecimal prob = new BigDecimal(0, mc);
            System.out.println("Classifier response: ");
            System.out.println("    - Class name: " + response.getClassName());
            if(showBayes) {
                System.out.println("    - Bayes probability: " + df.format(response.getBayesProb()));
                prob = response.getBayesProb();
            }
            if(showLogistics) {
                System.out.println("    - Logistic probability: " + df.format(response.getLogisticProb()));
                prob = response.getLogisticProb();
            }
            if(showBayes && showLogistics) {
                System.out.println("    - Average probability: " + df.format(response.getAverage()));
                prob = response.getAverage();
            }
            if(prob.compareTo(maximumProb) == 0) {
                maximumLikelihoodClass.add(response.getClassName());
            }
            else if(prob.compareTo(maximumProb) == 1) {
                maximumLikelihoodClass.clear();
                maximumLikelihoodClass.add(response.getClassName());
                maximumProb = prob;
            }
        });

        rbuilder.match(Aggregate.class, a -> {
            System.out.println("\n\n###########\n# Summary #\n###########\n");
            System.out.println("  - Maximum likelihood class: " + maximumLikelihoodClass + " with probability = " + df.format(maximumProb) + "\n\n");
        });

        rbuilder.match(NoAgents.class, a -> {
            System.out.println("\n\n###########\n# Summary #\n###########\n");
            System.out.println("  Actually, there are no agents running in the system!\n\n");
            getContext().stop(getSelf());
        });


        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }

}
