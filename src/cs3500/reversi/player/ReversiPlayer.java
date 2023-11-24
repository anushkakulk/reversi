package cs3500.reversi.player;

import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.controller.PlayerActionFeatures;

/**
 * Represents the interface for a Player of Reversi.
 */
public interface ReversiPlayer {

  /**
   * This method is where the player decides what the next move for their gameplay is, where they
   * can either Pass or Move.
   *
   */
  IPlayerMove getPlayerDecision(ReadOnlyReversiModel model);

  /**
   * Returns this player's game piece.
   */
  ReversiPiece getPiece();

   /**
   *
   * @param listener
   */
   void addPlayerActionListener(PlayerActionFeatures listener);
}
