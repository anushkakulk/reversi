package player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import model.ReadOnlyReversiModel;
import model.ReversiPiece;

public class AvoidNextToCornersStrategy implements IPlayerMoveStrategy {
  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece piece) {
    Map<ReversiPosn, Integer> possibleMoves = new HashMap<>();

    for (int q = -model.getHexSideLength() + 1; q < model.getHexSideLength(); q++) {
      int r1 = Math.max(-model.getHexSideLength() + 1, -model.getHexSideLength() - q + 1);
      int r2 = Math.min(model.getHexSideLength() - 1, model.getHexSideLength() - q - 1);
      for (int r = r1; r <= r2; r++) {
        int s = -q - r;

        if (model.isValidMove(q, r, s, piece) &&
                // avoid any spots nexts to corners positions!
                (!isBorderingCorner(q,r,s, model.getHexSideLength()))) {
          possibleMoves.put(new ReversiPosn(q, r, s), model.numTilesFlipped(q, r, s, piece));
        }
      }
    }
    return ReversiPosn.findBestMove(possibleMoves);
  }

  private boolean isBorderingCorner(int q, int r, int s, int hexSideLength) {
    int absQ = Math.abs(q);
    int absR = Math.abs(r);
    int absS = Math.abs(s);

    int hexSideMinus1 = hexSideLength - 1;
    int hexSideMinus2 = hexSideLength - 2;

    // a tile is bordering a corner if the tile's coordinates are any permutation of coords
    // (hexSideLength - 1, hexSideLength - 2, and 1) OR any permutation of coords
    // (hexSideLength - 2, hexSideLength - 2, and 0)
    return (absQ == hexSideMinus1 && absR == hexSideMinus2 && absS == 1) ||
            (absR == hexSideMinus1 && absS == hexSideMinus2 && absQ == 1) ||
            (absS == hexSideMinus1 && absQ == hexSideMinus2 && absR == 1) ||
            (absQ == hexSideMinus2 && absR == hexSideMinus2 && absS == 0) ||
            (absR == hexSideMinus2 && absS == hexSideMinus2 && absQ == 0) ||
            (absS == hexSideMinus2 && absQ == hexSideMinus2 && absR == 0);
  }

}

