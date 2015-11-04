package checker.typecheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import checker.model.ClassDecl;
import checker.model.ClassName;
import checker.model.FieldName;
import checker.model.Method;
import checker.model.MethodName;
import checker.passes.FJException;
import checker.util.Maybe;
import checker.util.Nothing;
import checker.util.Tuple;

////////////////////////////////////////////////////////////////
// Table of classes loaded in the code
public class ClassTable {

    private static final ClassName objectClassName = new ClassName("Object");

    
    private final Map<ClassName, ClassDecl> classTable;
	
    public ClassTable() {
	this.classTable = new HashMap<ClassName,ClassDecl>();
    }
	
    public void addClassDefinition(ClassDecl decl) throws FJException {
	ClassName name = decl.getClassName();
		
	if(this.classTable.containsKey(name)) {
	    throw new FJException("Duplicate definition for class: " + name.getClassName());
	}
		
	if(name.equals(objectClassName)) {
	    throw new FJException("Trying to define class Object");
	}
		
        this.classTable.put(name, decl);
    }

    // typecheck the ClassTable
    public void validateClassTable() throws FJException {
	List<FJException> exceptions = new ArrayList<FJException>();
		
	exceptions.addAll(checkForCycles());
	exceptions.addAll(checkAllClassNamesUsedAreDefined());
		
	if(exceptions.isEmpty()) {
	    exceptions.addAll(checkCOK());
	}

	if(!exceptions.isEmpty()) {
	    throw new FJExceptions(exceptions);
	}
    }

    // class-typing rule C-OK for the classtable
    private List<FJException> checkCOK() {
	List<FJException> exceptions = new ArrayList<FJException>();
		
	for(ClassDecl decl : this.classTable.values()) {
	    exceptions.addAll(decl.checkCOK(this));
	}
		
	return exceptions;
    }

    private List<FJException> checkAllClassNamesUsedAreDefined() {
	List<FJException> exceptions = new ArrayList<FJException>();
	Set<ClassName> referencedNames = new HashSet<ClassName>();
	for(ClassDecl value : this.classTable.values()) {
	    referencedNames.addAll(value.getAllReferencedClassNames());
	}
		
	referencedNames.remove(objectClassName);
		
	Set<ClassName> knowns = new HashSet<ClassName>(this.classTable.keySet());
	referencedNames.removeAll(knowns);
	for(ClassName c : referencedNames) {
	    exceptions.add(new FJUnknownClassNameException(c));
	}
	return exceptions;
    }

    private List<FJException> checkForCycles() throws FJException {
	final List<FJException> exceptions = new ArrayList<FJException>();
		
	final Set<ClassName> safeClasses = new HashSet<ClassName>();
		
	outer: for(ClassName c : this.classTable.keySet()) {
	    if(safeClasses.contains(c)) { continue; }
			
	    Set<ClassName> path = new HashSet<ClassName>();
			
	    while(path.add(c)) {
		c = getSuperClassName(c);
		if(c.equals(ClassTable.objectClassName)) {
		    safeClasses.addAll(path);
		    continue outer;
		}
	    }
	    exceptions.add(new FJPathCycleException(path));
	}
		
		
	return exceptions;
    }

    private ClassName getSuperClassName(ClassName c) throws FJException {
	if(!this.classTable.containsKey(c)) {
	    throw new FJException("ClassName: " + c + "  does not exist in classtable");
	} else {
	    return this.classTable.get(c).getSuperClass();
	}
    }

    // fields implementation
    @SuppressWarnings("unchecked")
    public List<Tuple<ClassName, FieldName>> fields(ClassName className) {
	if(className.equals(objectClassName)) {
	    return Collections.EMPTY_LIST;
	}
		
	ClassDecl c = this.classTable.get(className);
	List<Tuple<ClassName, FieldName>> cFields = c.getFields();
	List<Tuple<ClassName, FieldName>> superFields = fields(c.getSuperClass());
		
	List<Tuple<ClassName, FieldName>> allFields = new ArrayList<Tuple<ClassName, FieldName>>();
		
	allFields.addAll(cFields);
	allFields.addAll(superFields);
		
	return allFields;
    }

    // mtype implementation -- returns a Maybe
    public Maybe<Method> mType(MethodName mn, ClassName cn) {
        if(cn.equals(ClassTable.objectClassName)) {
            return new Nothing<Method>();
        }
        
        ClassDecl c = this.classTable.get(cn);
        
        return c.lookupMethod(mn, this);
    }

    /**
     * is codeType <: returnType
     */
    public boolean subtype(ClassName codeType, ClassName returnType) {
    	if(codeType.equals(returnType)) {
	    return true;
    	}
    	
    	ClassDecl codeDecl = this.classTable.get(codeType);
    	if(codeDecl != null) {
	    if(codeDecl.getSuperClass().equals(returnType)) {
		return true;
	    }
	    return (subtype(codeDecl.getSuperClass(), returnType));
    	}
    	
    	return false;
    	
    }

    public ClassName lookupField(FieldName fieldName, ClassName pathType) throws FJException {
        return this.classTable.get(pathType).getFieldType(fieldName);
    }
}
