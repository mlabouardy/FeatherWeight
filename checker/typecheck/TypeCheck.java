package checker.typecheck;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import checker.analysis.DepthFirstAdapter;
import checker.model.ArgumentName;
import checker.model.Cast;
import checker.model.ClassName;
import checker.model.FieldAccess;
import checker.model.Method;
import checker.model.MethodInvocation;
import checker.model.ObjectCreation;
import checker.model.Term;
import checker.model.Variable;
import checker.passes.FJException;
import checker.util.Arrow;
import checker.util.Maybe;
import checker.util.Tuple;

import static checker.util.ListUtil.*;

////////////////////////////////////////////////////////////////
// The typechecker is implemented as a visitor pattern around the
// classes implementing Term. There are exactly 5 classes implementing
// this interface :
// - Cast 
// - FieldAccess
// - MethodInvocation
// - ObjectCreation 
// - Variable
//
// The typechecker is initialized with a TypeEnvironment and a
// ClassTable, and then can typecheck a Term.

public class TypeCheck {

    private final Deque<ClassName> typeStack;
    private final Deque<Deque<ClassName>> backupStack;
    
    private final TypeEnvironment typeEnvironment;
    private final ClassTable classTable;
    private boolean stupidWarningTriggered;

    // Constructor
    public TypeCheck(TypeEnvironment typeEnvironment, ClassTable classTable) {
        this.typeEnvironment = typeEnvironment;
        this.classTable = classTable;
        this.typeStack = new ArrayDeque<ClassName>();
        this.backupStack = new ArrayDeque<Deque<ClassName>>();
        
        this.stupidWarningTriggered = false;
    }

    // Type checking function
    public ClassName typeCheck(Term code) throws FJException {
    
        code.visit(this);
        
        return this.typeStack.pop();
    }

    public void appyRule(Cast cast) {
        cast.getCastedTerm().visit(this);
        
        ClassName castedTermType = this.typeStack.pop();
        
        ClassName typeToCastTo = cast.getTypeToCastTo();
        
        if(this.classTable.subtype(castedTermType, typeToCastTo)) {
	    /* ok */   
        } else if(this.classTable.subtype(typeToCastTo, castedTermType)) {
	    /* ok */
        } else {
            this.stupidWarningTriggered = true;
        }
        
        this.typeStack.push(typeToCastTo);
        
    }

    public void appyRule(FieldAccess access) {
        
        access.getPath().visit(this);
        
        ClassName pathType = this.typeStack.pop();
        
        try {
            ClassName fieldType = this.classTable.lookupField(access.getFieldName(), pathType);
            
            this.typeStack.push(fieldType);
            
        } catch (FJException e) {
            throw new FJTypeCheckException(e);
        }
        
    }


    public void appyRule(final MethodInvocation invocation) {
        invocation.getPath().visit(this);
        final ClassName pathType = this.typeStack.pop();
        
        Maybe<Method> m = this.classTable.mType(invocation.getMethodName(), pathType);
        
        m.maybe(
                new Runnable() { 
                    public void run() {
                        throw new FJTypeCheckException("Can't find method in " + pathType);
                    }
                },
                
                new Arrow<Method, Runnable> () {
                    public Runnable run(final Method arg) {
                        return new Runnable() {
                            public void run() {

                                if(arg.getArguments().size() != invocation.getArguments().size()) {
                                    throw new FJTypeCheckException("Invoking a method with incorrect number of arguments");
                                }
                                
                                preserveTypeStack();
                                
                                for(Term e : invocation.getArguments() ) {
                                    e.visit(TypeCheck.this);
                                }
                                
                                List<ClassName> argTypes = restoreTypeStack();
                                
                                for(Tuple<ClassName, ClassName> cnPair : zip(argTypes, mapFst(arg.getArguments()))) {
                                    if(!TypeCheck.this.classTable.subtype(cnPair.getX(), cnPair.getY())) {
                                        throw new FJTypeCheckException("Invoking a method with an argument that is not a subtype");
                                    }
                                }
                                
                                TypeCheck.this.typeStack.push(arg.getReturnType());
                            }
                        };
                    }
                }).run();
                
    }

    public void appyRule(ObjectCreation creation) {
        List<ClassName> fieldTypes = mapFst(this.classTable.fields(creation.getNewClassName()));
        List<Term> terms = creation.getArgumentTerms();
        
        if(fieldTypes.size() != terms.size()) {
            throw new FJTypeCheckException("'new' with wrong number of arguments for the constructor");
        }
        
        preserveTypeStack();
        for(Term t : terms) {
            t.visit(this);
        }
        List<ClassName> argTypes = restoreTypeStack();
        
        for(Tuple<ClassName, ClassName> cnPair : zip(argTypes, fieldTypes)) {
            if(!this.classTable.subtype(cnPair.getX(), cnPair.getY())) {
                throw new FJTypeCheckException("Invoking new with an argument that is not of the right type");
            }
        }
        
        this.typeStack.push(creation.getNewClassName());
    }

    public void appyRule(Variable variable) {
        ArgumentName arg = variable.getArgumentName();
        
        ClassName cn = this.typeEnvironment.getBinding(arg);
        if(cn == null) { 
            throw new FJTypeCheckException("Variable: " + arg + " is unknown");
        }
        
        this.typeStack.push(cn);
    }

    
    private void preserveTypeStack() {
        this.backupStack.push(new ArrayDeque<ClassName>(this.typeStack));
    }

    private List<ClassName> restoreTypeStack() {
        Deque<ClassName> old = this.backupStack.pop();
        List<ClassName> current = new ArrayList<ClassName>(this.typeStack);
        this.typeStack.clear();
        this.typeStack.addAll(old);
        return current;
    }
    

}
