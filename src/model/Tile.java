package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Tile with cubic coordinates, where multiple tiles come
 * together to form a game board for Reversi.
 */
public class Tile {
  private final int q;
  private final int r;
  private final int s;

  /**
   * Constructs a Tile object with the given coordinates.
   *
   * @param q the q coordinate.
   * @param r the r coordinate.
   * @param s the s coordinate.
   */
  public Tile(int q, int r, int s) {
    // a tile uses cube coordinates
    this.q = q;
    this.r = r;
    this.s = s;
    if (q + r + s != 0) {
      throw new IllegalArgumentException("Bad cube coordinates. q+r+s must equal 0.");
    }
  }

  /**
   * Gets the q coord.
   *
   * @return the integer q coordinate.
   */
  public int getQ() {
    return q;
  }

  /**
   * Gets the r coord.
   *
   * @return the integer r coordinate.
   */
  public int getR() {
    return r;
  }

  /**
   * Gets the s coord.
   *
   * @return the integer s coordinate.
   */
  public int getS() {
    return s;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Tile)) {
      return false;
    }
    Tile tile = (Tile) o;
    return (this.q == tile.getQ()
            && this.r == tile.getR()
            && this.s == tile.getS());
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, r, s);
  }

  /**
   * Returns all neighboring tiles of this tile.
   *
   * @return A list of tiles that have the coordinates of one of each of the 6
   *                              neighbors to this tile.
   */
  List<Tile> getNeighbors() {
    int[][] cubeDirectionVectors = {
            {+1, 0, -1}, {+1, -1, 0}, {0, -1, +1},
            {-1, 0, +1}, {-1, +1, 0}, {0, +1, -1}
    };

    List<Tile> neighbors = new ArrayList<>();

    for (int i = 0; i < cubeDirectionVectors.length; i++) {
      int[] direction = cubeDirectionVectors[i];
      int neighborQ = this.q + direction[0];
      int neighborR = this.r + direction[1];
      int neighborS = this.s + direction[2];
      neighbors.add(new Tile(neighborQ, neighborR, neighborS));
    }

    return neighbors;
  }


  /**
   * gets the tile in a specified direction on a hexagonal grid from this tile.
   *
   * @param direction the array holding a direction vector for  [q, r, s]
   * @return the tile in a specified direction on a hexagonal grid from this.
   */
  Tile addDirection(int... direction) {
    return new Tile(this.q + direction[0], this.r + direction[1],
            this.s + direction[2]);
  }

}
