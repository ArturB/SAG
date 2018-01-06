package sagwedt.main;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import sagwedt.classifier.*;
import sagwedt.message.Request;
import sagwedt.message.Response;
import sagwedt.message.Untrained;
import weka.core.Instance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;


class LearnAgent extends AbstractActor {
    private TextClassifier classifierBayes = null;
    private TextClassifier classifierLogistic = null;
    private String className;

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public LearnAgent() {

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LearnAgent(String className) throws Exception {
        if(className == null) {
            throw new Exception();
        }
        this.className = className;
    }

    private Instance getCVAsVector(File plik) throws IOException {
        TextToVector converter = new TextToVector();
        return converter.convert(new String(
                Files.readAllBytes(Paths.get(plik.getPath()))
        ));
    }

    public void learnFromDirectory(
            String positiveExamplesPath,
            String negativeExamplesPath,
            int wordLimit
    ) throws Exception {

        if(positiveExamplesPath == null || negativeExamplesPath == null) throw new Exception();

        File positiveExamplesDir, negativeExamplesDir;

        StructureBuilder sbuilder = new StructureBuilder();
        sbuilder.setWordLimit(wordLimit);
        positiveExamplesDir = new File(positiveExamplesPath);
        negativeExamplesDir = new File(negativeExamplesPath);
        List<Instance> positiveData = new LinkedList<>();
        List<Instance> negativeData = new LinkedList<>();

        for(final File plik : positiveExamplesDir.listFiles()) {
            Instance inst = getCVAsVector(plik);
            sbuilder.add(inst);
            positiveData.add(inst);
        }

        for(final File plik : negativeExamplesDir.listFiles()) {
            Instance inst = getCVAsVector(plik);
            sbuilder.add(inst);
            negativeData.add(inst);
        }

        TrainingSet ts = sbuilder.generateEmptyTrainingSet();
        for(Instance ins : positiveData) {
            ts.add(ins, true);
        }
        for(Instance ins : negativeData) {
            ts.add(ins, false);
        }

        classifierBayes = new TextClassifier(ts, Algorithm.NAIVE_BAYES);
        classifierLogistic = new TextClassifier(ts, Algorithm.LOGISTIC_REGRESSION);
    }



    @Override
    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        rbuilder.match(Request.class, request -> {
            if (classifierBayes == null || classifierLogistic == null) {
                getSender().tell(new Untrained(className), getSelf());
            }

            Instance cv = new TextToVector().convert(request.getTextToClassify());
            double bayesProb = classifierBayes.classifyInstance(cv);
            double logisticProb = classifierLogistic.classifyInstance(cv);
            getSender().tell(new Response(bayesProb, logisticProb, className), getSelf());
        });
        rbuilder.matchAny(o -> log.info("Unknown message type!"));

        return rbuilder.build();
    }
}