package sagwedt.classifier;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.stopwords.Rainbow;
import weka.core.stopwords.StopwordsHandler;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.core.tokenizers.Tokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Klasa odpowiedzialna za konwersję tekstu do wektora słów.
 * Każde słowo jest najpierw przefiltrowane przez listę stopwords,
 * a następnie poddane stemmingowi.<br>
 * <br>
 * Domyślne zastosowane algorytmy:
 * <ul><li>uwzględniane są słowa złożone jedynie z liter</li>
 * <li>zastosowany algorytm stemmingu to: Iterated Lovins Stemmer</li>
 * <li>zastosowana lista stopwords: Rainbow</li></ul>
 */
public class TextToVector
{
	StringToWordVector m_filter;
	Instances m_inputStructure;
	
	public static String prefix = "w_";
	
	/**
	 * Tworzy konwerter z domyślnymi ustawieniami.
	 */
	public TextToVector()
	{
		ArrayList<Attribute> attList = new ArrayList<Attribute>();
		attList.add(new Attribute("text", true));
		m_inputStructure = new Instances("text.in", attList, 0);
		
		m_filter = new StringToWordVector();
		m_filter.setAttributeNamePrefix(prefix);
		m_filter.setOutputWordCounts(true);
		m_filter.setStopwordsHandler(new Rainbow());
		m_filter.setTokenizer(new AlphabeticTokenizer());
		m_filter.setStemmer(new IteratedLovinsStemmer());
		m_filter.setSelectedRange("1"); // indexed from 1
	}
	
	/**
	 * Zmienia domyślny użyty algorytm stemmingu.
	 * @param stemmer algorytm stemmingu z biblioteki Weka. Wartość null wyłącza algorytm stemmingu.
	 */
	public void setStemmer(Stemmer stemmer)
	{
		m_filter.setStemmer(stemmer);
	}
	
	/**
	 * Zmienia domyślną użytą listę stopwords.
	 * @param handler lista stopwords z biblioteki Weka. Wartość null wyłącza korzystanie ze stoplisty.
	 */
	public void setStopwordList(StopwordsHandler handler)
	{
		m_filter.setStopwordsHandler(handler);
	}
	
	/**
	 * Zmienia domyślny użyty sposób podziału na słowa.
	 * @param tokenizer algorytm tokenizacji z biblioteki Weka
	 */
	public void setTokenizer(Tokenizer tokenizer)
	{
		m_filter.setTokenizer(tokenizer);
	}
	
	/**
	 * Dokonuje konwersji tekstu na wektor słów.
	 * @param text treść tekstu
	 * @return wektor słów reprezentujący przekazany tekst
	 * @throws Exception wyjątek generowany przez Weka, jeżeli konwersja się nie powiedzie
	 */
	public Instance convert(String text) throws Exception
	{
		// the output format is set only when the batchFinished() method is called for the first time
		// (i.e. for the first batch). The method setInputFormat(...) resets the batch count.
		m_filter.setInputFormat(m_inputStructure);
		
		Instance instance = new DenseInstance(1);
		instance.setDataset(m_inputStructure);
		instance.setValue(0, text);
		
		m_filter.input(instance);
		m_filter.batchFinished();
		
		//TODO: Weka ma taki problem, że stosuje stemmer PRZED listą stopwords. W efekcie lista stopwords kompletnie nie działa. Trzeba będzie ręcznie przefiltrować słowa.
		
		return m_filter.output();
	}
}
