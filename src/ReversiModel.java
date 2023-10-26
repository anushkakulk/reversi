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
  public List<Tile> getBoard(int size) throws IllegalArgumentException;


  /**
   * Starts a game of Reversi with the given board.
   *
   * @param board a list of tiles for the game to be played upon.
   * @throws IllegalStateException    if the game has already started.
   * @throws IllegalArgumentException if the board is null or invalid.
   */
  public void startGame(List<Tile> board) throws IllegalStateException, IllegalArgumentException;

  /**
   * Moves a game piece to the specified coordinates.
   *
   * @param q The q coordinate of the cell.
   * @param r The r coordinate of the cell.
   * @param s The s coordinate of the cell.
   * @param currentPlayer The current player (e.g., ReversiPiece.BLACK or ReversiPiece.WHITE).
   * @throws IllegalStateException    if the game has not started.
   * @throws IllegalArgumentException if the coordinates are invalid or the move is not legal.
   */
  void movePiece(int q, int r, int s) throws IllegalStateException, IllegalArgumentException;


  /**
   * Returns the piece at the tile of the given coordinates in the gameboard
   *
   * @param q the q coord of the tile.
   * @param r the r coord of the tile.
   * @param s the s coord of the tile.
   * @return the ReversiPiece at the requested position
   * @throws IllegalStateException    if the game has already started.
   * @throws IllegalArgumentException if invalid coordinates.
   */
  public ReversiPiece getPieceAt(int q, int r, int s) throws IllegalStateException,
          IllegalArgumentException;


  /**
   * Returns the piece at the given tile in the gameboard
   *
   * @param t the given tile for which we wish to see the piece on top of it
   * @return the ReversiPiece at the requested position
   * @throws IllegalStateException    if the game has already started.
   * @throws IllegalArgumentException if invalid Tile
   */
  ReversiPiece getPieceAt(Tile t) throws IllegalStateException, IllegalArgumentException;

  /**
   * Returns the side length of the hexagonal game board
   *
   * @return the side length of the board
   * @throws IllegalStateException    if the game has already started.
   */
  int getHexSideLength();
}
