// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

// Represents a Literal in PL
// eg. P, -Q
public class LiteralPL implements Comparable<LiteralPL>
{
    // The name of the literal
    private String Name;
    // Whether or not the literal is negated; if negation is true then it is negated
    private boolean negation;
    
    // Parameterized Constructor
    public LiteralPL(String n, boolean neg)
    {
        this.Name = n;
        this.negation = neg;
    }
    
    // Method to print the Literal
    public void print()
    {
        System.out.println(this);
    }
    
    // Setter for name
    public void setName(String n)
    {
        this.Name = n;
    }
    
    // Getter for name
    public String getName()
    {
        return this.Name;
    }
    
    // Setter for negation
    public void setNeg(boolean b)
    {
        this.negation = b;
    }
    
    // Getter for negation
    public boolean getNeg()
    {
        return this.negation;
    }
    
    // Method that check's if two LiteralPLs are the same
    @Override
    public boolean equals(Object obj)
    {
    	// Cast the obj to a LiteralPL
        LiteralPL l = (LiteralPL)obj;

        // If they have the same name and the same negation return true
        if(l.getName().compareTo(this.Name) ==0 && l.getNeg() == this.negation)
        {
            return true;
        }
        // Else they are not the same, so return false
        else
        {
            return false;
        }

    }
	
    // Method that returns the hashCode for the LiteralPL
    @Override
    public int hashCode()
    {
    	// If it is negated
        if(this.negation)
        {
        	// Returns the hashCode of the Name added by 1
            return this.Name.hashCode() + 1;
        }
        else
        {
        	// Returns the hashCode of the Name
            return this.Name.hashCode() + 0;                        
        }
    }
	
    // Method that compares two LiteralPLs
    @Override
    public int compareTo(LiteralPL x)
    {
    	// Initialize an integer a to 0
        int a = 0;
        // Initialize an integer b to 0
        int b = 0;
        // If x is negated then set a to 1
        if(x.getNeg())
            a = 1;
        // If this is negated then set b to 1
        if(this.getNeg())
            b = 1;
        // Return the comparison of the Literals' Names added by the difference of a and b
        return x.getName().compareTo(Name) + a-b;
    }
    
    // Method that overrides the toString method from Object class
    @Override
    public String toString() {
    	return (negation)?"NOT_" + Name:Name;
    }
}
