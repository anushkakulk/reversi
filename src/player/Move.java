package player;

import model.ReversiModel;

/**
 * A Function Object that indicates moving a piece, a type of move that a player can make.
 */
public class Move implements IPlayerMove{

  private final ReversiPosn posn;

  public Move(ReversiPosn posn) {
    this.posn = posn;
  }

  public void run(ReversiModel model) {
    model.move(posn.q, posn.r, posn.s);
  }
}

