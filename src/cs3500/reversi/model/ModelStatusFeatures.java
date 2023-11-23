package cs3500.reversi.model;

/**
 * Represents an event/change that occurs to the model.
 */
public interface ModelStatusFeatures {

  /**
   * Handles the model's indication that the next player to play is given.
   *
   */
  void handlePlayerChange(ReversiPiece currPlayer);


}
