package view;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

import model.ReversiPiece;

public class HexTile {
  private final int q;
  private final int r;
  private final int s;
  private final int hexRadius;
  private int x;
  private int y; // Center y-coordinate of the hexagon
  private Color tileColor;
  private ReversiPiece piece;
  private Polygon hexagon;

  public HexTile(int q, int r, int s, int hexRadius) {
    this.q = q;
    this.r = r;
    this.s = s;

    this.hexRadius = hexRadius;
    calculateHexagonCoordinates();
  }

  public int getQ() {
    return this.q;
  }

  public int getR() {
    return this.r;
  }

  public int getS() {
    return this.s;
  }

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

  public void draw(Graphics g) {
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
    this.piece = piece;
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
