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

  private int consecutivePasses = 0;


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

    if (!validateCoordinatesInBoard(q, r, s)) { // make sure the given coordinates are in the board
      throw new IllegalArgumentException("Invalid Coordinates For Move");
    }
    if (getPieceAt(q, r, s) != ReversiPiece.EMPTY) { // throw if the tile is occupied
      throw new IllegalStateException("Tile at given coordinates is not empty.");
    }

    Tile dest = new Tile(q, r, s);
    List<Tile> allValidNeighbors = getValidNeighbors(dest);

    List<Tile> neighborsOccupiedByOtherPlayer = findNeighborsOccupiedByOpponent(allValidNeighbors,
            this.currentPlayer);
    if (neighborsOccupiedByOtherPlayer.isEmpty()) {
      throw new IllegalStateException("Invalid move, cannot move to given position as it is" +
              "not a legal empty cell");
    }
    flipTiles(neighborsOccupiedByOtherPlayer, dest);
    this.consecutivePasses = 0;
    switchPlayer();
  }

  private List<Tile> getValidNeighbors(Tile dest) {
    List<Tile> allValidNeighbors = new ArrayList<>();
    for (Tile t : dest.getNeighbors()) {
      if (handleCoordinate(t)) {
        allValidNeighbors.add(t); // all neighbors of the tile in the board
      }
    }
    return allValidNeighbors;
  }

  private List<Tile> findNeighborsOccupiedByOpponent(List<Tile> allValidNeighbors,
                                                     ReversiPiece currPlayer) {
    List<Tile> neighborsOccupiedByOtherPlayer = new ArrayList<>();
    for (Tile neighbor : allValidNeighbors) {
      ReversiPiece neighborPiece = getPieceAt(neighbor);
      if (neighborPiece != ReversiPiece.EMPTY && neighborPiece != currPlayer) {
        neighborsOccupiedByOtherPlayer.add(neighbor);
      }
    }
    return neighborsOccupiedByOtherPlayer;
  }

  private void flipTiles(List<Tile> neighborsOccupiedByOtherPlayer, Tile dest) {
    if (!isLegalMove(neighborsOccupiedByOtherPlayer, dest, this.currentPlayer)) {
      throw new IllegalStateException("Cannot make this move");
    }
    for (int i = 0; i < neighborsOccupiedByOtherPlayer.size(); i++) {
      Tile opp = neighborsOccupiedByOtherPlayer.get(i);
      int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(),
              opp.getS() - dest.getS()};
      ArrayList<Tile> toBeFlipped = new ArrayList<>();
      toBeFlipped.add(opp);
      Tile nextTile = opp.addDirection(direction);
      boolean validCoord = handleCoordinate(nextTile);
      while (validCoord) {
        ReversiPiece nextPiece = getPieceAt(nextTile);
        if (nextPiece != currentPlayer) { // opponent tile in sequence, add it to be flipped
          toBeFlipped.add(nextTile);
          nextTile = nextTile.addDirection(direction);
          validCoord = handleCoordinate(nextTile);
        } else {
          for (Tile tile : toBeFlipped) {
            ReversiPiece flip = getPieceAt(tile);
            flip = (flip == ReversiPiece.BLACK) ? ReversiPiece.WHITE : ReversiPiece.BLACK;
            gameBoard.put(tile, flip); // flip them!
          }
          gameBoard.put(dest, currentPlayer);
          toBeFlipped.clear();
        }
        if (toBeFlipped.isEmpty()) {
          break;
        }
      }
    }
  }


  private boolean handleCoordinate(Tile someTile) {
    boolean valid = false;
    try {
      valid = validateCoordinatesInBoard(someTile.getQ(), someTile.getR(), someTile.getS());
    } catch (IllegalArgumentException e) {
      // do nothing!
    }
    return valid;
  }

  // returns true if the move to the destination tile is legal, false if not.
  private boolean isLegalMove(List<Tile> neighborsOccupiedByOtherPlayer, Tile dest,
                              ReversiPiece playerToMove) {
    for (int i = 0; i < neighborsOccupiedByOtherPlayer.size(); i++) {
      Tile opp = neighborsOccupiedByOtherPlayer.get(i);
      int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(),
              opp.getS() - dest.getS()};

      Tile nextTile = opp.addDirection(direction);

      while (validateCoordinatesInBoard(nextTile.getQ(), nextTile.getR(), nextTile.getS())) {
        ReversiPiece nextPiece = getPieceAt(nextTile);

        if (nextPiece == ReversiPiece.EMPTY) {
          if (i == neighborsOccupiedByOtherPlayer.size() - 1) {
            return false; // no valid option in neighbors
          } else {
            break;
          }
        } else if (nextPiece != playerToMove) {
          nextTile = nextTile.addDirection(direction);
        } else {
          return true; // valid sequence, so legal move
        }
      }
    }
    return false;
  }

  @Override
  public void pass() throws IllegalStateException {
    this.consecutivePasses += 1;
    switchPlayer();
  }

  private void switchPlayer() {
    this.currentPlayer = this.currentPlayer == ReversiPiece.BLACK ?
            ReversiPiece.WHITE : ReversiPiece.BLACK;
  }

  @Override
  public boolean isGameOver() {
    checkHasGameStarted();
    return consecutivePasses >= 2 ||
            checkNoMoreMovesForOnePlayer(ReversiPiece.BLACK) ||
            checkNoMoreMovesForOnePlayer(ReversiPiece.WHITE)
            || spacesFull();
  }


  // helper that returns true if there is a legal move that could be made by the playerToCheck
  // on the board
  private boolean checkNoMoreMovesForOnePlayer(ReversiPiece playerToCheck) {
    for (int q = -this.hexSideLength + 1; q <= this.hexSideLength - 1; q++) {
      for (int r = this.hexSideLength - 1; r >= -this.hexSideLength + 1; r--) {
        for (int s = -this.hexSideLength + 1; s <= this.hexSideLength - 1; s++) {
          if (r + q + s == 0) {
            Tile tile = new Tile(q, r, s);
            List<Tile> opponentNeighbors = findNeighborsOccupiedByOpponent(getValidNeighbors(tile),
                    playerToCheck);
            if (isLegalMove(opponentNeighbors, tile, playerToCheck)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  // helper that returns true if all spaces on the board are filled.
  private boolean spacesFull() {
    for (int q = -this.hexSideLength + 1; q <= this.hexSideLength - 1; q++) {
      for (int r = this.hexSideLength - 1; r >= -this.hexSideLength + 1; r--) {
        for (int s = -this.hexSideLength + 1; s <= this.hexSideLength - 1; s++) {
          if (r + q + s == 0) {
            Tile tile = new Tile(q, r, s);
            if (gameBoard.get(tile) == ReversiPiece.EMPTY) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public ReversiPiece getWinner() {
    checkHasGameStarted();
    int whiteCount = (int) gameBoard.values().stream()
            .filter(piece -> piece == ReversiPiece.WHITE)
            .count();
    int blackCount = (int) gameBoard.values().stream()
            .filter(piece -> piece == ReversiPiece.BLACK)
            .count();

    return whiteCount > blackCount ? ReversiPiece.WHITE : ReversiPiece.BLACK;
  }
}
