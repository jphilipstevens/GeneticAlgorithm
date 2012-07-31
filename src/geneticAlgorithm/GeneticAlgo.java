package geneticAlgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import selectionAlgorithms.AbstractSelection;
import selectionAlgorithms.BestProportionSelection;
import selectionAlgorithms.RankSelection;
import selectionAlgorithms.RouletteWheelSelection;
import selectionAlgorithms.TournamentSelection;

import constants.ApplicationConstants;

import analysis.FitnessAnalysis;
import automaton.Automaton;


/**
 * There are 3^8 for delta and 16 for fn = 104976 possible automton
 * 
 * @author jono
 * 
 */
public class GeneticAlgo
{
	private int POPULATION_SIZE;
	private static final Double MAX_FITNESS = new Double(1.0);
	/**
	 * convienience accessors for constants. Saves cpu time over method calls
	 */
	private int mAtominaCount;
	private int mDeltaCount;

	// mutator
	// fitness
	// crossover
	// population
	// correct accepts
	// properStrings
	private Generation mGeneration;
	private String[] allBinaryNumbers;
	private boolean[] mAcceptingValues;
	private Mutator mMutator;
	private AbstractSelection mCrossover;
	private int mIteration = 1;
	Vector<Automaton> mWinningAtom;
	boolean mSolutionFound;
	FitnessAnalysis mfitnessAnalysis;

	public enum SelectionType
	{
		BEST_PROPORTION(0), ROULETTE(1), TOURNAMENT(2), RANK(3);

		public static SelectionType getSelection(int edge)
		{
			switch (edge)
			{
			case 1:
				return ROULETTE;
			case 2:
				return TOURNAMENT;
			case 3:
				return RANK;
			default:
				return BEST_PROPORTION;
			}
		}

		private final int index;

		private SelectionType(int index)
		{
			this.index = index;
		}

		public final int getIndex()
		{
			return index;
		}
	}

	public GeneticAlgo(int popSize, int autominaCount, boolean[] accepts, String[] binString, SelectionType type, boolean replacePopulation)
	{
		POPULATION_SIZE = popSize;
		mAtominaCount = autominaCount;
		mDeltaCount = mAtominaCount * 2;
		double crossProb = 0.70;
		if (type == SelectionType.ROULETTE)
		{
			mCrossover = new RouletteWheelSelection(mAtominaCount, mDeltaCount, POPULATION_SIZE, crossProb, replacePopulation);
		} else if (type == SelectionType.TOURNAMENT)
		{
			mCrossover = new TournamentSelection(mAtominaCount, mDeltaCount, POPULATION_SIZE, crossProb, replacePopulation);
		} else if (type == SelectionType.RANK)
		{
			mCrossover = new RankSelection(mAtominaCount, mDeltaCount, POPULATION_SIZE, crossProb, replacePopulation);
		} else
		{
			mCrossover = new BestProportionSelection(mAtominaCount, mDeltaCount, POPULATION_SIZE, crossProb, replacePopulation);
		}
		mAcceptingValues = new boolean[accepts.length];
		for (int i = 0; i < accepts.length; i++)
		{
			mAcceptingValues[i] = accepts[i];
		}

		allBinaryNumbers = new String[binString.length];
		for (int i = 0; i < binString.length; i++)
		{
			allBinaryNumbers[i] = binString[i];
		}
		mMutator = new Mutator();
		// generate generation 0
		Automaton[] population = new Automaton[POPULATION_SIZE];
		for (int i = 0; i < POPULATION_SIZE;)
		{
			population[i] = new Automaton(autominaCount, true);
			// lets make sure the random one does not equal the solution we
			// know.
			if (Automaton.equalsBaseAutomaton(population[i]) == false)
			{
				i++;
			}

		}
		Map<Automaton, Double> fitness = new HashMap<Automaton, Double>();
		for (Automaton atom : population)
		{
			Double fitnessValue = calculateFitness(atom, mAcceptingValues, binString);
			// System.out.println(fitnessValue.doubleValue());
			fitness.put(atom, fitnessValue);

		}
		mGeneration = new Generation(fitness, population);
		mWinningAtom = new Vector<Automaton>();
		System.out.println("GE stats:\ncrossover prob: " + mCrossover.getCrossoverProbability() + "\nmutation prob: " + Mutator.MUTATION_PROB);
		mSolutionFound = false;
		mfitnessAnalysis = new FitnessAnalysis(POPULATION_SIZE);
	}

	public static Double calculateFitness(Automaton atom, boolean[] b, String[] binString)
	{
		int count = 0;
		for (int i = 0; i < b.length; i++)
		{
			boolean val = atom.evaluateString(binString[i]);
			if (val == b[i])
			{
				count++;
			}
		}
		return new Double(((double) count) / ((double) b.length));
	}

	public Vector<Automaton> solve(boolean useCrossover, boolean[] accepts)
	{
		Vector<Automaton> newPopulation;
		Map<Automaton, Double> newFitness;
		double popFitness = 0;
		double topFitness = 0;
		while (!mSolutionFound)
		{
			mIteration++;
			topFitness = 0;
			if (ApplicationConstants.DEBUG)
			{
				System.out.println(mIteration);
			}
			// setup next generation stuff
			newPopulation = new Vector<Automaton>();
			newFitness = new HashMap<Automaton, Double>();

			/*
			 * //generate mating pool System.out.println("pre sorted list");
			 * for(Automaton atom : population) {
			 * System.out.println(mGeneration.getmFitness().get(atom)); }
			 * Automaton[] sortedList = mergeSort(population,
			 * mGeneration.getmFitness()); System.out.println("Sorted List");
			 * for(Automaton atom : sortedList) {
			 * System.out.println(mGeneration.getmFitness().get(atom)); }
			 */
			// perform crossover
			Automaton[] newGenerationMatingPool = null;
			try
			{
				newGenerationMatingPool = mCrossover.mate(mGeneration);
			} catch (java.lang.NegativeArraySizeException e)
			{
				e.printStackTrace();
				return null;
			}
			/*
			 * System.out.println("new Generation"); for(Automaton atom :
			 * newGenerationMatingPool) {
			 * System.out.println(calculateFitness(atom, mAcceptingValues,
			 * allBinaryNumbers)); } System.exit(0);
			 */
			for (Automaton atom : newGenerationMatingPool)
			{
				Automaton newAtom = mMutator.mutate(atom);
				newPopulation.add(newAtom);
				Double fitness = calculateFitness(newAtom, mAcceptingValues, allBinaryNumbers);
				
				popFitness += fitness.doubleValue();
				newFitness.put(newAtom, fitness);
				if(fitness > topFitness)
				{
					topFitness = fitness;
				}
				// System.out.println(fitness.doubleValue());
				if (fitness.compareTo(MAX_FITNESS) == 0)
				{
					mSolutionFound = true;
					mWinningAtom.add(newAtom);
				}
			}
			if (ApplicationConstants.DEBUG)
			{
				System.out.println("population Fitness " + popFitness);
			}
			mfitnessAnalysis.addGeneration(topFitness);
			popFitness = 0;
			Automaton[] atomArray;
			atomArray = (Automaton[]) newPopulation.toArray(new Automaton[newPopulation.size()]);
			Generation newGeneration = new Generation(newFitness, atomArray);
			mGeneration = newGeneration;
			if (mSolutionFound && mIteration > 1)
			{
				if (ApplicationConstants.DEBUG)
				{
					System.out.print("Solution found with iterations totaling :");
				}
				System.out.print(mIteration);
			}
		}
		return mWinningAtom;
	}

	public Mutator getmMutator()
	{
		return mMutator;
	}

	public Generation getmGeneration()
	{
		return mGeneration;
	}

	public void setmGeneration(Generation mGeneration)
	{
		this.mGeneration = mGeneration;
	}

	public int getmIteration()
	{
		return mIteration;
	}

	public void setmIteration(int mIteration)
	{
		this.mIteration = mIteration;
	}

	public AbstractSelection getmCrossover()
	{
		return mCrossover;
	}

	public FitnessAnalysis getMfitnessAnalysis()
	{
		return mfitnessAnalysis;
	}
	
	

}