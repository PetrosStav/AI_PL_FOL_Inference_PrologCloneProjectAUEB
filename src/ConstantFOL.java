// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

// A constant which is a term in FOL
public class ConstantFOL extends TermFOL implements Unifiable{
	
	// Parameterized Constructor
	public ConstantFOL(String name) {
		this.name = name;
	}

	// Method to override toString method from Object
	@Override
	public String toString() {
		return super.toString();
	}
	
}
