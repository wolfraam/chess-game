package com.github.chessgame.util;

/**
 * Android 5 doesn't support java.util.function.Supplier. Therefor backported here.
 */
public interface Supplier<T> {
    T get();
}
