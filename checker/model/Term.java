package checker.model;

import java.util.Set;

import checker.typecheck.TypeCheck;

public interface Term {

    Set<ClassName> getAllReferencedClassNames();

    void visit(TypeCheck check);

}
