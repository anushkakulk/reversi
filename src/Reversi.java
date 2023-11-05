//package cs3500.reversi;

import model.ReversiGameModel;
import model.ReversiModel;
import view.ReversiGUIView;

public final class Reversi {
  public static void main(String[] args) {
    ReversiModel model = new ReversiGameModel(6);
    ReversiGUIView view = new ReversiGUIView(model);
    view.setVisible(true);
  }
}