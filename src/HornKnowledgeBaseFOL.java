// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;

// Represents a KB that contains only HornClauses in FOL
public class HornKnowledgeBaseFOL {

	// Arraylist of all the HornClauses
	private ArrayList<HornClauseFOL> clauses;
	
	// Default Constructor
	public HornKnowledgeBaseFOL() {
		clauses = new ArrayList<>();
	}
	
	// Getter for Horn Clauses
	public ArrayList<HornClauseFOL> getHornClauses(){
		return clauses;
	}
	
}
