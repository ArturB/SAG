package sagwedt.classifier;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.stopwords.Rainbow;
import weka.core.stopwords.StopwordsHandler;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.core.tokenizers.Tokenizer;

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
	Stemmer m_stemmer;
	Tokenizer m_tokenizer;
	StopwordsHandler m_stopwordsHandler;
	
	public static String prefix = "w_";
	
	/**
	 * Tworzy konwerter z domyślnymi ustawieniami.
	 */
	public TextToVector()
	{
		m_stemmer = new IteratedLovinsStemmer();
		m_tokenizer = new AlphabeticTokenizer();
		m_stopwordsHandler = new Rainbow();
	}
	
	/**
	 * Zmienia domyślny użyty algorytm stemmingu.
	 * @param stemmer algorytm stemmingu z biblioteki Weka. Wartość null wyłącza algorytm stemmingu.
	 */
	public void setStemmer(Stemmer stemmer)
	{
		m_stemmer = stemmer;
	}
	
	/**
	 * Zmienia domyślną użytą listę stopwords.
	 * @param handler lista stopwords z biblioteki Weka. Wartość null wyłącza korzystanie ze stoplisty.
	 */
	public void setStopwordList(StopwordsHandler handler)
	{
		m_stopwordsHandler = handler;
	}
	
	/**
	 * Zmienia domyślny użyty sposób podziału na słowa.
	 * @param tokenizer algorytm tokenizacji z biblioteki Weka
	 */
	public void setTokenizer(Tokenizer tokenizer)
	{
		if(tokenizer == null)
			throw new IllegalArgumentException("tokenizer should be non-null");
		m_tokenizer = tokenizer;
	}
	
	/**
	 * Dokonuje konwersji tekstu na wektor słów.
	 * @param text treść tekstu
	 * @return wektor słów reprezentujący przekazany tekst
	 */
	public Instance convert(String text)
	{
		// word -> number of occurences, sorted lexicographically
		TreeMap<String, Integer> dictionary = new TreeMap<String, Integer>();
		
		m_tokenizer.tokenize(text);

		while(m_tokenizer.hasMoreElements())
		{
			String word = m_tokenizer.nextElement().toLowerCase();
			
			if(m_stopwordsHandler != null && m_stopwordsHandler.isStopword(word))
				continue;
			
			if(m_stemmer != null)
				word = m_stemmer.stem(word);
			
			if(dictionary.containsKey(word))
				dictionary.put(word, dictionary.get(word) + 1);
			else
				dictionary.put(word, 1);
		}
		
		// ----------
		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		int index = 0;
		double[] values = new double[dictionary.size()];
		int[] indices = new int[dictionary.size()];
		
		for(Map.Entry<String, Integer> entry: dictionary.entrySet())
		{
			attributes.add(new Attribute(entry.getKey(), false));
			values[index] = (double)entry.getValue();
			indices[index] = index;
			++index;
		}
		
		Instances dataset = new Instances("wordvector", attributes, 0);
		Instance instance = new SparseInstance(1.0, values, indices, dictionary.size());
		instance.setDataset(dataset);
		
		return instance;
	}
}