package io.github.wolfraam.chessgame.board.visual;

public class BoardVisualizer {

  public enum Perspective {
    WHITE,
    BLACK,
    CURRENT_PLAYER
  }


  public static final Perspective DEFAULT_PERSPECTIVE = Perspective.WHITE;

  public static String fenToBigAscii(String fen) {
    return fenToBigAscii(fen, DEFAULT_PERSPECTIVE);
  }

  public static String fenToBigAscii(String fen, Perspective perspective) {
    return null;
  }





}
