// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.Scanner;

// The class for the main method for PL Forward Chain Horn
// Reads a KB from a file
// Prompts the user what he/she want's to ask (Literal)
// Returns if what the user asked is logically inferred from the KB using forward chain algorithm for PL Horn
public class PL_Horn_Main {
	public static void main(String[] args) {
		
		// The file that we will use for the Knowledge Base
		String filename = "testHornPL_Example.txt";
		
		// Print message to user
		System.out.println("Reading KB for PL Forward Chain Horn from file: " + filename);
		
		CNFClause KB = PL_FOL_Reader.readFromFilePL(filename);
		
		// Print message to user
		System.out.println("Knowledge Base added.");
		
		// Evaluate that all the KB SubClauses are horn statements
		KB.evaluateHorn();
		
		// Print prompt to user
		System.out.print("What do you want to ask?\n>");
		
		// Create a scanner
		Scanner sc = new Scanner(System.in);
		
		// Take the user's input
		String input = sc.nextLine();
		
		// Analyze the user's input and put it into a CNF Clause
		CNFClause cnfa = PL_FOL_Reader.analyseInputPL(input);
		
		// If the CNF from the user's input has more than 1 literal then print error
		if(cnfa.getSubclauses().size()>1 || cnfa.getSubclauses().get(0).getLiterals().size()>1) {
			System.out.println("You must enter one positive literal!");
			sc.close();
			return;
		}
		
		// Get the positive literal from the CNFClause from the user's input
		LiteralPL a = cnfa.getSubclauses().get(0).getLiteralsList().next();
		
		// Print the answer to the user using Horn Forward Chain for PL
		System.out.println("What you asked is : " + ForwardChainHornPL.PL_HornForwardChain(KB, a));
		
		// Close the scanner
		sc.close();
		
	}
}
