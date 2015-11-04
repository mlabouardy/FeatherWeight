package checker.model;

import static checker.util.ListUtil.mapFst;
import static checker.util.ListUtil.mapSnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import checker.passes.FJException;
import checker.typecheck.ClassTable;
import checker.typecheck.TypeCheck;
import checker.typecheck.TypeEnvironment;
import checker.util.Arrow;
import checker.util.Tuple;

public class Method {
    
    private static final ArgumentName thisArgName = new ArgumentName("this");
    
    public static ArgumentName getThisArgName() {
    	return thisArgName;
    }
    
    private final ClassName returnType;
    private final MethodName methodName;
    private final List<Tuple<ClassName, ArgumentName>> arguments;
    private final Term code;
	
    public Method(ClassName returnType, MethodName methodName, Term code) {
	this.returnType = returnType;
	this.methodName = methodName;
	this.code = code;
	this.arguments = new ArrayList<Tuple<ClassName,ArgumentName>>();
    }
	
    public void addArgument(ClassName cn, ArgumentName an) {
	this.arguments.add(new Tuple<ClassName, ArgumentName>(cn,an));
    }

    public Set<ClassName> getAllReferencedClassNames() {
	Set<ClassName> allReferencedClassNames = new HashSet<ClassName>();
	allReferencedClassNames.add(this.returnType);
		
	for(Tuple<ClassName, ArgumentName> arg : this.arguments) {
	    allReferencedClassNames.add(arg.getX());
	}
		
	allReferencedClassNames.addAll(this.code.getAllReferencedClassNames());
		
	return allReferencedClassNames;
    }

    // typecheck a method
    public List<FJException> checkMOk(ClassDecl decl, ClassTable c) {
	final List<FJException> exceptions = new ArrayList<FJException>();
        
        exceptions.addAll(sanityCheckArguments());
        if(exceptions.isEmpty()) {
            c.mType(this.methodName, decl.getSuperClass()).maybe(
				 null,
				 new Arrow<Method, Object>() {
				     public Object run(Method arg) {
					 exceptions.addAll(checkMethodOverridesCorrectly(arg));
					 return null;
				     }
				 }
								 );
            
        }
        
        if(exceptions.isEmpty()) {
            exceptions.addAll(typeCheckCode(decl.getClassName(), c));
        }
        
        return exceptions;
    }

    private Collection<? extends FJException> typeCheckCode(ClassName thisClassName, ClassTable ct) {
        List<FJException> exceptions = new ArrayList<FJException>();
        
        TypeEnvironment te = new TypeEnvironment();
        te.addBinding(Method.thisArgName, thisClassName);
        
        for(Tuple<ClassName, ArgumentName> arg : this.arguments) {
            te.addBinding(arg.getY(), arg.getX());
        }
        
        try {
            ClassName codeType = new TypeCheck(te, ct).typeCheck(this.code);

            if(!ct.subtype(codeType, this.returnType)) {
                exceptions.add(new FJException("Method return type is not a supertype of the code type"));
            }
        } catch (FJException e) {
            exceptions.add(e);
        }
        
        return exceptions;
    }

    private Collection< ? extends FJException> sanityCheckArguments() {
        List<FJException> exceptions = new ArrayList<FJException>();
        Set<ArgumentName> args = new HashSet<ArgumentName>(mapSnd(this.arguments));
        if(args.size() != this.arguments.size()) {
            exceptions.add(new FJException("Duplicate argument name in method"));
        }
        
        if(args.contains(Method.thisArgName)) {
            exceptions.add(new FJException("this as an argument name in method"));
        }
        
        return exceptions;
    }

    public Object getName() {
        return this.methodName;
    }

    private Collection< ? extends FJException> checkMethodOverridesCorrectly(Method superClassVersion) {
        List<FJException> exceptions = new ArrayList<FJException>();
        
        if(!mapFst(superClassVersion.arguments).equals(mapFst(this.arguments))) {
            exceptions.add(new FJException("Method does not override argumets correctly"));
        }
        
        if(!this.returnType.equals(superClassVersion.returnType)) {
            exceptions.add(new FJException("Method does not override return type correctly"));
        }
        
        return exceptions;
    }

    public List<Tuple<ClassName, ArgumentName>> getArguments() {
        return new ArrayList<Tuple<ClassName, ArgumentName>>(this.arguments);
    }

    public ClassName getReturnType() {
        return this.returnType;
    }

    
}
