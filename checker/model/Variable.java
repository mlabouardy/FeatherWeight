package checker.model;

import java.util.Collections;
import java.util.Set;

import checker.typecheck.TypeCheck;

public class Variable implements Term {

	private final ArgumentName argName;
	
	public Variable(ArgumentName aname) {
		this.argName = aname;
	}

	@SuppressWarnings("unchecked")
	public Set<ClassName> getAllReferencedClassNames() {
		return Collections.EMPTY_SET;
	}

    public void visit(TypeCheck check) {
        check.appyRule(this);
    }

    public ArgumentName getArgumentName() {
        return this.argName;
    }

}
