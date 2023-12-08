package cs3500.reversi.view;


import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;
import java.util.Optional;

import cs3500.reversi.model.ReversiPiece;

/**
 * Represents a Tile for the view: this tile observes facets of a Tile for the model, and contains
 * information for rendering that information as a hexagon game tile in the view.
 */
public class HexTile implements IHexTile {
  private final int q; // our game coordinates q
  private final int r; // our game coordinates r
  private final int s; // our game coordinates s
  private Optional<Integer> worth = Optional.empty();
  protected final int hexRadius; // the radius of the drawn hexagon (in pixels)
  protected int x; // the x-coordinate for a hexagon's center (in pixels)
  protected int y; // the y-coordinate for a hexagon's center (in pixels)
  protected Color tileColor; // the color of the tile to be drawn
  protected ReversiPiece piece; // the piece that's on top of every hexTile in our game
  protected Polygon hexagon;

  /**
   * Creates a Hextile for the given model Tile's q r s coords.
   *
   * @param q         the Hextile's q coord
   * @param r         the Hextile's r coord
   * @param s         the Hextile's s coord
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
   *
   * @return this Hextile's q coord.
   */
  public int getQ() {
    return this.q;
  }

  /**
   * Gets this Hextile's r coord.
   *
   * @return this Hextile's r coord
   */
  public int getR() {
    return this.r;
  }

  /**
   * Gets this Hextile's s coord.
   *
   * @return this Hextile's s coord.
   */
  public int getS() {
    return this.s;
  }

  /**
   * Determines if this hextile contains the given point.
   *
   * @param point a Point2D, a point on the canvas.
   * @return true if this hextile contains the given point.
   */
  public boolean containsPoint(Point2D point) {
    return this.hexagon.contains(point);
  }

  /**
   * Calculates the pixel equivalent (x,y) coordinates  of the tile's (q,r,s) coordinates and
   * creates the polygon shape using the pixel coordinates.
   */
  private void calculateHexagonCoordinates() {
    int x = hexToPixelX(this.q, this.r);
    int y = hexToPixelY(this.r);
    this.x = x;
    this.y = y;
    this.hexagon = calculateHexagonVertices(this.x, this.y, this.hexRadius);
  }


  /**
   * Sets this hexTile's color (for when it is rendered).
   *
   * @param color the color the hexTile should be filled with when it's drawn.
   */
  public void setColor(Color color) {
    this.tileColor = Objects.requireNonNull(color);
  }

  /**
   * Sets this hexTile's piece, which sits on top of the hexTile (for when it is rendered).
   *
   * @param piece the piece that sits on top of the hexTile that will be drawn.
   */
  public void setPiece(ReversiPiece piece) {
    this.piece = Objects.requireNonNull(piece);
  }

  @Override
  public void setWorth(int num) {
    this.worth = Optional.of(num);
  }



  /**
   * Renders this HexTile.
   *
   * @param g the graphic object to draw on top of.
   */
  public void draw(Graphics g) {
    g.setColor(this.tileColor);
    g.fillPolygon(this.hexagon);

    if (piece != ReversiPiece.EMPTY) {
      g.setColor(piece == ReversiPiece.BLACK ? Color.BLACK : Color.WHITE);
      int pieceSize = hexRadius / 2;
      int pieceX = x - pieceSize / 2;
      int pieceY = y - pieceSize / 2;
      g.fillOval(pieceX, pieceY, pieceSize, pieceSize);
    }

    g.setColor(Color.BLACK);
    g.drawPolygon(this.hexagon);

    if (this.tileColor == Color.CYAN && piece == ReversiPiece.EMPTY && worth.isPresent()) {
      Graphics2D g2d = (Graphics2D) g.create(); // copy the graphic objgect
      g2d.setColor(Color.BLACK);
      Font font = new Font("Arial", Font.PLAIN, 20);
      g2d.setFont(font);

      int textX = x - hexRadius / 2;
      int textY = y - hexRadius / 2;

      // flip it overrrrr
      g2d.scale(1, -1);


      g2d.drawString(String.valueOf(worth.get()), textX, -textY);

      g2d.dispose();
    }
    this.worth = Optional.empty();
  }





  // helper method that translates our game's coordinates of this hextile
  // into the screen's x coordinate equivalent.
  private int hexToPixelX(int q, int r) {
    return (int) (Math.sqrt(3) * hexRadius * (q + r / 2.0));
  }

  // helper method that translates our game's coordinates of this hextile
  // into the screen's y coordinate equivalent.
  private int hexToPixelY(int r) {
    return (int) -(1.5 * hexRadius * r);
  }

  // creates a hexagon polygon shape dependent on the x y coordinates and of a certain size.
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

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof HexTile)) {
      return false;
    }
    HexTile hexTile = (HexTile) obj;
    return q == hexTile.q && r == hexTile.r && s == hexTile.s &&
        hexRadius == hexTile.hexRadius && x == hexTile.x && y == hexTile.y &&
        tileColor.equals(hexTile.tileColor) && piece == hexTile.piece &&
        hexagon.equals(hexTile.hexagon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, r, s, hexRadius, x, y, tileColor, piece, hexagon);
  }

}