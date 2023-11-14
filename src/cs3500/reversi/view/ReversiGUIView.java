package cs3500.reversi.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import cs3500.reversi.model.ReadOnlyReversiModel;

/**
 * The Gui Frame for visually displaying a game of Reversi.
 */
public class ReversiGUIView extends JFrame implements ICanvasEvent {
  private final static int cellWidth = 100;
  private final static int cellHeight = 100;

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
    ReversiPanel canvas = new ReversiPanel(model, boardWidth, boardHeight);
    canvas.addPanelListener(this);
    getContentPane().add(canvas, BorderLayout.CENTER);

    add(canvas);
    pack();
    setVisible(true);
  }

  @Override
  public void tileClicked(int q, int r, int s) {
    System.out.println("Tile Clicked: " + q + " " + r + " " + s);
  }

  /**
   * adds a listener for the tile clicks to the list of listeners in the view.
   *
   * @param listener a handler for when a tile is clicked in the view.
   */
  public void addViewTileClickedListener(ViewTileClickedHandler listener) {
    // nothing in here for now, will need this when the controller is implemented!
  }
}
