package player;

/**
 * A Function Object that indicates moving a piece, a type of move that a player can make.
 */
public class MovePiece implements IPlayerMove {
  // made these fields  public final, so that the controller could just do MovePiece.q, .r, and .s
  // to easily access their fields, rather than using getters. Since its final, the field are never
  // mutated.
  public final int q;
  public final int r;
  public final int s;

  /**
   * creates a movePiece Object, where the given coordinates are the coordinates of the destination
   * tile that the player wants to move to.
   *
   * @param q - the q coord of dest tile
   * @param r - the r coord of dest tile
   * @param s - the s coord of dest tile
   */
  public MovePiece(int q, int r, int s) {
    this.q = q;
    this.r = r;
    this.s = s;
  }

}
