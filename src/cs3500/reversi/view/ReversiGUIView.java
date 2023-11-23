package cs3500.reversi.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.*;

import cs3500.reversi.model.ReversiModel;

/**
 * The Gui Frame for visually displaying a game of Reversi.
 */
public class ReversiGUIView extends JFrame implements ReversiView, PlayerActionFeatures {
  private final static int cellWidth = 100;
  private final static int cellHeight = 100;

  private final ReversiPanel panel;


  /**
   * Creates the gui view with a given read only reversi model.
   *
   * @param model a model for a game of Reversi, with only observation methods.
   */
  public ReversiGUIView(ReversiModel model) {
    Objects.requireNonNull(model);
    int boardWidth = model.getHexSideLength() * 3 / 2 * cellWidth;
    int boardHeight = model.getHexSideLength() * 3 / 2 * cellHeight;
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(true);
    setLocationRelativeTo(null);
    panel = new ReversiPanel(model, boardWidth, boardHeight);
    panel.addPlayerActionListener(this);
    getContentPane().add(panel, BorderLayout.CENTER);

    add(panel);
    pack();
    setVisible(true);
  }

  @Override
  public void handleTileClicked  (int q, int r, int s) {
    System.out.println("Tile Clicked: " + q + " " + r + " " + s);
  }

  @Override
  public void handleMoveChosen(int q, int r, int s) {
    System.out.println("Tile Moved to: " + q + " " + r + " " + s);

  }

  @Override
  public void handlePassChosen() {
    System.out.println("Player Passed");
  }


  public void addPlayerActionListener(PlayerActionFeatures listener) {
    this.panel.addPlayerActionListener(listener);
  }

  public void update(){
    this.panel.update();
  }

  @Override
  public void handleInvalidOperation(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Invalid Move", JOptionPane.ERROR_MESSAGE);
  }

}
