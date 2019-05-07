// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

// Class that has the forward chain algorithm for Horn PL that asks if a Literal a is logically inferred from a KB
// which contains only Horn statements in PL
public class ForwardChainHornPL {
	
	// Method that performs the horn forward chain in PL 
	@SuppressWarnings("unlikely-arg-type")
	public static boolean PL_HornForwardChain(CNFClause KB,LiteralPL a) {
		
		// Horn Statements are written as CNF
		// in the program we use getName to get the Literal without regarding if
		// it is positive or negative for uniqueness
		
		// Check that the KB is made of horn statements
		if(!KB.isHorn()) {
			System.out.println("Knowledge Base is not made of Horn Statements!");
			return false;
		}
		
		// Check if Literal a is positive
		if(a.getNeg()) {
			System.out.println("Literal must be positive!");
			return false;
		}
		
		// Find the all the literals in the KB
		// Create a hashset to find the unique ones (not duplicates)
		HashSet<String> lits = new HashSet<>();
		for(CNFSubClause sub : KB.getSubclauses()) {
			for(LiteralPL l : sub.getLiterals()) {
				lits.add(l.getName());
			}
		}
		
		// The agenda of Literals we haven't inspected yet - begins with all the facts
		Stack<LiteralPL> agenda = new Stack<>();
		for(CNFSubClause sub : KB.getSubclauses()) {
			// If it has only one literal
			if(sub.getLiterals().size()==1) {
				// Get the literal
				LiteralPL l = sub.getLiterals().iterator().next();
				// If it is positive it is a fact -- add it to the agenda
				if(!l.getNeg()) agenda.push(l);
			}
		}
		
		// Table that has every literal and whether we have inferred them
		HashMap<String,Boolean> inferred = new HashMap<>();
		// Put every unique literal in the refered hashmap
		// with the key false
		for(String l : lits) {
			inferred.put(l, false);
		}
		
		// Table that has every Horn clause's positive literal and the count of the literals needed to infer them (become true)
		HashMap<CNFSubClause,Integer> count = new HashMap<>();
		// For evert subclause in the KB
		for(CNFSubClause sub : KB.getSubclauses()) {
			// Initialize a variable to 0
			int n = 0;
			// For every literal
			for(LiteralPL l : sub.getLiterals()) {
				// If it is negative
				if(l.getNeg()) {
					// Increment the variable
					n++;
				}
			}
			// Put every subclause in the hashmap with the key that
			// corresponds to how many literals have to be inferred
			if(n!=0) {
				count.put(sub, n);
			}
		}
		
		// Check the agenda for literal a
		for(LiteralPL l : agenda) {
			if(l.equals(a)) return true;
		}
		
		while(!agenda.isEmpty()) {
			// Pop the literal in the agenda
			LiteralPL p = agenda.pop();
			// If the literal is not inferred
			if(!inferred.get(p.getName())) {
				// Infer it
				inferred.replace(p.getName(), true);
				// For each subclause in the count hashmap
				for(Map.Entry<CNFSubClause, Integer> c : count.entrySet()) {
					// For each literal in the subclause
					for(LiteralPL l : c.getKey().getLiterals()) {
						// If it has the same literal name
						if(l.getName().equals(p.getName())) {
							// Decrease the count
							c.setValue(c.getValue()-1);
						}
					}
					
					// If the count is zero then the subclause is triggered
					if(c.getValue()==0) {
						// Find the positive of the subclause
						
						// For each literal in the subclause
						for(LiteralPL l : c.getKey().getLiterals()) {
							// If it is positive
							if(!l.getNeg()) {
								// If the literal is what we want to resolve return true
								if(l.equals(a)) return true;
								// Else push it ot the agenda
								agenda.push(l);
								// break;
							}
						}
						// Remove the subclause
						count.remove(c);
						
					}
				}
			}
		}
		// If the agenda is empty and the literal hasnt been found then
		// it cannot be resolved, so return false
		return false;
	}
	
}
