package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.*;
import cs3500.reversi.view.ReversiGUIView;

/**
 * Represents the way to create and play a game of reversi.
 */
public final class Reversi {
  /**
   * Creates and Runs the interactive gui for a game of reversi.
   *
   * @param args input to main.
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("Cannot begin a game with invalid inputs for players");
    }
    ReversiModel model = new ReversiGameModel(6);
    ReversiGUIView view1 = new ReversiGUIView(model);
    ReversiGUIView view2 = new ReversiGUIView(model);
    Player p1 = ReversiArgParser.parsePlayers(args).getPlayer1();
    Player p2 = ReversiArgParser.parsePlayers(args).getPlayer2();
    IReversiController controller = new ReversiController(model, p1, view1);
    IReversiController controller2 = new ReversiController(model, p2, view2);
    model.startGame();
  }

  /**
   * Represents a helper class for parsing players from command line arguments.
   */
  private static class ReversiArgParser {
    private int argIndex;
    private Player player1;
    private Player player2;

    /**
     * Creates a helper parser objects for Reversi.
     * @param args the arguments to read.
     */
    public ReversiArgParser(String[] args) {
      this.argIndex = 0;
      IPlayerMoveStrategy strat1 = getStrategy(args);
      this.argIndex += 1;
      IPlayerMoveStrategy strat2 = getStrategy(args);
      this.player1 = new Player(new Strategy(strat1), ReversiPiece.BLACK);
      this.player2 = new Player(new Strategy(strat2), ReversiPiece.WHITE);
    }

    private IPlayerMoveStrategy getStrategy(String[] args) {
      if (this.argIndex >= args.length) {
        throw new IllegalArgumentException("Index out of bounds");
      }
      String input = args[this.argIndex].toUpperCase();
      switch (input) {
        case "HUMAN":
          return new HumanStrategy();
        case "STRATEGY1":
          return new CaptureMostStrategy();
        case "STRATEGY2":
          return new AvoidNextToCornersStrategy();
        case "STRATEGY3":
          return new PlayCornersStrategy();
        case "MANYSTRATEGY":
          this.argIndex += 1;
          int numStrategies = Integer.parseInt(args[this.argIndex]);
          this.argIndex += 1;
          List<IPlayerMoveStrategy> all = new ArrayList<>();
          for (int i = 0; i < numStrategies; i++) {
            this.argIndex += i;
            all.add(getStrategy(args));
          }
          return new ManyStrategy(all);
        default:
          throw new IllegalArgumentException("Invalid Player Type");
      }
    }

    public Player getPlayer1() {
      return this.player1;
    }

    public Player getPlayer2() {
      return this.player2;
    }

    /**
     * Static method for parsing players from command line arguments.
     *
     * @param args input to main.
     * @return ReversiArgParser instance containing parsed players.
     */
    public static ReversiArgParser parsePlayers(String[] args) {
      return new ReversiArgParser(args);
    }
  }
}
