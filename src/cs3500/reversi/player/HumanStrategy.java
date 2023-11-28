package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents the strategy for a human player, which involves reading user input to determine the
 * next move.
 */
public class HumanStrategy implements IPlayerMoveStrategy {

  /**
   * Creates an instance of HumanStrategy.
   */
  public HumanStrategy() {
    // nothing in a human strategy.
  }

  @Override
  public Optional<IPlayerMove> playStrategy(ReadOnlyReversiModel model, ReversiPiece player) {
    // like i don't want to do anything here! but if i return empty, that indicates a pass!
    // so instead, we have a new type of IPlayerMove which indicates a human choice.
    return Optional.of(new HumanChoice());
  }
}
