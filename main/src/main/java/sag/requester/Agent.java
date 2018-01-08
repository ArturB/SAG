package sag.requester;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import sag.message.Request;
import sag.message.Response;

public class Agent extends AbstractActor {

    private String serverPath;
    private Boolean showBayes, showLogistics;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props(String serverPath, Boolean showBayes, Boolean showLogistics) {
        return Props.create(sag.requester.Agent.class, () -> new sag.requester.Agent(serverPath, showBayes, showLogistics));
    }

    public Agent(String serverPath, Boolean showBayes, Boolean showLogistics) {
        this.serverPath = serverPath;
        this.showBayes = showBayes;
        this.showLogistics = showLogistics;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        rbuilder.match(Request.class, request -> {
            getContext().actorSelection(serverPath).tell(request, getSelf());
            System.out.println("Request.class");
        });

        rbuilder.match(Response.class, response -> {
            System.out.println("Classifier response: ");
            System.out.println("    - Class name: " + response.getClassName());
            if(showBayes) {
                System.out.println("    - Bayes probability: " + response.getBayesProb());
            }
            if(showLogistics) {
                System.out.println("    - Logistic probability: " + response.getLogisticProb());
            }
            System.out.println("");
        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }

}
