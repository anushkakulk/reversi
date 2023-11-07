package view;

import view.HexTile;
import view.ICanvasEvent;
import model.ReadOnlyReversiModel;
import model.ReversiPiece;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ReversiPanel extends JPanel implements MouseListener {
  private final ReadOnlyReversiModel gameModel;
  private final List<HexTile> hexTiles;
  private final List<ICanvasEvent> listeners;

  private HexTile selectedHexTile;
  private boolean cellSelected = false;

  private int panelWidth;
  private int panelHeight;

  public ReversiPanel(ReadOnlyReversiModel model, int panelWidth, int panelHeight) {
    setPreferredSize(new Dimension(panelWidth * 100 , panelHeight * 100));
    this.gameModel = model;
    this.hexTiles = createHexTiles(model);
    addMouseListener(this);
    selectedHexTile = new HexTile(-1, -1, -1, model.getHexSideLength());

    this.listeners = new ArrayList<>();
  }

  public void addPanelListener(ICanvasEvent listener) {
    this.listeners.add(listener);
  }

  private void emitTileClick(int q, int r, int s) {
    for (ICanvasEvent e : listeners) {
      e.tileClicked(q, r, s);
    }
  }

  private List<HexTile> createHexTiles(ReadOnlyReversiModel model) {
    List<HexTile> tiles = new ArrayList<>();
    int hexRadius = 20;

    int xOffset = 0;
    int yOffset = 0;

    for (int q = -model.getHexSideLength() + 1; q < model.getHexSideLength(); q++) {
      int r1 = Math.max(-model.getHexSideLength() + 1, -model.getHexSideLength() - q + 1);
      int r2 = Math.min(model.getHexSideLength() - 1, model.getHexSideLength() - q - 1);

      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        ReversiPiece piece = gameModel.getPieceAt(q, r, s);

        // Calculate the x and y coordinates based on grid layout
        int x = (int) (xOffset + q);
        int y = (int) (yOffset + r);

        HexTile hexTile = new HexTile(q, r, s, hexRadius);
        hexTile.setColor(Color.GRAY);
        hexTile.setPiece(piece);
        tiles.add(hexTile);
      }
    }

    return tiles;
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.transform(transformLogicalToPhysical());

    //g2d.fillRect(-getWidth(), getHeight(), getWidth(), getHeight());

    for (HexTile hexTile : hexTiles) {
      hexTile.draw(g2d); // draw all the tiles
    }
  }


    private Dimension getPreferredLogicalSize() {
      return new Dimension(1000, 1000);
    }

  /**
   * Computes the transformation that converts board coordinates
   * (with (0,0) in center, width and height our logical size)
   * into screen coordinates (with (0,0) in upper-left,
   * width and height in pixels).
   * <p>
   * @return The necessary transformation
   */

    private AffineTransform transformLogicalToPhysical() {
      AffineTransform ret = new AffineTransform();
      Dimension preferred = getPreferredLogicalSize();

      double scaleX = (double)getWidth() / preferred.getWidth();
      double scaleY = (double)getHeight() / preferred.getHeight();

      ret.translate(getWidth() / 2., getHeight() / 2.);
      ret.scale(scaleX, scaleY);
      ret.scale(1, -1);
      return ret;
    }


  /**
   * Computes the transformation that converts screen coordinates
   * (with (0,0) in upper-left, width and height in pixels)
   * into board coordinates (with (0,0) in center, width and height
   * our logical size).
   * <p>
   * @return The necessary transformation
   */
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(1, -1);
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
    return ret;
  }


  @Override
  public void mouseClicked(MouseEvent e) {
    Point2D pointClicked = transformPhysicalToLogical().transform(e.getPoint(), null);
    boolean cellClicked = false; // has a cell been clicked

    for (HexTile hexTile : hexTiles) {
      if (hexTile.containsPoint(pointClicked)) {
        cellClicked = true;
        emitTileClick(hexTile.getQ(), hexTile.getR(), hexTile.getS());
        if (cellSelected) {
          // check if the click is for the tile already highlighted
          if (selectedHexTile == hexTile) {
            // then deselect it
            cellSelected = false;
            selectedHexTile.setColor(Color.GRAY);
          } else {
            // deselect the old cell and set the new selected one to blue!
            hexTile.setColor(Color.CYAN);
            selectedHexTile.setColor(Color.GRAY);
            selectedHexTile = hexTile;
          }
        } else {
          // no cell is already selected, so just set the cell to blue
          cellSelected = true;
          hexTile.setColor(Color.CYAN);
          selectedHexTile = hexTile;
        }

        repaint();
        break;
      }
    }

    if (!cellClicked && cellSelected) {
      // Clicking outside the boundary with a cell selected deselects the cell
      cellSelected = false;
      selectedHexTile.setColor(Color.GRAY);
      repaint();
    }
  }
  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
}