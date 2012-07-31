package automaton;

import java.util.Random;

public class DeltaObject extends AutomatonFunction
{
	private int[][] mTransitions;

	public DeltaObject(Automaton atom)
	{
		super(atom);
		mTransitions = new int[atom.getmDeltaCount()][3];
		setDefaultTransitions();
	}

	public DeltaObject(DeltaObject delta)
	{
		super(delta.getmParent());
		mTransitions = new int[delta.getTransitionCount()][3];
		int[][] origTransitions = delta.getTransitions();
		for (int i = 0; i < delta.getTransitionCount(); i++)
		{
			mTransitions[i][0] = origTransitions[i][0];
			mTransitions[i][1] = origTransitions[i][1];
			mTransitions[i][2] = origTransitions[i][2];
		}
	}

	private void setDefaultTransitions()
	{
		/*
		 * mTransitions[0] = "q0,0,q1"; mTransitions[1] = "q0,1,q2";
		 * mTransitions[2] = "q1,0,q0"; mTransitions[3] = "q1,1,q3";
		 * mTransitions[4] = "q2,0,q3"; mTransitions[5] = "q2,1,q0";
		 * mTransitions[6] = "q3,0,q2"; mTransitions[7] = "q3,1,q1";
		 */
		mTransitions[0][0] = 0;
		mTransitions[0][1] = 0;
		mTransitions[0][2] = 1;

		mTransitions[1][0] = 0;
		mTransitions[1][1] = 1;
		mTransitions[1][2] = 2;

		mTransitions[2][0] = 1;
		mTransitions[2][1] = 0;
		mTransitions[2][2] = 0;

		mTransitions[3][0] = 1;
		mTransitions[3][1] = 1;
		mTransitions[3][2] = 3;

		mTransitions[4][0] = 2;
		mTransitions[4][1] = 0;
		mTransitions[4][2] = 3;

		mTransitions[5][0] = 2;
		mTransitions[5][1] = 1;
		mTransitions[5][2] = 0;

		mTransitions[6][0] = 3;
		mTransitions[6][1] = 0;
		mTransitions[6][2] = 2;

		mTransitions[7][0] = 3;
		mTransitions[7][1] = 1;
		mTransitions[7][2] = 1;

		/**
		 * anything more we randomize
		 */
		Random rand = new Random();
		for (int i = 8, input = 0, j = 4; i < mTransitions.length; i++, input = i % 2)
		{
			// assuming i is the q number bad but best we can do on short notice
			mTransitions[i][0] = j;
			mTransitions[i][1] = input;
			mTransitions[i][2] = rand.nextInt(mParent.getmAutominaCount());
			if (input == 1)
			{
				j++;
			}
		}
	}

	public void resetTransitions()
	{
		setDefaultTransitions();
	}

	public int[] getSplitString(int index)
	{
		return mTransitions[index];
	}

	public int getStartState(int index)
	{
		return getSplitString(index)[0];
	}

	public int getInput(int index)
	{
		return getSplitString(index)[1];
	}

	public int getEndState(int index)
	{
		return getSplitString(index)[2];
	}

	public void setStartState(int index, int state)
	{
		mTransitions[index][0] = state;
	}

	public void setEndState(int index, int state)
	{
		mTransitions[index][2] = state;
	}

	public void setInput(int index, int input)
	{
		mTransitions[index][1] = input;
	}

	public int transition(int state, int bit)
	{
		for (int i = 0; i < mParent.getmDeltaCount(); i++)
		{
			if (mTransitions[i][0] == state && mTransitions[i][1] == bit)
			{
				return mTransitions[i][2];
			}
		}
		return -1;
	}

	public int getTransitionCount()
	{
		return mParent.getmDeltaCount();
	}

	public void setTransitions(int[][] transitions)
	{
		for (int i = 0; i < mParent.getmDeltaCount(); i++)
		{
			mTransitions[i][0] = transitions[i][0];
			mTransitions[i][1] = transitions[i][1];
			mTransitions[i][2] = transitions[i][2];
		}
	}

	public int[][] getTransitions()
	{
		return mTransitions;
	}
}
