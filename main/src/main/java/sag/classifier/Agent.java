package sag.classifier;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import sag.message.*;
import weka.core.Instance;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Pojedynczy agent wykonujący zadanie klasyfikacji binarnej: obiekt należy bądź nie zależy do przetwarzanej jednej klasy danych. Klasyfikacja wykonywana jest dwoma algorytmami ({@link sag.classifier.Algorithm}): naiwnym Bayesem oraz regresją logistyczną.
 */
public class Agent extends AbstractActor {

    private TextClassifier classifierBayes = null;
    private TextClassifier classifierLogistic = null;
    private String className;

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Konstruktor agenta. Świeżo skonstruowany agent nie jest jeszcze niczego nauczony.
     * @param className Nazwa analizowanej klasy.
     */
    public Agent(String className) {
        this.className = className;
    }

    /**
     * Generator klasyfikatora jako aktora Akki {@link Props}.
     * @param className Nazwa analizowanej przez klasyfikator klasy.
     * @return
     */
    static public Props props(String className) {
        return Props.create(Agent.class, () -> new Agent(className));
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Konwertuje plik tekstowy z CV do postaci wektora biblioteki Weka.
     * @param plik Uchwyt do pliku z CV.
     * @return Wektor biblioteki Weka odpowiadający CV.
     * @throws IOException Podana ścieżka jest niepoprawna.
     */
    private Instance getCVAsVector(File plik) throws IOException {
        TextToVector converter = new TextToVector();
        return converter.convert(new String(
                Files.readAllBytes(Paths.get(plik.getPath()))
        ));
    }

    /**
     * Uczy agenta na podstawie zbioru plików CV z zadanego katalogu. Jeżeli agent był już wcześniej czegoś uczony, to wcześniej zapisana baza wiedzy zostaje nadpisana nowymi danymi.
     * @param positiveExamplesPath Katalog z przykładami CV należących do klasy.
     * @param negativeExamplesPath Katalog z przykładami CV nienależących do klasy.
     * @param wordLimit Limit słów używanych w klasyfikatorze.
     * @throws IOException Któraś z podanych ścieżek jest niepoprawna.
     */
    public void learnFromDirectory(
            String positiveExamplesPath,
            String negativeExamplesPath,
            int wordLimit
    ) throws IOException, WekaException {

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

    /**
     * Obsługa komunikatów:
     * {@link sag.message.Request} - wykonaj zadanie klasyfikacji na podanym w komunikacie CV i odeślij odpowiedź {@link sag.message.Response}
     * {@link sag.message.Learn} - wykonaj uczenie klasyfikatorów na zadanym zbiorze danych.
     * @return Receiver komunikatów.
     */
    @Override
    public Receive createReceive() {
        ReceiveBuilder rbuilder = ReceiveBuilder.create();

        // REQUEST - wykonaj klasyfikcję
        rbuilder.match(Request.class, request -> {
            Instance cv = new TextToVector().convert(request.getTextToClassify());
            BigDecimal bayesProb = new BigDecimal(classifierBayes.classifyInstance(cv));
            BigDecimal logisticProb = new BigDecimal(classifierLogistic.classifyInstance(cv));
            getSender().tell(new Response(request.getRequester(), bayesProb, logisticProb, className), getSelf());
            log.info("Classification request received from " + getSender().path().toString() + ". Reply sent. ");
        });

        // LEARN - naucz klasyfikatory
        rbuilder.match(Learn.class, learn -> {
            try {
                learnFromDirectory(
                        learn.getPositiveDataPath(),
                        learn.getNegativeDataPath(),
                        learn.getWordLimit());
                getSender().tell(new LearnReply(true, "Success"), getSelf());
            }
            catch(IOException ioe) {
                getSender().tell(new LearnReply(false, "Invalid data path"), getSelf());
                getContext().stop(getSelf());
            }
            catch(WekaException e) {
                getSender().tell(new LearnReply(false, e.getMessage()), getSelf());
                getContext().stop(getSelf());
            }
        });

        // DEFAULT
        rbuilder.matchAny(o -> log.info(getSelf().path().toString() + " - unknown message type " + o.getClass().getCanonicalName()));

        return rbuilder.build();
    }
}