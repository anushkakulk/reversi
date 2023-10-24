import java.util.List;

/**
 * Represents the Textual representation of a Game of Reversi.
 */
public class ReversiTextualView {
  private final ReversiModel model;
  private final Appendable output;

  /**
   * Constructor with ReversiModel argument, which has all info needed for rendering.
   *
   * @param model  the Reversi game model that we are viewing.
   * @param output the Appendable to which the view should append the output.
   */
  public ReversiTextualView(ReversiModel model, Appendable output) {
    this.model = model;
    this.output = output;
  }

  /**
   * Constructor with ReversiModel argument, which has all info needed for rendering.
   *
   * @param model the Reversi game model that we are viewing.
   */
  public ReversiTextualView(ReversiModel model) {
    this.model = model;
    this.output = new StringBuilder();
  }

  /**
   * Represents the ReversiModel as a String.
   *
   * @return a string showing the current state of the ReversiModel.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int hexSideLength = model.getHexSideLength();

    for (int r = -hexSideLength + 1; r < hexSideLength; r++) {
      int qStart = Math.max(-hexSideLength + 1, -hexSideLength - r + 1);
      int qEnd = Math.min(hexSideLength - 1, hexSideLength - r - 1);

      sb.append(" ".repeat(Math.abs(r))); // take care of any of the indenting!

      for (int q = qStart; q <= qEnd; q++) {
        Tile tile = new Tile(q, r, -q - r);
        ReversiPiece piece = model.getPieceAt(tile);
        if (piece == ReversiPiece.EMPTY) {
          sb.append("_ ");
        } else if (piece == ReversiPiece.BLACK) {
          sb.append("X ");
        } else if (piece == ReversiPiece.WHITE) {
          sb.append("O ");
        }
      }

      sb.append("\n");
    }

    return sb.toString();
  }


}
