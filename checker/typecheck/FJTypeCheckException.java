/*
 * Created on 23 Oct 2007
 */
package checker.typecheck;

import checker.passes.FJException;

public class FJTypeCheckException extends RuntimeException {

    public FJTypeCheckException(String string) {
        super(string);
    }

    public FJTypeCheckException(FJException e) {
        super(e);
    }
        
}
