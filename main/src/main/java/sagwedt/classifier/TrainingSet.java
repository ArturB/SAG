package sagwedt.classifier;

import java.util.ArrayList;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

/**
 * Klasa przechowująca strukturę przyszłego klasyfikatora oraz wszystkie
 * wektory słów, które zostaną później wykorzystane jako dane uczące.<br>
 * <br>
 * Sposób użycia: pusty zbiór uczący otrzymuje się za pomocą metody
 * {@code StructureBuilder.generateEmptyTrainingSet}. Wektory słów do zbioru
 * uczącego dodaje się metodą {@code addToTrainingSet}.
 */
public class TrainingSet
{
	private Instances m_dataset;
	
	/**
	 * Ustawia strukturę tworzonego modelu klasyfikatora.
	 * 
	 * @param structure rządana struktura modelu klasyfikatora
	 */
	public TrainingSet(Instances structure)
	{
		m_dataset = new Instances(structure);
		m_dataset.setClassIndex(0);
	}
	
	/**
	 * Dodaje wskazany wektor słów do zbioru uczącego.
	 * @param instance wektor słów
	 * @param isPositive czy przekazany wektor słów należy do rozpatrywanej przez klasyfikator klasy
	 */
	public void add(Instance instance, boolean isPositive)
	{
		Instance adapted = adaptInstance(instance);
		
		if(isPositive)
			adapted.setValue(0, StructureBuilder.VAL_YES);
		else
			adapted.setValue(0, StructureBuilder.VAL_NO);
		
		m_dataset.add(adapted);
	}
	
	/**
	 * Dokonuje konwersji wektora słów tak, by był postaci zgodnej
	 * ze strukturą zbioru uczącego.
	 * @param instance wektor słów
	 * @return dostosowany wektor słów
	 */
	public Instance adaptInstance(Instance instance)
	{
		// pairs to be inserted into the sparse instance
		ArrayList<Double> values = new ArrayList<Double>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		// tag
		values.add(0.0);
		indices.add(0);
		
		int instanceIndex = 0;
		String instanceString = null;
		
		for(int structIndex = 1; structIndex < m_dataset.numAttributes() && instanceIndex < instance.numAttributes();)
		{
			if(instanceString == null)
				instanceString = instance.attribute(instanceIndex).name();
				
			int cmp = instanceString.compareTo(m_dataset.attribute(structIndex).name());
			
			if(cmp == 0)
			{
				values.add(instance.value(instanceIndex));
				indices.add(structIndex);
				++structIndex;
				++instanceIndex;
				instanceString = null;
			} else if(cmp < 0) // słowo zostało przeskoczone
			{
				++instanceIndex;
				instanceString = null;
			} else
				++structIndex;
		}
		
		double[] attValues = new double[values.size()];
		int[] attIndices = new int[indices.size()];
		
		for(int i=0; i<values.size(); ++i)
		{
			attValues[i] = values.get(i);
			attIndices[i] = indices.get(i);
		}
		
		Instance newInst = new SparseInstance(1.0, attValues, attIndices, m_dataset.numAttributes());
		newInst.setDataset(m_dataset);
		
		return newInst;
	}
	
	public Instances getDataset()
	{
		return m_dataset;
	}
}