package selectionAlgorithms;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import geneticAlgorithm.Generation;
import automaton.Automaton;

public class RankSelection extends AbstractSelection
{

	public RankSelection(int atomCount, int deltaCount, int populationSize,
			double croosoverProb, boolean replacePopulation)
	{
		super(atomCount, deltaCount, populationSize, croosoverProb, replacePopulation);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Automaton[] parentSurviveReproduction(Generation generation)
	{
		Map<Automaton, Double> fitness = generation.getmFitness();
		Automaton[] population = mergeSort(generation.getmPopulation(), fitness);

		Automaton[] children = new Automaton[POPULATION_SIZE
				- survivingPopulation];
		Vector<Automaton> parents = new Vector<Automaton>();
		for (int i = 0; i < children.length;)
		{
			int parent1 = mRand.nextInt(POPULATION_SIZE);
			int parent2 = mRand.nextInt(POPULATION_SIZE);
			if (mRand.nextDouble() <= CROSSOVER_PROB)
			{
				children[i] = crossover(population[parent1],
						population[parent2]);
				parents.add(population[parent1]);
				parents.add(population[parent2]);
				i++;
			}
		}
		List<Automaton> subparents = parents.subList(0, survivingPopulation);
		Automaton[] parentArr;
		parentArr = (Automaton[]) subparents.toArray(new Automaton[subparents
				.size()]);
		return append(parentArr, children);
	}

	@Override
	protected Automaton[] salmonReproduction(Generation generation)
	{
		Map<Automaton, Double> fitness = generation.getmFitness();
		Automaton[] population = mergeSort(generation.getmPopulation(), fitness);
		Automaton[] children = new Automaton[POPULATION_SIZE];
		for (int i = 0; i < children.length;)
		{
			if (mRand.nextDouble() <= CROSSOVER_PROB)
			{
				int parent1 = mRand.nextInt(POPULATION_SIZE);
				int parent2 = mRand.nextInt(POPULATION_SIZE);
				children[i] = crossover(population[parent1],
						population[parent2]);
				i++;
			}
		}
		return children;
	}

}
