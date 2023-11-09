package player;

import java.util.Map;
import java.util.Optional;

import model.ReadOnlyReversiModel;
import model.ReversiPiece;


/**
 * Represents a strategy pattern for Reversi players.
 * Implementations of this interface define strategies for making decisions in a Reversi game,
 * like where to move a piece to.
 */
public interface IPlayerMoveStrategy {

  /**
   * Gets the ReversiPosn for a move in the model, if deemed valid by model.
   * @param model the model from which the strategy is determining what the next move is.
   * @param piece the piece the strategy is playing for.
   * @return a ReversiPosn, the best destination the given player's piece should move to,
   * ONLY IF the strategy finds at least one valid move for.
   */
  Optional<ReversiPosn> playStrategy(ReadOnlyReversiModel model, ReversiPiece piece);

}
