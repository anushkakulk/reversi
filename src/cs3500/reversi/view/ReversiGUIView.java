package cs3500.reversi.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JOptionPane;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.controller.Event;
import cs3500.reversi.provider.controller.Listener;

/**
 * The Gui Frame for visually displaying a game of Reversi.
 */
public class ReversiGUIView extends JFrame implements ReversiView, PlayerActionFeatures {
  private static final int cellWidth = 100;
  private static final int cellHeight = 100;
  private final ReversiPanel panel;


  /**
   * Creates the gui view with a given read only reversi model.
   *
   * @param model a model for a game of Reversi, with only observation methods.
   */
  public ReversiGUIView(ReadOnlyReversiModel model) {
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
  public void handleTileClicked(int q, int r, int s) {
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


  @Override
  public void addPlayerActionListener(PlayerActionFeatures listener) {
      this.panel.addPlayerActionListener(listener);
  }


  @Override
  public void update() {
    this.panel.update();
  }

  @Override
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Invalid Move", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayTitle(String titleMessage) {
    this.setTitle("Reversi Game: " + titleMessage);
  }

  @Override
  public void update(Event e) {

  }
}
