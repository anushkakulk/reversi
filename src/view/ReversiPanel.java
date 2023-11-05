package view;

import view.HexTile;
import view.ICanvasEvent;
import model.ReadOnlyReversiModel;
import model.ReversiPiece;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ReversiPanel extends JPanel implements MouseListener {
  private final ReadOnlyReversiModel gameModel;
  private final List<HexTile> hexTiles;
  private final List<ICanvasEvent> listeners;

  private HexTile selectedHexTile;
  private boolean cellSelected = false; // Track the selection status



  public ReversiPanel(ReadOnlyReversiModel model, int panelWidth, int panelHeight) {
    setPreferredSize(new Dimension(panelWidth, panelHeight));
    this.gameModel = model;
    this.hexTiles = createHexTiles(model);
    addMouseListener(this);
    selectedHexTile = new HexTile(-1, -1, -1, model.getHexSideLength(),
            0, 0); // Initialize a default "unselected" cell

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

    // TODO!!! SOMETHING ABOUT THE CENTERING OF THESE TILES IS OFF!!!! ITS NOT AT THE
   //  CENTER OF FRAME
    int xOffset = -(getWidth() - hexRadius * model.getHexSideLength());
    int yOffset = -(getHeight() - hexRadius * model.getHexSideLength());

    for (int q = -model.getHexSideLength() + 1; q < model.getHexSideLength(); q++) {
      int r1 = Math.max(-model.getHexSideLength() + 1, -model.getHexSideLength() - q + 1);
      int r2 = Math.min(model.getHexSideLength() - 1, model.getHexSideLength() - q - 1);

      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        ReversiPiece piece = gameModel.getPieceAt(q, r, s);
        HexTile hexTile = new HexTile(q, r, s, hexRadius, xOffset, yOffset);
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

    g.setColor(Color.WHITE); // white bg
    g.fillRect(0, 0, getWidth(), getHeight());

    for (HexTile hexTile : hexTiles) {
      hexTile.draw(g); // draw all the tiles
    }
  }


  @Override
  public void mouseClicked(MouseEvent e) {
    Point pointClicked = e.getPoint();
    boolean cellClicked = false; // has a cell been clicked

    for (HexTile hexTile : hexTiles) {
      if (hexTile.containsPoint(pointClicked)) {
        cellClicked = true;
        emitTileClick(hexTile.getR(), hexTile.getQ(), hexTile.getS());
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
