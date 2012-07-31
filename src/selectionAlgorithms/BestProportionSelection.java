package selectionAlgorithms;
import geneticAlgorithm.Generation;
import automaton.Automaton;

public class BestProportionSelection extends AbstractSelection
{


	public BestProportionSelection(int atomCount, int deltaCount,
			int populationSize, double croosoverProb, boolean replacePopulation)
	{
		super(atomCount, deltaCount, populationSize, croosoverProb, replacePopulation);
	}


	@Override
	protected Automaton[] parentSurviveReproduction(Generation generation)
	{
		Automaton[] population = mergeSort(generation.getmPopulation(),
				generation.getmFitness());
		int childrenPop = POPULATION_SIZE - survivingPopulation;
		Automaton[] children = new Automaton[childrenPop];
		int mate1Index = 0;
		int mate2Index = 0;
		for (int i = 0; i < children.length;)
		{
			mate1Index = mRand.nextInt(survivingPopulation);
			mate2Index = mRand.nextInt(survivingPopulation);
			if (mRand.nextDouble() <= CROSSOVER_PROB)
			{
				children[i] = crossover(population[mate1Index],
						population[mate2Index]);
				i++;
			}
		}
		for (int i = 0, j = 0; i < population.length; i++)
		{
			if (i > survivingPopulation)
			{
				population[i] = children[j];
				j++;
			}
		}
		return population;
	}

	@Override
	protected Automaton[] salmonReproduction(Generation generation)
	{
		Automaton[] population = mergeSort(generation.getmPopulation(),
				generation.getmFitness());
		Automaton[] children = new Automaton[POPULATION_SIZE];
		int mate1Index = 0;
		int mate2Index = 0;
		for (int i = 0; i < children.length;)
		{
			mate1Index = mRand.nextInt(survivingPopulation);
			mate2Index = mRand.nextInt(survivingPopulation);
			if (mRand.nextDouble() <= CROSSOVER_PROB)
			{
				children[i] = crossover(population[mate1Index],
						population[mate2Index]);
				i++;
			}
		}
		
		return children;
	}
}
