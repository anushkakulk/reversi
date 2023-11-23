package cs3500.reversi;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.CaptureMostStrategy;
import cs3500.reversi.player.HumanStrategy;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.Strategy;
import cs3500.reversi.view.ReversiGUIView;

/**
 * Represents the way to create and play a game of reversi.
 */
public final class Reversi {
  /**
   * Creates and Runs the interactive gui for a game of reversi.
   * @param args input to main.
   */
  public static void main(String[] args) {
    ReversiModel model = new ReversiGameModel(6);
    ReversiGUIView view1 = new ReversiGUIView(model);
    ReversiGUIView view2 = new ReversiGUIView(model);
    Player human1 = new Player(new Strategy(new HumanStrategy()), ReversiPiece.BLACK);
    IReversiController controller = new ReversiController(model, human1, view1);
    Player human = new Player(new Strategy(new CaptureMostStrategy()), ReversiPiece.WHITE);
    IReversiController controller2 = new ReversiController(model, human, view2);
    model.startGame();

  }
}
