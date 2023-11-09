package view;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

import model.ReversiPiece;

/**
 * Represents a Tile for the view: this tile observes facets of a Tile for the model, and contains
 * information for rendering that information as a hexagon game tile in the view.
 */
public class HexTile {
  private final int q; // our game coordinates q
  private final int r; // our game coordinates r
  private final int s; // our game coordinates s
  private final int hexRadius; // the radius of the drawn hexagon (in pixels)
  private int x; // the x-coordinate for a hexagon's center (in pixels)
  private int y; // the y-coordinate for a hexagon's center (in pixels)
  private Color tileColor; // the color of the tile to be drawn
  private ReversiPiece piece; // the piece that's on top of every hexTile in our game
  private Polygon hexagon;

  /**
   * Creates a Hextile for the given model Tile's q r s coords.
   * @param q the Hextile's q coord
   * @param r the Hextile's r coord
   * @param s the Hextile's s coord
   * @param hexRadius the radius of thi hextile, for when it is drawn on the canvas.
   */
  public HexTile(int q, int r, int s, int hexRadius) {
    this.q = q;
    this.r = r;
    this.s = s;
    this.hexRadius = hexRadius;
    calculateHexagonCoordinates();
  }

  /**
   * Gets this Hextile's q coord.
   * @return this Hextile's q coord.
   */
  public int getQ() {
    return this.q;
  }

  /**
   * Gets this Hextile's r coord.
   * @return this Hextile's r coord
   */
  public int getR() {
    return this.r;
  }

  /**
   * Gets this Hextile's s coord.
   * @return this Hextile's s coord.
   */
  public int getS() {
    return this.s;
  }

  /**
   * Determines if this hextile contains the given point.
   * @param point a Point2D, a point on the canvas.
   * @return true if this hextile contains the given point.
   */
  public boolean containsPoint(Point2D point) {
    return this.hexagon.contains(point);
  }


  private void calculateHexagonCoordinates() {
    int x = hexToPixelQ(this.q, this.r) ;
    int y = hexToPixelR(this.r);
    this.x = x;
    this.y = y;
    this.hexagon = calculateHexagonVertices(this.x, this.y, this.hexRadius);
  }

  public void draw(Graphics g) { //191 219.5
    g.setColor(this.tileColor);
    g.fillPolygon(this.hexagon);

    if (piece != ReversiPiece.EMPTY) { // draw the piece on top
      g.setColor(piece == ReversiPiece.BLACK ? Color.BLACK : Color.WHITE);
      int pieceSize = hexRadius / 2;
      int pieceX = x - pieceSize / 2;
      int pieceY = y - pieceSize / 2;
      g.fillOval(pieceX, pieceY, pieceSize, pieceSize);
    }

    g.setColor(Color.BLACK);
    g.drawPolygon(this.hexagon);
  }

  public void setColor(Color color) {
    this.tileColor = Objects.requireNonNull(color);
  }

  public void setPiece(ReversiPiece piece) {
    this.piece = Objects.requireNonNull(piece);
  }


  private int hexToPixelQ(int q, int r) {
    return (int) (Math.sqrt(3) * hexRadius * (q + r / 2.0));
  }

  private int hexToPixelR(int r) {
    return (int) (1.5 * hexRadius * r);
  }

  private Polygon calculateHexagonVertices(int x, int y, int radius) {
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    double angle = Math.PI / 6; // 30 degrees in radians

    for (int i = 0; i < 6; i++) {
      xPoints[i] = (int) (x + radius * Math.cos(angle + i * Math.PI / 3));
      yPoints[i] = (int) (y + radius * Math.sin(angle + i * Math.PI / 3));
    }

    return new Polygon(xPoints, yPoints, 6);
  }


}