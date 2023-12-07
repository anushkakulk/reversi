package cs3500.reversi.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.*;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents the canvas of our game, and handles any user input (either key or mouse) to the game
 * board and updates it accordingly.
 */
public class ReversiPanel extends JPanel implements IPanel {
  private final ReadOnlyReversiModel gameModel;
  private final List<PlayerActionFeatures> listeners;
  private final int hexRadius;
  private List<IHexTile> hexTiles;
  private IHexTile selectedHexTile;
  private boolean cellSelected = false;

  /**
   * Creates a ReversiPanel canvas for the game of Reversi, with a size dependent on the size of
   * the gui frame.
   *
   * @param model       the model for a game of Reversi, with only observation methods in it.
   * @param panelWidth  the width of the gui panel.
   * @param panelHeight the height of the gui panel.
   */
  public ReversiPanel(ReadOnlyReversiModel model, int panelWidth, int panelHeight) {
    setPreferredSize(new Dimension(panelWidth * 100, panelHeight * 100));
    this.gameModel = model;
    this.hexRadius = 20;
    this.update();
    addMouseListener(this);
    addKeyListener(this);
    selectedHexTile = new HexTile(-1, -1, -1, model.getHexSideLength());
    this.listeners = new ArrayList<>();
    setFocusable(true);
    requestFocusInWindow();
  }


  @Override
  public void addPlayerActionListener(PlayerActionFeatures listener) {
    this.listeners.add(Objects.requireNonNull(listener));
  }

  /**
   * Notifies listeners that the tile at the given coords was clicked by a player.
   *
   * @param q the chosen tile's q coord.
   * @param r the chosen tile's r coord.
   * @param s the chosen tile's s coord.
   */
  @Override
  public void notifyTileClicked(int q, int r, int s) {
    for (PlayerActionFeatures e : listeners) {
      e.handleTileClicked(q, r, s);
    }
  }

  @Override
  public void notifyMoveChosen(int q, int r, int s) {
    for (PlayerActionFeatures e : listeners) {
      e.handleMoveChosen(q, r, s);
    }
  }

  @Override
  public void notifyPassChosen() {
    for (PlayerActionFeatures e : listeners) {
      e.handlePassChosen();
    }
  }

  // creates a list of hextiles (the view's tiles), with one hextile corresponsing to every tile
  // in the model.
  @Override
  public void updateHextiles(ReadOnlyReversiModel model) {
    List<IHexTile> tiles = new ArrayList<>();
    //int hexRadius = 20;

    for (int q = -model.getHexSideLength() + 1; q < model.getHexSideLength(); q++) {
      int r1 = Math.max(-model.getHexSideLength() + 1, -model.getHexSideLength() - q + 1);
      int r2 = Math.min(model.getHexSideLength() - 1, model.getHexSideLength() - q - 1);

      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        ReversiPiece piece = gameModel.getPieceAt(q, r, s);
        HexTile hexTile = new HexTile(q, r, s, hexRadius);
        hexTile.setColor(Color.GRAY);
        hexTile.setPiece(piece);
        tiles.add(hexTile);
      }
    }

    this.hexTiles = tiles;
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.transform(transformLogicalToPhysical());

    for (IHexTile hexTile : hexTiles) {
      hexTile.draw(g2d); // draw all the tiles
    }
  }

  /**
   * Updates the view based on the status of the game model.
   */
  public void update() {
    updateHextiles(gameModel);
    this.repaint();
  }

  @Override
  public List<IHexTile> getHexTiles() {
   return List.copyOf(this.hexTiles);
  }

  @Override
  public void handleHintOn(int num) {

  }


  private Dimension getPreferredLogicalSize() {
    return new Dimension(1000, 1000);
  }

  /**
   * Computes the transformation that converts board coordinates
   * (with (0,0) in center, width and height our logical size)
   * into screen coordinates (with (0,0) in upper-left,
   * width and height in pixels).
   *
   * @return The necessary transformation
   */

  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();

    double scaleX = (double) getWidth() / preferred.getWidth();
    double scaleY = (double) getHeight() / preferred.getHeight();

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
   *
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
  public void mouseHelper(Point2D p) {
    boolean cellClicked = false; // has a cell been clicked

    for (IHexTile hexTile : hexTiles) {
      if (hexTile.containsPoint(p)) {
        System.out.println("omgg");
        cellClicked = true;
        notifyTileClicked(hexTile.getQ(), hexTile.getR(), hexTile.getS());
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
      // clicking outside the boundary with a cell selected deselects the cell
      cellSelected = false;
      selectedHexTile.setColor(Color.GRAY);
      repaint();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Point2D pointClicked = transformPhysicalToLogical().transform(e.getPoint(), null);
    this.mouseHelper(pointClicked);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // nothing to handle here
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // nothing to handle here
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // nothing to handle here
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // nothing to handle here
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // nothing to handle here
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (cellSelected && keyCode == KeyEvent.VK_ENTER) {
      notifyMoveChosen(selectedHexTile.getQ(), selectedHexTile.getR(), selectedHexTile.getS());
      cellSelected = false;
      selectedHexTile.setColor(Color.GRAY);
      repaint();
    } else if (keyCode == KeyEvent.VK_SPACE) {
      notifyPassChosen();
      cellSelected = false;
      selectedHexTile.setColor(Color.GRAY);
      repaint();
    }
  }


  @Override
  public void keyReleased(KeyEvent e) {
    // nothing to handle here
  }

  @Override
  public int getHexSideLength() {
    return this.gameModel.getHexSideLength();
  }

  @Override
  public JComponent getJComponent() {
    return this;
  }

  @Override
  public ReadOnlyReversiModel getModelState() {
    return this.gameModel;
  }
}