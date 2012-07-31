package geneticAlgorithm;
import java.util.HashMap;
import java.util.Map;

import automaton.Automaton;

public class Generation
{
	Map<Automaton, Double> mFitness;
	Automaton[] mPopulation;

	public Generation(Map<Automaton, Double> fitness, Automaton[] population)
	{
		this.mFitness = new HashMap<Automaton, Double>(fitness);
		this.mPopulation = population;
		mPopulation = new Automaton[population.length];
		for (int i = 0; i < population.length; i++)
		{
			mPopulation[i] = population[i];
		}
	}

	public Map<Automaton, Double> getmFitness()
	{
		return mFitness;
	}

	public void setmFitness(Map<Automaton, Double> mFitness)
	{
		this.mFitness = mFitness;
	}

	public Automaton[] getmPopulation()
	{
		return mPopulation;
	}

	public void setmPopulation(Automaton[] mPopulation)
	{
		this.mPopulation = mPopulation;
	}

}
