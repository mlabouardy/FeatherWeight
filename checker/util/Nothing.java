/*
 * Created on 23 Oct 2007
 */
package checker.util;

public class Nothing<T> implements Maybe<T> {

    public T fromJust() {
        return null;
    }

    public boolean isJust() {
        return false;
    }

    public boolean isNothing() {
        return true;
    }

    public T fromMaybe(T defaultValue) {
        return defaultValue;
    }

    public <S> S maybe(S defaultValue, Arrow<T, S> code) {
        return defaultValue;
    }

}
