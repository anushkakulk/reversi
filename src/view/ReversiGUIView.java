package view;

import java.awt.*;

import javax.swing.*;

import model.ReadOnlyReversiModel;

public class ReversiGUIView extends JFrame implements ICanvasEvent {
  private final static int cellWidth = 100;
  private final static int cellHeight = 100;

  public ReversiGUIView(ReadOnlyReversiModel model) {
    int boardWidth = model.getHexSideLength() * 3/2 * cellWidth;
    int boardHeight = model.getHexSideLength() * 3/2 * cellHeight;

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

  public void addViewTileClickedListener(ViewTileClickedHandler listener) {
    // nothing in here for now, will need this when the controller is implemented!
  }
}
