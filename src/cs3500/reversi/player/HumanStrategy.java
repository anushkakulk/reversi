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
  private final Scanner input;

  /**
   * Initializes a HumanStrategy with a new scanner object to read input from.
   */
  HumanStrategy() {
    this(new InputStreamReader(System.in));
  }

  /**
   * Creates an instance of HumanStrategy.
   *
   * @param input the scanner object to read user input from.
   */
  public HumanStrategy(Readable input) {
    this.input = new Scanner(input);
  }

  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece player) {
    System.out.println("Enter the destination tile's q, r, and s coordinates separated by " +
            "spaces, or type 'p'/'P' to pass your turn:");

    if (input.hasNext("p|P")) {
      input.next();
      return Optional.empty();
    } else {
      try {
        int q = input.nextInt();
        int r = input.nextInt();
        int s = input.nextInt();
        if (model.isValidMove(q, r, s, player)) {
          return Optional.of(new ReversiPosn(q, r, s));
        }
      } catch (InputMismatchException ignored) {

      }
    }
    return handleBadInput(model, player);
  }

  private Optional<ReversiPosn> handleBadInput(ReadOnlyReversiModel model, ReversiPiece player) {
    input.nextLine();
    System.out.println("Invalid input. Enter 'p' to pass or three space-separated " +
            "numbers for coordinates.");
    return playStrategy(model, player);
  }
}
