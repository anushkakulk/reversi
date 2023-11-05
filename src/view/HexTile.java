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
  private int xOffset; // Center x-coordinate of the hexagon
  private int yOffset; // Center y-coordinate of the hexagon
  private int x; // Center x-coordinate of the hexagon
  private int y; // Center y-coordinate of the hexagon
  private Color tileColor;
  private ReversiPiece piece;
  private Polygon hexagon; // Stores the vertices of the hexagon

  public HexTile(int q, int r, int s, int hexRadius, int xOffset, int yOffset) {
    this.q = q;
    this.r = r;
    this.s = s;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
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

  public void calculateHexagonCoordinates() {
    // Calculate the pixel coordinates of the hexagon based on cubic coordinates.
    int x = hexToPixelQ(this.q, this.r) + xOffset;
    int y = hexToPixelR(this.r) + yOffset;
    // Store x and y as the center of the hexagon.
    this.x = x;
    this.y = y;
    // Calculate the vertices of the hexagon based on the center coordinates.
    this.hexagon = calculateHexagonVertices(this.x, this.y, this.hexRadius);
  }

  public void draw(Graphics g) {
    g.setColor(this.tileColor);
    g.fillPolygon(this.hexagon);

    // Draw the piece (if not empty)
    if (piece != ReversiPiece.EMPTY) {
      g.setColor(piece == ReversiPiece.BLACK ? Color.BLACK : Color.WHITE);
      int pieceSize = hexRadius / 2;
      int pieceX = x - pieceSize / 2;
      int pieceY = y - pieceSize / 2;
      g.fillOval(pieceX, pieceY, pieceSize, pieceSize);
    }

    g.setColor(Color.BLACK); // Set outline color
    g.drawPolygon(this.hexagon);
  }

  public void setColor(Color color) {
    this.tileColor = Objects.requireNonNull(color);
  }

  public void setPiece(ReversiPiece piece) {
    this.piece = piece;
  }

  // Implement hexToPixelQ and hexToPixelR for your specific hexagon layout.
  // These methods will convert cubic coordinates to pixel coordinates based on your layout.
  // These examples are for a flat-topped hexagon layout.
  public int hexToPixelQ(int q, int r) {
    int x = (int) (3.0 / 2.0 * hexRadius * q);
    return x;
  }

  public int hexToPixelR(int r) {
    int y = (int) (Math.sqrt(3) * hexRadius * (r + 0.5 * q));
    return y;
  }

  // Implement calculateHexagonVertices for your specific hexagon layout.
  // This method will calculate the vertices of the hexagon based on the center coordinates.
  // These examples are for a flat-topped hexagon layout.
  public Polygon calculateHexagonVertices(int x, int y, int radius) {
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    for (int i = 0; i < 6; i++) {
      double angle = 2.0 * Math.PI * i / 6;
      xPoints[i] = (int) (x + radius * Math.cos(angle));
      yPoints[i] = (int) (y + radius * Math.sin(angle));
    }
    return new Polygon(xPoints, yPoints, 6);
  }


}
