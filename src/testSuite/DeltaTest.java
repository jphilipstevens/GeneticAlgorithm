package testSuite;

import org.junit.Test;

import automaton.Automaton;
import automaton.DeltaObject;
import junit.framework.TestCase;

public class DeltaTest extends TestCase
{
	private Automaton atom1;
	private DeltaObject delta1;
	private int atomCount;

	public DeltaTest(String name)
	{
		super(name);
		
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		atomCount = 6;
		atom1 = new Automaton(atomCount, false);
		delta1 = atom1.getDelta();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	@Test public void testDelta()
	{
		System.out.println("Test Delta");
		
		assertTrue(delta1.getmParent().equals(atom1));
	}
	
	@Test public void testStartTransition()
	{
		int endState = 0;
		for(int i = 0; i < delta1.getTransitionCount(); i++)
		{
			System.out.println("state "+ i);
			delta1.setStartState(i, endState);
			assertTrue(delta1.getStartState(i) == endState);
		}
	}

}
