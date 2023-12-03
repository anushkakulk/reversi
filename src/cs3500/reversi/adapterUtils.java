package cs3500.reversi;

import java.awt.geom.Point2D;

import cs3500.reversi.model.Tile;

public class adapterUtils {


  public static  Tile changeProviderCoordToTileCoord(int x, int y, int sideLength) {

    x  = x - sideLength+ 1;
    y = y - sideLength+ 1;
    int q;
    if (sideLength % 2 == 1) {
      q = (x - (y - (y & 1)) / 2);
    } else {
      q = (x - (y + (y & 1)) / 2);
    }
    int r = y;
    int s = -(q + r);
    return new Tile(q, r, s);
  }


  public static Point2D changeTileCoordToProviderCoord(int q, int r, int s, int sideLength) {
    int x;
    if (sideLength % 2 == 1) {
      x = (q + (r - (r & 1)) / 2);
    } else {
      x = (q + (r + (r & 1)) / 2);
    }
    int y = r;
    return new Point2D.Double(x + sideLength - 1, y + sideLength - 1);
  }
}
