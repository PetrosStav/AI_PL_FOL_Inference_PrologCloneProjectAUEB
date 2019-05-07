// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.Scanner;

// The class for the main method for PL Resolution
// Reads a KB from a file
// Prompts the user what he/she want's to ask
// Returns if what the user asked is logically inferred from the KB using PL Resolution algorithm
public class PL_CNF_Main {
	public static void main(String[] args) {
		
		// The file that we will use for the Knowledge Base
		String filename = "testPL_08_15.txt";
		
		// Print message to user
		System.out.println("Reading KB for PL Resolution from file: " + filename);
		
		// Read the KB from the file specified above
		CNFClause KB = PL_FOL_Reader.readFromFilePL(filename);
		
		// Print message to user
		System.out.println("Knowledge Base added.");
		
		// Print prompt to user
		System.out.print("What do you want to ask? (Enter the negation in CNF) \n>");
		
		// Create a Scanner
		Scanner sc = new Scanner(System.in);
		
		// Take the user's input
		String input = sc.nextLine();
		
		// Analyze the user's input to a CNFClause
		CNFClause a = PL_FOL_Reader.analyseInputPL(input);
		
		// Print answer to user using PL_Resolution
		System.out.println("What you asked is : " + ResolutionPL.PL_Resolution(KB, a));
		
		// Close the scanner
		sc.close();
		
	}
}
