package checker.typecheck;

import java.util.Set;

import checker.model.ClassName;
import checker.passes.FJException;

public class FJPathCycleException extends FJException {

	private static final long serialVersionUID = 1L;

	public FJPathCycleException(Set<ClassName> cyclicPath) {
		super("Cycle in path: " + cyclicPath);
		
	}

}
