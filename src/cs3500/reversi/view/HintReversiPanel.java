package cs3500.reversi.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import java.util.List;

import java.util.Optional;

import javax.swing.*;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;

public class HintReversiPanel extends JPanel implements IPanel {
  private IHexTile selectedHexTile;
  private Optional<Integer> selectedTilePoints;
  private boolean cellSelected = false;
  private final IPanel panel;
  private final static int boardHelp = 3 / 2 * 100;
  private boolean hintsOn = true;

  public HintReversiPanel(ReadOnlyReversiModel m) {
    this.panel = new cs3500.reversi.view.ReversiPanel(m,
            m.getHexSideLength() * boardHelp,
            m.getHexSideLength() * boardHelp);
  }

  public HintReversiPanel(IPanel p) {
    this.panel = p;
    this.selectedHexTile = new HexTile(-1, -1, -1, this.panel.getHexSideLength());
    setPreferredSize(new Dimension(boardHelp * 100, boardHelp * 100));
    addMouseListener(this);
    addKeyListener(this);
    setFocusable(true);
    requestFocusInWindow();
    this.panel.updateHextiles(this.panel.getModelState());
  }


  @Override
  public void addPlayerActionListener(PlayerActionFeatures listener) {
    this.panel.addPlayerActionListener(listener);
  }

  @Override
  public void notifyMoveChosen(int q, int r, int s) {
    this.panel.notifyMoveChosen(q,  r,  s);
  }

  @Override
  public void notifyPassChosen() {
    this.panel.notifyPassChosen();
  }

  @Override
  public int getHexSideLength() {
    return this.panel.getHexSideLength();
  }

  @Override
  public void update() {
    this.panel.update();
    this.repaint();

  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.transform(transformLogicalToPhysical());

    System.out.println("hellooo!");
    for (IHexTile hexTile : this.panel.getHexTiles()) {
      hexTile.draw(g2d); // draw all the tiles
    }
  }

  @Override
  public ReadOnlyReversiModel getModelState() {
    return this.panel.getModelState();
  }

  @Override
  public List<IHexTile> getHexTiles() {
    return this.panel.getHexTiles();
  }

  @Override
  public void handleHintOn(int num) {
    this.selectedTilePoints = Optional.of(num);
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (cellSelected && keyCode == KeyEvent.VK_ENTER) {
      this.panel.notifyMoveChosen(selectedHexTile.getQ(), selectedHexTile.getR(), selectedHexTile.getS());
      cellSelected = false;
      selectedHexTile.setColor(Color.GRAY);
      repaint();
    } else if (keyCode == KeyEvent.VK_SPACE) {
      this.panel.notifyPassChosen();
      cellSelected = false;
      selectedHexTile.setColor(Color.GRAY);
      repaint();
    } else if (keyCode == KeyEvent.VK_H) {
      this.hintsOn = !this.hintsOn;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    this.panel.keyReleased(e);
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
  public void mouseClicked(MouseEvent e) {
    Point2D pointClicked = transformPhysicalToLogical().transform(e.getPoint(), null);
    boolean cellClicked = false; // has a cell been clicked

    for (IHexTile hexTile : this.panel.getHexTiles()) {
      if (hexTile.containsPoint(pointClicked)) {
        cellClicked = true;
        notifyTileClicked(hexTile.getQ(), hexTile.getR(), hexTile.getS());
        if (hintsOn) {
          System.out.println("hints on");
          if (selectedTilePoints.isPresent()) {
            hexTile.setWorth(selectedTilePoints.get());
          }
        }
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

  @Override
  public JComponent getJComponent() {
    return this;
  }

  @Override
  public void notifyTileClicked(int q, int r, int s) {
    this.panel.notifyTileClicked(q,r,s);
  }

  @Override
  public void updateHextiles(ReadOnlyReversiModel model) {
    this.panel.updateHextiles(model);
  }

}