package main;
import geneticAlgorithm.GeneticAlgo;
import geneticAlgorithm.GeneticAlgo.SelectionType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import constants.ApplicationConstants;

import analysisCode.ValidateAutomaton;
import automaton.Automaton;

public class main
{
	private static boolean POPULATION_TESTS = true;
	private static int GA_ITERATIONS = 1;
	private static boolean GENERALIZATION_TEST = false;
	private static boolean REPLACE_POPULATION = false;
	/**
	 * Print the discovered DFAs F and delta functions
	 */
	private static int popSize = 1000;

	public static void main(String[] args)
	{
		FileWriter fstream = null;
		BufferedWriter out = null;
		Calendar date = Calendar.getInstance();
		/*try
		{
			fstream = new FileWriter("/home/jono/workspace/CIS6420Project/Data/" + date.get(date.DAY_OF_MONTH) + "-" + date.get(date.HOUR_OF_DAY) +"_" + date.get(date.MINUTE) + "_"+ date.get(date.SECOND) + ".data");
			out = new BufferedWriter(fstream);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		int AutomatonCount = 4;
		boolean solveWithEp = true;
		Vector<Automaton> solutions;
		String[] allBinaryNumbers = null;
		boolean[] accepts = null;
		if (GENERALIZATION_TEST)
		{
			Vector<String> bitStr = new Vector<String>();
			Vector<Boolean> acceptsV = new Vector<Boolean>();
			getTestFile("a4train.txt", bitStr, acceptsV);
			allBinaryNumbers = bitStr.toArray(new String[bitStr.size()]);
			Boolean[] tmpArr;
			tmpArr = acceptsV.toArray(new Boolean[acceptsV.size()]);
			accepts = new boolean[tmpArr.length];
			for (int i = 0; i < accepts.length; i++)
			{
				accepts[i] = tmpArr[i];
			}
		} else
		{
			allBinaryNumbers = new String[65];
			accepts = getAccepts(AutomatonCount, allBinaryNumbers);

		}
		ValidateAutomaton validate = new ValidateAutomaton(allBinaryNumbers, accepts);
		if (solveWithEp)
		{
			StringBuffer dataBuf = new  StringBuffer();
			int crossOverNum = CrossoverType.BEST_PROPORTION.getIndex();
			for (; crossOverNum <= CrossoverType.RANK.getIndex(); crossOverNum++)
			{
				dataBuf.append(crossOverNum).append(';');
				System.out.println("Automina Count " + AutomatonCount);
				for (int popRuns = 100; popRuns <= 100000; popRuns *= 10)
				{
					if (POPULATION_TESTS)
					{
						popSize = popRuns;
					}
					dataBuf.append(popSize).append(';');
					System.out.println("Population Size = " + popSize);
					for (int runs = 0; runs < GA_ITERATIONS;)
					{
						GeneticAlgo algorithm = new GeneticAlgo(popSize, AutomatonCount, accepts, allBinaryNumbers, CrossoverType.getSelection(crossOverNum), REPLACE_POPULATION);
						Calendar cal = Calendar.getInstance();
						long time1 = cal.getTimeInMillis();
						solutions = algorithm.solve(false, accepts);
						cal = Calendar.getInstance();
						long time2 = cal.getTimeInMillis() - time1;
						if (ApplicationConstants.DEBUG)
						{
							System.out.print(" total time to execute ");
						}
						System.out.println("," + time2);
						dataBuf.append(algorithm.getmIteration() + "").append(' ').append(time2 + "").append(';');
						if (solutions == null)
						{
							return;
						}
						for (Automaton atom : solutions)
						{
							if (!validate.EvaluateAutomaton(atom))
							{
								System.out.println("NOT A SOLUTION");
								return;
							}
							if (Automaton.equalsBaseAutomaton(atom) == false && ApplicationConstants.DEBUG)
							{
								StringBuffer str1 = new StringBuffer();
								for (int a : atom.getFn().getAcceptStates())
								{
									str1.append(a).append(" ");
								}
								StringBuffer str2 = new StringBuffer();
								for (int b = 0; b < atom.getmDeltaCount(); b++)
								{
									str2.append("\n\t\t").append(atom.getDelta().getTransitions()[b][0] + "").append(" ").append(atom.getDelta().getTransitions()[b][1] + "").append(" ").append(
											atom.getDelta().getTransitions()[b][2] + "").append(" ");
								}
								System.out.println("Automaton\n\tFunction: " + str1 + "\n\tDelta: " + str2);
							} else
							{
								if (ApplicationConstants.DEBUG)
								{
									System.out.println("Solution is base atom");
								}
							}

						}
						if (GENERALIZATION_TEST)
						{
							labelTestData(solutions);
						}
						runs++;
					}
					try
					{
						out.write(dataBuf.append("\n").toString());
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (POPULATION_TESTS == false)
					{
						break;
					}
					System.out.println("\n\n");
				}
			}
			try
			{
				out.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			EvaluateSelection eval = new EvaluateSelection(REPLACE_POPULATION, popSize, AutomatonCount, accepts, allBinaryNumbers);
			eval.evaluate();
		} else
		{
			Automaton atom;
			int iteration = 0;
			Calendar cal;
			for (int x = 0; x < 100; x++)
			{
				iteration = 0;
				atom = new Automaton(AutomatonCount, true);
				cal = Calendar.getInstance();
				long time1 = cal.getTimeInMillis();
				while (GeneticAlgo.calculateFitness(atom, accepts, allBinaryNumbers) != 1.0)
				{
					atom = new Automaton(AutomatonCount, true);
					iteration++;
				}
				cal = Calendar.getInstance();
				long time2 = cal.getTimeInMillis() - time1;
				System.out.println(iteration + " " + time2);
			}
		}
	}

	private static Integer getCOFromKeyboard()
	{

		BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
		Integer intgr = null;
		try
		{
			intgr = new Integer(keyboardInput.readLine());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return intgr;
	}

	private static boolean[] getAccepts(int AutomatonCount, String[] allBinaryNumbers)
	{
		boolean[] accepts = new boolean[65];
		accepts[0] = true;
		allBinaryNumbers[0] = "";
		for (int i = 0; i < 64; i++)
		{
			allBinaryNumbers[i + 1] = Integer.toBinaryString(i);
			int oneCount = 0;
			int zeroCount = 0;
			for (int x = 0; x < allBinaryNumbers[i + 1].length(); x++)
			{
				if (allBinaryNumbers[i + 1].charAt(x) == '0')
				{
					zeroCount++;
				} else
				{
					oneCount++;
				}
			}
			if (zeroCount % 2 == 0 && oneCount % 2 == 0)
			{
				accepts[i + 1] = true;
			} else
			{
				accepts[i + 1] = false;
			}
		}
		Automaton automaton = new Automaton(AutomatonCount, false);
		int i = 0;
		for (String binString : allBinaryNumbers)
		{
			// does the automaton work at its default setting?
			if (automaton.evaluateString(binString) != accepts[i])
			{
				System.out.println("for i = " + i + " " + automaton.evaluateString(binString) + " != " + accepts[i]);
			}
			i++;
		}
		return accepts;
	}

	private static void getTestFile(String file, Vector<String> bitStr, Vector<Boolean> accepts)
	{
		BufferedReader input;
		try
		{
			input = new BufferedReader(new FileReader(file));

			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS
			 * the newline. it returns null only for the END of the stream. it
			 * returns an empty String if two newlines appear in a row.
			 */
			while ((line = input.readLine()) != null)
			{
				int length = line.length();
				StringBuffer buf = new StringBuffer("");
				boolean labelSection = false;
				for (int i = 0; i < length; i++)
				{
					char chr = line.charAt(i);
					if (chr == ',')
					{
						labelSection = true;
					} else if (labelSection == false)
					{
						buf.append(chr);
					} else if (labelSection)
					{
						accepts.add(chr == '1');
					}
				}
				bitStr.add(buf.toString());
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private static void labelTestData(Vector<Automaton> solutions)
	{
		/*
		 * String[] allBinaryNumbers = new String[257]; allBinaryNumbers[0] =
		 * ""; for(int i = 0; i < allBinaryNumbers.length - 1; i++) {
		 * allBinaryNumbers[i+1] = Integer.toBinaryString(i); }
		 */
		String[] allBinaryNumbers =
		{ "00", "10", "11", "000", "001", "010", "0000", "0001", "0010", "0011", "0111", "00011", "00110", "00111", "01000", "01011", "01100", "10000", "10001", "10100", "10101", "10111", "11001",
				"11111", "000000", "000001", "000010", "000011", "000101", "000111", "001001", "001011", "001100", "001101", "010001", "010010", "010110", "011001", "011011", "011100", "011101",
				"100001", "100010", "100011", "100101", "101000", "101010", "101100", "110010", "110011", "110100", "110101", "111101", "111110", "0000001", "0000010", "0000100", "0000101",
				"0001010", "0001100", "0001110", "0010000", "0010010", "0010110", "0010111", "0011000", "0011001", "0100000", "0100001", "0100010", "0100011", "0100101", "0100110", "0100111",
				"0101010", "0101011", "0101110", "0110100", "0110101", "0110110", "0111000", "0111010", "0111100", "0111101", "0111110", "1000001", "1000010", "1000011", "1000101", "1000111",
				"1001001", "1001011", "1001100", "1001101", "1001110", "1001111", "1010001", "1010110", "1010111", "1011001", "1011011", "1011100", "1011101", "1011110", "1100001", "1100011",
				"1100100", "1100110", "1100111", "1101000", "1101001", "1101010", "1101011", "1101100", "1101101", "1101110", "1101111", "1110010", "1110100", "1110111", "1111010", "1111011",
				"1111100", "1111110", "1111111", "00000100", "00000101", "00001010", "00001011", "00001101", "00001111", "00010000", "00010001", "00010010", "00010011", "00010100", "00010101",
				"00010111", "00011000", "00011010", "00011011", "00011100", "00011101", "00011111", "00100001", "00100100", "00100111", "00101000", "00101001", "00101010", "00101100", "00110010",
				"00110101", "00110111", "00111000", "00111010", "00111011", "00111101", "01000010", "01000100", "01000111", "01001010", "01001011", "01001100", "01001101", "01001110", "01010010",
				"01010011", "01010101", "01010110", "01010111", "01011000", "01011001", "01011100", "01011110", "01011111", "01100000", "01100001", "01100100", "01101000", "01101010", "01101011",
				"01101100", "01101111", "01110001", "01110010", "01110100", "01110101", "01110111", "01111001", "01111010", "01111011", "01111101", "10000011", "10000111", "10001000", "10001001",
				"10001010", "10001100", "10001101", "10001110", "10001111", "10010000", "10010010", "10010100", "10010101", "10010111", "10011000", "10011010", "10011011", "10100000", "10100001",
				"10100010", "10100011", "10100110", "10101000", "10101011", "10101110", "10110010", "10110011", "10110111", "10111011", "11000001", "11000101", "11000110", "11001000", "11001001",
				"11001010", "11001100", "11001101", "11001111", "11010000", "11010001", "11010110", "11010111", "11011011", "11011111", "11100010", "11100100", "11100110", "11100111", "11101000",
				"11101001", "11101101", "11101110", "11110000", "11110011", "11110100", "11110101", "11110110", "11111000", "11111001", "11111100", "11111101", "11111110", "11111111" };
		for (Automaton atom : solutions)
		{
			System.out.println("ATOM");
			for (String binString : allBinaryNumbers)
			{
				System.out.println(binString + "," + (atom.evaluateString(binString) ? 1 : 0));
			}
		}

	}
}