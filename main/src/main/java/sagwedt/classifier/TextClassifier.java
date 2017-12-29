package sagwedt.classifier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;

/**
 * Klasyfikator binarny dokonujący klasyfikacji wektorów słów.<br>
 * <br>
 * Sposób użycia: najpierw należy stworzyć strukturę klasyfikatora (klasa {@code StructureBuilder}).
 * Na jej podstawie tworzy się instancję tej klasy (jako argument należy podać algorytm klasyfikacji -
 * aktualnie albo {@code TextClassifier.NAIVE_BAYES} albo {@code TextClassifier.LOGISTIC_REGRESSION}.
 * Tak utworzony klasyfikator jest na razie w fazie uczenia się. Wektory słów będących danymi trenującymi
 * dodaje się metodą {@code addToTrainingSet}. By zakończyć fazę uczenia należy wywołać metodę
 * {@code buildClassifier}. Tak utworzony klasyfikator następnie się odpytuje za pomocą metody
 * {@code classifyInstance}.
 */
public class TextClassifier
{
	/** Wybrane dostępne algorytmy klasyfikacji */
	public enum Algorithm {
		NAIVE_BAYES,
		LOGISTIC_REGRESSION
	}
	
	private AbstractClassifier m_classifier;
	private TrainingSet m_trainingSet;
	
	/**
	 * Na podstawie podanego zbioru uczącego oraz algorytmu klasyfikacji z biblioteki
	 * Weka generuje ostateczny klasyfikator.
	 * @param trainingSet zbiór uczący
	 * @param classifier wybrany algorytm klasyfikacji z biblioteki Weka
	 * @throws Exception 
	 */
	public TextClassifier(TrainingSet trainingSet, AbstractClassifier classifier) throws Exception
	{
		m_trainingSet = trainingSet;
		m_classifier = classifier;
		m_classifier.buildClassifier(m_trainingSet.getDataset());
	}
	
	/**
	 * Na podstawie podanego zbioru uczącego oraz algorytmu klasyfikacji
	 * generuje ostateczny klasyfikator.
	 * @param trainingSet zbiór uczący
	 * @param classifierType wybrany algorytm klasyfikacji
	 * @throws Exception 
	 */
	public TextClassifier(TrainingSet trainingSet, Algorithm classifierType) throws Exception
	{
		this(trainingSet, enumToClassifier(classifierType));
	}
	
	private static AbstractClassifier enumToClassifier(Algorithm classifierType)
	{
		switch(classifierType)
		{
			case NAIVE_BAYES: return new NaiveBayes();
			case LOGISTIC_REGRESSION: return new Logistic();
		}
		
		throw new IllegalArgumentException("Wrong classifier enum");
	}
	
	/**
	 * Tworzy klasyfikator na podstawie zawartości strumienia danych
	 * @param in strumień danych zawierający zapisany klasyfikator
	 */
	public static TextClassifier loadClassifier(InputStream in)
	{
		//TODO:
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	/**
	 * Tworzy klasyfikator na podstawie zawartości pliku na dysku.
	 * 
	 * @param path ścieżka do pliku z zapisanym klasyfikatorem.
	 * @throws FileNotFoundException jeżeli nastąpił problem z wczytywaniem pliku.
	 */
	public static TextClassifier loadClassifier(String path) throws FileNotFoundException
	{
		return loadClassifier(new FileInputStream(path));
	}
	
	/**
	 * Zapisuje model klasyfikatora do pliku.
	 * @param path ścieżka do pliku, w którym zostanie zapisany model klasyfikatora
	 * @throws FileNotFoundException jeżeli nastąpił błąd podczas zapisu modelu do pliku
	 */
	public void dumpClassifier(String path) throws FileNotFoundException
	{
		dumpClassifier(new FileOutputStream(path));
	}
	
	/**
	 * Zrzuca model klasyfikatora do strumienia danych.
	 * @param out strumień danych, do którego zostanie zapisany model klasyfikatora
	 */
	public void dumpClassifier(OutputStream out)
	{
		// TODO:
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	/**
	 * Zwraca prawdopodobieństwo tego, że podany wektor słów
	 * należy do klasy decyzyjnej obsługiwanej przez klasyfikator.
	 * @param instance wektor słów
	 * @return wyznaczona wartość prawdopodobieństwa z zakresu od 0.0 do 1.0
	 */
	public double classifyInstance(Instance instance)
	{
		// TODO:
		throw new UnsupportedOperationException("not implemented yet");
	}
}
