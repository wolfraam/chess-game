package io.github.wolfraam.chessgame.notation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NotationMappingTest {
    @Test
    public void testException() {
        Assertions.assertThrows(NullPointerException.class, () ->
                new NotationMapping(null, "a", "a", "a", "a"));
    }
}