package cs3500.reversi.player;

import cs3500.reversi.model.ReversiModel;

/**
 * This interface is used to create union data. Represents the Player Moves that are
 * available in the reversi game.
 */
public interface IPlayerMove {

  /**
   * Executes the Player Move on the model.
   * @param model the actual reversi model we wish to execute the move on (given to an IPlayerMove
   *              via the controller)
   */
  void run(ReversiModel model);
}
