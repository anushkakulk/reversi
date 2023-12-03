package cs3500.reversi.provider.model;

import java.io.Serializable;
import java.util.List;

import cs3500.reversi.provider.discs.Disc;
import cs3500.reversi.provider.discs.DiscColor;
import cs3500.reversi.provider.player.PlayerTurn;

/**
 * Represents a ReadOnlyReversiModel.
 */
public interface ReadOnlyReversiModel extends Serializable {

  // does a player have a legal moves
  // check adjacency to opposite cell color and run bfs

  /**
   * Signal if the game is over or not. A game is over if one of the players does not
   * occupy any discs, or if both players have no legal moves available,
   * or if all discs on the board are occupied. Why it belongs in the model:
   * the controller and view need to know when game is over.
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  boolean isGameOver();

  /**
   * Returns the player who made the latest move.
   *
   * @return the last player to make a move
   * @throws IllegalStateException if the game hasn't been started yet
   */
  PlayerTurn currentTurn();

  /**
   * Returns the disc at the specified coordinates.
   * Why it belongs in the model: the textview needs to know what kind of
   * disc is at a coordinate point when rendering the view.
   *
   * @param x the x coordinate of the desired disc on the game board
   * @param y the y coordinate of the desired disc on the game board.
   * @return the desired disc
   * @throws IllegalStateException    if the game hasn't started yet
   * @throws IllegalArgumentException if the int x,y is invalid
   */
  Disc getDiscAt(int x, int y);

  /**
   * Returns whether the disc at the specified coordinates is flipped or not.
   * Why it belongs in the model: the textview needs to know if a
   * disc is flipped when rendering.
   *
   * @param x the x coordinate of the desired disc on the game board.
   *        Must be a non-negative integer within the valid range.
   * @param y the y coordinate of the desired disc on the game board.
   *        Must be a non-negative integer within the valid range.
   * @return true if the disc at the given position is flipped, false if not.
   *        A faced down disc is not flipped.
   * @throws IllegalStateException    if the game hasn't been started yet.
   * @throws IllegalArgumentException if the coordinates are invalid.
   *        Invalid coordinates are outside the bounds of the game board.
   */
  boolean isDiscFlipped(int x, int y);

  /**
   * returns a grids dimensions.
   * Why it belongs in the model: the textview needs to know
   * the dimensions for creating a grid.
   *
   * @return an integer specifying the dimensions.
   */
  int getDimensions();

  /**
   * returns a current game state.
   * Why it belongs in the model: the controller needs to know what to
   * output to the text view based on a certain game state
   *
   * @throws IllegalStateException if game hasn't started;
   */
  GameState getCurrentGameState();

  /**
   * Gets a copy of the current state of the game board in play.
   *
   * @return a copy of the mutable Reversi game board
   * @throws IllegalStateException if the game has not started yet
   */
  Disc[][] getCurrentBoardState();

  boolean canPlayerPlay(int x, int y);

  int getScore(PlayerTurn playerTurn);

  boolean doesPlayerHaveLegalMove();

  String getType();

  PlayerTurn getOpponent(PlayerTurn player);

  DiscColor getPlayerColor(PlayerTurn player);

  boolean checkValidCoordinates(int x, int y);

  List<ReadOnlyReversiModel> getGameStates();

  List<List<Integer>> getMoves();

}
