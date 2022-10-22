package com.github.chessgame.result;

/**
 * Possible reasons for a draw.
 */
public enum DrawType {
    FIFTY_MOVE_RULE,
    INSUFFICIENT_MATERIAL,
    STALE_MATE,
    THREEFOLD_REPETITION
}
