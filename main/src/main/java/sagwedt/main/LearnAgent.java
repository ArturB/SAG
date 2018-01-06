package sagwedt.main;

import sagwedt.classifier.*;
import sagwedt.message.*;

import java.beans.ExceptionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;
import sagwedt.message.Request;
import weka.core.Instance;


class LearnAgent extends UntypedActor {
    TextClassifier classifierBayes = null;
    TextClassifier classifierLogistic = null;
    String className;

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
    public void onReceive(Object message) throws Exception {
        if (message instanceof Request) {
            if(classifierBayes == null || classifierLogistic == null) {
                getSender().tell(new Untrained(className), getSelf());
            }

            Request request = (Request) message;
            Instance cv = new TextToVector().convert(request.getTextToClassify());
            double bayesProb = classifierBayes.classifyInstance(cv);
            double logisticProb = classifierLogistic.classifyInstance(cv);
            getSender().tell(new Response(bayesProb, logisticProb, className), getSelf());

        } else {
            unhandled(message);
        }
    }
}