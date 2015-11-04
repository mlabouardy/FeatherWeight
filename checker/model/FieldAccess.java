package checker.model;

import java.util.Set;

import checker.typecheck.TypeCheck;

public class FieldAccess implements Term {

	private Term path;
	private FieldName field;
	
	public FieldAccess(Term path, FieldName field) {
		this.path = path;
		this.field = field;
	}

	public Set<ClassName> getAllReferencedClassNames() {
		return path.getAllReferencedClassNames();
	}

    public void visit(TypeCheck check) {
        check.appyRule(this);
    }

    public Term getPath() {
        return this.path;
    }

    public FieldName getFieldName() {
        return this.field;
    }
}
