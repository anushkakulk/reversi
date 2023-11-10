package cs3500.reversi;

import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiGUIView;

public final class Reversi {
  public static void main(String[] args) {
    ReversiModel model = new ReversiGameModel(6);
    ReversiGUIView view = new ReversiGUIView(model);
    view.setVisible(true);
  }
}