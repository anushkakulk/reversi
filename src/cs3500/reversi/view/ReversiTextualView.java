package cs3500.reversi.view;

import cs3500.reversi.controller.SimpleReversiView;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.model.Tile;

/**
 * Represents the Textual representation of a Game of Reversi.
 */
public class ReversiTextualView implements SimpleReversiView {
  private final ReversiModel model;


  /**
   * Constructor with ReversiModel argument, which has all info needed for rendering.
   *
   * @param model the Reversi game model that we are viewing.
   */
  public ReversiTextualView(ReversiModel model) { // GRADERS: USE THIS TO TEST!
    // CREATE A MODEL OBJECT, THEN CREATE A VIEW OBJECT AND PASS IN THAT MODEL.
    // CALLING toString() on THAT VIEW OBJECT WILL OUTPUT THE TEXTUAL VIEW OF THE MODEL.
    this.model = model;
  }

  /**
   * Represents the ReversiModel as a String.
   *
   * @return a string showing the current state of the ReversiModel
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int hexSideLength = model.getHexSideLength();

    // iterate through rows based on hexagonal cubic coordinates.
    for (int r = -hexSideLength + 1; r < hexSideLength; r++) {
      int qStart = Math.max(-hexSideLength + 1, -hexSideLength - r + 1);
      int qEnd = Math.min(hexSideLength - 1, hexSideLength - r - 1);

      sb.append(" ".repeat(Math.abs(r))); // take care of any of the indenting, based on row index!

      // iterate through cols based on hexagonal cubic coordinates.
      for (int q = qStart; q <= qEnd; q++) {
        Tile t = new Tile(q, r, -q - r);
        ReversiPiece piece = model.getPieceAt(t); // get the piece at the specified tile
        if (piece == ReversiPiece.WHITE
                || piece == ReversiPiece.EMPTY
                || piece == ReversiPiece.BLACK) {
          sb.append(piece).append(" "); // this appends the string rep of the piece at that spot
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }

}
