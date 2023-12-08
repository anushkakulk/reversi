package cs3500.reversi.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs3500.reversi.model.ReversiPiece;

public interface IHexTile {
  int getQ();
  int getR();
  int getS();

  boolean containsPoint(Point2D p);
  void draw(Graphics g);
  void setColor(Color color);
  void setPiece(ReversiPiece piece);

  void setWorth(int num);
}
