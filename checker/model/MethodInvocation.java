package checker.model;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import checker.typecheck.TypeCheck;

public class MethodInvocation implements Term {

	private final Term path;
	private final MethodName methodName;
	private final List<Term> arguments;

	public MethodInvocation(Term path, MethodName methodName, List<Term> arguments) {
		this.path = path;
		this.methodName = methodName;
		this.arguments = arguments;
	}

	public Set<ClassName> getAllReferencedClassNames() {
		Set<ClassName> allReferencedClassNames = new HashSet<ClassName>();
		allReferencedClassNames.addAll(path.getAllReferencedClassNames());
		for(Term t : arguments) {
			allReferencedClassNames.addAll(t.getAllReferencedClassNames());
		}
		return allReferencedClassNames;
	}

    public void visit(TypeCheck check) {
        check.appyRule(this);
    }

    public Term getPath() {
        return this.path;
    }

    public MethodName getMethodName() {
        return this.methodName;
    }

    public List<Term> getArguments() {
        return new ArrayList<Term>(this.arguments);
    }
}
