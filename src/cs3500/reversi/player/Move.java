package cs3500.reversi.player;

import java.util.Objects;


/**
 * A Function Object that indicates moving a piece, a type of move that a player can make.
 */
public class Move implements IPlayerMove {
  private final ReversiPosn posn;

  /**
   * Creates a move function object.
   *
   * @param posn the position to move to.
   */
  public Move(ReversiPosn posn) {
    this.posn = posn;
  }

  @Override
  public void notifyPlayer(Player p) {
    p.notifyMoveChosen(posn.q, posn.r, posn.s);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.posn);
  }


  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Move)) {
      return false;
    }
    Move move = (Move) obj;
    return this.posn.equals(move.posn);
  }
}

