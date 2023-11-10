package cs3500.reversi.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents a strategy for only playing into corners in Reversi.
 */
public class PlayCornersStrategy implements IPlayerMoveStrategy {

  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece piece) {
    Map<ReversiPosn, Integer> possibleMoves = new HashMap<>();

    for (int q = -model.getHexSideLength() + 1; q < model.getHexSideLength(); q++) {
      int r1 = Math.max(-model.getHexSideLength() + 1, -model.getHexSideLength() - q + 1);
      int r2 = Math.min(model.getHexSideLength() - 1, model.getHexSideLength() - q - 1);
      for (int r = r1; r <= r2; r++) {
        int s = -q - r;

        if (model.isValidMove(q, r, s, piece) &&
                // play into the corner positions!
                isCornerPosition(q, r, s, model.getHexSideLength())
        ) {
          possibleMoves.put(new ReversiPosn(q, r, s), model.numTilesFlipped(q, r, s, piece));
        }
      }
    }
    return ReversiPosn.findBestMove(possibleMoves);
  }

  // helper method to determine whether the given coords are for a corner tile in the reversi board
  private boolean isCornerPosition(int q, int r, int s, int hexSideLength) {
    int absQ = Math.abs(q);
    int absR = Math.abs(r);
    int absS = Math.abs(s);

    int hexSideMinus1 = hexSideLength - 1;

    return (absQ == hexSideMinus1 && absR == hexSideMinus1 && absS == 0) ||
            (absR == hexSideMinus1 && absS == hexSideMinus1 && absQ == 0) ||
            (absS == hexSideMinus1 && absQ == hexSideMinus1 && absR == 0);
  }
}