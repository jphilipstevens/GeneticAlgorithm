package automaton;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class FObject extends AutomatonFunction
{
	Vector<Integer> mStrings = new Vector<Integer>();

	public FObject(Automaton parent)
	{
		super(parent);
		setDefaultState();
	}

	public FObject(FObject fn)
	{
		super(fn.getmParent());
		mStrings.addAll(fn.getmStrings());
	}

	public Vector<Integer> getmStrings()
	{
		return mStrings;
	}

	private void setDefaultState()
	{
		mStrings.add(0);

	}

	public Integer[] getAcceptStates()
	{
		Integer[] arr;
		arr = (Integer[]) mStrings.toArray(new Integer[mStrings.size()]);
		return arr;
	}

	public int getFnElementCount()
	{
		return mStrings.size();
	}

	public boolean addState(int state)
	{
		// search for state
		// return false if its there
		if (mStrings.contains(new Integer(state)))
		{
			return false;
		}
		mStrings.add(state);
		return true;
	}

	public boolean removeRandState(Random randGenerator)
	{
		if (getFnElementCount() < 1)
		{
			return false;
		}
		mStrings.remove(randGenerator.nextInt(getFnElementCount()));
		return true;
	}

	public boolean isStateAccepted(int state)
	{
		/*
		 * Integer[] states = getAcceptStates(); for(int i = 0; i <
		 * states.length; i++) { if(states[i] == state) { return true; } }
		 */
		if (mStrings.contains(new Integer(state)))
		{
			return true;
		}
		return false;
	}

	public void clear()
	{
		mStrings.clear();

	}

	public boolean canAddState()
	{
		return getAcceptStates().length != mParent.getmAutominaCount();
	}

	public void setAccepts(Integer[] childFn)
	{
		mStrings.clear();
		mStrings.addAll(Arrays.asList(childFn));
	}
}
