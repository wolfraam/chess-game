package com.github.chessgame.util;

/**
 * Android 5 doesn't support java.util.function.Consumer. Therefor backported here.
 */
public interface Consumer<T> {
    void accept(T t);
}