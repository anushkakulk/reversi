package player;

import model.ReadOnlyReversiModel;


/**
 * Represents a strategy pattern for Reversi players.
 * Implementations of this interface define strategies for making decisions in a Reversi game,
 * such as whether to make a move or pass.
 */
public interface IPlayerMoveStrategy {
  /**
   * Gets the PlayerMove (either pass or movepiece) in the model, if deemed valid by model.
   */
  IPlayerMove playStrategy(ReadOnlyReversiModel model);

}
