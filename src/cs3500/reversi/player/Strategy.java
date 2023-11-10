package cs3500.reversi.player;

import java.util.Optional;

import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents an Infallible Strategy for Reversi.
 */
public class Strategy {
IPlayerMoveStrategy attempt; // the fallible strategy we wish to play with

  /**
   * Creates an Infallible Strategy with a fallible strategy to follow.
   * @param stratToPlay the fallible IPlayerMoveStrategy the player wishes to play with.
   */
  public Strategy(IPlayerMoveStrategy stratToPlay) {
    this.attempt = stratToPlay;
  }

  /**
   * Returns a function object (either a pass or move object) depending on the result of finding the
   * next move determined by the fallible strategy.
   * @param board the read only model from which the strategy is detemring the next best move
   * @param who the piece the strategy is playing for.
   * @return an IPlayerMove(either a pass or move object), which the move to play next.
   */
  public IPlayerMove chooseMove(ReadOnlyReversiModel board, ReversiPiece who) {
    Optional<ReversiPosn> guess = this.attempt.playStrategy(board, who);
    if (guess.isPresent()) { return new Move(guess.get()); }
    return new Pass();
  }
}
