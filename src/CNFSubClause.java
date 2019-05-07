// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

// Class for a CNF SubClause which consists of a disjunction of Literals
public class CNFSubClause implements Comparable<CNFSubClause>
{
    // A HashSet of all the literals contained in the SubClause
    private HashSet<LiteralPL> literals;
    // Boolean variable to indicate if the SubClause is a horn statements
    private boolean horn;
    
    // Default Constructor
    public CNFSubClause()
    {
    	// Create a new HashSet of Literals
        literals = new HashSet<LiteralPL>();
        // Set the default value for the horn boolean to false
        horn = false;
    }
    
    // Getter for the literals HashSet
    public  HashSet<LiteralPL> getLiterals()
    {
        return literals;
    }
    
    // Getter for the literals HashSet Iterator
    public Iterator<LiteralPL> getLiteralsList()
    {
        return literals.iterator();
    }
    
    // Method that returns if there are no literals in the HashSet
    public boolean isEmpty()
    {
        return literals.isEmpty();
    }
    
    // Method that returns if the SubClause is a horn statement
    public boolean isHorn() {
    	return horn;
    }
    
    // Evaluate the SubClause If it is a horn statement
    public void evaluateHorn() {
    	// Start with 0 positives
    	int c = 0;
    	// For every Literal
    	for(LiteralPL l : literals) {
    		// If it is positive
    		if(!l.getNeg()) {
    			// Increment the counter
    			c++;
    		}
    		// If positives > 1 then it isn't horn
    		if(c>1) {
    			horn = false;
    			return;
    		}
    	}
    	// If c=0 or c=1 then it is horn
    	horn = true;
    }
    
    // Print method for the SubClause
    public void print()
    {
        System.out.println(this);
    }

    // Applies resolution on two CNFSubClauses
    // The resulting clause will contain all the literals of both CNFSubclauses
    // except the pair of literals that are a negation of each other.
    public static ArrayList<CNFSubClause> resolution(CNFSubClause CNF_SC_1, CNFSubClause CNF_SC_2)
    {
    	// Create an ArrayList to hold all the new SubClauses
        ArrayList<CNFSubClause> newClauses = new ArrayList<CNFSubClause>();

        // Get the iterator for the first SubCLause
        Iterator<LiteralPL> iter = CNF_SC_1.getLiteralsList();

        // The loop runs for all literals producing a different new clause
        // for each different pair of literals that negate each other
        
        // While the first SubClause has more Literals
        while(iter.hasNext())
        {            
        	// Get the next literal
            LiteralPL l = iter.next();
            // Create the negative of the literal l
            LiteralPL m = new LiteralPL(l.getName(), !l.getNeg());

            // If the second SubClause contains the Literal m which
            // is the negation of the Literal l in the first clause
            if(CNF_SC_2.getLiterals().contains(m))
            {
                // We construct a new SubClause that contains all the literals of both CNFSubclauses
            	// except the Literal l and it's negation (Literal m) 
                CNFSubClause newClause = new CNFSubClause();

                // We add the literals of the first SubClause in a new HashSet of Literals
                HashSet<LiteralPL> CNF_SC_1_Lits = new HashSet<LiteralPL>(CNF_SC_1.getLiterals());
                // We add the literals of the second SubClause in a new hashSet of Literals
                HashSet<LiteralPL> CNF_SC_2_Lits = new HashSet<LiteralPL>(CNF_SC_2.getLiterals());
                // Remove the Literal l from the first HashSet
                CNF_SC_1_Lits.remove(l);
                // Remove the Literal m from the second HashSet
                CNF_SC_2_Lits.remove(m);

                // Normally we have to remove duplicates of the same literal
                // the new clause must not contain the same literal more than once
                // But since we use HashSet only one copy of a literal will be contained anyway
                
                // Add the first HashSet to the newClause
                newClause.getLiterals().addAll(CNF_SC_1_Lits);
                // Add the second HashSet to the newClause
                newClause.getLiterals().addAll(CNF_SC_2_Lits);
                // Add the new SubClause to the list of the new SubClauses 
                newClauses.add(newClause);
            }
        }
        // Return the List of the new SubClauses
        return newClauses;
    }
    
    // Method that returns if two CNFSubCLauses are equal
    @Override
    public boolean equals(Object obj)
    {
    	// Cast the object to a CNFSubClause
        CNFSubClause l = (CNFSubClause)obj;

        // Check if the Literal list size of the current SubClause
        // and the size of literal list of l are the same, if not
        // return false
        if(l.getLiterals().size() != this.getLiterals().size())
            return false;
        
        // Get the iterator for the SubClause's Literals
        Iterator<LiteralPL> iter = l.getLiteralsList();
        
        // While there are more Literals in the SubClause
        while(iter.hasNext())
        {
        	// Get the next Literal
            LiteralPL lit = iter.next();
            // If the current SubCLause doesn't contain
            // the literal lit return false
            if(!this.getLiterals().contains(lit))
                return false;
        }
        // If nothing from the above returned false
        // then it is the same, return true
        return true;
    }
	
    // Method that returns the hashCode of the SubClause
    @Override
    public int hashCode()
    {
    	// Get the iterator for the Literals from the SubClause
        Iterator<LiteralPL> iter = this.getLiteralsList();
        // Initialize an integer to 0
        int code = 0;
        
        // While there are more Literals in the iterator
        while(iter.hasNext())
        {
        	// Get the next Literal
            LiteralPL lit = iter.next();
            // Calculate the Literal's hashCode and add it to code variable
            code = code + lit.hashCode();
        }
        // Return the code variable
        return code;
    }
	
    // Method that compares two SubCLauses
    @Override
    public int compareTo(CNFSubClause x)
    {
    	// Initialize an integer to 0
        int cmp = 0;
        
        // Get an iterator to the SubClause x Literals 
        Iterator<LiteralPL> iter = x.getLiteralsList();
        
        // While the iterator iter has more Literals
        while(iter.hasNext())
        {
        	// Get the next literal
            LiteralPL lit = iter.next();
            
            // Get an iterator to this SubClause Literals
            Iterator<LiteralPL> iter2 = this.getLiterals().iterator();
            
            // While the iterator iter2 has more Literals
            while(iter2.hasNext())
            {                
            	// Get the next Literal
                LiteralPL lit2 = iter2.next();
                // Calculate the compareTo method for the two literals
                // and add the returned integer to the cmp variable
                cmp = cmp + lit.compareTo(lit2);
            }
        }
        // Return the cmp variable
        return cmp;
    }
    
    // Method to override the toString method from Object
    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    	
    	// If there are more than 1 literals in the subclause
		// then append to sb a starting parenthesis
		if(this.getLiterals().size()>1) sb.append("(");
		
		// Get the iterator for the SubClause Literals
		Iterator<LiteralPL> it = this.getLiteralsList();
		
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
		if(this.getLiterals().size()>1) sb.append(")");
    	
		// Return the StringBuilder contents
		return sb.toString();
    }
}
