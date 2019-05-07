// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;
import java.util.Scanner;

// The class for the main method for FOL Forward Chain Horn
// Reads a KB from a file
// Prompts the user what he/she want's to ask
// Returns if what the user asked is logically inferred from the KB using FOL Forward Chain Horn algorithm
// Or a list of possible solutions (list of unifiers) for what is asked if it the user's prompt has variables
// eg. Criminal(x) --> {x/West}
public class FOL_Horn_Main {
	public static void main(String[] args) {
		
		// The file that we will use for the Knowledge Base
		String filename = "testHornFOL_12E_2.txt";
		
		// Print message to user
		System.out.println("Reading KB for FOL Forward Chain Horn from file: " + filename);
		
		// Read the KB from the file specified above
		HornKnowledgeBaseFOL KB = PL_FOL_Reader.readFromFileKBHornFOL(filename);
		
		// Print message to user
		System.out.println("Knowledge Base added.");
		
		// Print prompt to user
		System.out.print("What do you want to ask?\n>");
		
		// Create a scanner
		Scanner sc = new Scanner(System.in);
		
		// Take the user's input
		String input = sc.nextLine();
		
		// Analyze the user's input to a FOL Literal
		LiteralFOL a = PL_FOL_Reader.analyseInputLiteralFOL(input);
		
		// Boolean to specify the output
		// True : return a list of unifiers and print them all
		// False : return a specific unifier using user's input
		boolean returnList = false;
		
		if(returnList) {
			
			// Fill a list of unifiers using Horn Forward Chain for FOL
			ArrayList<Unifier> unifierList = ForwardChainHornFOL.FOL_ForwardChain(KB, a, 100, 100);
			
			// For every unifier in the list
			for(Unifier u : unifierList) {
				// If the unifier is not a failure
				if(!u.isFailure()) {
					// If the unifier is empty
					if(u.getUnifierMap().isEmpty()) {
						// Print the answer YES
						System.out.println("YES");
					}else {
						// Print the unifier contents
						System.out.println(u);
					}
				}else {
					// Print the answer NO
					System.out.println("NO");
				}
			}
			
		}else {
			
			// Boolean to specify if the user want to return the first found
			// True : return the first unifier found
			// False : in every unifier found print it and ask the user for "More" results
			//		   or to return the last unifier found
			boolean returnFirst = false;
			
			// Get the unifier using Horn Forward Chain for FOL
			Unifier theta = ForwardChainHornFOL.FOL_ForwardChain(KB, a,100,returnFirst);
			// If the unifier is not a failure
			if(!theta.isFailure()) {
				// If the unifier is empty
				if(theta.getUnifierMap().isEmpty()) {
					// Print the answer YES
					System.out.println("YES");
				}else {
					// Print the unifier contents
					System.out.println(theta);
				}
			}else {
				// Print the answer NO
				System.out.println("NO");
			}
			
		}
		
		// Close the scanner
		sc.close();
		
	}
}
