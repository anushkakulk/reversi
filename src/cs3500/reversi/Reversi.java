package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.AvoidNextToCornersStrategy;
import cs3500.reversi.player.CaptureMostStrategy;
import cs3500.reversi.player.HumanStrategy;
import cs3500.reversi.player.ManyStrategy;
import cs3500.reversi.player.PlayCornersStrategy;
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
    Player AI = new Player(new Strategy(new CaptureMostStrategy()), ReversiPiece.BLACK);
    IReversiController controller = new ReversiController(model, AI, view1);
    Player human = new Player(new Strategy(new HumanStrategy()), ReversiPiece.WHITE);
    IReversiController controller2 = new ReversiController(model, human, view2);
    model.startGame();
  }
}
