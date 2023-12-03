package cs3500.reversi.player;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents the interface for a Player of Reversi.
 */
public interface ReversiPlayer {

  /**
   * This method is where the player decides what the next move for their gameplay is, where they
   * can either Pass or Move.
   */
  IPlayerMove getPlayerDecision(ReadOnlyReversiModel model);

  /**
   * Returns this player's game piece.
   */
  ReversiPiece getPiece();

  /**
   * Adds a listener for player actions (like a move or pass being chosen) for a player.
   *
   * @param listener some PlayerActionFeatures that will listen to notifications emitted by this
   *                 player
   */
  void addPlayerActionListener(PlayerActionFeatures listener);

  void notifyPassChosen();

  void notifyMoveChosen(int q, int r, int s);
}
