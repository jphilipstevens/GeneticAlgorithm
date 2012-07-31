package selectionAlgorithms;

import geneticAlgorithm.Generation;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import automaton.Automaton;
import automaton.DeltaObject;
import automaton.FObject;


public abstract class AbstractSelection {

	protected int POPULATION_SIZE;
	protected double CROSSOVER_PROB;
	protected double MATING_POOL_FRACTION = 0.75;
	
	protected boolean salmonReproduction;
	protected int survivingPopulation;
	protected Random mRand;
	protected int mAtominaCount;
	protected int mDeltaCount;
	
	
	public AbstractSelection(int atomCount, int deltaCount, int populationSize, double croosoverProb, boolean replacePopulation)
	{
		mAtominaCount = atomCount;
		mDeltaCount = deltaCount;
		CROSSOVER_PROB = croosoverProb;
		POPULATION_SIZE = populationSize;
		mRand = new Random();
		salmonReproduction = replacePopulation;
		if(!salmonReproduction)
		{
			survivingPopulation = (int) Math.round((double) populationSize * MATING_POOL_FRACTION);
		}
	}
	
	public Automaton[] mate(Generation generation)
	{
		Automaton[] population;
		if (salmonReproduction)
		{
			population = salmonReproduction(generation);
		}else
		{
			population = parentSurviveReproduction(generation);
		}
		return population;
	}
	
	public boolean isSalmonReproduction()
	{
		return salmonReproduction;
	}

	public void setSalmonReproduction(boolean salmonReproduction)
	{
		this.salmonReproduction = salmonReproduction;
	}

	protected Automaton crossover(Automaton atom1, Automaton atom2)
	{
		Automaton child = new Automaton(mAtominaCount, false);
		int fn1Pivot = 0;
		Integer[] atom1Fn =  atom1.getFn().getAcceptStates();
		int fn2Pivot = 0;
		if(atom1.getFn().getFnElementCount() > 0)
		{
			fn1Pivot = mRand.nextInt(atom1.getFn().getFnElementCount());	
		}
		if(atom2.getFn().getFnElementCount() > 0)
		{
			fn2Pivot = mRand.nextInt(atom2.getFn().getFnElementCount());	
		}

		Vector<Integer> childFn = new Vector<Integer>();
		for(int i = 0; i < fn1Pivot; i++)
		{
			childFn.add(atom1Fn[i]);
		}
		Integer[] fn2 = atom2.getFn().getAcceptStates();
		for(int i = fn2Pivot; i < fn2.length; i++)
		{
			if(childFn.contains(fn2[i]) == false)
			{
				childFn.add(fn2[i]);
			}
		}
		Integer[] fnArray;
		fnArray = (Integer[])childFn.toArray(new Integer[childFn.size()]);
		FObject childFunction = new FObject(child);

		childFunction.setAccepts(fnArray);

		int deltaPivot = mRand.nextInt(mDeltaCount);

		int[][] childDelta = new int[mDeltaCount][3];
		int[][] atom1Delta = atom1.getDelta().getTransitions();
		int[][] atom2Delta = atom2.getDelta().getTransitions();

		for(int i = 0; i < mDeltaCount; i++)
		{
			if(i < deltaPivot)
			{
				childDelta[i] = atom1Delta[i];
			}else
			{
				childDelta[i] = atom2Delta[i];
			}
		}

		DeltaObject childDeltaO = new DeltaObject(child);
		childDeltaO.setTransitions(childDelta);
		child.setDelta(childDeltaO);
		child.setFn(childFunction);
		return child;
	}
	protected Automaton[] mergeSort(Automaton[] population,
			Map<Automaton, Double> map) {
		/*
		 * function merge_sort(m)
    if length(m) ≤ 1
        return m
    var list left, right, result

    var integer middle = length(m) / 2
    for each x in m up to middle
         add x to left
    for each x in m after middle
         add x to right
    left = merge_sort(left)
    right = merge_sort(right)
    if left.last_item > right.first_item
         result = merge(left, right)
    else
         result = append(left, right)
    return result

		 */
		if(population.length <= 1)
		{
			return population;
		}
		Automaton[] left, right, result;
		int midPoint = population.length/2;
		left = new Automaton[midPoint];
		right = new Automaton[population.length - midPoint];
		result = new Automaton[population.length];
		for(int i = 0; i < midPoint; i++)
		{
			left[i] = population[i];
		}
		for(int i = 0, j = midPoint; j < population.length; i++, j++)
		{
			right[i] = population[j];
		}

		left = mergeSort(left, map);
		right = mergeSort(right, map);
		if(map.get(left[left.length - 1]) < map.get(right[0]))
		{
			result = merge(left, right, map);
		}else
		{
			result = append(left, right);
		}
		return result;
	}

	protected Automaton[] append(Automaton[] left, Automaton[] right) {
		Vector<Automaton> result = new Vector<Automaton>(Arrays.asList(left));
		result.addAll(Arrays.asList(right));
		Automaton[] atomArray;
		atomArray = (Automaton[])result.toArray(new Automaton[result.size()]);
		return atomArray;
	}

	private Automaton[] merge(Automaton[] left, Automaton[] right,
			Map<Automaton, Double> map) {
		/**
		 * function merge(left,right)
    var list result
    while length(left) > 0 and length(right) > 0
        if first(left) ≤ first(right)
            append first(left) to result
            left = rest(left)
        else
            append first(right) to result
            right = rest(right)
    end while
    if length(left) > 0 
        append left to result
    else  
        append right to result
    return result

		 */
		Vector<Automaton> result = new Vector<Automaton>();
		Vector<Automaton> leftV = new Vector<Automaton>(Arrays.asList(left));
		Vector<Automaton> rightV = new Vector<Automaton>(Arrays.asList(right));

		while(leftV.size() > 0 && rightV.size() > 0)
		{
			if(map.get(leftV.get(0)) > map.get(rightV.get(0)))
			{
				result.add(leftV.get(0));
				leftV.remove(0);
			}else
			{
				result.add(rightV.get(0));
				rightV.remove(0);
			}
		}
		if(leftV.size() > 0)
		{
			result.addAll(leftV);
		}else
		{
			result.addAll(rightV);
		}
		Automaton[] atomArray;
		atomArray = (Automaton[])result.toArray(new Automaton[result.size()]);
		return atomArray;
	}
	public double getCrossoverProbability() {
		return CROSSOVER_PROB;
	}
	
	/**
	 * this mating is like salmon, and the parents dont make it to the next generation
	 * @param generation
	 * @return
	 */
	protected abstract Automaton[] salmonReproduction(Generation generation);
	
	/**
	 * The mating pool survives to replenish the population
	 * @param generation
	 * @return
	 */
	protected abstract Automaton[] parentSurviveReproduction(Generation generation);
	
	
}
