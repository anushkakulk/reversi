package cs3500.reversi.provider.model;

import cs3500.reversi.provider.controller.Listener;

/**
 * Represents a ReversiModel.
 */
public interface ReversiModel extends ReadOnlyReversiModel {

  /**
   * Starts a new game of Reversi.
   * Reason why it belongs in the model: the player needs an entry point to
   * specify what kind of custom board size they want to make and
   * also the model needs to know when a game has been started.
   *
   * @param boardSize the square nxn dimension to create the board with
   * @throws IllegalArgumentException If the given board size dimension is invalid
   * @throws IllegalStateException    if the game has already started
   */
  void startGame(int boardSize);

  /**
   * Makes a move on the Reversi board by placing a disc on the specified disc.
   * This method will place the current player's disc on the given row and column.
   * If the move is successful, it will flip any of the opponent's discs that
   * are surrounded in straight lines (horizontally, vertically, or
   * diagonally) between the newly placed disc and
   * another disc of the current player.Why it belongs in the model:
   * this method belongs in the model because the person needs an entry
   * point into the model for making a move.
   *
   * @param x the coordinate of the desired disc to make a move to
   * @param y the coordinate of the desired disc to make a move to
   * @throws IllegalArgumentException If the attempted move is not allowable
   * @throws IllegalStateException    if the game hasn't been started yet
   */
  void makeMove(int x, int y);

  /**
   * Gives up the current player's turn by toggling between playerTurns.
   * Why it belongs in the model: the controller needs an entry point into the
   * model to perform a pass
   *
   * @throws IllegalStateException if the game hasn't started yet
   */
  void pass();

  void addListener(Listener l);
}