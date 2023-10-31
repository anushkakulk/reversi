package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReversiGameModel implements ReversiModel {

  // the actual game board is a map of every coordinated tile to a reversi piece.
  // we chose this representation for gameBoard as the pieces at a certain position are constantly
  // changing as the game gets played, so corresponding each tile to a piece makes it simple to both
  // update the piece at a position, query the tile for what piece its on it, and could be updated
  // if we have any changes to ReversiPiece.
  private final Map<Tile, ReversiPiece> gameBoard = new HashMap<>();
  // INVARIANT: All tile coordinates (q, r, s) in the gameBoard must be within the allowed range
  // determined by the hexagonal grid's side length (hexSideLength) and must satisfy the hexagonal
  // grid constraints.


  // ex: a board with hexSideLength 2 means there are a total of 7 tiles, one center, and one ring
  // on hexagons around it, forming what looks like a side length of 2 for all 6 sides.
  // INVARIANT: hexSideLength is greater than or equal to 2
  private final int hexSideLength; // the side length size of the game board

  // gets switched to the other piece after every move/pass
  private ReversiPiece currentPlayer; // the piece of the current player.

  // INVARIANT: consecutivePasses is between 0 and 2 (inclusive).
  private int consecutivePasses = 0; // used to keep track of consecutive passes during game.
  private GameStatus gameStatus; // the status of the game: either PLAYED, WON, or STALEMATE


  /**
   * Creates a Reversi Game Object, which has no attributes except for the fact that the game
   * hasn't started.
   */
  public ReversiGameModel(int hexSideLength) {
    // here, the invariant (hexSideLength is greater than or equal to 2) is upheld, since we throw
    // error if hexSideLength < 2 in createBoard, therefore never instantiating this.hexSideLength
    // to something < 2.
    List<Tile> board = this.createBoard(hexSideLength);
    this.hexSideLength = hexSideLength;
    for (Tile t : board) {
      this.gameBoard.put(t, ReversiPiece.EMPTY); // the board starts completely empty
    }
    initStartingPositions(); // this places the players in starting position in the board
    this.currentPlayer = ReversiPiece.BLACK;
    this.gameStatus = GameStatus.PLAYING;
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
        // here, the invariant about tile creation is upheld. we index q from -hexSideLength + 1
        // to hexSideLength - 1, and calculate r and s such that q + r + s = 0. This ensures we
        // always only create Tiles for our board where all the coordinates add up to 0 and each
        // coordinate is a valid cubic coordinate that appears in the cubic coordinate system of
        // a hexagon board of sideLength given.
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

    if (getPieceAt(q, r, s) != ReversiPiece.EMPTY) {
      // INVARIANT: A move must be made to an empty tile.
      throw new IllegalStateException("Tile at given coordinates is not empty.");
    }
    if (checkNoMoreMovesForOnePlayer(this.getCurrentPlayer())) {
      pass(); // If a player has no legal moves, they are required to pass
    }
    Tile dest = new Tile(q, r, s);
    List<Tile> allValidNeighbors = getValidNeighbors(dest); // get all neighbors of dest in board
    List<Tile> neighborsOccupiedByOtherPlayer = findNeighborsOccupiedByOpponent(allValidNeighbors,
            this.getCurrentPlayer()); // get all tiles with opponent pieces neighboring the dest
    if (neighborsOccupiedByOtherPlayer.isEmpty()) {
      throw new IllegalStateException("Invalid move, cannot move to given position as it is" +
              "not a legal empty cell");
    }
    flipTiles(neighborsOccupiedByOtherPlayer, dest);
    this.consecutivePasses = 0;
    updateStatusIfGameOver();
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
    if (!isLegalMove(neighborsOccupiedByOtherPlayer, dest, this.getCurrentPlayer())) {
      throw new IllegalStateException("Cannot make this move");
    }
    for (Tile opp : neighborsOccupiedByOtherPlayer) {
      int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(),
              opp.getS() - dest.getS()};
      ArrayList<Tile> toBeFlipped = new ArrayList<>();
      toBeFlipped.add(opp);
      // add the direction vector to find the next tile. the next tile is in the same direction as
      // the neighboring tile with the opponent is as the neighboring tile with the opponent is
      // to the destination tile.
      Tile nextTile = opp.addDirection(direction);

      while (handleCoordinate(nextTile)) {
        if (!processNextTile(nextTile, dest, toBeFlipped)) { // we found the end of the sequence
          break;
        }
        nextTile = nextTile.addDirection(direction);
      }
    }
  }


  // processNextTile is called on nextTile, which is the tile moving from the dest tile to the
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
    updateStatusIfGameOver();
    // INVARIANT: The current player must switch between ReversiPiece.BLACK and ReversiPiece.WHITE
    // after each valid move or pass.
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
    for (int q = -hexSideLength + 1; q < hexSideLength; q++) {
      int r1 = Math.max(-hexSideLength + 1, -hexSideLength - q + 1);
      int r2 = Math.min(hexSideLength - 1, hexSideLength - q - 1);
      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        Tile tile = new Tile(q, r, s);
        ReversiPiece currentPiece = getPieceAt(tile);
        if (currentPiece == playerToCheck || currentPiece == ReversiPiece.EMPTY) {
          List<Tile> opponentNeighbors =
                  findNeighborsOccupiedByOpponent(getValidNeighbors(tile), playerToCheck);
          if (isLegalMove(opponentNeighbors, tile, playerToCheck)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  // helper that returns true if all spaces on the board are filled.
  private boolean spacesFull() {
    for (int q = -hexSideLength + 1; q < hexSideLength; q++) {
      int r1 = Math.max(-hexSideLength + 1, -hexSideLength - q + 1);
      int r2 = Math.min(hexSideLength - 1, hexSideLength - q - 1);
      for (int r = r1; r <= r2; r++) {
        int s = -q - r;
        Tile tile = new Tile(q, r, s);
        if (gameBoard.get(tile) == ReversiPiece.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public ReversiPiece getWinner() {
    if (isGameOver()) {
      int whiteCount = (int) gameBoard.values().stream()
              .filter(piece -> piece == ReversiPiece.WHITE)
              .count();
      int blackCount = (int) gameBoard.values().stream()
              .filter(piece -> piece == ReversiPiece.BLACK)
              .count();

      if (whiteCount == blackCount) {
        return ReversiPiece.EMPTY; // this means no winner, its a tie
      } else if (whiteCount > blackCount) {
        return ReversiPiece.WHITE;
      } else {
        return ReversiPiece.BLACK;
      }
    }
    throw new IllegalStateException("Game is still being played");
  }

  private void updateStatusIfGameOver() {
    if (this.isGameOver()) {
      if (this.getWinner() == ReversiPiece.EMPTY) {
        this.gameStatus = GameStatus.STALEMATE;
      } else {
        this.gameStatus = GameStatus.WON;
      }
    }
  }

  @Override
  public ReversiPiece getCurrentPlayer() {
    return this.currentPlayer;
  }

  @Override
  public GameStatus getGameStatus() {
    return this.gameStatus;
  }
}
