// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

// A Unifier class that holds a dictionary(hashmap) that translates variables to terms
// for use in FOL
// eg. theta = {x/John,y/Mary}
// If the unification can't be done then the unifier becomes a failure
// If the unification hasn't failed and the dictionary is empty, that means that the terms
// which are unified are the same
// eg. Literals Criminal(West) and Criminal(West) can be unified with the empty unifier: { }
public class Unifier {
	
	// A hashmap to store all the translation from variables to terms
	private HashMap<VariableFOL,TermFOL> unifierMap;
	// A boolean to indicate if the unifier is a failure
	private boolean failure;
	
	// Default Constructor
	public Unifier() {
		unifierMap = new HashMap<>();
		failure = false;
	}
	
	// Parameterized Constructor
	public Unifier(HashMap<VariableFOL,TermFOL> unifierMap) {
		setUnifierMap(unifierMap);
		failure = false;
	}
	
	// Getter for failure
	public boolean isFailure() {
		return failure;
	}

	// Setter for failure
	public void setFailure(boolean failure) {
		this.failure = failure;
	}

	// Getter for unifierMap
	public HashMap<VariableFOL, TermFOL> getUnifierMap() {
		return unifierMap;
	}

	// Setter for unifierMap
	public void setUnifierMap(HashMap<VariableFOL, TermFOL> unifierMap) {
		this.unifierMap = new HashMap<>();
		for(Entry<VariableFOL,TermFOL> e : unifierMap.entrySet()) {
			this.unifierMap.put(e.getKey(), e.getValue());
		}
	}
	
	// Print method for unifier
	public void print() {
		System.out.println(this);
	}

	// Method that returns the hashCode of the Unifier
	@Override
	public int hashCode() {
		// Initialize the prime number 31
		final int prime = 31;
		// Initialize an integer (result) to 1
		int result = 1;
		// Multiply the result by the prime number and add the unifierMap hashCode if it isn't null
		result = prime * result + ((unifierMap == null) ? 0 : unifierMap.hashCode());
		// Return the result variable
		return result;
	}

	// Method that returns if an Object is the same as this Unifier
	@Override
	public boolean equals(Object obj) {
		// If it is the same object return true
		if (this == obj)
			return true;
		// If obj is null return false
		if (obj == null)
			return false;
		// If the objects have different class then return false
		if (getClass() != obj.getClass())
			return false;
		// Cast the obj to a Unifier
		Unifier other = (Unifier) obj;
		// If they don't have the same unifierMap or one's is null
		// and the other's isn't return false
		if (unifierMap == null) {
			if (other.unifierMap != null)
				return false;
		} else if (!unifierMap.equals(other.unifierMap))
			return false;
		// Else if nothing of the above
		// It is the same object(Unifier), so return true
		return true;
	}
	
	// Method that overrides the toString method from Object class
	@Override
	public String toString() {
		// Create a new StringBuilder
		StringBuilder sb = new StringBuilder();
		// Append a left bracket
		sb.append("{ ");
		// Get an iterator to the unifierMap
		Iterator<Entry<VariableFOL, TermFOL>> it = unifierMap.entrySet().iterator();
		// While the iterator has more Entries
		while(it.hasNext()) {
			// Get the next entry
			Entry<VariableFOL,TermFOL> e = (Entry<VariableFOL, TermFOL>) it.next();
			// Append the variable, then the '/' symbol and then the term corresponding to the variable
			// in the unifierMap, also if there are more entries append a comma
			sb.append(e.getKey() + "/" + e.getValue() + (it.hasNext()?" , ":" "));
		}
		// Append a right bracket
		sb.append(" }");
		// Return the String of the StringBuilder
		return sb.toString();
	}
	
	
}
