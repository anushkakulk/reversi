package cs3500.reversi.player;

import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Scanner;

import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents the strategy for a human player, which involves reading user input to determine the
 * next move.
 */
public class HumanStrategy implements IPlayerMoveStrategy {
  private final Scanner input;

  /**
   * Initializes a HumanStrategy with a new scanner object to read input from.
   */
  HumanStrategy() {
    this(new InputStreamReader(System.in));
  }

  /**
   * Creates an instance of HumanStrategy.
   * @param input the scanner object to read user input from.
   */
  public HumanStrategy(Readable input) {
    this.input = new Scanner(input);
  }

  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece player) {
    System.out.println("Enter the destination tile's q, r, and s coordinates " +
            "(with spaces in between) you wish to move to, or enter 'p'/'P' to pass your turn.");
    if (input.next().equalsIgnoreCase("p")) {
      return Optional.empty();
    } else {
      int q = input.nextInt();
      int r = input.nextInt();
      int s = input.nextInt();
      return Optional.of(new ReversiPosn(q, r, s));
    }
  }
}
