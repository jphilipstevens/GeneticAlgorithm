package analysisCode;

import automaton.Automaton;

public class ValidateAutomaton
{

	private boolean[] mAccepts;
	private String[] mBinaryStr;

	public ValidateAutomaton(String[] binaryStr, boolean[] accepts)
	{
		if(binaryStr.length != accepts.length)
		{
			throw new IllegalArgumentException("binaryStr, and accepts have to be the same length");
		}
		mAccepts = accepts;
		mBinaryStr = binaryStr;
	}
	
	//currently  only works with the 
	public boolean EvaluateAutomaton(Automaton atom)
	{
		for (int i = 0; i < mBinaryStr.length; i++)
		{
			boolean val = atom.evaluateString(mBinaryStr[i]);
			if (val != mAccepts[i])
			{
				return false;
			}
		}
		return true;
	}
}
