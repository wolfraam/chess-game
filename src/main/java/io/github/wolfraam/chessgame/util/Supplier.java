package io.github.wolfraam.chessgame.util;

/**
 * Android 5 doesn't support java.util.function.Supplier. Therefor back-ported here.
 */
public interface Supplier<T> {
    T get();
}
