package checker.typecheck;

import checker.model.ClassName;
import checker.passes.FJException;

public class FJUnknownClassNameException extends FJException {

	private static final long serialVersionUID = 2033621077764702888L;

	public FJUnknownClassNameException(ClassName c) {
		super("Unknown class name: " + c);
	}

}
