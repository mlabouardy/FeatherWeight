package checker.typecheck;

import java.util.List;

import checker.passes.FJException;

public class FJExceptions extends FJException {

	private static final long serialVersionUID = 7705202817447949895L;

	public FJExceptions(List<? extends FJException> cs) {
		super(cs.toString());
	}

}
