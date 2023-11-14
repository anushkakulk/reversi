package cs3500.reversi.player;

import java.util.Objects;

import cs3500.reversi.model.ReversiModel;

/**
 * A Function Object that indicates Passing, a type of move that a player can make.
 */
public class Pass implements IPlayerMove {

  /**
   * Creates a Pass Object.
   */
  public Pass() {
    // no fields in a pass, its just an indication to pass.
  }

  @Override
  public void run(ReversiModel model) {
    model.pass();
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }


  @Override
  public boolean equals(Object obj) {
    return obj instanceof Pass;
  }

}