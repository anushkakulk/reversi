import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReversiGameModel implements ReversiModel {

  // the actual game board is a map of every coordinated tile to a reversi piece.
  private final Map<Tile, ReversiPiece> gameBoard = new HashMap<>();
  // true if the game has started, false if not
  private boolean gameStarted;
  // the side length size of the game board
  // ex: a board with hexSideLength 2 means there are a total of 7 tiles, one center, and one ring
  // on hexagons around it, forming what looks like a side length of 2 for all 6 sides.
  private int hexSideLength;
  private ReversiPiece currentPlayer;

  /**
   * Creates a Reversi Game Object, which has no attributes except for the fact that the game
   * hasn't started.
   */
  public ReversiGameModel() {
    this.gameStarted = false;
  }

  @Override
  public List<Tile> getBoard(int hexSideLength) throws IllegalArgumentException {
    if (hexSideLength < 2) {
      throw new IllegalArgumentException("Cannot play with a board with " +
              "side length smaller than 2");
    }
    List<Tile> board = new ArrayList<>(); // Create an ArrayList
    for (int q = -hexSideLength + 1; q < hexSideLength; q++) {
      int r1 = Math.max(-hexSideLength + 1, -hexSideLength - q + 1);
      int r2 = Math.min(hexSideLength - 1, hexSideLength - q - 1);
      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        board.add(new Tile(q, r, s));
      }
    }
    return board;
  }

  @Override
  public void startGame(List<Tile> board) throws IllegalStateException, IllegalArgumentException {
    validateConditionsToStartGame(board); // check board and contents aren't null & not started yet
    this.hexSideLength = (int) ((1 + Math.sqrt(1 + 4 * (board.size() - 1) / 3)) / 2);
    validateHexagon(board); // make sure the tiles given actually form a complete hexagonic board
    for (Tile t : board) {
      this.gameBoard.put(t, ReversiPiece.EMPTY); // the board starts completely empty
    }
    initStartingPositions(); // this places the players in starting position in the board
    this.gameStarted = true;
    this.currentPlayer = ReversiPiece.BLACK;
  }

  // helper method that puts pieces in starting position: places 3 black and 3 white pieces in
  // alternating order in the inner most hexagon.
  private void initStartingPositions() {
    Tile centerTile = new Tile(0, 0, 0);
    List<Tile> neighbors = centerTile.getNeighbors();
    ReversiPiece[] alternatingPieces = {ReversiPiece.BLACK, ReversiPiece.WHITE};
    int alternatingPieceIndex = 0;
    for (Tile neighbor : neighbors) {
      this.gameBoard.put(neighbor, alternatingPieces[alternatingPieceIndex]);
      alternatingPieceIndex = 1 - alternatingPieceIndex; // this flips the init black and white
    }
  }

  // iterates through the tile lists and makes sure that only complete hexagons are given in
  // a complete hexagons is a hexagon contains all tiles with coords (q, r, s) for all q, r, s from
  // -hexSideLength + 1 to hexSideLength - 1
  private void validateHexagon(List<Tile> tiles) {
    for (int q = -this.hexSideLength + 1; q <= this.hexSideLength - 1; q++) {
      for (int r = this.hexSideLength - 1; r >= -this.hexSideLength + 1; r--) {
        for (int s = -this.hexSideLength + 1; s <= this.hexSideLength - 1; s++) {
          if (r + q + s == 0) {
            Tile tile = new Tile(q, r, s);
            if (!tiles.contains(tile)) {
              // the list of tiles doesn't contain a tile needed to form a valid board
              throw new IllegalArgumentException("Bad board given to play Reversi with");
            }
          }
        }
      }
    }
  }



  // checks for a null board, null contents, if the game started already, or invalid board size
  private void validateConditionsToStartGame(List<Tile> board) {
    if (board == null || board.contains(null) || (board.size() % 6) != 1) {
      throw new IllegalArgumentException("Cannot play game with null inputted board");
    }
    if (this.gameStarted) {
      throw new IllegalStateException("Cannot start game when game is already started");
    }
  }

  @Override
  public ReversiPiece getPieceAt(int q, int r, int s) throws IllegalStateException, IllegalArgumentException {
    checkHasGameStarted();
    validateCoordinatesInBoard(q, r, s);
    return this.gameBoard.get(new Tile(q, r, s));
  }

  @Override
  public ReversiPiece getPieceAt(Tile t) throws IllegalStateException, IllegalArgumentException {
    checkHasGameStarted();
    Objects.requireNonNull(t);
    validateCoordinatesInBoard(t.getQ(), t.getR(), t.getS());
    return this.gameBoard.get(t);
  }


  // helper method that throws the given coordinates are for a tile outside of the game board,
  // otherwise returns true, meaning the coordinates are for a tile in the game board.
  private boolean validateCoordinatesInBoard(int q, int r, int s) {
    if (q >= this.hexSideLength || r >= this.hexSideLength || s >= this.hexSideLength
            || q <= -this.hexSideLength || r <= -this.hexSideLength || s <= -this.hexSideLength) {
      throw new IllegalArgumentException("Accessing a tile out of bounds!");
    }
    return true;
  }

  protected void checkHasGameStarted() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("The game has not been started yet");
    }
  }

  @Override
  public int getHexSideLength() {
    return this.hexSideLength;
  }

  @Override
  public void move(int q, int r, int s) {
    if (!validateCoordinatesInBoard(q, r, s)) {
      return; // make sure the given coordinates are in the board even
    }

    Tile dest = new Tile(q, r, s);
    List<Tile> allValidNeighbors = new ArrayList<>();
    for (Tile t : dest.getNeighbors()) {
      try {
        if (validateCoordinatesInBoard(t.getQ(), t.getR(), t.getS())) {
          allValidNeighbors.add(t);
        }
      } catch (IllegalArgumentException e) {
        // dont do anything, that just means that neighbor isnt in the board
      }
    }

    List<Tile> neighborsOccupiedByOtherPlayer = new ArrayList<>();
    for (Tile neighbor : allValidNeighbors) {
      ReversiPiece neighborPiece = getPieceAt(neighbor);
      if (neighborPiece != ReversiPiece.EMPTY && neighborPiece != currentPlayer) {
        neighborsOccupiedByOtherPlayer.add(neighbor); // the neighbor is occupied by the opposite player
      }
    }
    if (neighborsOccupiedByOtherPlayer.isEmpty()) {
      throw new IllegalStateException("Invalid move, cannot move to given position as it is" +
              "not a legal empty cell");
    }
    for (int i = 0; i < neighborsOccupiedByOtherPlayer.size(); i++) {
      Tile opp = neighborsOccupiedByOtherPlayer.get(i); // iterate through opponents
        int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(),
                opp.getS() - dest.getS()}; // calc the direction vector from the dest to the neighbor

        ArrayList<Tile> toBeFlipped = new ArrayList<>();
        toBeFlipped.add(opp); // create a list to store tiles that need to be flipped

        Tile nextTile = opp.addDirection(direction);// calc the next tile to check in same direction

        while (validateCoordinatesInBoard(nextTile.getQ(), nextTile.getR(), nextTile.getS())) {
          // continue while the next tile is within the game board
          ReversiPiece nextPiece = getPieceAt(nextTile); // get the piece at the next tile
          if (nextPiece == ReversiPiece.EMPTY) { // ff the next tile is empty, it's an invalid move
            if (i == neighborsOccupiedByOtherPlayer.size() - 1) {
              // we've checked all options and none of them were legal, so the move cant be made
              throw new IllegalStateException("Cannot make this move");
            } else {
              // although this direction doesn't work, we have to still check the other directions
              break;
            }
          } else if (nextPiece != currentPlayer) { // if the next tile is occupied by the opponent
            toBeFlipped.add(nextTile);// add the tile to the list of tiles to be flipped
            nextTile = nextTile.addDirection(direction); // move to the next tile in same direction
          } else if (nextPiece == currentPlayer) {
            for (Tile tile : toBeFlipped) {
              // if the next tile is occupied by the current player
              // Iterate through the tiles to be flipped
              ReversiPiece flip = getPieceAt(tile);
              // Get the piece at the tile to be flipped
              // flip the piece from opponent to current player (not usre if im doing this right)
              flip = (flip == ReversiPiece.BLACK)
                      ? ReversiPiece.WHITE : ReversiPiece.BLACK;
              gameBoard.put(tile, flip); // updating the game board with the new piece at a tile
            }
            gameBoard.put(dest, currentPlayer);
            toBeFlipped.clear();
            // putting the dest on the board and then clearing bc the loop may keep going
          }
          if (toBeFlipped.isEmpty()) {
            break; //if mt that means it alr printed onto the gameboard
          }
        }

    }
    switchPlayer();
  }


  @Override
  public void switchPlayer() {
    this.currentPlayer = this.currentPlayer == ReversiPiece.BLACK ?
            ReversiPiece.WHITE : ReversiPiece.BLACK;
  }
}
