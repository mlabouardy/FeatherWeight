/*
 * Created on 23 Oct 2007
 */
package checker.util;

public interface Arrow<T1, T2> {

    T2 run(T1 arg);
}
