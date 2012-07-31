package analysis;

import java.util.Vector;

public class FitnessAnalysis
{
	
	private Vector<Double> mAverageFitness;
	private double mPopulationSize;
	public FitnessAnalysis(int populationSize)
	{
		mAverageFitness = new Vector<Double>();
		mPopulationSize = (double)populationSize;
	}
	
	public void addGeneration(Double TotalFitness)
	{
		mAverageFitness.add(TotalFitness);
	}
	
	public double getAverageFitness(int generation)
	{
		return mAverageFitness.get(generation) / mPopulationSize; 
	}
	
	public int totalIterations()
	{
		return mAverageFitness.size();
	}

	public Vector<Double> getmAverageFitness()
	{
		return mAverageFitness;
	}
}
