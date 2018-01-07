package sag.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Klasa, która generuje ostateczną strukturę klasyfikatora na podstawie przekazanych
 * wektorów słów reprezentujących teksty.<br>
 * <br>
 * Sposób użycia: metodą {@code add} dodaje się wektory słów (wygenerowane przez klasę {@code TextToVector}).
 * Następnie na podstawie zebranych wektorów słów tworzy się pusty zbiór
 * uczący (za pomocą metody {@code generateEmptyTrainingSet}).
 */
public class StructureBuilder
{
	private TreeMap<String, Integer> m_wordCounts;
	private int m_wordLimit;
	private Instances m_calculatedDataset;
	
	public static final String NO = "no";
	public static final String YES = "yes";
	public static final double VAL_NO = 0.0;
	public static final double VAL_YES = 1.0;
	
	/**
	 * Tworzy StructureBuilder z domyślnymi wartościami.<br>
	 * <br>
	 * Domyślnie jest brak ograniczeń na wielkość struktury.
	 */
	public StructureBuilder()
	{
		m_wordCounts = new TreeMap<String, Integer>();
		m_wordLimit = Integer.MAX_VALUE;
		m_calculatedDataset = null;
	}
	
	/**
	 * Dodaje wektor słów do pamięci.
	 * @param instance wektor słów
	 */
	public void add(Instance instance)
	{
		m_calculatedDataset = null;
		
		for(int i=0; i<instance.dataset().numAttributes(); ++i)
		{
			Attribute att = instance.dataset().attribute(i);
			int finalCount = (int)instance.value(i);
			
			if(m_wordCounts.containsKey(att.name()))
				finalCount += m_wordCounts.get(att.name());
			
			m_wordCounts.put(att.name(), finalCount);
		}
	}
	
	/**
	 * Ustawia, ile najczęściej występujących słów z wektorów słów należy być
	 * uwzględnionych w ostatecznej strukturze klasyfikatora.
	 * @param limit limit słów. Dla nieograniczonej ilości słów należy podać liczbę niedodatnią.
	 */
	public void setWordLimit(int limit)
	{
		if(limit < 1)
			limit = Integer.MAX_VALUE;
		
		if(limit != m_wordLimit && m_calculatedDataset != null && limit < m_calculatedDataset.numAttributes())
			m_calculatedDataset = null;
		
		m_wordLimit = limit;
	}
	
	/**
	 * Generuje strukturę klasyfikatora na podstawie dotychczas zebranych wektorów słów.
	 * Struktura powinna zawierać co najmniej jedno słowo.
	 * @return struktura klasyfikatora
	 */
	public TrainingSet generateEmptyTrainingSet()
	{
		if(m_wordCounts.isEmpty())
			throw new IllegalStateException("no words in the structure");
		
		if(m_calculatedDataset != null)
			return new TrainingSet(m_calculatedDataset);
		
		TreeMap<String, Integer> destDict;
		
		if(m_wordLimit >= m_wordCounts.size())
			destDict = m_wordCounts;
		else
		{
			/*
			
			Example: limit = 3
			a - 4, b - 10, c - 1, d - 4, e - 6, f - 80
			
			f - 80, b - 10, e - 6, d - 4, a - 4, c - 1 // sort by frequency
			f - 80, b - 10, e - 6                      // truncate
			b - 10, e - 6, f - 80                      // sort by word
			
			*/
			
			class SortPair implements Comparable<SortPair>
			{
				public int count;
				public String word;
				
				public SortPair(int _count, String _word)
				{
					count = _count;
					word = _word;
				}
				
				public int compareTo(SortPair pair)
				{
					if(count == pair.count)
						return word.compareTo(pair.word);
					return Integer.compare(count, pair.count);
				}
			}
			
			TreeSet<SortPair> sortSet = new TreeSet<SortPair>();
			SortPair smallest = null;
			
			for(Map.Entry<String, Integer> entry: m_wordCounts.entrySet())
			{
				SortPair pair = new SortPair(entry.getValue(), entry.getKey());
				
				if(sortSet.size() < m_wordLimit)
				{
					sortSet.add(pair);
					
					if(smallest == null || pair.compareTo(smallest) < 0)
						smallest = pair;
				} else if(smallest.compareTo(pair) < 0)
				{
					sortSet.remove(smallest);
					sortSet.add(pair);
					smallest = sortSet.first();
				}
			}
			
			// ----
			
			destDict = new TreeMap<String, Integer>();
			
			for(SortPair pair: sortSet)
				destDict.put(pair.word, pair.count);
		}
		
		// --------------
		
		ArrayList<Attribute> attList = new ArrayList<Attribute>();
		attList.add(new Attribute("positive", Arrays.asList(NO, YES)));
		
		for(Map.Entry<String, Integer> entry: destDict.entrySet())
			attList.add(new Attribute(entry.getKey(), false));
		
		m_calculatedDataset = new Instances("structure", attList, 0);
    	return new TrainingSet(m_calculatedDataset);
	}
}
