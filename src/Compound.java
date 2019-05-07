// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

// Interface for all the Compound Objects in FOL
public interface Compound {
	
	// All Objects that are compound must have:
	
	// Getter of Terms
	public TermFOL[] getTerms();
	
	// Setter for Terms
	public void setTerms(TermFOL[] terms);
		
	// Getter for order
	public int getOrder();
	
	// Setter for order
	public void setOrder(int order);
	
}
