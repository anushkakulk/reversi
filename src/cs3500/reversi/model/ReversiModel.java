package cs3500.reversi.model;

import cs3500.reversi.controller.ModelStatusFeatures;

/**
 * Represents the primary model interface for playing a game of Reversi.
 */
public interface ReversiModel extends ReadOnlyReversiModel, IModel {

  /**
   * makes a move on behalf of the current player to the given coordinates if it is a valid move.
   *
   * @param r r coord of destination hexagon
   * @param q q coord of destination hexagon
   * @param s s coord of destination hexagon
   * @throws IllegalStateException    if game is over
   * @throws IllegalStateException    if the move is invalid (meaning either the dest tile is empty,
   *                                  or if the disc being played is adjacent (in at least one
   *                                  direction) to a straight line of the opponent player’s discs,
   *                                  at the far end of which is another disc of the current player.
   * @throws IllegalArgumentException if the coordinates are invalid (not in the board).
   */
  void move(int q, int r, int s) throws IllegalStateException,
      IllegalArgumentException;


  /**
   * switches to the next player's turn.
   */
  void pass();

  /**
   * Begins the game of Reversi. no plays can occur unless this method is called at the beginning.
   */
  void startGame();


  /**
   * Adds a listener for model status changes.
   *
   * @param listener a modelStatusFeatures listener, that is waiting for
   *                 notifications from the model.
   */
  void addModelStatusListener(ModelStatusFeatures listener);

}