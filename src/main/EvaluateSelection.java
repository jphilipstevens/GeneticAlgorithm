package main;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import analysis.FitnessAnalysis;
import automaton.Automaton;
import automaton.DeltaObject;
import automaton.FObject;

import geneticAlgorithm.GeneticAlgo;
import geneticAlgorithm.GeneticAlgo.SelectionType;

public class EvaluateSelection
{
	private static int GA_ITERATIONS = 5;
	private boolean mReplacePop;
	private int mPopSize;
	private boolean[] mAccepts;
	private String[] mBinString;
	private int mAutomatonCount;
	private String timeStamp;

	public EvaluateSelection(boolean replacePop, int popSize, int autominaCount, boolean[] accepts, String[] binString)
	{
		mReplacePop = replacePop;
		mPopSize = popSize;
		mAccepts = accepts;
		mBinString = binString;
		mAutomatonCount = autominaCount;
		Calendar cal = Calendar.getInstance();
		timeStamp = new String(cal.get(cal.DAY_OF_MONTH) + "-" + cal.get(cal.HOUR_OF_DAY) + "_" + cal.get(cal.MINUTE) + "_" + cal.get(cal.SECOND) + ".data");
	}


	public void evaluate()
	{
		int crossOverNum = SelectionType.BEST_PROPORTION.getIndex();
		/*for (; crossOverNum <= SelectionType.RANK.getIndex(); crossOverNum++)
		{
			runSelectionTest(crossOverNum);
		}*/
		
		runSelectionTest(crossOverNum);
		
		
		
	}
	private void runSelectionTest(int crossOverNum)
	{
		for (int runs = 0; runs < GA_ITERATIONS; runs++)
		{
			GeneticAlgo algorithm = new GeneticAlgo(mPopSize, mAutomatonCount, mAccepts, mBinString, SelectionType.getSelection(crossOverNum), mReplacePop);
			Calendar cal = Calendar.getInstance();
			long time1 = cal.getTimeInMillis();
			Vector<Automaton> solutions = algorithm.solve(false, mAccepts);
			cal = Calendar.getInstance();
			long time2 = cal.getTimeInMillis() - time1;
			generateStatsFile(SelectionType.getSelection(crossOverNum), algorithm, time2);
			generateSolutionsFile(SelectionType.getSelection(crossOverNum), solutions);
		}
	}
	private void generateStatsFile(SelectionType type, GeneticAlgo algo, long runTime)
	{
		/**
		 * run format format <selection type>;iter runtime {avgFit1 avgFit2 ...avgfitN}; 
		 * <selection type>;iter runtime {avgFit1 avgFit2 ...avgfitN};
		 */
		StringBuffer outBuf = new StringBuffer("\n");
		outBuf.append(algo.getmIteration()).append(' ').append(runTime);
		StringBuffer avgFitBuf = new StringBuffer("\n");
		FitnessAnalysis fa = algo.getMfitnessAnalysis();
		for(int i = 0; i < fa.totalIterations();i++)
		{
			avgFitBuf.append(fa.getmAverageFitness().get(i)).append(' ');
		}
		outBuf.append('\n').append(avgFitBuf);
		System.out.println(outBuf.toString());

		FileWriter wstream = null;
		try
		{
			wstream = new FileWriter("/home/jono/workspace/CIS6420Project/Data/" + timeStamp + ".data", true);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter wbuf = new BufferedWriter(wstream);
		
		try
		{
			wbuf.write(outBuf.toString());
			wbuf.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @param type
	 * @param solutions
	 */
	private void generateSolutionsFile(SelectionType type, Vector<Automaton> solutions)
	{
		Vector<Integer> atomcount = new Vector<Integer>();
		Vector<Automaton> distinctAtoms = new Vector<Automaton>();
		/**
		 * format 
		 * <selection type> {<automaton>} count;{<automaton>} count;{<automaton>} count; <selection type> {<automaton>} count;
		 * 
		 * an <automaton>
		 * <fn1 fn2 ... fnN> <deltaEnd1 deltaend2...deltaN>
		 */
		// count the distinct atoms
		while (solutions.size() > 0)
		{
			Automaton refAtom = solutions.remove(0);
			distinctAtoms.add(refAtom);
			int count = 1;
			for (int j = 1; j < solutions.size();)
			{
				if (solutions.get(j).equals(refAtom))
				{
					count++;
					solutions.remove(j);
				}else
				{
					j++;
				}
			}
			atomcount.add(count);
		}
		StringBuffer outBuf;
		if(distinctAtoms.size() ==0)
		{
			outBuf = new StringBuffer("nothing found");
		}else
		{
			outBuf = new StringBuffer();
		}
		for(int index = 0; index < distinctAtoms.size(); index++)
		{
			Automaton atom = distinctAtoms.get(index);
			StringBuffer atomBuf = new StringBuffer("\n");
			//for one atom
			StringBuffer fnBuf = new StringBuffer("<");
			FObject fn = atom.getFn();
			for(int fnIndex = 0; fnIndex < fn.getFnElementCount(); fnIndex++)
			{
				fnBuf.append(fn.getAcceptStates()[fnIndex]).append(' ');
			}
			fnBuf.replace(fnBuf.length()-1, fnBuf.length(), ">");

			StringBuffer deltaBuf = new StringBuffer("<");
			DeltaObject delta = atom.getDelta();
			for(int deltaIndex = 0; deltaIndex < delta.getTransitionCount(); deltaIndex++)
			{
				deltaBuf.append(delta.getEndState(deltaIndex)).append(' ');
			}
			deltaBuf.replace(deltaBuf.length()-1, deltaBuf.length(), ">");
			atomBuf.append(fnBuf).append(deltaBuf).append('}').append(" " + atomcount.get(index)).append('\n');
			outBuf.append(atomBuf);
		}

		System.out.println(outBuf);
		
		FileWriter wstream = null;
		try
		{
			wstream = new FileWriter("/home/jono/workspace/CIS6420Project/Data/" + timeStamp + ".solutions.data", true);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter wbuf = new BufferedWriter(wstream);
		
		try
		{
			wbuf.write(outBuf.toString());
			wbuf.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
