// Authors
// Stavropoulos Petros (AM : 3150230)
// Savvidis Konstantinos (AM : 3150229)
// Mpanakos Vasileios (AM : 3140125)

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

// Class that has the algorithm for the horn forward chain in FOL that asks if a Literal in FOL is inferred from
// the KB, which has only Horn Statements in FOL, or asks to return a list of possible substitutions in which the
// Literal can be changed so that it is inferred from the KB
// eg. Criminal(x) --> {x/West}
public class ForwardChainHornFOL {
	
	// Method that performs the horn forward chain algorithm in FOL
	// HornKnowledgeBaseFOL KB is the knowledge base of horn statements
	// LiteralFOL a is the literal that the user asks the KB
	// maxsteps is a threshold of how many loops to execute after stopping the algorithm because an answer can't be found (FOL is semi decidable)
	// returnFirst is to indicate if the user wants to return the first unifier found by the algorithm or to show each unifier for the user to select
	// Return a Unifier that has all the substitutions for the unification or it is a failure
	public static Unifier FOL_ForwardChain(HornKnowledgeBaseFOL KB, LiteralFOL a,int maxsteps,boolean returnFirst) {
		
		// Global count for new vars
		int count_var = 1;
		
		// Check if a has Variables or it is a specific question for a positive literal
		// Create a boolean variable to store the answer
		boolean isSpecificQuestion = true;
		// For each Term in literal a
		for(TermFOL t : a.getTerms()) {
			// Check if term has variables
			if(hasVariables(t)) {
				// If it has then it is not a specific question
				isSpecificQuestion = false;
				// Break the loop
				break;
			}
		}
		
		// Create a scanner
		Scanner sc = new Scanner(System.in);
		
		// Search first the facts given for the literal a
		for(HornClauseFOL h : KB.getHornClauses()) {
			// If it has no result, thus it is a fact
			if(h.getResult()==null) {
				// Get the literal from the horn clause
				LiteralFOL l = h.getHypotheses().get(0);
				// Initialize a boolean that indicates if the following terms have variables
				boolean hasV = false;
				// For each term in l
				for(TermFOL t : l.getTerms()) {
					// If the term has variables
					if(hasVariables(t)) {
						// Set hasV to true and break the loop
						hasV = true;
						break;
					}
				}
				// If any term has variables then change the variable names using count_var
				if(hasV) newVars(l, count_var++);
				// Create a unifier
				Unifier theta = new Unifier();
				// Unify the literal with a
				theta = LiteralFOL.unify(l,a,theta);
				// If it has not failed
				if(!theta.isFailure()) {
					// If theta isn't empty then the fact probably is a fact for everyone
					// eg. Loves(x,y)
					// meaning that everyone loves everyone
					// eg. Link(x,NextOf(x))
					// meaning that x is linked with next of x
					if(!theta.getUnifierMap().isEmpty()) {
						// Substitute the variables in l according to theta
						l = substituteVars(l, theta);
						// Create a new temporal unifier
						Unifier theta_t = new Unifier();
						// Unify a again with the substituted l using the new unifier
						// and assign it to the old unifier
						theta = LiteralFOL.unify(l, a, theta_t);
					}
					// If it is not a specific question for an implied fact
					if(!isSpecificQuestion && !returnFirst) {
						// Print the unifier and ask if the user wants more results
						System.out.print(theta + "\nType 'M' for more results >");
						// Get the user's input and put the first char in a variable
						char c = sc.nextLine().charAt(0);
						// If the character is not 'M'
						if(c!='M') {
							// Close the scanner
							sc.close();
							// Return the unifier
							return theta;
						}
						// Else continue the loop
					}else {
						// Close the scanner
						sc.close();
						// If it is a specific question then return the unifier
						return theta;
					}
				}
			}
		}
		
		// Initialize a list that holds all the new facts
		ArrayList<HornClauseFOL> newfacts = null;
		
		// Loop as long as new facts are being created
		do {
			// Create the newfacts list ( empty it if it had elements ) 
			newfacts = new ArrayList<>();
			
			// For every HornClause in KB that has result
			for(HornClauseFOL clause : KB.getHornClauses()) {
				if(clause.getResult()!=null) {
					
					// Set new vars
					newVars(clause, count_var++);
					
					// PATTERN MATCHING
					
					// Get how many literals are the hypotheses
					int litcount = clause.getHypotheses().size();
					
					// Get all the subset's of that length
					// Get all the facts from the KB and add them as literals
					ArrayList<LiteralFOL> facts = new ArrayList<>();
					for(HornClauseFOL cl : KB.getHornClauses()) {
						// If it doesnt have a result
						if(cl.getResult()==null) {
							// Then it is a fact which is a literal
							// add it to the facts
							facts.add(cl.getHypotheses().get(0));
						}
					}
					
					// Get the k-Subsets with k=litcount from the facts in the KB
					// to try to unify each with the Horn Clause
					List<Set<LiteralFOL>> subsets = getSubsets(facts, litcount);
					
					// Create the list of all the Literals in the clause
					ArrayList<Unifiable> clauseLiteralList = new ArrayList<>();
					// For each literal in the clause hypotheses
					for(LiteralFOL l : clause.getHypotheses()) {
						// Add the literal to the list
						clauseLiteralList.add(l);
					}
					
					// Sort the list by literal name
					Collections.sort(clauseLiteralList, new Comparator<Unifiable>() {
						@Override
						public int compare(Unifiable o1, Unifiable o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});
					
					// For every set of literals we found
					for(Set<LiteralFOL> set : subsets) {
						// Create a list of all the literals in the set
						ArrayList<Unifiable> setLiteralList = new ArrayList<>();
						// For each literal in the set
						for(LiteralFOL l : set) {
							// Check if literal has variables
							// Create a boolean to store the answer
							boolean hasV = false;
							// For every term in the literal
							for(TermFOL t : l.getTerms()) {
								// Check if the term has variables
								if(hasVariables(t)) {
									// If it has chage hasV to true
									hasV = true;
									// and break the loop
									break;
								}
							}
							// If it has asssign new variables
							if(hasV) {
								newVars(l,count_var++);
							}
							
							// Add the literal to the list
							setLiteralList.add(l);
						}
						
						// Sort the list by literal name
						Collections.sort(setLiteralList, new Comparator<Unifiable>() {
							@Override
							public int compare(Unifiable o1, Unifiable o2) {
								return o1.getName().compareTo(o2.getName());
							}
						});
						
						// Try to unify
						Unifier theta = new Unifier();
						
						// Apply unify to the two lists
						theta = LiteralFOL.unify(clauseLiteralList, setLiteralList, theta);
						
						// Check if theta is not failure
						if(!theta.isFailure()) {
							
							// Get the result of the Horn Clause as a literal
							LiteralFOL res = clause.getResult();
							
							// Substitute variables in the result using the unifier
							LiteralFOL sres = substituteVars(res, theta);
							
							// CHECK COPY
							
							// Boolean to store the value if it is a copy
							// of another literal in the KB (from the facts)
							// or from the new facts
							boolean isCopy = false;
							
							// Create a list of Literals for the new horn facts
							// in this loop
							ArrayList<LiteralFOL> newfactLits = new ArrayList<>();
							
							// For every horn clause, because we know it is a fact and it has only
							// one hypothesis (the fact), get it and add it to the list
							for(HornClauseFOL h : newfacts) newfactLits.add(h.getHypotheses().get(0));
							
							// For every literal in the new facts in this loop
							for(LiteralFOL l : newfactLits) {
								// Check if sres is a copy of l
								if(checkCopyOf(sres,l)) {
									// If it is set isCopy to true
									isCopy = true;
									// Break the loop
									break;
								}
							}
							
							// Only check the facts of the KB, if sres is not a copy of any newfact in this loop
							// because otherwise it is not needed
							if(!isCopy) {
								// For every literal in the facts of the KB
								for(LiteralFOL l : facts) {
									// Check if sres is a copy of l
									if(checkCopyOf(sres, l)) {
										// If it is set isCopy to true
										isCopy = true;
										// Break the loop
										break;
									}
								}
							}
							
							// If it isn't a copy of the facts in the KB or the new facts
							// in this loop
							if(!isCopy) {
								
								// Put the literal to the new facts as a HornClause
								// Create a List container for the new fact (as a literal)
								ArrayList<LiteralFOL> newLiteralListConstainer = new ArrayList<>();
								// Add it to the list
								newLiteralListConstainer.add(sres);
								// Make a horn fact (result==null) from the list
								HornClauseFOL newHornfact = new HornClauseFOL(newLiteralListConstainer, null);
								
								// Add it to the newfacts list
								newfacts.add(newHornfact);
								
								// CHECK a
								
								// Try to unify the literal with a to see if it what we are looking for
								
								// Create a new unifier
								Unifier thetaA = new Unifier();
								
								// Unify the literal of the new fact with a literal
								thetaA = LiteralFOL.unify(sres,a,thetaA);
								
								// If it has not failed
								if(!thetaA.isFailure()) {
									// If theta isn't empty then the fact probably is a fact for everyone
									// eg. Loves(x,y)
									// meaning that everyone loves everyone
									// eg. Link(x,NextOf(x))
									// meaning that x is linked with next of x
									if(!theta.getUnifierMap().isEmpty()) {
										// Substitute the variables in sres according to theta
										sres = substituteVars(sres, thetaA);
										// Create a new temporal unifier
										Unifier theta_t = new Unifier();
										// Unify a again with the substituted sres using the new unifier
										// and assign it to the old unifier
										thetaA = LiteralFOL.unify(sres, a, theta_t);
									}
									// If it is not a specific question for an implied fact
									if(!isSpecificQuestion && !returnFirst) {
										// Print the unifier and ask if the user wants more results
										System.out.print(thetaA + "\nType 'M' for more results >");
										// Get the user's input and put the first char in a variable
										char c = sc.nextLine().charAt(0);
										// If the character is not 'M'
										if(c!='M') {
											// Close the scanner
											sc.close();
											// Return the unifier
											return thetaA;
										}
										// Else continue the loop
									}else {
										// Close the scanner
										sc.close();
										// If it is a specific question then return the unifier
										return thetaA;
									}
								}
								
								
							}
							
						}
						
					}
					
				}
			}
			
			// Add new facts to the KB
			// For each new horn fact
			for(HornClauseFOL h : newfacts) {
				// Add it to the KB
				KB.getHornClauses().add(h);
			}
			
			maxsteps = maxsteps - 1;
			
		}while(!newfacts.isEmpty() && maxsteps > 0);
		// Return a failed Unifier
		// Create the unifier
		Unifier fail = new Unifier();
		// Set it to failure
		fail.setFailure(true);
		// Close the scanner
		sc.close();
		// Return the failed unifier
		return fail;
	}
	
	// Method that performs the horn forward chain algorithm in FOL
	// HornKnowledgeBaseFOL KB is the knowledge base of horn statements
	// LiteralFOL a is the literal that the user asks the KB
	// maxsteps is a threshold of how many loops to execute after stopping the algorithm because an answer can't be found (FOL is semi decidable)
	// maxresults is a threshold for the size of the list of the unifiers to return (there maybe infinite answers)
	// eg. Human(FatherOf(FatherOf(FatherOf(...))))
	// Return a list of Unifiers that have all the substitutions for each unification, ending with a failure (there are no more answers)
public static ArrayList<Unifier> FOL_ForwardChain(HornKnowledgeBaseFOL KB, LiteralFOL a,int maxsteps,int maxresults) {
	
		// Global count for new vars
		int count_var = 1;
	
		// Create a unifierList to store the answers
		ArrayList<Unifier> unifierList = new ArrayList<>();
	
		// Check if a has Variables or it is a specific question for a positive literal
		// Create a boolean variable to store the answer
		boolean isSpecificQuestion = true;
		// For each Term in literal a
		for(TermFOL t : a.getTerms()) {
			// Check if term has variables
			if(hasVariables(t)) {
				// If it has then it is not a specific question
				isSpecificQuestion = false;
				// Break the loop
				break;
			}
		}
		
		// Search first the facts given for the literal a
		for(HornClauseFOL h : KB.getHornClauses()) {
			// If it has no result, thus it is a fact
			if(h.getResult()==null) {
				// Get the literal from the horn clause
				LiteralFOL l = h.getHypotheses().get(0);
				// Initialize a boolean that indicates if the following terms have variables
				boolean hasV = false;
				// For each term in l
				for(TermFOL t : l.getTerms()) {
					// If the term has variables
					if(hasVariables(t)) {
						// Set hasV to true and break the loop
						hasV = true;
						break;
					}
				}
				// If any term has variables then change the variable names using count_var
				if(hasV) newVars(l, count_var++);
				// Create a unifier
				Unifier theta = new Unifier();
				// Unify the literal with a
				theta = LiteralFOL.unify(l,a,theta);
				// If it has not failed
				if(!theta.isFailure()) {
					// If theta isn't empty then the fact probably is a fact for everyone
					// eg. Loves(x,y)
					// meaning that everyone loves everyone
					// eg. Link(x,NextOf(x))
					// meaning that x is linked with next of x
					if(!theta.getUnifierMap().isEmpty()) {
						// Substitute the variables in l according to theta
						l = substituteVars(l, theta);
						// Create a new temporal unifier
						Unifier theta_t = new Unifier();
						// Unify a again with the substituted l using the new unifier
						// and assign it to the old unifier
						theta = LiteralFOL.unify(l, a, theta_t);
					}
					// If the the algorithm hasn't reached the max results
					if(maxresults>0) {
						// Add theta to the unifierList
						unifierList.add(theta);
						// If it is a specific question about a positive literal return the unifierList (with 1 unifier)
						if(isSpecificQuestion) return unifierList;
						// Else decrease the maxresults counter
						maxresults = maxresults - 1;
					}else {
						// If we have reached the maxresults return the unifierList
						return unifierList;
					}
				}
			}
		}
		
		// Initialize a list that holds all the new facts
		ArrayList<HornClauseFOL> newfacts = null;
		
		// Loop as long as new facts are being created
		do {
			// Create the newfacts list ( empty it if it had elements ) 
			newfacts = new ArrayList<>();
			
			// For every HornClause in KB that has result
			for(HornClauseFOL clause : KB.getHornClauses()) {
				if(clause.getResult()!=null) {
					
					// Set new vars
					newVars(clause, count_var++);
					
					// PATTERN MATCHING
					
					// Get how many literals are the hypotheses
					int litcount = clause.getHypotheses().size();
					
					// Get all the subset's of that length
					// Get all the facts from the KB and add them as literals
					ArrayList<LiteralFOL> facts = new ArrayList<>();
					for(HornClauseFOL cl : KB.getHornClauses()) {
						// If it doesn't have a result
						if(cl.getResult()==null) {
							// Then it is a fact which is a literal
							// add it to the facts
							facts.add(cl.getHypotheses().get(0));
						}
					}
					
					// Get the k-Subsets with k=litcount from the facts in the KB
					// to try to unify each with the Horn Clause
					List<Set<LiteralFOL>> subsets = getSubsets(facts, litcount);
					
					// Create the list of all the Literals in the clause
					ArrayList<Unifiable> clauseLiteralList = new ArrayList<>();
					// For each literal in the clause hypotheses
					for(LiteralFOL l : clause.getHypotheses()) {
						// Add the literal to the list
						clauseLiteralList.add(l);
					}
					
					// Sort the list by literal name
					Collections.sort(clauseLiteralList, new Comparator<Unifiable>() {
						@Override
						public int compare(Unifiable o1, Unifiable o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});
					
					// For every set of literals we found
					for(Set<LiteralFOL> set : subsets) {
						// Create a list of all the literals in the set
						ArrayList<Unifiable> setLiteralList = new ArrayList<>();
						// For each literal in the set
						for(LiteralFOL l : set) {
							// Check if literal has variables
							// Create a boolean to store the answer
							boolean hasV = false;
							// For every term in the literal
							for(TermFOL t : l.getTerms()) {
								// Check if the term has variables
								if(hasVariables(t)) {
									// If it has chage hasV to true
									hasV = true;
									// and break the loop
									break;
								}
							}
							// If it has asssign new variables
							if(hasV) {
								newVars(l,count_var++);
							}
							
							// Add the literal to the list
							setLiteralList.add(l);
						}
						
						// Sort the list by literal name
						Collections.sort(setLiteralList, new Comparator<Unifiable>() {
							@Override
							public int compare(Unifiable o1, Unifiable o2) {
								return o1.getName().compareTo(o2.getName());
							}
						});
						
						// Try to unify
						Unifier theta = new Unifier();
						
						// Apply unify to the two lists
						theta = LiteralFOL.unify(clauseLiteralList, setLiteralList, theta);
						
						// Check if theta is not failure
						if(!theta.isFailure()) {
							
							// Get the result of the Horn Clause as a literal
							LiteralFOL res = clause.getResult();
							
							// Substitute variables in the result using the unifier
							LiteralFOL sres = substituteVars(res, theta);
							
							// CHECK COPY
							
							// Boolean to store the value if it is a copy
							// of another literal in the KB (from the facts)
							// or from the new facts
							boolean isCopy = false;
							
							// Create a list of Literals for the new horn facts
							// in this loop
							ArrayList<LiteralFOL> newfactLits = new ArrayList<>();
							
							// For every horn clause, because we know it is a fact and it has only
							// one hypothesis (the fact), get it and add it to the list
							for(HornClauseFOL h : newfacts) newfactLits.add(h.getHypotheses().get(0));
							
							// For every literal in the new facts in this loop
							for(LiteralFOL l : newfactLits) {
								// Check if sres is a copy of l
								if(checkCopyOf(sres,l)) {
									// If it is set isCopy to true
									isCopy = true;
									// Break the loop
									break;
								}
							}
							
							// Only check the facts of the KB, if sres is not a copy of any newfact in this loop
							// because otherwise it is not needed
							if(!isCopy) {
								// For every literal in the facts of the KB
								for(LiteralFOL l : facts) {
									// Check if sres is a copy of l
									if(checkCopyOf(sres, l)) {
										// If it is set isCopy to true
										isCopy = true;
										// Break the loop
										break;
									}
								}
							}
							
							// If it isn't a copy of the facts in the KB or the new facts
							// in this loop
							if(!isCopy) {
								
								// Put the literal to the new facts as a HornClause
								// Create a List container for the new fact (as a literal)
								ArrayList<LiteralFOL> newLiteralListConstainer = new ArrayList<>();
								// Add it to the list
								newLiteralListConstainer.add(sres);
								// Make a horn fact (result==null) from the list
								HornClauseFOL newHornfact = new HornClauseFOL(newLiteralListConstainer, null);
								
								// Add it to the newfacts list
								newfacts.add(newHornfact);
								
								// CHECK a
								
								// Try to unify the literal with a to see if it what we are looking for
								
								// Create a new unifier
								Unifier thetaA = new Unifier();
								
								// Unify the literal of the new fact with a literal
								thetaA = LiteralFOL.unify(sres,a,thetaA);
								
								// If it has not failed
								if(!thetaA.isFailure()) {
									// If theta isn't empty then the fact probably is a fact for everyone
									// eg. Loves(x,y)
									// meaning that everyone loves everyone
									// eg. Link(x,NextOf(x))
									// meaning that x is linked with next of x
									if(!theta.getUnifierMap().isEmpty()) {
										// Substitute the variables in sres according to theta
										sres = substituteVars(sres, thetaA);
										// Create a new temporal unifier
										Unifier theta_t = new Unifier();
										// Unify a again with the substituted sres using the new unifier
										// and assign it to the old unifier
										thetaA = LiteralFOL.unify(sres, a, theta_t);
									}
									if(maxresults>0) {
										unifierList.add(thetaA);
										if(isSpecificQuestion) return unifierList;
										maxresults = maxresults - 1;
									}else {
										return unifierList;
									}
								}
								
								
							}
							
						}
						
					}
					
				}
			}
			
			// Add new facts to the KB
			// For each new horn fact
			for(HornClauseFOL h : newfacts) {
				// Add it to the KB
				KB.getHornClauses().add(h);
			}
			
			maxsteps = maxsteps - 1;
			
		}while(!newfacts.isEmpty() && maxsteps > 0);
		// Return a failed Unifier
		// Create the unifier
		Unifier fail = new Unifier();
		// Set it to failure
		fail.setFailure(true);
		
		// Add it to the unifier list
		unifierList.add(fail);
		
		// Return it
		return unifierList;
	}
	
	// Method that assigns new variables to a horn clause according to the integer n
	private static void newVars(HornClauseFOL clause,int n) {
		// For every Literal in the hypotheses
		for(LiteralFOL l : clause.getHypotheses()) {
			// Assign new variables to each literal
			newVars(l,n);
		}
		// If the horn clause has a result
		if(clause.getResult()!=null) {
			// Assign new variables to the result
			newVars(clause.getResult(),n);
		}
	}
	
	// Method that assigns new variables to a FOL literal according to the integer n
	private static void newVars(LiteralFOL literal,int n) {
		// For every term in the literal
		for(TermFOL t : literal.getTerms()) {
			// Change the variables using n
			newVars(t,n);
		}
	}
	
	// Method that assigns new variables to a FOL term according to the integer n
	// if it is a variable then append baseName with n
	private static void newVars(TermFOL term,int n) {
		// If the term is a variable
		if(term.getClass()==VariableFOL.class) {
			// Cast it to a variable
			VariableFOL v = (VariableFOL)term;
			// Change it's base name adding the number n
			term.setName(v.getBaseName()+""+n);
		}
		// If term is a function
		if(term.getClass()==FunctionFOL.class) {
			// Cast the term to a function
			FunctionFOL func = (FunctionFOL)term;
			// For every term of the function
			for(TermFOL t : func.getTerms()) {
				// Change the variables using n
				newVars(t,n);
			}
		}
	}
	
	// Method that checks if LiteralFOL a is a copy of LiteralFOL b
	// meaning that the only difference from the two literals is the
	// name of their variables, which is irrelevant for their meaning
	private static boolean checkCopyOf(LiteralFOL a,LiteralFOL b) {
		
		// Optional check negation (not actually needed since in Horn Clauses everything is positive)
		if(a.getNegation()!=b.getNegation()) return false;
		
		// Firstly check order
		if(a.getOrder()!=b.getOrder()) return false;
		
		// Secondly check name
		if(!a.getName().equals(b.getName())) return false;
		
		// Create a boolean variable of default value true
		// (Imply that it is a copy)
		boolean isCopy = true;
		
		// For each term of literal a and b
		for(int i=0;i<a.getOrder();i++) {
			// Check each term of the functions if they are copies of each other
			if(!checkCopyOf(a.getTerms()[i], b.getTerms()[i])) {
				// If it isn't then we found a term that they differ
				// Set isCopy to false
				isCopy = false;
				// Break the loop
				break;
			}
		}
		// Return isCopy
		return isCopy;
	}
	
	// Method that checks if TermFOL a is a copy of TermFOL b
	// The terms are copies if they are both variables, or
	// if they are both functions with the same names and constants
	private static boolean checkCopyOf(TermFOL a,TermFOL b) {
		// If the terms are of different classes then they aren't copies
		if(a.getClass()!=b.getClass()) return false;
		// If the terms are Variables then they are copies
		// because they can be interchangeable (x - y)
		if(a.getClass()==VariableFOL.class) return true;
		// If they are functions
		if(a.getClass()==FunctionFOL.class) {
			// Cast each term to functions
			FunctionFOL func1 = (FunctionFOL)a;
			FunctionFOL func2 = (FunctionFOL)b;
			// If they have different order return false
			if(func1.getOrder()!=func2.getOrder()) return false;
			// If they have different name return false
			if(!func1.getName().equals(func2.getName())) return false;
			// Set a boolean isCopy to default value true
			boolean isCopy = true;
			// For each term of the functions
			for(int i=0;i<func1.getOrder();i++) {
				// Check if each term is a copy or not
				if(!checkCopyOf(func1.getTerms()[i], func2.getTerms()[i])) {
					// If we found even one that is not a copy (eg. different constants)
					// Set isCopy to false
					isCopy = false;
					// Break the loop
					break;
				}
			}
			// Return isCopy variable
			return isCopy;
		}
		// Else it is a constant - check names
		return a.getName().equals(b.getName());
	}
	
	// Method that substitutes all the variables in a FOL Literal to terms
	// according to the Unifier theta
	private static LiteralFOL substituteVars(LiteralFOL b,Unifier theta) {
		// Create a copy of the Literal
		LiteralFOL litC = new LiteralFOL(b.getName(), b.getOrder(), b.getTerms(),b.getNegation());
		
		// For every term of the Literal
		for(int i=0;i<litC.getOrder();i++) {
			// Substitute it's variables to the keys corresponding to the unifier
			litC.getTerms()[i] = substvar(litC.getTerms()[i], theta);
		}
		
		// Check if the literal with the substituted vars has more vars that
		// are in theta and need to be substituted (chained substitutions)
		
		// Initialize a boolean to false indicating if the literal has more
		// vars that are in theta and need to be substituted
		boolean hasV = false;
		
		// For every Term in the Literal liC
		for(TermFOL t : litC.getTerms()) {
			
			// If t is a variable and theta contains it then it needs more substitution
			if(hasVariablesTheta(t,theta)) {
				// Set the boolean to true
				hasV = true;
				// Break the loop
				break;
			}
		}
		
		// If the literal needs more substitution repeat
		// the process with litC and set it to litC
		if(hasV) litC = substituteVars(litC, theta);
		
		// Return the new literal
		return litC;
	}
	
	// Method that substitutes all the variables in a FOL Term to terms
	// according to the Unifier theta
	private static TermFOL substvar(TermFOL term,Unifier theta) {
		// If the term is a variable and there is a key in the unifier for that variable
		if(term.getClass()==VariableFOL.class && theta.getUnifierMap().containsKey(term)) {
			// Change the name of the variable to the value specified by the unifier
			// and return the changed term
			return theta.getUnifierMap().get(term);
		}else if(term.getClass()==FunctionFOL.class) {
			// If the term is a function, cast it as a function
			FunctionFOL func = (FunctionFOL)term;
			// For each term of the function
			for(int i=0;i<func.getOrder();i++) {
				// Execute the method for the function and return the term
				func.getTerms()[i] = substvar(func.getTerms()[i], theta);
			}
			// Return the func (term)
			return func;
		}else {
			// Else if nothing can be substituted then return the term as it is
			return term;
		}
	}
	
	// Method that checks if a TermFOL has variables or if it is a variable
	private static boolean hasVariables(TermFOL term) {
		// If the term is a variable return true
		if(term.getClass()==VariableFOL.class) return true;
		// Else if the term is a function
		else if(term.getClass()==FunctionFOL.class) {
			// Cast it to a function
			FunctionFOL func = (FunctionFOL)term;
			// For every term of the function
			for(TermFOL t : func.getTerms()) {
				// If it has a variable return true
				if(hasVariables(t)) return true;
			}
		}
		// If nothing of the above returned true then it doesnt
		// have a variable, so return false
		return false;
	}
	
	// Method that checks if a TermFOL has variables of theta unifier (as keys) or if it is a variable in theta
	private static boolean hasVariablesTheta(TermFOL term, Unifier theta) {
		// If the term is a variable return true
		if(term.getClass()==VariableFOL.class && theta.getUnifierMap().containsKey((VariableFOL)term)) return true;
		// Else if the term is a function
		else if(term.getClass()==FunctionFOL.class) {
			// Cast it to a function
			FunctionFOL func = (FunctionFOL)term;
			// For every term of the function
			for(TermFOL t : func.getTerms()) {
				// If it has a variable return true
				if(hasVariablesTheta(t,theta)) return true;
			}
		}
		// If nothing of the above returned true then it doesnt
		// have a variable, so return false
		return false;
	}
	
	// Recursive method that returns the k-subsets of a list of Literals -- used by the method below
	private static void getSubsets(List<LiteralFOL> inputList, int k, int idx, Set<LiteralFOL> currentSet,List<Set<LiteralFOL>> solution) {
	    // If the size of the current set equals k (the number of literals we want in the set)
	    if (currentSet.size() == k) {
	    	// Add the current set to the solutions
	        solution.add(new HashSet<>(currentSet));
	        // Return (recursion successful base condition)
	        return;
	    }
	    // If the index has reached the end of the input list of literals
	    // then return (recursion unsuccessful/"end of list" base condition)
	    if (idx == inputList.size()) return;
	    // Get the literal from the inputSet at index->idx
	    LiteralFOL x = inputList.get(idx);
	    // Add the literal to the currentSet
	    currentSet.add(x);
	    // Execute getSubsets for idx+1, with the literal x
	    getSubsets(inputList, k, idx+1, currentSet, solution);
	    // Remove the literal added before
	    currentSet.remove(x);
	    // Execute getSubsets for idx+1, without the literal x
	    getSubsets(inputList, k, idx+1, currentSet, solution);
	}
	
	// Method that returns the k-subsets of a list of Literals
	private static List<Set<LiteralFOL>> getSubsets(List<LiteralFOL> inputList, int k) {
		// Create the solution list of sets that will be filled
	    List<Set<LiteralFOL>> solution = new ArrayList<>();
	    // Execute getSubsets for the inputList,k from idx:0 giving solution as the list of sets
	    // to be filled with all the k-subsets of the inputList
	    getSubsets(inputList, k, 0, new HashSet<LiteralFOL>(), solution);
	    // Return the list of sets solution
	    return solution;
	}

}
