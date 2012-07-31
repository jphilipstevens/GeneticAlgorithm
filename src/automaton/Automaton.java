package automaton;
import java.util.Random;

public class Automaton
{
	/**
	 * 
	 */
	public static final Automaton baseAtom = new Automaton(4, false);
	private int mAutominaCount;
	private int mDeltaCount;
	private int[] mAutomina;
	private DeltaObject mDelta;
	private FObject mFn;
	private Random randGen = new Random();

	public Automaton(int autominaCount, boolean randomize)
	{
		mAutominaCount = autominaCount;
		mDeltaCount = mAutominaCount * 2;
		mAutomina = new int[mAutominaCount];
		for (int i = 0; i < mAutominaCount; i++)
		{
			mAutomina[i] = i;
		}
		mDelta = new DeltaObject(this);
		mFn = new FObject(this);
		if (randomize)
		{
			radomizeAutomaton(mFn);
		}
	}

	public Automaton(Automaton automaton)
	{
		mDelta = new DeltaObject(automaton.getDelta());
		mFn = new FObject(automaton.getFn());
	}

	private void radomizeAutomaton(FObject fn)
	{
		randomizeDelta();
		mFn.clear();
		int randAccepts = randGen.nextInt(mAutominaCount);
		boolean[] fns = new boolean[mAutominaCount];
		for (int i = 0; i < randAccepts; i++)
		{
			int automina = mAutomina[randGen.nextInt(mAutominaCount)];
			while (mFn.addState(automina) == false)
			{
				automina = mAutomina[randGen.nextInt(mAutominaCount)];
			}

		}

	}

	public void randomizeDelta()
	{
		for (int i = 0; i < mDelta.getTransitionCount(); i++)
		{
			int newEndState = mAutomina[randGen.nextInt(mAutominaCount)];
			mDelta.setEndState(i, newEndState);
		}

	}

	public int getAutomina(int index)
	{
		return mAutomina[index];
	}

	public boolean evaluateString(String binStr) throws NullPointerException
	{
		StringBuffer binary = new StringBuffer(binStr);
		binary = binary.reverse();
		int state = 0;
		for (int i = 0; i < binStr.length(); i++)
		{
			int bit = binary.charAt(i) - '0';
			state = mDelta.transition(state, bit);
			// sanity check in case bad mojo happened
			if (state == -1)
			{
				throw new NullPointerException();
			}
		}
		return mFn.isStateAccepted(state);
	}

	public DeltaObject getDelta()
	{
		return mDelta;
	}

	public void setDelta(DeltaObject delta)
	{
		this.mDelta = delta;
		mDelta.setmParent(this);
	}

	public FObject getFn()
	{
		return mFn;
	}

	public void setFn(FObject fn)
	{
		this.mFn = fn;
		mFn.setmParent(this);
	}

	public int getmAutominaCount()
	{
		return mAutominaCount;
	}

	public int getmDeltaCount()
	{
		return mDeltaCount;
	}

	public static boolean equalsBaseAutomaton(Automaton atom)
	{
		if (atom.getFn().getFnElementCount() != baseAtom.getFn()
				.getFnElementCount())
		{
			return false;
		}
		for (int i = 0; i < baseAtom.getFn().getFnElementCount(); i++)
		{
			if (atom.getFn().getmStrings().contains(
					baseAtom.getFn().getAcceptStates()[i]) == false)
			{
				return false;
			}
		}

		for (int i = 0; i < atom.getDelta().getTransitionCount(); i++)
		{
			if (baseAtom.getDelta().getStartState(i) != atom.getDelta()
					.getStartState(i))
			{
				return false;
			}
			if (baseAtom.getDelta().getInput(i) != atom.getDelta().getInput(i))
			{
				return false;
			}
			if (baseAtom.getDelta().getEndState(i) != atom.getDelta()
					.getEndState(i))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Automaton atom)
	{
		if (atom.getFn().getFnElementCount() != getFn()
				.getFnElementCount())
		{
			return false;
		}
		for (int i = 0; i < getFn().getFnElementCount(); i++)
		{
			if (atom.getFn().getmStrings().contains(
					getFn().getAcceptStates()[i]) == false)
			{
				return false;
			}
		}

		for (int i = 0; i < atom.getDelta().getTransitionCount(); i++)
		{
			if (getDelta().getStartState(i) != atom.getDelta()
					.getStartState(i))
			{
				return false;
			}
			if (getDelta().getInput(i) != atom.getDelta().getInput(i))
			{
				return false;
			}
			if (getDelta().getEndState(i) != atom.getDelta()
					.getEndState(i))
			{
				return false;
			}
		}
		return true;
	}
}
