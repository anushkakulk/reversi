package cs3500.reversi;

import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
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
    ReversiGUIView view = new ReversiGUIView(model);
    view.setVisible(true);
  }
}


// play to a non trivial point in the game, and then start the view.
//    model.move(1, -2, 1); // black
//    model.move(2, -1, -1); // white
//    model.move(-2, 1, 1); // black
//    model.move(1, -3, 2); // white
//    model.move(1, 1, -2); // black
//    model.move(-1, 2, -1); // white
//    model.move(2, -3, 1); // black
//    model.move(-1, -1, 2); // white
//    model.pass(); // black
//    model.move(3, -4, 1);// white
//    model.move(4, -5, 1);// black