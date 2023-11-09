package player;

import model.ReversiModel;

/**
 * A Function Object that indicates Passing, a type of move that a player can make.
 */
public class Pass implements IPlayerMove {

  /**
   * Creates a Pass Object.
   */
  public Pass() {

  }

  @Override
  public void run(ReversiModel model) {
    model.pass();
  }

}