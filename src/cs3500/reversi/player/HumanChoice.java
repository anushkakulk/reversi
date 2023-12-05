package cs3500.reversi.player;


/**
 * Represents the another type of PlayerMove, which represents the move of letting the human
 * player decide the next move.
 */
public class HumanChoice implements IPlayerMove {

  public HumanChoice() {
    // represents a human move;
  }

  @Override
  public void notifyPlayer(ReversiPlayer p) {
    // do nothing since this human player shouldn't notify the controller, the view should notify
  }
}
