package checker.passes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import checker.analysis.DepthFirstAdapter;
import checker.node.ACastTerm;
import checker.node.AFieldAccessTerm;
import checker.node.AMethodCallTerm;
import checker.node.AObjectCreationNonLeftRecTerm;
import checker.node.ATermListTermList;
import checker.node.AThisNonLeftRecTerm;
import checker.node.AVariableNonLeftRecTerm;
import checker.node.Node;
import checker.node.PTerm;
import checker.model.ArgumentName;
import checker.model.Cast;
import checker.model.ClassName;
import checker.model.FieldAccess;
import checker.model.FieldName;
import checker.model.Method;
import checker.model.MethodInvocation;
import checker.model.MethodName;
import checker.model.ObjectCreation;
import checker.model.Term;
import checker.model.Variable;

////////////////////////////////////////////////////////////////
// Class that traverses an AST and construct a Term via the buildTerm
// function
public class TermBuilder extends DepthFirstAdapter {

    final Deque<Term> termStack;
    final Deque<Deque<Term>> backupStack;
	
    public TermBuilder() {
	this.termStack = new ArrayDeque<Term>();
	this.backupStack = new ArrayDeque<Deque<Term>>();
    }
	
    public Term buildTerm(PTerm term) {
	term.apply(this);
	return termStack.pop();
    }
	
    @Override
    public void caseAVariableNonLeftRecTerm(AVariableNonLeftRecTerm node) {
	ArgumentName aname = new ArgumentName(node.getIdentifier().
					      getText().trim());
	termStack.push(new Variable(aname));
    };

    @Override
    public void caseAThisNonLeftRecTerm(AThisNonLeftRecTerm node) {
	termStack.push(new Variable(Method.getThisArgName()));
    }
	
    @Override
    public void caseATermListTermList(ATermListTermList node) {
	node.getTerm().apply(this);
	for(Node n : (List<? extends Node>) node.getCommaTerm()) {
	    n.apply(this);
	}
    }
	
    @Override
    public void caseAFieldAccessTerm(AFieldAccessTerm node) {
	node.getNonLeftRecTerm().apply(this);
	Term path = termStack.pop();
		
	FieldName field = new FieldName(node.getIdentifier().
					getText().trim());
	termStack.push(new FieldAccess(path, field));
    }
	
    @Override
    public void caseAMethodCallTerm(AMethodCallTerm node) {
	node.getNonLeftRecTerm().apply(this);
	Term path = termStack.pop();
		
	MethodName methodName = new MethodName(node.getIdentifier().
					       getText().trim());
		
	preserveTermStack();
	node.getTermList().apply(this);
	List<Term> visitedTerms = restoreTermStack();
		
	termStack.push(new MethodInvocation(path, methodName, visitedTerms));
    }
	
    @Override
    public void caseAObjectCreationNonLeftRecTerm(AObjectCreationNonLeftRecTerm node) {
	ClassName className = new ClassName(node.getIdentifier().
					    getText().trim());
		
	preserveTermStack();
	node.getTermList().apply(this);
	List<Term> visitedTerms = restoreTermStack();
		
	termStack.push(new ObjectCreation(className, visitedTerms));
    };
	
    @Override
    public void caseACastTerm(ACastTerm node) {
	ClassName className = new ClassName(node.getIdentifier().
					    getText().trim());
		
	node.getNonLeftRecTerm().apply(this);
		
	Term castedTerm = termStack.pop();
		
	termStack.push(new Cast(className, castedTerm));
    }
	
    private void preserveTermStack() {
	backupStack.push(new ArrayDeque<Term>(termStack));
    }

    private List<Term> restoreTermStack() {
	Deque<Term> old = backupStack.pop();
	List<Term> current = new ArrayList<Term>(termStack);
	termStack.clear();
	termStack.addAll(old);
	return current;
    }

}
