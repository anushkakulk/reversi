import java.util.List;

/**
 * Represents the primary model interface for playing a game of Reversi.
 */
public interface ReversiModel {

  /**
   * Return a valid and complete board of Tiles to play a game of Reversi.
   *
   * @throws IllegalArgumentException if the size is invalid.
   * @returns a usable game board for playing a game of Reversi.
   */
  List<Tile> getBoard(int size) throws IllegalArgumentException;


  /**
   * Starts a game of Reversi with the given board.
   *
   * @param board a list of tiles for the game to be played upon.
   * @throws IllegalStateException    if the game has already started.
   * @throws IllegalArgumentException if the board is null or invalid.
   */
  void startGame(List<Tile> board) throws IllegalStateException, IllegalArgumentException;


  /**
   * Returns the piece at the tile of the given coordinates in the gameboard
   *
   * @param q the q coord of the tile.
   * @param r the r coord of the tile.
   * @param s the s coord of the tile.
   * @return the ReversiPiece at the requested position
   * @throws IllegalStateException    if the game has not yet started
   * @throws IllegalArgumentException if invalid coordinates.
   */
  ReversiPiece getPieceAt(int q, int r, int s) throws IllegalStateException,
          IllegalArgumentException;


  /**
   * Returns the piece at the given tile in the gameboard
   *
   * @param t the given tile for which we wish to see the piece on top of it
   * @return the ReversiPiece at the requested position
   * @throws IllegalStateException    if the game has not yet started
   * @throws IllegalArgumentException if invalid Tile
   */
  ReversiPiece getPieceAt(Tile t) throws IllegalStateException, IllegalArgumentException;

  /**
   * Returns the side length of the hexagonal game board
   *
   * @return the side length of the board
   * @throws IllegalStateException if the game has not yet started
   */
  int getHexSideLength() throws IllegalStateException;


  /**
   * makes a move on behalf of the current player to the given coordinates if it is a valid move.
   *
   * @param r r coord of destination hexagon
   * @param q q coord of destination hexagon
   * @param s s coord of destination hexagon
   * @throws IllegalStateException if the game has not yet started, or if the move is invalid.
   * @throws IllegalArgumentException if the coordinates are invalid.
   */
  void move(int q, int r, int s) throws IllegalStateException,
          IllegalArgumentException;;



  /**
   * switches to the next player's turn.
   *
   * @throws IllegalStateException if the game has not yet started.
   */
  void pass() throws IllegalStateException;


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
     * @throws IllegalStateException if the game has not started.
     */
  ReversiPiece getWinner();

}
