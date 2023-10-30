import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReversiGameModel implements ReversiModel {

  // the actual game board is a map of every coordinated tile to a reversi piece.
  private final Map<Tile, ReversiPiece> gameBoard = new HashMap<>();

  // the side length size of the game board
  // ex: a board with hexSideLength 2 means there are a total of 7 tiles, one center, and one ring
  // on hexagons around it, forming what looks like a side length of 2 for all 6 sides.
  private int hexSideLength;
  private ReversiPiece currentPlayer; // the piece of the current player.

  private int consecutivePasses = 0; // used to keep track of consecutive passes during game.


  /**
   * Creates a Reversi Game Object, which has no attributes except for the fact that the game
   * hasn't started.
   */
  public ReversiGameModel(int hexSideLength) {
    List<Tile> board = this.createBoard(hexSideLength);
    this.hexSideLength = hexSideLength;
    for (Tile t : board) {
      this.gameBoard.put(t, ReversiPiece.EMPTY); // the board starts completely empty
    }
    initStartingPositions(); // this places the players in starting position in the board
    this.currentPlayer = ReversiPiece.BLACK;
  }

  // helper method that constructs a valid hexagon board of the given hexSideLength
  private List<Tile> createBoard(int hexSideLength) throws IllegalArgumentException {
    if (hexSideLength < 2) {
      throw new IllegalArgumentException("Cannot play with a board with " +
          "side length smaller than 2");
    }
    List<Tile> board = new ArrayList<>();
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


  @Override
  public ReversiPiece getPieceAt(int q, int r, int s) throws IllegalStateException,
      IllegalArgumentException {
    validateCoordinatesInBoard(q, r, s);
    return this.gameBoard.get(new Tile(q, r, s));
  }

  @Override
  public ReversiPiece getPieceAt(Tile t) throws IllegalStateException, IllegalArgumentException {
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

  @Override
  public int getHexSideLength() {
    return this.hexSideLength;
  }

  @Override
  public void move(int q, int r, int s) {
    if (!validateCoordinatesInBoard(q, r, s)) { // make sure the given coordinates are in the board
      throw new IllegalArgumentException("Invalid Coordinates For Move");
    }
    if (getPieceAt(q, r, s) != ReversiPiece.EMPTY) { // invalid move if the tile is occupied
      throw new IllegalStateException("Tile at given coordinates is not empty.");
    }

    Tile dest = new Tile(q, r, s);
    List<Tile> allValidNeighbors = getValidNeighbors(dest); // get all neighbors of dest in board
    List<Tile> neighborsOccupiedByOtherPlayer = findNeighborsOccupiedByOpponent(allValidNeighbors,
        this.currentPlayer); // get all tiles with opponent pieces neighboring the dest

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

  // helper method that processes the sequence of tiles to validly move from the dest tile to a
  // currentPlayer's piece, and flips them at the end if valid.
  private void flipTiles(List<Tile> neighborsOccupiedByOtherPlayer, Tile dest) {
    if (!isLegalMove(neighborsOccupiedByOtherPlayer, dest, this.currentPlayer)) {
      throw new IllegalStateException("Cannot make this move");
    }

    for (int i = 0; i < neighborsOccupiedByOtherPlayer.size(); i++) {
      Tile opp = neighborsOccupiedByOtherPlayer.get(i);
      int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(), opp.getS() - dest.getS()};
      ArrayList<Tile> toBeFlipped = new ArrayList<>();
      toBeFlipped.add(opp);
      Tile nextTile = opp.addDirection(direction);

      while (handleCoordinate(nextTile)) {
        if (!processNextTile(nextTile, dest, toBeFlipped)) { // we found the end of the sequence
          break;
        }
        nextTile = nextTile.addDirection(direction);
      }
    }
  }


  private boolean processNextTile(Tile nextTile, Tile dest, ArrayList<Tile> toBeFlipped) {
    ReversiPiece nextPiece = getPieceAt(nextTile);

    if (nextPiece != currentPlayer) {
      // opponent tile in sequence, add it to be flipped
      toBeFlipped.add(nextTile);
      return handleCoordinate(nextTile);
    } else if (nextPiece == ReversiPiece.EMPTY) {
      return false;
    } else {
      flipTilesInSequence(toBeFlipped);
      gameBoard.put(dest, currentPlayer); // put down the final tile in the sequence, move is made
      return false; // no more tiles to process
    }
  }

  private void flipTilesInSequence(ArrayList<Tile> toBeFlipped) {
    for (Tile tile : toBeFlipped) {
      ReversiPiece flip = getPieceAt(tile);
      flip = (flip == ReversiPiece.BLACK) ? ReversiPiece.WHITE : ReversiPiece.BLACK;
      gameBoard.put(tile, flip); // flip them!
    }
  }


  // handles the exception thrown by validateCoordinatesInBoard, since we use the
  // getNeighbors method in the Tile class, where all 6 neighbors of a tile may or may not be in the
  // current board being played
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
      // find the direction vector from the opponent's piece respective to the dest tile.
      int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(),
          opp.getS() - dest.getS()};

      // get the tile that is in the same direction from the opponent's piece as the opponent's
      // piece is to the dest tile.
      Tile nextTile = opp.addDirection(direction);

      while (handleCoordinate(nextTile)) {
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
            ReversiPiece currentPiece = getPieceAt(tile);
            if (currentPiece == playerToCheck || currentPiece == ReversiPiece.EMPTY) {
              List<Tile> opponentNeighbors = findNeighborsOccupiedByOpponent(getValidNeighbors(tile),
                  playerToCheck);
              if (isLegalMove(opponentNeighbors, tile, playerToCheck)) {
                return false;
              }
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
    if(isGameOver()) {
      int whiteCount = (int) gameBoard.values().stream()
          .filter(piece -> piece == ReversiPiece.WHITE)
          .count();
      int blackCount = (int) gameBoard.values().stream()
          .filter(piece -> piece == ReversiPiece.BLACK)
          .count();

      if (whiteCount == blackCount) {
        return ReversiPiece.EMPTY;
      } else if (whiteCount > blackCount) {
        return ReversiPiece.WHITE;
      } else {
        return ReversiPiece.BLACK;
      }
    }
    return null;
  }
}
