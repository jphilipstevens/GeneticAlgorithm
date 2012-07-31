package selectionAlgorithms;

import geneticAlgorithm.Generation;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import automaton.Automaton;

public class RouletteWheelSelection extends AbstractSelection
{
	public RouletteWheelSelection(int atomCount, int deltaCount,
			int populationSize, double croosoverProb, boolean replacePopulation)
	{
		super(atomCount, deltaCount, populationSize, croosoverProb, replacePopulation);
	}

	private int binarySearch(int[] sortedList, int valueWanted)
	{
		int low = 0;
		int high = sortedList.length - 1;
		int mid;

		while (low <= high)
		{
			mid = (low + high) / 2;

			if (sortedList[mid] < valueWanted)
				low = mid + 1;
			else if (sortedList[mid] > valueWanted)
				high = mid - 1;
			else
				return mid;
		}
		return low; // NOT_FOUND = -1
	}

	@Override
	protected Automaton[] parentSurviveReproduction(Generation generation)
	{
		Map<Automaton, Double> fitness = generation.getmFitness();
		Automaton[] population = generation.getmPopulation(); // mergeSort(generation.getmPopulation(),
		// fitness);
		// sum all the fitnesses s
		// save interval sums to an array
		// pick a random number btn 0, s
		// /get that automaton
		// do this for the population size

		// randomly select individuals and mate til we fill the new generation
		int[] fitnessSums = new int[POPULATION_SIZE];
		int fitnessTotal = 0;
		for (int i = 0; i < POPULATION_SIZE; i++)
		{
			fitnessTotal += (fitness.get(population[i]))*100.0;
			fitnessSums[i] = fitnessTotal;
		}
		Automaton[] children = new Automaton[POPULATION_SIZE
				- survivingPopulation];
		Vector<Automaton> parents = new Vector<Automaton>();
		for (int i = 0; i < children.length;)
		{
			int randFitness1 = mRand.nextInt(fitnessTotal);
			int randFitness2 = mRand.nextInt(fitnessTotal);
			// do we continue to mate them?
			if (mRand.nextDouble() <= CROSSOVER_PROB)
			{
				int foundIndex1 = binarySearch(fitnessSums, randFitness1);
				int foundIndex2 = binarySearch(fitnessSums, randFitness2);

				children[i] = crossover(population[foundIndex1],
						population[foundIndex2]);
				parents.add(population[foundIndex1]);
				parents.add(population[foundIndex2]);
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
		// need sorting?
		Automaton[] population = generation.getmPopulation();// mergeSort(generation.getmPopulation(),
		// fitness);
		// sum all the fitnesses s
		// save interval sums to an array
		// pick a random number btn 0, s
		// /get that automaton
		// do this for the population size

		// randomly select individuals and mate til we fill the new generation
		int[] fitnessSums = new int[POPULATION_SIZE];
		int fitnessTotal = 0;
		for (int i = 0; i < POPULATION_SIZE; i++)
		{
			fitnessTotal += fitness.get(population[i]) * 100.0;
			fitnessSums[i] = fitnessTotal;
		}
		Automaton[] children = new Automaton[POPULATION_SIZE];
		for (int i = 0; i < POPULATION_SIZE;)
		{
			int randFitness1 = mRand.nextInt(fitnessTotal);
			int randFitness2 = mRand.nextInt(fitnessTotal);
			if (mRand.nextDouble() <= CROSSOVER_PROB)
			{
				int foundIndex1 = binarySearch(fitnessSums, randFitness1);
				int foundIndex2 = binarySearch(fitnessSums, randFitness2);
				children[i] = crossover(population[foundIndex1],
						population[foundIndex2]);
				i++;
			}

		}
		return children;
	}
}
