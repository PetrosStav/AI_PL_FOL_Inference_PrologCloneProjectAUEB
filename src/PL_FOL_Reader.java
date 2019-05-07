// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Class that holds all the methods needed for I/O
// Reading KB from file for PL
// Reading KB from file for Horn FOL
// Analyzing a String input for PL/FOL
public class PL_FOL_Reader {
	
	// Method that reads KB from file for PL
	// returns KB as a CNFCLause
	public static CNFClause readFromFilePL(String filename) {
		
		// Initialize the clause that will be returned
		CNFClause clause = new CNFClause();
		
		// Initialize the bufferedreader for the file to null
		BufferedReader br = null;
		try {
			// Open the file using the br
			br = new BufferedReader(new FileReader(new File(filename)));
			// String variable for each line of the file
			String line;
			// While we aren't at the end of the file
			while((line=br.readLine())!=null) {
				// Analyse the line and return the clause
				CNFClause inclause = analyseInputPL(line);
				// Add each subclause of the clause to the one that we will return
				for(CNFSubClause sub : inclause.getSubclauses()) {
					clause.getSubclauses().add(sub);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return the clause
		return clause;
	}
	
	// Method that reads from file KB for Horn FOL
	// returns KB as a HornKnowledgeBaseFOL Object
	public static HornKnowledgeBaseFOL readFromFileKBHornFOL(String filename) {
		// Initialize the clause that will be returned
		HornKnowledgeBaseFOL KB = new HornKnowledgeBaseFOL();
		
		// Initialize the bufferedreader for the file to null
		BufferedReader br = null;
		try {
			// Open the file using the br
			br = new BufferedReader(new FileReader(new File(filename)));
			// String variable for each line of the file
			String line;
			// While we aren't at the end of the file
			while((line=br.readLine())!=null) {
				// Analyse the line and return the clause
				HornClauseFOL clause = analyseInputHornClauseFOL(line);
				// Add the clause to the Knowledge Base
				KB.getHornClauses().add(clause);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return the clause
		return KB;
	}
	
	// Method that analyzes a String input to a CNFClause for PL
	public static CNFClause analyseInputPL(String input) {

		// Get input String for CNF and trim the whitespace chars
		input = input.trim();
		
		// Remove all parentheses as the programs awaits a CNF
		// so there is no confusion about the order of AND,OR
		input = input.replaceAll("\\(", "");
		input = input.replaceAll("\\)", "");
		input = input.trim();

		// Initialize the CNFClause
		CNFClause clause = new CNFClause();
		
		// Split the input into subclauses using the word AND
		String[] subs = input.split("AND");
		// For each subclause
		for(int i = 0;i < subs.length; i++) {
			// Inizialize the subclause
			CNFSubClause subclause = new CNFSubClause();
			
			// Get the according subclause from the string array
			// and trim the whitespace chars
			String sub = subs[i].trim();
			
			// Split the subclause into literals using the word OR
			String[] lits = sub.split("OR");
			
			for(int j = 0; j < lits.length; j++) {
				// Get the Literal from the String array
				String lit = lits[j];
				
				// Trim the whitespace chars
				lit = lit.trim();
				
				// Check if it is negative
				boolean neg = false;
				
				// Trim the negative part according to which way to represent it
				// is used if there is and set the boolean variable accordingly
				if(lit.startsWith("-") || lit.startsWith("NOT_")) {
					neg = true;
					if(lit.startsWith("-")) lit = lit.substring(1);
					else lit = lit.substring(4);
				}
				
				// Add the Literal to the subclause
				subclause.getLiterals().add(new LiteralPL(lit,neg));
			}
			
			// Add the subclause to the clause
			clause.getSubclauses().add(subclause);
		}
		
		return clause;
	}
	
	// Method that analyzes String input to a HornClauseFOL for FOL
	public static HornClauseFOL analyseInputHornClauseFOL(String input) {
		// Trim the input of whitespace chars
		input = input.trim();
		
		// Split the horn clause in the hypotheses literals and the result
		String[] horn_parts = input.split("=>");
		
		// Create an arraylist of fol literals
		ArrayList<LiteralFOL> hypotheses = new ArrayList<>();
		
		// Split the left side of the horn clause with the word 'AND'
		String[] literals = horn_parts[0].split("AND");
		
		// For each literal as a String
		for(String l : literals) {
			// Trim it of whitespace chars
			l = l.trim();
			// Use the analyse method to add it to the List
			hypotheses.add(analyseInputLiteralFOL(l));
		}
		
		// Initialize the HornClauseFOL
		HornClauseFOL clause = null;
		
		// Check if it has result
		if(horn_parts.length>1) {
			// If it has, create the horn clause using the hypotheses and the result
			// which is in the second horn_part (trim it before adding)
			
			LiteralFOL res = analyseInputLiteralFOL(horn_parts[1].trim());
			
			clause = new HornClauseFOL(hypotheses,res);
		}else {
			// Else create the horn clause only using the hypotheses and setting the
			// result to null
			clause = new HornClauseFOL(hypotheses,null);
		}
		
		// Return the Horn Clause
		return clause;
	}
	
	// Method that analyzes a String input to a LiteralFOL for FOL
	public static LiteralFOL analyseInputLiteralFOL(String input) {
		// Trim the input whitespace chars
		input = input.trim();
		
		// Find the index of the first '('
		int idx = input.indexOf('(');
		
		// Find the name of the literal (relation)
		String litName = input.substring(0, idx);
		
		// Find if it is negated
		boolean negation = false;
		// If the Literal's Name starts with '-' or 'NOT_'
		if(litName.startsWith("-") || litName.startsWith("NOT_")) {
			// Then it is negated
			negation = true;
			// If the name started with '-' then remove 1 character from the literal's name
			if(litName.startsWith("-")) litName = litName.substring(1);
			// Else remove 4 characters from the litera's name
			else litName = litName.substring(4);
		}
		
		// Get rid of the literal along with the parentheses in the end
		input = input.substring(idx+1, input.length()-1).trim();
		
		// Get every term by splitting by ','
		String[] lterms = customSplit(input, ',');
		
		// Create the Literal
		LiteralFOL lit = new LiteralFOL(litName, lterms.length, negation);
		
		// Create the Term array that will be filled
		TermFOL[] terms = new TermFOL[lterms.length];
		
		// Index for the terms
		int t_i = 0;
		
		// For every term we must find what it is
		for(String t : lterms) {
			// Trim the string term
			t = t.trim();
			
			// Check if it has parentheses, thus making it a function
			int p = t.indexOf('(');
			
			// If it doesn't have parentheses then it is either a Variable or a Constant
			// Else it is a function
			if(p==-1) {
				// Check the first letter
				char c = t.charAt(0);
				
				// If it is UpperCase then it is a Constant
				// Else it is a Variable
				if(Character.isUpperCase(c) || Character.isDigit(c)) {
					terms[t_i++] = new ConstantFOL(t);
				}else {
					terms[t_i++] = new VariableFOL(t);
				}
			}else {
				terms[t_i++] = analyseFunctionFOL(t);
			}
			
		}
		
		// Set the terms that we found
		lit.setTerms(terms);
		
		// Return the literal
		return lit;
	}
	
	// Method that analyzes a String input to a FunctionFOL for FOL
	private static FunctionFOL analyseFunctionFOL(String function) {
		
		// Trim the function
		function = function.trim();
		
		// Find the index of the first '('
		int f_idx = function.indexOf('(');
		
		// Find the name of the function
		String FuncName = function.substring(0, f_idx);
		
		// Get rid of the function name along with the parentheses in the end
		function = function.substring(f_idx+1, function.length()-1).trim();
		
		// Get every term by splitting by ','
		String[] fterms = customSplit(function, ',');
		
		// Index for fterms
		int fi = 0;
		
		// Create the function
		FunctionFOL func = new FunctionFOL(FuncName, fterms.length);
		
		// Create the funciton terms
		TermFOL[] terms_f = new TermFOL[fterms.length];
		
		for(String ft : fterms) {
			// Trim the string term
			ft = ft.trim();
			
			// Check if it has parentheses, thus making it a function
			int p = ft.indexOf('(');
			
			// If it doesn't have parentheses then it is either a Variable or a Constant
			// Else it is a function
			if(p==-1) {
				// Check the first letter
				char c = ft.charAt(0);
				
				// If it is UpperCase then it is a Constant
				// Else it is a Variable
				if(Character.isUpperCase(c) || Character.isDigit(c)) {
					terms_f[fi++] = new ConstantFOL(ft);
				}else {
					terms_f[fi++] = new VariableFOL(ft);
				}
			}else {
				// Analyse the function using recursion
				terms_f[fi++] = analyseFunctionFOL(ft);
			}
				
		}
		
		// Set the terms for the function
		func.setTerms(terms_f);
		
		return func;
	}
	
	// Custom split function that will ignore parentheses
	private static String[] customSplit(String input, char split) {
		// Integer to check if we are inside parentheses - and in how many
		int inside = 0;
		// ArrayList of the index of every split character we found
		ArrayList<Integer> indices = new ArrayList<>();
		// Iterate the input
		for(int i=0;i<input.length();i++) {
			// If there is a left parentheses then we are inside
			if(input.charAt(i)=='(') inside++;
			// If there is a right parentheses then we are are not inside
			if(input.charAt(i)==')') inside--;
			// If we found a split character and we are not inside add the index to the list
			if(input.charAt(i)==split && inside==0) indices.add(i);
		}
		// Create an array of Strings for each splitted part according to the number
		// of indices found
		String[] input_parts = new String[indices.size()+1];
		// Index for the string array
		int si = 0;
		// Previous index
		int prev = -1;
		// For every index
		for(int i : indices) {
			// Take the part from the string
			String part = input.substring(prev+1,i);
			// Put it into the array after it is trimmed of whitespace chars
			input_parts[si++] = part.trim();
			// Set previous index
			prev = i;
		}
		// Put the last part in the array after it is trimmed of whitespace chars
		input_parts[si] = input.substring(prev+1);
		
		// Return the String array
		return input_parts;
	}
	
}
