package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.MergedReversiModel;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.AdaptedPlayer;
import cs3500.reversi.player.AvoidNextToCornersStrategy;
import cs3500.reversi.player.CaptureMostStrategy;
import cs3500.reversi.player.HumanStrategy;
import cs3500.reversi.player.IPlayerMoveStrategy;
import cs3500.reversi.player.ManyStrategy;
import cs3500.reversi.player.PlayCornersStrategy;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.ReversiPlayer;
import cs3500.reversi.player.Strategy;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.player.PlayerTurn;
import cs3500.reversi.provider.strategy.MaximizeCaptureStrategy;
import cs3500.reversi.provider.view.ReversiGUI;
import cs3500.reversi.view.ReversiView;
import cs3500.reversi.view.AdaptedView;
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
//
//    if (args.length < 2) {
//      throw new IllegalArgumentException("Cannot begin a game with invalid inputs for players");
//    }

    MergedReversiModel adaptedModel = new MergedReversiModel(6);
    ReversiView adaptedView = new AdaptedView(new ReversiGUI(adaptedModel), adaptedModel);
    ReversiView view2 = new ReversiGUIView(adaptedModel);
    // here is where it parses command-line args
    ReversiPlayer p1 = new Player(new Strategy(new CaptureMostStrategy()),  ReversiPiece.BLACK);
            // ReversiArgParser.parsePlayers(args).getPlayer1();
    // Player p2 = ReversiArgParser.parsePlayers(args).getPlayer2();
    ReversiPlayer p2 = new AdaptedPlayer(PlayerTurn.PLAYER2,
            new MaximizeCaptureStrategy(adaptedModel, PlayerTurn.PLAYER2));

    IReversiController controller = new ReversiController(adaptedModel, p1, view2);
    IReversiController controller2 = new ReversiController(adaptedModel, p2, adaptedView);
    adaptedModel.startGame();
  }

  /**
   * Represents a helper class for parsing players from command line arguments.
   */
  private static class ReversiArgParser {
    private int argIndex;
    private final Player player1;
    private final Player player2;

    /**
     * Creates a helper parser objects for Reversi.
     * @param args the arguments to read.
     */
    private ReversiArgParser(String[] args) {
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
          List<IPlayerMoveStrategy> all = new ArrayList<>();
          for (int i = 0; i < numStrategies; i++) {
            this.argIndex += 1;
            all.add(getStrategy(args));
          }
          return new ManyStrategy(all);
        default:
          throw new IllegalArgumentException("Invalid Player Type");
      }
    }

    /**
     * Gets the constructed player object for player1 from the parser.
     * @return a player.
     */
    public Player getPlayer1() {
      return this.player1;
    }

    /**
     * Gets the constructed player object for player2 from the parser.
     * @return a player.
     */
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
