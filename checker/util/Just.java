/*
 * Created on 23 Oct 2007
 */
package checker.util;

public class Just<T> implements Maybe<T> {

    private final T value;
    
    public Just(T value) {
        this.value = value;
    }
    
    public T fromJust() {
        return this.value;
    }

    public boolean isJust() {
        return true;
    }

    public boolean isNothing() {
        return false;
    }

    public T fromMaybe(T defaultValue) {
        return this.value;
    }

    public <S> S maybe(S defaultValue, Arrow<T, S> code) {
        return code.run(this.value);
    }

}
