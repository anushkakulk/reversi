package player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import model.ReadOnlyReversiModel;
import model.ReversiPiece;
public class CaptureMostStrategy implements IPlayerMoveStrategy {
  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece piece) {
    Map<ReversiPosn, Integer> possibleMoves = new HashMap<>();

    for (int q = -model.getHexSideLength() + 1; q < model.getHexSideLength(); q++) {
      int r1 = Math.max(-model.getHexSideLength() + 1, -model.getHexSideLength() - q + 1);
      int r2 = Math.min(model.getHexSideLength() - 1, model.getHexSideLength() - q - 1);
      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        if (model.isValidMove(q, r, s, piece)) {
          possibleMoves.put(new ReversiPosn(q, r, s), model.numTilesFlipped(q, r, s, piece));
        }
      }
    }
    return ReversiPosn.findBestMove(possibleMoves);
  }
}
