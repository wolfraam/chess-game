package io.github.wolfraam.chessgame.util;

/**
 * Android 5 doesn't support java.util.function.Consumer. Therefor back-ported here.
 */
public interface Consumer<T> {
    void accept(T t);
}