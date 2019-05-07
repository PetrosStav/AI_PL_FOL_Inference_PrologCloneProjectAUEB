// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.Arrays;


// Represents a Function of First Order Logic
// eg. FatherOf(x), FatherOf(John), LeftFootOf(Mary) etc
public class FunctionFOL extends TermFOL implements Unifiable,Compound{

	// The number of order of the Literal ( how many strings inside the literal )
	private int order;
	
	// The strings in the Function ( constants and/or variables and/or functions of the previous)
	// constants start with an Uppercase Letter
	// variables start with a Lowercase Letter
	// functions have parentheses after their name
	private TermFOL[] terms;
	
	// Constructor without specifying the terms
	public FunctionFOL(String name,int order) {
		this.name = name;
		this.order = order;
		terms = new TermFOL[order];
	}
	
	// Constructor with all parameters
	public FunctionFOL(String name,int order,TermFOL[] terms) {
		this.name = name;
		this.order = order;
		setTerms(terms);
	}

	// Getter for Name
	public String getName() {
		return name;
	}

	// Setter for Name
	public void setName(String name) {
		this.name = name;
	}

	// Getter for Order
	public int getOrder() {
		return order;
	}

	// Setter for Order
	public void setOrder(int order) {
		this.order = order;
	}

	// Getter for terms
	public TermFOL[] getTerms() {
		return terms;
	}

	// Setter for terms
	public void setTerms(TermFOL[] terms) {
		// If the size of the terms array is different from the Function's order
		// throw an illegalArgumentException
		if(terms.length!=order) throw new IllegalArgumentException();
		// Assign a new array of terms to the function's terms
		this.terms = new TermFOL[order];
		// For each term
		for(int i=0;i<order;i++) {
			// If it is a variable
			if(terms[i].getClass()==VariableFOL.class) {
				// Cast it to a variable
				VariableFOL v = (VariableFOL)terms[i];
				// Create a new variable from the term and assign it to
				// the corresponding term in the function
				this.terms[i] = new VariableFOL(v.getName());
			}else if(terms[i].getClass()==FunctionFOL.class) {
				// Else if it is a function
				// Cast it to a function
				FunctionFOL f = (FunctionFOL)terms[i];
				// Create a new function from the term and assign it to
				// the corresponding term in the function
				this.terms[i] = new FunctionFOL(f.getName(),f.getOrder(),f.getTerms());
			}else {
				// Else it is a constant
				// Cast it to a constant
				ConstantFOL c = (ConstantFOL)terms[i];
				// Create a new constant from the term and assign it to
				// the corresponding term in the function
				this.terms[i] = new ConstantFOL(c.getName());
			}
		}
	}

	// Method that returns the hashCode of the Function
	@Override
	public int hashCode() {
		// Initialize the prime number 31
		final int prime = 31;
		// Get the hashCode of the superclass and put it into
		// a variable named result
		int result = super.hashCode();
		// Multiply the result by the prime number and add to it the order
		// of the function as an integer
		result = prime * result + order;
		// Multiply the result by the prime number and add to it the hashCode
		// of the terms array
		result = prime * result + Arrays.hashCode(terms);
		// Return the result variable
		return result;
	}

	// Method that returns if an Object is the same as this Function
	@Override
	public boolean equals(Object obj) {
		// If it is the same object return true
		if (this == obj)
			return true;
		// If the superclass equals method returns false
		// then return false
		if (!super.equals(obj))
			return false;
		// If the objects have different class
		// then return false
		if (getClass() != obj.getClass())
			return false;
		// Cast the obj to a Function
		FunctionFOL other = (FunctionFOL) obj;
		// If they don't have the same order return false
		if (order != other.order)
			return false;
		// If they don't have the same terms return false
		if (!Arrays.equals(terms, other.terms))
			return false;
		// Else if nothing of the above
		// It is the same object(Function) so return true
		return true;
	}
	
	// Method that overrides the toString method from Object class
	@Override
	public String toString() {
		return super.toString() + Arrays.toString(terms);
	}
	
}
