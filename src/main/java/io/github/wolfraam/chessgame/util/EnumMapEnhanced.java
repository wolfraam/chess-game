package io.github.wolfraam.chessgame.util;

import java.util.EnumMap;
import java.util.Objects;

/**
 * Android 5 doesn't support Map.computeIfAbsentSupport. Therefor backported here.
 */
public class EnumMapEnhanced<K extends Enum<K>, V> extends EnumMap<K, V> {

    public EnumMapEnhanced(final Class<K> keyType) {
        super(keyType);
    }

    public V computeIfAbsentSupport(final K key,
                                    final Function<K, V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v;
        if ((v = get(key)) == null) {
            final V newValue;
            if ((newValue = mappingFunction.call(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }
}
