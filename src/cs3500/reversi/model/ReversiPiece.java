package cs3500.reversi.model;

/**
 * Represents a game piece for Reversi that sits on top of every
 * tile in the game board.
 */
public enum ReversiPiece {
  EMPTY("_"), BLACK("X"), WHITE("O");
  private final String display;

  ReversiPiece(String disp) {
    this.display = disp;
  }

  @Override
  public String toString() {
    return display;
  }
}