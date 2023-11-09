package player;

import java.util.List;
import java.util.Optional;

import model.ReadOnlyReversiModel;
import model.ReversiPiece;

public class ManyStrategy implements IPlayerMoveStrategy {
  private final List<IPlayerMoveStrategy> strategyList;

  public ManyStrategy(List<IPlayerMoveStrategy> s) {
    this.strategyList = s;
  }


  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece piece) {
    for (IPlayerMoveStrategy strat : this.strategyList) {
      Optional<ReversiPosn> move = strat.playStrategy(model, piece);
      if (move.isPresent()) {
        return move; // returns the first posn found
      }
    }
    return Optional.empty();  // none of the strats returned a move!
  }
}
