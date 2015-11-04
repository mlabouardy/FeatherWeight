package checker.model;

import java.util.HashSet;
import java.util.Set;

import checker.typecheck.TypeCheck;

public class Cast implements Term {

	private final ClassName className;
	private final Term castedTerm;
	
	public Cast(ClassName className, Term castedTerm) {
		this.className = className;
		this.castedTerm = castedTerm;
	}

	public Set<ClassName> getAllReferencedClassNames() {
		Set<ClassName> allReferencedClassNames = new HashSet<ClassName>();
		allReferencedClassNames.add(className);
		allReferencedClassNames.addAll(castedTerm.getAllReferencedClassNames());
		return allReferencedClassNames;
	}

    public void visit(TypeCheck check) {
        check.appyRule(this);
    }

    public Term getCastedTerm() {
        return this.castedTerm;
    }

    public ClassName getTypeToCastTo() {
        return this.className;
    }
}
