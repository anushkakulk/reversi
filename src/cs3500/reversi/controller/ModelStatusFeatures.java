package cs3500.reversi.controller;

import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.provider.controller.Listener;

/**
 * Represents an event/change that occurs to the model.
 */
public interface ModelStatusFeatures extends Listener {

  /**
   * Handles the model's indication that the next player to play is given.
   */
  void handlePlayerChange(ReversiPiece currPlayer);


  /**
   * Handles the model's notification that the game is over.
   */
  void handleGameOver();

}
