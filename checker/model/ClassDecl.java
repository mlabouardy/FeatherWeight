package checker.model;

import static checker.util.ListUtil.mapSnd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import checker.passes.FJException;
import checker.typecheck.ClassTable;
import checker.util.Just;
import checker.util.Maybe;
import checker.util.Tuple;

////////////////////////////////////////////////////////////////
// Class representing a class declaration
public class ClassDecl {

    private final ClassName className;
    private final ClassName superClass;
	
    private final List<Tuple<ClassName, FieldName>> fields;
	
    private final Constructor constructor;
	
    private final List<Method> methods;

    public ClassDecl(ClassName className, ClassName superClass,
		     Constructor constructor) {
	this.className = className;
	this.superClass = superClass;
	this.fields = new ArrayList<Tuple<ClassName,FieldName>>();
	this.constructor = constructor;
	this.methods = new ArrayList<Method>();
    }
	
    public void addField(ClassName cn, FieldName fn) {
	this.fields.add(new Tuple<ClassName,FieldName>(cn,fn));
    }
	
    public void addMethod(Method m) {
	this.methods.add(m);
    }

    public ClassName getClassName() {
	return this.className;
    }

    public ClassName getSuperClass() {
	return this.superClass;
    }

    public Set<ClassName> getAllReferencedClassNames() {
	Set<ClassName> referencedClassNames = new HashSet<ClassName>();
	referencedClassNames.add(className);
	referencedClassNames.add(superClass);
	for(Tuple<ClassName, FieldName> t : fields) {
	    referencedClassNames.add(t.getX());
	}
	referencedClassNames.addAll(constructor.getAllReferencedClassNames());
	for(Method m : methods) {
	    referencedClassNames.addAll(m.getAllReferencedClassNames());
	}
	return referencedClassNames;
    }

    // typecheck the class
    public List<FJException> checkCOK(ClassTable c) {
	List<FJException> exceptions = new ArrayList<FJException>();
		
	List<Tuple<ClassName, FieldName>> superFields = c.fields(superClass);
		
	try{
	    checkNoFieldNameShadowing(superFields);
	    checkConstructorReturnTypeMatchesUp();
	    checkConstructorHasGoodArguments(c);
	} catch(FJException e) {
	    exceptions.add(e);
	    return exceptions;
	}
		
	for(Method m : this.methods) {
	    exceptions.addAll(m.checkMOk(this, c));
	}		
		
	return exceptions;
    }

    private void checkConstructorReturnTypeMatchesUp() throws FJException {
	if(!constructor.getReturnClassName().equals(className)) {
	    throw new FJException("Constructor has wrong return type"); 
	}
    }

    private void checkConstructorHasGoodArguments(ClassTable c) throws FJException {
	List<Tuple<ClassName, FieldName>> superFields = c.fields(superClass);
		
	constructor.checkFieldsAreSane(fields, superFields);
    }

    private void checkNoFieldNameShadowing(List<Tuple<ClassName, FieldName>> superFields)
	throws FJException {
	Set<FieldName> superNames = new HashSet<FieldName>(mapSnd(superFields));
	Set<FieldName> myNames = new HashSet<FieldName>(mapSnd(fields));
		
	if(myNames.size() != fields.size()) {
	    throw new FJException("Duplicate field name(s)");
	}
		
	if(superNames.removeAll(myNames)) {
	    throw new FJException("Field name shadowed");
	}
		
    }

    public List<Tuple<ClassName, FieldName>> getFields() {
	return new ArrayList<Tuple<ClassName,FieldName>>(this.fields);
    }

    public Maybe<Method> lookupMethod(MethodName mn, ClassTable ct) {
        for(Method m : this.methods) {
            if(m.getName().equals(mn)) {
                return new Just<Method>(m);
            }
        }
        
        return ct.mType(mn, this.superClass);
    }

    public ClassName getFieldType(FieldName fieldName) throws FJException {
        for(Tuple<ClassName, FieldName> field : this.fields) {
            if(field.getY().equals(fieldName)) {
                return field.getX();
            }
        }
        
        throw new FJException("Looking up a field that doesn't exist!");
    }
	
}
