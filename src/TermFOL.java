// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

// This is a term of either a Literal or a Function in FOL
// It is abstract so no instances can be made of it
public abstract class TermFOL implements Unifiable{
	
	// The name of the Term
	protected String name;

	// Getter for the name
	public String getName() {
		return name;
	}

	// Setter for the name
	public void setName(String name) {
		this.name = name;
	}

	// Method that returns the hashCode of the Term
	@Override
	public int hashCode() {
		// Initialize the prime number 31
		final int prime = 31;
		// Initialize an integer (result) to 1
		int result = 1;
		// Multiply the result by the prime number and add the hashCode of the name if it isn't null
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		// Return the result variable
		return result;
	}

	// Method that returns if an Object is the same as this Term
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
		// Cast the obj to a TermFOL
		TermFOL other = (TermFOL) obj;
		// If they don't have the same name or one is null
		// and the other's isn't return false
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		// Else if nothing of the above
		// It is the same object(TermFOL), so return true
		return true;
	}
	
	// Method that overrides the toString method from Object class
	@Override
	public String toString() {
		return name;
	}
	
}
