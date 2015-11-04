package checker.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import checker.passes.FJException;
import checker.util.Tuple;
import static checker.util.ListUtil.*;



public class Constructor {

	private final ClassName returnType;
	private final List<Tuple<ClassName, FieldName>> arguments;
	
	private final List<FieldName> superFields;
	private final List<FieldName> localFields;
	
	public Constructor(ClassName returnType) {
		this.returnType = returnType;
		this.superFields = new ArrayList<FieldName>();
		this.localFields = new ArrayList<FieldName>();
		this.arguments = new ArrayList<Tuple<ClassName,FieldName>>();
	}
	
	public void addSuperField(FieldName fn) {
		this.superFields.add(fn);
	}
	
	public void addLocalField(FieldName fn) {
		this.localFields.add(fn);
	}
	
	public void addArgument(ClassName cn, FieldName fn) {
		this.arguments.add(new Tuple<ClassName, FieldName>(cn,fn));
	}

	public Set<ClassName> getAllReferencedClassNames() {
		Set<ClassName> allReferencedClassNames = new HashSet<ClassName>();
		allReferencedClassNames.add(returnType);
		
		for(Tuple<ClassName, FieldName> arg : arguments) {
			allReferencedClassNames.add(arg.getX());
		}
		
		return allReferencedClassNames;
	}

	public void checkFieldsAreSane(List<Tuple<ClassName, FieldName>> fields,
			List<Tuple<ClassName, FieldName>> superFields) throws FJException {
		List<Tuple<ClassName, FieldName>> allFields = new ArrayList<Tuple<ClassName,FieldName>>();
		allFields.addAll(superFields);
		allFields.addAll(fields);
		
		if(!arguments.equals(allFields)) {
			throw new FJException("Constructor fields do not match parent class");
		}
		
		if(!(mapFst(superFields).equals(this.superFields))){
			throw new FJException("Superfields in constructor do not match up");
		}
		
		if(!(mapSnd(fields).equals(this.localFields))) {
			throw new FJException("Localfields in constructor do not mach up");
		}
		
	}

	public Object getReturnClassName() {
		return returnType;
	}
}

