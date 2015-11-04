/*
 * Created on 23 Oct 2007
 */
package checker.util;

public interface Maybe<T> {
    
    T fromMaybe(T defaultValue);
    
    <S> S maybe(S defaultValue, Arrow<T,S> code);
    
    T fromJust();
    
    boolean isJust();
    
    boolean isNothing();
    
}
