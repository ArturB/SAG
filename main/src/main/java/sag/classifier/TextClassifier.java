package sag.classifier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

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
	public static void main(String[] args)
	{
		TextToVector conv = new TextToVector();
		conv.setStemmer(null);
		conv.setStopwordList(null);
		
		Instance cv = conv.convert("a b c b c b b x");
		
		StructureBuilder builder = new StructureBuilder();
		builder.add(cv);
		
		TrainingSet set = builder.generateEmptyTrainingSet();
		
		try
		{
			TextClassifier bayes = new TextClassifier(set, Algorithm.NAIVE_BAYES);
			System.out.println(bayes.classifyInstance(cv));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private AbstractClassifier m_classifier;
	private TrainingSet m_trainingSet;
	
	private TextClassifier() {}
	
	/**
	 * Na podstawie podanego zbioru uczącego oraz algorytmu klasyfikacji z biblioteki
	 * Weka generuje ostateczny klasyfikator. Zbiór uczący powinien zawierać co najmniej
	 * jeden wektor słów.
	 * @param trainingSet zbiór uczący
	 * @param classifier wybrany algorytm klasyfikacji z biblioteki Weka
	 * @throws WekaException kiedy zbudowanie klasyfikatora przez bibliotekę Weka nie powiodło się
	 */
	public TextClassifier(TrainingSet trainingSet, AbstractClassifier classifier) throws WekaException
	{
		Instances dataset = m_trainingSet.getDataset();
		
		if(dataset.numInstances() == 0)
			throw new IllegalArgumentException("training set must have at least one word vector");
		
		m_trainingSet = trainingSet;
		m_classifier = classifier;
		
		try
		{
			m_classifier.buildClassifier(dataset);
		} catch(Exception e)
		{
			throw new WekaException(e);
		}
	}
	
	/**
	 * Na podstawie podanego zbioru uczącego oraz algorytmu klasyfikacji
	 * generuje ostateczny klasyfikator.
	 * @param trainingSet zbiór uczący
	 * @param classifierType wybrany algorytm klasyfikacji
	 * @throws WekaException kiedy zbudowanie klasyfikatora przez bibliotekę Weka nie powiodło się
	 */
	public TextClassifier(TrainingSet trainingSet, Algorithm classifierType) throws WekaException
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
	 * @throws IOException jeżeli wczytanie klasyfikatora ze strumienia nie powiedzie się
	 */
	public static TextClassifier loadClassifier(InputStream in) throws IOException
	{
		try
		{
			Object[] objs = SerializationHelper.readAll(in);	
			TextClassifier classifier = new TextClassifier();
			
			classifier.m_classifier = (AbstractClassifier)objs[0];
			classifier.m_trainingSet = (TrainingSet)objs[1];
			
			return classifier;
		} catch(Exception e)
		{
			throw new IOException(e);
		}
	}
	
	/**
	 * Tworzy klasyfikator na podstawie zawartości pliku na dysku.
	 * 
	 * @param path ścieżka do pliku z zapisanym klasyfikatorem.
	 * @throws FileNotFoundException jeżeli nastąpił problem z wczytywaniem danych z pliku
	 * @throws IOException jeżeli wczytanie klasyfikatora ze strumienia nie powiedzie się
	 */
	public static TextClassifier loadClassifier(String path) throws FileNotFoundException, IOException
	{
		return loadClassifier(new FileInputStream(path));
	}
	
	/**
	 * Zapisuje model klasyfikatora do pliku.
	 * @param path ścieżka do pliku, w którym zostanie zapisany model klasyfikatora
	 * @throws FileNotFoundException jeżeli nastąpił błąd podczas zapisu modelu do pliku
	 * @throws IOException jeżeli wczytanie klasyfikatora ze strumienia nie powiedzie się
	 */
	public void dumpClassifier(String path) throws FileNotFoundException, IOException
	{
		dumpClassifier(new FileOutputStream(path));
	}
	
	/**
	 * Zrzuca model klasyfikatora do strumienia danych.
	 * @param out strumień danych, do którego zostanie zapisany model klasyfikatora
	 * @throws IOException jeżeli wczytanie klasyfikatora ze strumienia nie powiedzie się
	 */
	public void dumpClassifier(OutputStream out) throws IOException
	{
		try
		{
			SerializationHelper.writeAll(out, new Object[] {m_classifier, m_trainingSet});
		} catch(Exception e)
		{
			throw new IOException(e);
		}
	}
	
	/**
	 * Zwraca prawdopodobieństwo tego, że podany wektor słów
	 * należy do klasy decyzyjnej obsługiwanej przez klasyfikator.
	 * @param instance wektor słów
	 * @return wyznaczona wartość prawdopodobieństwa z zakresu od 0.0 do 1.0
	 * @throws WekaException jeżeli wyznaczenie wartości prawdopodobieństwa przez bibliotekę Weka nie powiedzie się 
	 */
	public double classifyInstance(Instance instance) throws WekaException
	{
		try
		{
			double[] dist = m_classifier.distributionForInstance(m_trainingSet.adaptInstance(instance));
			return dist[1];
		} catch(Exception e)
		{
			throw new WekaException(e);
		}
	}
}
