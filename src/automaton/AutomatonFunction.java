package automaton;

public abstract class AutomatonFunction
{

	protected Automaton mParent;

	public AutomatonFunction(Automaton parent)
	{
		mParent = parent;
	}

	public Automaton getmParent()
	{
		return mParent;
	}

	public void setmParent(Automaton mParent)
	{
		this.mParent = mParent;
	}

}
