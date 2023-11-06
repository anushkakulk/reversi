package view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import model.ReadOnlyReversiModel;

public class ReversiGUIView extends JFrame implements ICanvasEvent {
  private final static int cellWidth = 100;
  private final static int cellHeight = 100;
  private final ReversiPanel canvas;


  public ReversiGUIView(ReadOnlyReversiModel model) {
    int cellWidth = 100;
    int cellHeight = 100;
    int boardWidth = model.getHexSideLength() * 2 * cellWidth;
    int boardHeight = model.getHexSideLength() * 2 * cellHeight;

    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(true);
    setLocationRelativeTo(null);
    this.canvas = new ReversiPanel(model, boardWidth, boardHeight);
    this.canvas.addPanelListener(this);
    add(this.canvas);
    pack();
    setVisible(true);

  }

  @Override
  public void tileClicked(int q, int r, int s) {
    System.out.println("Tile Clicked: " + q + " " + r + " " + s);
  }

  public void addViewTileClickedListener(ViewTileClickedHandler listener) {
  }
}
