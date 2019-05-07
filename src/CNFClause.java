// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;
import java.util.Iterator;

// Class for a CNFClause which consists of a conjunction of CNFSubClauses
public class CNFClause 
{	
	// ArrayList with all the SubClauses the Clause has
	private ArrayList<CNFSubClause> theClauses;
	// Boolean variable to indicate if the Clause consist's of horn statements
	private boolean horn;
	
	// Default Constructor
	public CNFClause() {
		// Create a new arrayList
		theClauses = new ArrayList<CNFSubClause>();
		// Set horn to false as a default
		horn = false;
	}
	
	// Getter for SubClauses
    public ArrayList<CNFSubClause> getSubclauses()
    {
        return theClauses;
    }
    
    // Contains method for a SubClause
    public boolean contains(CNFSubClause newS)
    {
    	// For every SubCLause in the Clause
        for(int i = 0; i < theClauses.size(); i ++)
        {
        	// If the Literals in the current SubClause are equal to the Literals in the newS SubCLause
            if(theClauses.get(i).getLiterals().equals(newS.getLiterals()))
            {
            	// Return true - it contains newS
                return true;
            }
        }
        // If none SubClause has been found with the same Literals
        // return false -- it doesn't contain newS
        return false;
    }
    
    // Evaluate the Clause if all it's SubClauses are horn statements
    public void evaluateHorn() {
    	// For every subclause in theClauses
    	for(CNFSubClause sub : theClauses) {
    		// Evaluate Horn for the subclause
    		sub.evaluateHorn();
    		// If it isn't then the clause isn't horn
    		if(!sub.isHorn()) {
    			horn = false;
    			return;
    		}
    	}
    	// If all subclauses are horn then the CNF clause is also horn
    	horn = true;
    }
    
    // Getter for horn boolean
    public boolean isHorn() {
    	return horn;
    }
    
    // Print method for the clause
    public void print() {
    	System.out.println(this);
    }
    
    // Method to override toString from Object
    @Override
	public String toString() {
    	
    	// Create a new StringBuilder
    	StringBuilder sb = new StringBuilder();
    	
    	// If there are more than 1 SubClauses in the Clause
    	// then append to sb a starting parenthesis
    	if(this.getSubclauses().size()>1) sb.append("(");
    	
    	// Get an iterator for the SubClauses
    	Iterator<CNFSubClause> its = this.getSubclauses().iterator();
    	
    	// While the iterator has more SubClauses
    	while(its.hasNext()) {
    		
    		// Get the next SubCLause
    		CNFSubClause sub = its.next();
    		
    		// If there are more than 1 literals in the subclause
    		// then append to sb a starting parenthesis
    		if(sub.getLiterals().size()>1) sb.append("(");
    		
    		// Get the iterator for the SubClause Literals
    		Iterator<LiteralPL> it = sub.getLiteralsList();
    		
    		// While the iterator has more Literals
    		while(it.hasNext()) {
    			
    			// Get the next Literal
    			LiteralPL l = it.next();
    			
    			// If it is Negative
    			if(l.getNeg()) {
    				// Append to sb the literal with a NOT_ prefix
    				sb.append("NOT_" + l.getName());
    			}else {
    				// Append to sb the literal
    				sb.append(l.getName());
    			}
    			
    			// If there is a next literal
    			if(it.hasNext()) {
    				// Append to sb an "OR" connector for the literals
    				sb.append(" OR ");
    			}
    			
    		}
    		
    		// If the SubClause has more than 1 literal
    		// then append to sb a closing parenthesis
    		if(sub.getLiterals().size()>1) sb.append(")");
    		
    		// If there is a next SubClause
    		if(its.hasNext()) {
    			// Append to sb an "AND" connector for the SubClauses
				sb.append(" AND ");
			}
    		
    	}
    	
    	// If the Clause has more than 1 SubClauses
    	// then append to sb a closing parenthesis
    	if(this.getSubclauses().size()>1) sb.append(")");
    	
    	// Return the StringBuilder contents
    	return sb.toString();
    }
    
}
