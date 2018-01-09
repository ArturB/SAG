package sag.requester;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import sag.message.Request;
import sag.message.Response;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Agent programu Requester, zlecający do serwera zadania klasyfikacji tekstu i agregujący otrzymane odpowiedzi.
 */
public class Agent extends AbstractActor {

    private int timeout;
    private String serverPath;
    private Boolean showBayes, showLogistics;

    LinkedList<Response> replies;
    //private LinkedList<String> maximumLikelihoodClass;
    //private double maximumProb;

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
        this.replies = new LinkedList<>();
        //this.maximumLikelihoodClass = "";
        //this.maximumProb = 0;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
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
            replies.clear();
            //System.out.println("Request.class");
            //Thread.sleep(timeout);
            //System.out.println("    - Maximum-likelihood class: " + maximumLikelihoodClass);
        });

        rbuilder.match(Response.class, response -> {
            replies.add(response);
            DecimalFormat df = new DecimalFormat("0.0000");
            System.out.println("Classifier response: ");
            System.out.println("    - Class name: " + response.getClassName());
            if(showBayes) {
                System.out.println("    - Bayes probability: " +
                        df.format(response.getBayesProb()));
            }
            if(showLogistics) {
                System.out.println("    - Logistic probability: " +
                        df.format(response.getLogisticProb()));
            }
            if(showBayes && showLogistics) {
                System.out.println("    - Average probability: " +
                        df.format(response.getAverage()));
            }
        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }

}
