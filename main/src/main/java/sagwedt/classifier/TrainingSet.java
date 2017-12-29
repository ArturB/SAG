package sagwedt.classifier;

import weka.core.Instance;
import weka.core.Instances;

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
		//TODO: 
		throw new UnsupportedOperationException("not implemented yet");
		
		// apply the instance to the dataset

		/*
		 * dataset structure:
		 * {positive, w_abcd, w_foo, w_qwerty, w_xyz}
		 * instance vector:
		 * {w_abcd, w_qwerty}
		 */
		
		/*int structIndex = 1; // omitting the tag
		int instanceIndex = 0;
		
		ArrayList<Pair<Integer, Integer>> values = new ArrayList<Integer>();
		
		Instance newInst = new SparseInstance()*/
	}
	
	/**
	 * Dokonuje konwersji wektora słów tak, by był postaci zgodnej
	 * ze strukturą zbioru uczącego.
	 * @param instance wektor słów
	 * @return dostosowany wektor słów
	 */
	public Instance adaptInstance(Instance instance)
	{
		//TODO:
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public Instances getDataset()
	{
		return m_dataset;
	}
}
