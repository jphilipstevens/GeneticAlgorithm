package analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AnalysisData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getTestFile();
	}
	private static void getTestFile()
	{
		BufferedReader input1;
		BufferedReader input2;
		Vector<String>bitStr = new Vector<String>();
		Map<String, Integer> results = new HashMap<String, Integer>();

		try {
			input1 = new BufferedReader(new FileReader("a4test.txt"));
			input2 = new BufferedReader(new FileReader("A4Results.txt"));

			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS
			 * the newline. it returns null only for the END of the stream. it
			 * returns an empty String if two newlines appear in a row.
			 */
			while ((line = input1.readLine()) != null)
			{
				int length = line.length();
				StringBuffer buf = new StringBuffer("");
				boolean labelSection = false;
				for(int i = 0; i < length; i++)
				{
					char chr = line.charAt(i);
					if(chr == ',')
					{
						labelSection = true;
					}else if(labelSection == false)
					{
						buf.append(chr);
					}				
				}
				bitStr.add(buf.toString());
			}
			
			line = null;
			Vector<String> newStr = new Vector<String>();
			while ((line = input2.readLine()) != null)
			{
				int length = line.length();
				StringBuffer buf = new StringBuffer("");
				boolean labelSection = false;
				Integer intResult = null;
				for(int i = 0; i < length; i++)
				{
					char chr = line.charAt(i);
					if(chr == ',')
					{
						labelSection = true;
					}else if(labelSection == false)
					{
						buf.append(chr);
					}else if (labelSection == true)
					{
						intResult = new Integer(chr - '0');
					}
				}
				newStr.add(buf.toString());
			}
			for( String a : bitStr)
			{
				System.out.print("\"" + a + "\",");
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

}
