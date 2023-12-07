package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.adapter.MergedReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.adapter.AdaptedPlayer;
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
import cs3500.reversi.provider.strategy.CornersStrategy;
import cs3500.reversi.provider.strategy.IStrategy;
import cs3500.reversi.provider.strategy.MaximizeCaptureStrategy;
import cs3500.reversi.provider.strategy.MiniMaxStrategy;
import cs3500.reversi.provider.view.ReversiGUI;
import cs3500.reversi.view.HintReversiPanel;
import cs3500.reversi.view.ReversiPanel;
import cs3500.reversi.view.ReversiView;
import cs3500.reversi.adapter.AdaptedView;
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



    MergedReversiModel adaptedModel = new MergedReversiModel(6);
    int boardWidth = adaptedModel.getHexSideLength() * 3 / 2 * 100;
    int boardHeight = adaptedModel.getHexSideLength() * 3 / 2 * 100;
    ReversiView view1 = new ReversiGUIView(
            new HintReversiPanel(new ReversiPanel(adaptedModel, boardWidth, boardHeight)));
    ReversiView view2 = new ReversiGUIView(adaptedModel);
    // here is where it parses command-line args
    ReversiArgParser parser = ReversiArgParser.parsePlayers(args, adaptedModel);
    ReversiPlayer p1 = parser.getPlayer1();
    ReversiPlayer p2 = parser.getPlayer2();

    IReversiController controller = new ReversiController(adaptedModel, p1, view1);
    IReversiController controller2 = new ReversiController(adaptedModel, p2, view2);
    adaptedModel.startGame();
  }

  /**
   * Represents a helper class for parsing players from command line arguments.
   */
  private static class ReversiArgParser {
    private int argIndex;
    private String[] args;
    private ReadOnlyReversiModel model;

    /**
     * Creates a helper parser objects for Reversi.
     *
     * @param args the arguments to read.
     */
    private ReversiArgParser(String[] args, ReadOnlyReversiModel model) {
      this.args = args;
      this.argIndex = 0;
      this.model = model;
    }

    /**
     * Static method for parsing players from command line arguments.
     *
     * @param args input to main.
     * @return ReversiArgParser instance containing parsed players.
     */
    public static ReversiArgParser parsePlayers(String[] args, ReadOnlyReversiModel m) {
      return new ReversiArgParser(args, m);
    }

    private IStrategy getProviderStrategy(String[] args, PlayerTurn p) {
      if (this.argIndex >= args.length) {
        throw new IllegalArgumentException("Index out of bounds");
      }

      String input = args[this.argIndex].toUpperCase();
      switch (input) {
        case "PROVIDER1":
          return new MaximizeCaptureStrategy(this.model, p);
        case "PROVIDER2":
          return new CornersStrategy(this.model, p, false);
        case "PROVIDER3":
          return new CornersStrategy(this.model, p, true);
        case "PROVIDER4":
          return new MiniMaxStrategy(this.model, p);
        default:
          throw new IllegalArgumentException("Invalid Player Type");
      }
    }

    private IPlayerMoveStrategy getNormalStrategy(String[] args) {
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
            all.add(getNormalStrategy(args));
          }
          return new ManyStrategy(all);
        default:
          throw new IllegalArgumentException("Invalid Player Type");
      }
    }

    private ReversiPlayer createNormalPlayer(IPlayerMoveStrategy strategy, ReversiPiece piece) {
      return new Player(new Strategy(strategy), piece);
    }

    private ReversiPlayer createAdaptedPlayer(IStrategy strategy, ReversiPiece piece) {
      PlayerTurn playerTurn = (piece == ReversiPiece.BLACK)
          ? PlayerTurn.PLAYER1 : PlayerTurn.PLAYER2;
      return new AdaptedPlayer(playerTurn, strategy);
    }

    public ReversiPlayer getPlayer1() {
      ReversiPlayer p;
      String input = this.args[argIndex].toUpperCase();
      if ("PROVIDER1".equals(input) || "PROVIDER2".equals(input) || "PROVIDER3".equals(input)
          || "PROVIDER4".equals(input)) {
        p = createAdaptedPlayer(getProviderStrategy(args,
                PlayerTurn.PLAYER1),
            ReversiPiece.BLACK);
      } else {
        p = createNormalPlayer(getNormalStrategy(args), ReversiPiece.BLACK);
      }
      this.argIndex += 1;
      return p;
    }

    public ReversiPlayer getPlayer2() {
      ReversiPlayer p;
      String input = this.args[argIndex].toUpperCase();
      if ("PROVIDER1".equals(input) || "PROVIDER2".equals(input) || "PROVIDER3".equals(input)
          || "PROVIDER4".equals(input)) {
        p = createAdaptedPlayer(getProviderStrategy(args,
                PlayerTurn.PLAYER2),
            ReversiPiece.WHITE);
      } else {
        p = createNormalPlayer(getNormalStrategy(args), ReversiPiece.WHITE);
      }
      return p;
    }
  }
}




