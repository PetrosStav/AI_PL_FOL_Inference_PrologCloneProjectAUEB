// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;

// Class that has the PL_Resolution algorithm that asks if a CNFClause a is logically inferred from a KB
public class ResolutionPL 
{     
    // The resolution algorithm -- The CNF Clause given to resolve is the CNF of the negative we want to resolve
    public static boolean PL_Resolution(CNFClause KB, CNFClause a) {
    	
    	// Create a new CNFClause with the name clauses
        CNFClause clauses = new CNFClause();
        // Add to clauses all the SubClauses of the KB
        clauses.getSubclauses().addAll(KB.getSubclauses());
        
        // Plus the CNF of the negation of what we want to prove (it is given in CNF by the parameters)
        
        // For every SubCLause in a
        for(CNFSubClause s : a.getSubclauses()) {
        	// Add the SubClause to clauses
            clauses.getSubclauses().add(s);
        }

        // Initialize a boolean that indicates when the loop will stop to false
        boolean stop = false;
        
        // We will try resolution till either we reach a contradiction or cannot produce any new clauses
        
        // While stop is false
        while(!stop)
        {
        	// Create a new ArrayList of SubClauses that will hold the new SubClauses in this loop
            ArrayList<CNFSubClause> newsubclauses = new ArrayList<CNFSubClause>();
            // Assign all the SubClauses of the clauses CNFClause to a reference subclauses 
            ArrayList<CNFSubClause> subclauses = clauses.getSubclauses();

            // For every SubClause in subclauses
            for(int i = 0; i < subclauses.size(); i++)
            {
            	// Get the SubClause and assign it to Ci
                CNFSubClause Ci = subclauses.get(i);

                // For every SubClause in subclauses after Ci
                for(int j = i+1; j < subclauses.size(); j++)
                {
                	// Get the SubClause and assign it to Cj
                    CNFSubClause Cj = subclauses.get(j);

                    // Apply resolution to Ci and Cj and get all the new SubClauses that are resolved
                    ArrayList<CNFSubClause> new_subclauses_for_ci_cj = CNFSubClause.resolution(Ci, Cj);

                    // For every SubClause in new_subclauses_for_ci_cj
                    for(int k = 0; k < new_subclauses_for_ci_cj.size(); k++)
                    {
                    	// Get the SubClause and assign it to newsubclause
                        CNFSubClause newsubclause = new_subclauses_for_ci_cj.get(k);

                        // If it is empty then the subclause generated has reached contradiction and the CNFClause a has been proved
                        // so return true
                        if(newsubclause.isEmpty()) 
                        {   
                            return true;
                        }
                        
                        // If it is not contained in either the newsubclauses or the clauses then it doesn't already exist
                        if(!newsubclauses.contains(newsubclause) && !clauses.contains(newsubclause))
                        {
                        	// Add it to the newsubclauses
                            newsubclauses.add(newsubclause);
                        }
                    }                           
                }
            }
            
            // Initialize a boolean that indicates if there is a new SubClause found to false
            boolean newClauseFound = false;
            
            // Because of "if(!newsubclauses.contains(newsubclause) && !clauses.contains(newsubclause))",
            // if there isnt a new subclause that is different from the clauses list (copy of KB at the start) then newsubclauses
            // should be empty, otherwise it will have unique new subclauses
            
            // If newsubclauses is not empty
            if(!newsubclauses.isEmpty()) {
            	// Add all the new SubClauses to the clauses (copy of KB at the start)
            	clauses.getSubclauses().addAll(newsubclauses);
            	// Indicate that we have found new SubClauses
            	newClauseFound = true;
            }

            // If we haven't found new SubClauses then the Knowledge Base does not logically infer the literal we wanted to prove
            // so stop the loop
            if(!newClauseFound)
            {
                stop = true;
            }   
        }
        // The loop stopped and we haven't found a contradiction, so return false
        return false;
    }

}
