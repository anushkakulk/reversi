package cs3500.reversi.view;

import java.awt.*;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JOptionPane;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.controller.Event;

/**
 * The Gui Frame for visually displaying a game of Reversi.
 */
public class ReversiGUIView extends JFrame implements ReversiView, PlayerActionFeatures {
  private static final int cellWidth = 100;
  private static final int cellHeight = 100;
  private final IPanel panel;


  /**
   * Creates the gui view with a given ReversiPanel.
   *
   * @param panel a ReversiPanel for rendering the game.
   */
  public ReversiGUIView(IPanel panel) {
    Objects.requireNonNull(panel);
    this.panel = panel;
    initialize();
  }

  /**
   * Creates the gui view with a given read-only reversi model and a ReversiPanel.
   *
   * @param model a model for a game of Reversi, with only observation methods.
   */
  public ReversiGUIView(ReadOnlyReversiModel model) {
    int boardWidth = model.getHexSideLength() * 3 / 2 * cellWidth;
    int boardHeight = model.getHexSideLength() * 3 / 2 * cellHeight;
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    Objects.requireNonNull(model);
    panel = new ReversiPanel(model, boardWidth, boardHeight);
    initialize();
  }

  private void initialize() {
    int boardWidth = panel.getHexSideLength() * 3 / 2 * cellWidth;
    int boardHeight = panel.getHexSideLength() * 3 / 2 * cellHeight;
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(true);
    setLocationRelativeTo(null);
    panel.addPlayerActionListener(this);
    add(panel.getJComponent());
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
  public void update(Event e) {
    //Providers code
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
  public void handleHintOn(int num) {
    this.panel.handleHintOn(num);
  }


}
