package selectionAlgorithms;

import geneticAlgorithm.Generation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import automaton.Automaton;


public class TournamentSelection extends AbstractSelection
{
	private int mTournamentSize;
	public TournamentSelection(int atomCount, int deltaCount,
			int populationSize, double croosoverProb, boolean replacePopulation)
	{
		super(atomCount, deltaCount, populationSize, croosoverProb, replacePopulation);
		//pick a tournament size
		mTournamentSize = (int)Math.round(populationSize * 0.25);
	}

	protected Automaton[] salmonReproduction(Generation generation)
	{
		Automaton[] children = new Automaton[POPULATION_SIZE];
		Map<Automaton, Double> fitness = generation.getmFitness();
		Automaton[] population = generation.getmPopulation();
		
		for(int i = 0; i < children.length; i++)
		{
			Automaton parent1 = playTournament(population, fitness);
			Automaton parent2 = playTournament(population, fitness);
			children[i] = crossover(parent1, parent2);
		}
		return children;
	}
	
	@Override
	protected Automaton[] parentSurviveReproduction(Generation generation)
	{
		Automaton[] children = new Automaton[POPULATION_SIZE - survivingPopulation];
		Map<Automaton, Double> fitness = generation.getmFitness();
		Automaton[] population = mergeSort(generation.getmPopulation(), fitness);
		Vector<Automaton> parents = new Vector<Automaton>();
		for(int i = 0; i < children.length; i++)
		{
			Automaton parent1 = playTournament(population, fitness);
			Automaton parent2 = playTournament(population, fitness);
			children[i] = crossover(parent1, parent2);
			parents.add(parent1);
			parents.add(parent2);
		}
		List<Automaton> subparents = Arrays.asList(population).subList(0, survivingPopulation);
		Automaton[] parentArr;
		parentArr = (Automaton[]) subparents.toArray(new Automaton[subparents.size()]);
		return append(parentArr, children);
	}
	
	private Automaton playTournament(Automaton[] population, Map<Automaton, Double> fitness)
	{
		int winnerIndex = mRand.nextInt(POPULATION_SIZE);
		Automaton winner = population[winnerIndex];
		for(int j = 0; j < mTournamentSize - 1; j++)
		{
			int contestantIndex = mRand.nextInt(POPULATION_SIZE);
			if(fitness.get(population[contestantIndex]) > fitness.get(winner))
			{
				winner = population[contestantIndex];
				winnerIndex = contestantIndex;
			}
		}
		return winner;
	}
}
