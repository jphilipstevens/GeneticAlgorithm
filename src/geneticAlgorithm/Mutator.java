package geneticAlgorithm;
import java.util.Random;

import automaton.Automaton;
import automaton.FObject;

public class Mutator
{
	private Random randGenerator;
	public static double MUTATION_PROB = 0.05;

	public Mutator()
	{
		randGenerator = new Random();
	}

	public Automaton mutate(Automaton automaton)
	{
		Automaton newAtom = automaton;
		;
		double dVal = randGenerator.nextDouble();
		if (dVal <= MUTATION_PROB)
		{
			int mutatuionType = randGenerator.nextInt(3);
			// if fn isnt full then we can use this mutator
			if (mutatuionType == 0 && automaton.getFn().canAddState())
			{
				boolean val = false;
				while (val == false)
				{
					val = addToF(newAtom);
				}

			}else if (mutatuionType == 1)
			{
				removeFromF(newAtom.getFn());
			}else
			{
				alterDelta(newAtom);
			}
		}
		return newAtom;
	}

	private boolean addToF(Automaton autom)
	{
		return autom.getFn().addState(
				autom.getAutomina(randGenerator.nextInt(autom
						.getmAutominaCount())));
	}

	private boolean removeFromF(FObject fn)
	{
		return fn.removeRandState(randGenerator);
	}

	private boolean alterDelta(Automaton atom)
	{
		atom.randomizeDelta();
		return true;
	}

}
