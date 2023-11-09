package player;

import java.util.Optional;
import java.util.Scanner;

import model.ReadOnlyReversiModel;
import model.ReversiPiece;

public class HumanStrategy implements IPlayerMoveStrategy {

  Scanner input;

  HumanStrategy() {
    this(new Scanner(System.in));
  }

  public HumanStrategy(Scanner input) {
    this.input = input;
  }

  @Override
  public Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece player) {
    System.out.println("Enter the destination tile's q, r, and s coordinates " +
            "(with spaces between all of them) to move there or enter 'p' to pass your turn.");
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
