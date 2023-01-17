package io.github.wolfraam.chessgame.util;

import java.util.Iterator;
import java.util.Set;

/**
 * Android 5 doesn't support Set.removeIf. Therefor back-ported here.
 */
public class RemoveIfSupport {
    public static <T> void run(final Set<T> set, final Function<T, Boolean> function) {
        final Iterator<T> iterator = set.iterator();
        while (iterator.hasNext()) {
            final T t = iterator.next();
            if (function.call(t)) {
                iterator.remove();
            }
        }
    }
}
