package io.github.wolfraam.chessgame.util;

/**
 * Android 5 doesn't support java.util.function.Predicate. Therefor back-ported here.
 */
public interface Predicate<T> {
    boolean test(T t);
}
