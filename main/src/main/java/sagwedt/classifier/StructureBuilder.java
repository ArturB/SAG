package sagwedt.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

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
	//private int m_wordLimit;
	
	public StructureBuilder()
	{
		m_wordCounts = new TreeMap<String, Integer>();
	}
	
	/**
	 * Dodaje wektor słów do pamięci.
	 * @param instance wektor słów
	 */
	public void add(Instance instance)
	{
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
		throw new UnsupportedOperationException("not implemented yet");
		
		/*if(limit < 1)
			m_wordLimit = Integer.MAX_VALUE;
		else
			m_wordLimit = limit;*/
	}
	
	/**
	 * Generuje strukturę klasyfikatora na podstawie dotychczas zebranych wektorów słów.
	 * @return struktura klasyfikatora
	 */
	public TrainingSet generateEmptyTrainingSet()
	{
		//TODO: tymczasowo jest tworzona struktura z nieograniczoną liczbą słów
		
		ArrayList<Attribute> attList = new ArrayList<Attribute>();
		attList.add(new Attribute("positive", Arrays.asList("yes", "no")));
		
		for(Map.Entry<String, Integer> entry: m_wordCounts.entrySet())
			attList.add(new Attribute(entry.getKey(), false));
		
    	return new TrainingSet(new Instances("structure", attList, 0));
		
		/*
		class VSort implements Comparator<Map.Entry<String, Integer>> {
			public int compare(Map.Entry<String, Integer> one, Map.Entry<String, Integer> two) {
				return two.getValue() - one.getValue(); // smallest values first
			}
		}
		
		VSort comp = new VSort();
		Map.Entry<String, Integer> smallestInVector = null;
		SortedSet<Map.Entry<String, Integer>> set = new TreeSet<Map.Entry<String, Integer>>(comp);
		//  5 10 100 150     
		//
		for(Map.Entry<String, Integer> entry: m_wordCounts.entrySet())
		{
			if(set.size() < m_wordLimit)
			{
				set.add(entry);
				
				if(smallestInVector == null)
					smallestInVector = entry;
				else
					smallestInVector = Math.min(smallestInVector, entry.getValue());
			} else if(smallestInVector < entry.getValue())
				set.remove(arg0);
		}
		*/
	}
}
