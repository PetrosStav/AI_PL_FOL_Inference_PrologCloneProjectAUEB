// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;
import java.util.Iterator;

// Represents a Horn Clause which has hypotheses and a result
// eg. Cat(x) => Animal(x)
//	   Loves(x,y) AND -Loves(y,x) => Sad(x)
//     Animal(x) AND Owns(John,x) => Pet(x)
// If a Horn Clause hasn't a result (result==null) then the
// clause has only 1 hypotheses and it is called a fact
// eg. Loves(Mary,George), Cat(Psita)
public class HornClauseFOL {

	// Hypotheses : left side of Horn Clause
	ArrayList<LiteralFOL> hypotheses;
	
	// Result : right side of Horn Clause
	LiteralFOL result;
	
	// Default Constructor
	public HornClauseFOL() {
		hypotheses = new ArrayList<>();
		result = null;
	}
	
	// Parameterized Constructor
	public HornClauseFOL(ArrayList<LiteralFOL> hypotheses,LiteralFOL result) {
		// Set the hypotheses
		setHypotheses(hypotheses);
		// Set the result
		if(result==null) this.result=null;
		else setResult(result);
	}

	// Getter for hypotheses
	public ArrayList<LiteralFOL> getHypotheses() {
		return hypotheses;
	}

	// Setter for hypotheses
	public void setHypotheses(ArrayList<LiteralFOL> hypotheses) {
		// Create hypotheses arraylist
		this.hypotheses = new ArrayList<>();
		// For each literal in hypotheses list from the parameter
		for(LiteralFOL l : hypotheses) {
			// Add each new literal to the hypotheses
			this.hypotheses.add(new LiteralFOL(l.getName(),l.getOrder(),l.getTerms(),l.getNegation()));
		}
	}

	// Getter for result
	public LiteralFOL getResult() {
		return result;
	}

	// Setter for result
	public void setResult(LiteralFOL result) {
		this.result = new LiteralFOL(result.getName(),result.getOrder(),result.getTerms(),result.getNegation());
	}

	// Method that return the hashcode for the HornClause
	@Override
	public int hashCode() {
		// Initialize the prime number 31
		final int prime = 31;
		// Initialize an integer (result) to 1
		int result = 1;
		// Multiply the result by the prime number and add the hashCode of the hypotheses if they are not null
		result = prime * result + ((hypotheses == null) ? 0 : hypotheses.hashCode());
		// Multiply the result by the prime number and add the hasCode of the result if it is not null
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		// Return the result variable
		return result;
	}

	// Method that checks if two HornClauses are equal
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
		// Cast the obj to a HornClauseFOL
		HornClauseFOL other = (HornClauseFOL) obj;
		// If they don't have the same hypotheses or one's are null
		// and the other's aren't return false
		if (hypotheses == null) {
			if (other.hypotheses != null)
				return false;
		} else if (!hypotheses.equals(other.hypotheses))
			return false;
		// If they dont have the same result or one's is null
		// and the other's isn't return false
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		// Else if nothing of the above
		// It is the same object(HornCLauseFOL), so return true
		return true;
	}
	
	// Method that overrides the toString method from Object class
	@Override
	public String toString() {
		// Create a StringBuilder
		StringBuilder sb = new StringBuilder();
		// Get the iterator for the hypotheses
		Iterator<LiteralFOL> it = hypotheses.iterator();
		// While there are more LiteralFOLs
		while(it.hasNext()) {
			// Append the String representation of the LiteralFOL
			sb.append(it.next());
			// If it is not the last LiteralFOL
			if(it.hasNext()) {
				// Append an "AND"
				sb.append(" AND ");
			}
		}
		// If we have a result
		if(result!=null) {
			// Append the "=>" symbol and the result
			sb.append(" => " + result);
		}
		return sb.toString();
	}
	
	
}
