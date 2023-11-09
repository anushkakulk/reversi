package player;

import model.ReadOnlyReversiModel;
import model.ReversiModel;

/**
 * Represents the interface for a Player of Reversi.
 */
public interface ReversiPlayer {

  /**
   * This method is where the player decides what the next move for their gameplay is, where they
   * can either Pass or Move.
   *
   * @return IPlayerMove, which is either a Pass or a Move function object.
   */
  IPlayerMove getPlayerDecision(ReadOnlyReversiModel model);

}
