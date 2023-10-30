import java.util.List;

/**
 * Represents the primary model interface for playing a game of Reversi.
 */
public interface ReversiModel {


  /**
   * Returns the piece at the tile of the given coordinates in the gameboard
   *
   * @param q the q coord of the tile.
   * @param r the r coord of the tile.
   * @param s the s coord of the tile.
   * @return the ReversiPiece at the requested position
   * @throws IllegalArgumentException if invalid coordinates.
   */
  ReversiPiece getPieceAt(int q, int r, int s) throws IllegalArgumentException;


  /**
   * Returns the piece at the given tile in the gameboard
   *
   * @param t the given tile for which we wish to see the piece on top of it
   * @return the ReversiPiece at the requested position
   * @throws IllegalArgumentException if invalid Tile
   */
  ReversiPiece getPieceAt(Tile t) throws IllegalArgumentException;

  /**
   * Returns the side length of the hexagonal game board
   *
   * @return the side length of the board
   */
  int getHexSideLength();


  /**
   * makes a move on behalf of the current player to the given coordinates if it is a valid move.
   *
   * @param r r coord of destination hexagon
   * @param q q coord of destination hexagon
   * @param s s coord of destination hexagon
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
   */
  ReversiPiece getWinner();
}