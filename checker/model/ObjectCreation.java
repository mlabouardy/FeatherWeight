package checker.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import checker.typecheck.TypeCheck;

public class ObjectCreation implements Term {

	private final ClassName className;
	private final List<Term> ctrArgs;

	public ObjectCreation(ClassName className, List<Term> ctrArgs) {
		this.className = className;
		this.ctrArgs = ctrArgs;
	}

	public Set<ClassName> getAllReferencedClassNames() {
		Set<ClassName> allReferencedClassNames = new HashSet<ClassName>();
		allReferencedClassNames.add(this.className);
		for(Term t : this.ctrArgs) {
			allReferencedClassNames.addAll(t.getAllReferencedClassNames());
		}
		return allReferencedClassNames;
	}

    public void visit(TypeCheck check) {
        check.appyRule(this);
    }

    public ClassName getNewClassName() {
        return this.className;
    }

    public List<Term> getArgumentTerms() {
        return new ArrayList<Term>(this.ctrArgs);
    }
}
