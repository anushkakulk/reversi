package cs3500.reversi.controller;

/**
 * Represents something that emits notifications about player actions for Reversi.
 */
public interface IEmitPlayerActions {

  /**
   * Notifies listeners that a move was chosen by a player to the tile at the given coords.
   * @param q the chosen tile's q coord.
   * @param r the chosen tile's r coord.
   * @param s the chosen tile's s coord.
   */
  void notifyMoveChosen(int q, int r, int s);


  /**
   * Notifies listeners that a pass was chosen by a player.
   */
  void notifyPassChosen();
}
