// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

// A variable which is a term in FOL
public class VariableFOL extends TermFOL implements Unifiable{

	// The baseName of the variable (needed because the real name will be changed by the newvars method)
	String baseName;
	
	// Parameterized Constructor
	public VariableFOL(String name) {
		baseName = name;
		this.name = name;
	}

	// Getter for the baseName
	public String getBaseName() {
		return baseName;
	}

	// Setter for the baseName
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	// Method that overrides the toString method from Object class
	@Override
	public String toString() {
		return super.toString();
	}
	
}
