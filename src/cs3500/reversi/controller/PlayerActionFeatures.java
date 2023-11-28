package cs3500.reversi.controller;

/**
 * Represents any event that occurs on the canvas.
 */
public interface PlayerActionFeatures {
  /**
   * does something when the event is that the tile at given coordinates was clicked.
   * @param xCoord - the clicked tile's xCoord.
   * @param rCoord - the clicked tile's rCoord.
   * @param sCoord - the clicked tile's sCoord.
   */
  void handleTileClicked(int xCoord, int rCoord, int sCoord);

  /**
   * handles the event that the next move is a move to the tile with that coord was chosen.
   * @param xCoord - the chosen tile's xCoord.
   * @param rCoord - the chosen tile's rCoord.
   * @param sCoord - the chosen tile's sCoord.
   */
  void handleMoveChosen(int xCoord, int rCoord, int sCoord);

  /**
   * handles the event that a pass was chosen as the next move.
   */
  void handlePassChosen();


}