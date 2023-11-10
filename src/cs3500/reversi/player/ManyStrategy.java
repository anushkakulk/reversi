package cs3500.reversi.player;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents a strategy that relies on having a series of strategies to play. If one fails, it
 * tries to find a move from the next strategy in the list, until the strategy hasn't found a posn
 * to move to after iterating through the list of strategies.
 */
public class ManyStrategy implements IPlayerMoveStrategy {
  private final List<IPlayerMoveStrategy> strategyList;

  /**
   * Creates an instance of ManyStrategy.
   * @param s the list of strategies we want our manyStrategies to have in its repertoire.
   */
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
