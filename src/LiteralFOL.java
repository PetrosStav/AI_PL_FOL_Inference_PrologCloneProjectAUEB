// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;
import java.util.Arrays;

// Represents a Literal of First Order Logic
// eg. Cat(x) , Loves(x,y) , Cat(Psita) , Loves(John,y) , Cat(PetOf(x)) , Cat(PetOf(Mary)) etc
public class LiteralFOL implements Unifiable,Compound{
	
	// The name of the Literal
	private String name;
	
	// The number of order of the Literal ( how many strings inside the literal )
	private int order;
	
	// The strings in the Literal ( constants and/or variables and/or functions of the previous)
	// constants start with an Uppercase Letter
	// variables start with a Lowercase Letter
	// functions have parentheses after their name
	private TermFOL[] terms;
	
	// Whether or not the literal is negated; if negation is true then it is negated
	private boolean negation;
	
	// Constructor without specifying the terms
	public LiteralFOL(String name,int order,boolean negation) {
		this.name = name;
		this.order = order;
		this.negation = negation;
		terms = new TermFOL[order];
	}
	
	// Constructor with all parameters
	public LiteralFOL(String name,int order,TermFOL[] terms,boolean negation) {
		this.name = name;
		this.order = order;
		setTerms(terms);
		this.negation = negation;
	}
	
	// Print the literal
	public void print()
    {
        if(negation)
            System.out.println("NOT_" + name);
        else
            System.out.println(name);
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
		// Assign a new array of terms to the literal's terms
		this.terms = new TermFOL[order];
		// For each term
		for(int i=0;i<order;i++) {
			// If it is a variable
			if(terms[i].getClass()==VariableFOL.class) {
				// Cast it to a variable
				VariableFOL v = (VariableFOL)terms[i];
				// Create a new variable from the term and assign it to
				// the corresponding term in the literal
				this.terms[i] = new VariableFOL(v.getName());
			}else if(terms[i].getClass()==FunctionFOL.class) {
				// Else if it is a function
				// Cast it to a function
				FunctionFOL f = (FunctionFOL)terms[i];
				// Create a new function from the term and assign it to
				// the corresponding term in the literal
				this.terms[i] = new FunctionFOL(f.getName(),f.getOrder(),f.getTerms());
			}else {
				// Else it is a constant
				// Cast it to a constant
				ConstantFOL c = (ConstantFOL)terms[i];
				// Create a new constant from the term and assign it to
				// the corresponding term in the literal
				this.terms[i] = new ConstantFOL(c.getName());
			}
		}
	}

	// Getter for Negation
	public boolean getNegation() {
		return negation;
	}

	// Setter for Negation
	public void setNegation(boolean negation) {
		this.negation = negation;
	}

	// Method that returns the hashCode of the Function
	@Override
	public int hashCode() {
		// Initialize the prime number 31
		final int prime = 31;
		// Initialize an integer (result) to 1
		int result = 1;
		// Multiply the result by the prime number and add the name hashCode if it isn't null
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		// Multiply the result by the prime number and add a prime number depending if it is
		// a negation or not
		result = prime * result + (negation ? 1231 : 1237);
		// Multiply the result by the prime number and add the hashCode of the terms array
		result = prime * result + Arrays.hashCode(terms);
		// Multiply the result by the prime number and add the order of the literal
		result = prime * result + order;
		// Return the result
		return result;
	}

	// Method that returns if an Object is the same as this LiteralFOL
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
		// Cast the obj to a LiteralFOL
		LiteralFOL other = (LiteralFOL) obj;
		// If they don't have the same name or one is null
		// and the other's isn't return false
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		// If they don't have the same negation (true/false) return false
		if (negation != other.negation)
			return false;
		// If they don't have the same Terms return false
		if (!Arrays.equals(terms, other.terms))
			return false;
		// If they don't have the same order return false
		if (order != other.order)
			return false;
		// Else if nothing of the above
		// It is the same object(LiteralFOL), so return true
		return true;
	}
	
	// Method that overrides the toString method from Object class
	@Override
	public String toString() {
		return ((negation)?"NOT_":"") + name + Arrays.toString(terms);
	}
	
	// Method that unifies the unifiable objects z and x using unifier theta
	// Return the resulting unifier, which can be a failure if the unification
	// can't be done
	public static Unifier unify(Unifiable z,Unifiable x, Unifier theta) {
		// If failure return the failure
		if(theta.isFailure()) return theta;
		
		// If z == x then return the unifier theta
		if(z.equals(x)) return theta;
		
		// If z is a variable
		if(z.getClass() == VariableFOL.class) {
			// If x is instance of TermFOL
			if(TermFOL.class.isInstance(x)) return unify_var((VariableFOL)z,(TermFOL)x,theta);
		}
		
		// If x is a variable
		if(x.getClass() == VariableFOL.class) {
			// If z is instance of TermFOL
			if(TermFOL.class.isInstance(z)) return unify_var((VariableFOL)x,(TermFOL)z,theta);
		}
		
		// If both have the same class and they are compound
		if(z.getClass() == x.getClass() && Compound.class.isInstance(z)) {
			// If the have the same name
			if(z.getName().equals(x.getName())) {
					// Cast z to Compound
					Compound ztemp = (Compound)z;
					// Cast x to Compound
					Compound xtemp = (Compound)x;
					// Make a list of z's args
					ArrayList<Unifiable> argsz = new ArrayList<>(Arrays.asList(ztemp.getTerms()));
					// Make a list of x's args
					ArrayList<Unifiable> argsx = new ArrayList<>(Arrays.asList(xtemp.getTerms()));
					// Return the unification of their args
					return unify(argsz,argsx,theta);
			}
		}
		
		//Else return failure
		theta.setFailure(true);
		return theta;
	}
	
	// Method that unifies the List of unifiable objects z and x using unifier theta
	// Return the resulting unifier, which can be a failure if the unification
	// can't be done
	public static Unifier unify(ArrayList<Unifiable> z,ArrayList<Unifiable> x, Unifier theta) {
		// If both are lists and have the same size
		if(z.size()==x.size()) {
			// If the size is 1
			if(z.size()==1) {
				// Return the unification of the first (and only) arguments
				return unify(z.get(0),x.get(0),theta);
			}else {
				// Return the unification of the rest of the list of their arguments
				// using as theta the unification of the first arguments
				return unify(new ArrayList<Unifiable>(z.subList(1,z.size())),new ArrayList<Unifiable>(x.subList(1, x.size())), unify(z.get(0),x.get(0),theta));
			}
		}
		// Else return failure
		theta.setFailure(true);
		return theta;
	}
	
	// Method that unifies a variable b with a term x using unifier theta
	// Return the resulting unifier, which can be a failure if the unification
	// can't be done
	private static Unifier unify_var(VariableFOL b, TermFOL x, Unifier theta) {
		
		// If b/value in theta
		if(theta.getUnifierMap().containsKey(b)) return unify(theta.getUnifierMap().get(b),x,theta);
		
		// If x is variable
		if(x.getClass()==VariableFOL.class) {
			// If x/value in theta
			if(theta.getUnifierMap().containsKey(x)) return unify(b,theta.getUnifierMap().get(x),theta);
		}
		
		// If x is function
		if(x.getClass()==FunctionFOL.class) {
			// CHeck if b is occurring in x
			FunctionFOL f = (FunctionFOL)x;
			// For each term of function f
			for(TermFOL t : f.getTerms()) {
				// If we find b in a term of f
				if(b.equals(t)) {
					// Return failure
					theta.setFailure(true);
					return theta;
				}
			}
		}
		
		// Else return {b/x} U theta
		theta.getUnifierMap().put(b, x);
		return theta;
	}
	
	
}
