package cs3500.reversi.player;

import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

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
  }

  @Override
  public Optional<IPlayerMove> playStrategy(ReadOnlyReversiModel model, ReversiPiece player) {
    // like i don't want to do anything here! but if i return empty, that indicates a pass!
    return Optional.of(new HumanChoice());
  }
}
