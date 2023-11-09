package player;

import model.ReversiModel;

/**
 * A Function Object that indicates moving a piece, a type of move that a player can make.
 */
public class Move implements IPlayerMove{
  private final ReversiPosn posn;

  /**
   * Creates a move function object.
   * @param posn the position to move to.
   */
  public Move(ReversiPosn posn) {
    this.posn = posn;
  }

  @Override
  public void run(ReversiModel model) {
    model.move(posn.q, posn.r, posn.s);
  }
}

