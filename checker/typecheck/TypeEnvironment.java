package checker.typecheck;

import java.util.HashMap;
import java.util.Map;

import checker.model.ArgumentName;
import checker.model.ClassName;
import checker.model.FieldName;

////////////////////////////////////////////////////////////////
// A class binding ArgumentName to ClassName (variables to types)
public class TypeEnvironment {

	private final Map<ArgumentName, ClassName> bindings;

	public TypeEnvironment() {
		this.bindings = new HashMap<ArgumentName, ClassName>();
	}
	
	public ClassName getBinding(ArgumentName f) {
		return this.bindings.get(f);
	}
	
	public void addBinding(ArgumentName f, ClassName c) {
		if(null != this.bindings.put(f, c)) {
		    throw new RuntimeException("Precondition failure");      
        }
	}
}
