package model;

/**
 * represents a read-only model for a Reversi game,
 * only providing access to game state information, such as pieces on the board,
 * current player, and game status.
 */
public interface ReadOnlyReversiModel {
  /**
   * Returns the piece at the tile of the given coordinates in the gameboard.
   *
   * @param q the q coord of the tile.
   * @param r the r coord of the tile.
   * @param s the s coord of the tile.
   * @return the ReversiPiece at the requested position
   * @throws IllegalArgumentException if invalid coordinates.
   */
  ReversiPiece getPieceAt(int q, int r, int s) throws IllegalArgumentException;


  /**
   * Returns the piece at the given tile in the gameboard.
   *
   * @param t the given tile for which we wish to see the piece on top of it
   * @return the ReversiPiece at the requested position
   * @throws IllegalArgumentException if invalid Tile
   */
  ReversiPiece getPieceAt(Tile t) throws IllegalArgumentException;

  /**
   * Returns the side length of the hexagonal game board.
   *
   * @return the side length of the board
   */
  int getHexSideLength();

  /**
   * Return whether the game is over. The game is over when either the board is full, or
   * one player has won.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Gets the winner of the game, which is the player with the most pieces on the board.
   *
   * @return the winner of the game, or null if the game is not over.
   * @throws IllegalStateException if the game is not over.
   */
  ReversiPiece getWinner() throws IllegalStateException;


  /**
   * Gets the piece of the current player of the game, which is the player with the most pieces
   * whose turn it is.
   *
   * @return the piece corresponding to the current player.
   */
  ReversiPiece getCurrentPlayer();


  /**
   * Gets the current status of the game, which is either playing, won, or stalemate.
   *
   * @return the game status corresponding to the current status of the game.
   */
  GameStatus getGameStatus();


  /**
   * Gets the current score of the given player, which is the number of tiles that player has pieces
   *              on the board.
   * @param player the piece associated with the player whose score we want to check
   * @return the score of the given player.
   */
  int getScore(ReversiPiece player);
}
