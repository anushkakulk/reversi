import java.util.ArrayList;
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
  // ex: a board with hexSideLength 6 means there are
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
        //System.out.println("q: " + q + " r: " + r + " s: "+ s);
        // TODO MAKE SURE YOU UNDERSTAND THE ORDER THIS MAKES TILES IN FOR TESTING
        board.add(new Tile(q, r, s));
      }
    }
    return board;
  }

  @Override
  public void startGame(List<Tile> board) throws IllegalStateException, IllegalArgumentException {
    validateConditionsToStartGame(board); // check board and contents aren't null & not started yet
    validateHexagon(board); // make sure the tiles given actually form a complete hexagonic board
    for (Tile t : board) {
      this.gameBoard.put(t, ReversiPiece.EMPTY);
    }
    initStartingPositions();
    this.hexSideLength = (int) Math.round((Math.sqrt(4 * board.size() + 1) + 1) / 3);
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
  public void validateHexagon(List<Tile> tiles) {
    for (int r = -this.hexSideLength + 1; r <= this.hexSideLength - 1; r++) {
      for (int q = -this.hexSideLength + 1; q <= this.hexSideLength - 1; q++) {
        int s = -r - q;

        if (r + q + s == 0) {
          Tile tile = new Tile(q, r, s);
          if (!tiles.contains(tile)) {
            // the list of tiles doesn't contain a tile needed to form a valid board
            throw new IllegalArgumentException("bad board given to play Reversi with");
          }
        }
      }
    }
  }


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

  @Override
  public void switchPlayer() {
    this.currentPlayer = this.currentPlayer == ReversiPiece.BLACK ? ReversiPiece.WHITE : ReversiPiece.BLACK;
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started yet");
    }

    // isValidMove for all of the outterpieces on the board
    //if both players take a pass
    //if one player has no pieces left
    return false;
  }

  @Override
  public ReversiPiece getWinner() {
    // TODO IMPLEMENT THIS
    return ReversiPiece.EMPTY;
  }


  // helper method that throws the given coordinates are for a tile outside of the game board
  //!!changed this to return a boolean
  private boolean validateCoordinatesInBoard(int q, int r, int s) {
    if (q >= this.hexSideLength || r >= this.hexSideLength || s >= this.hexSideLength
        /*|| q <= -this.hexSideLength || r <= -this.hexSideLength || s <= -this.hexSideLength*/) {
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

  private boolean isLegalMove(int q, int r, int s, ReversiPiece currentPlayer) {
    // define the six neighbor directions
    int[][] directions = {
        {+1, 0, -1}, {+1, -1, 0}, {0, -1, +1},
        {-1, 0, +1}, {-1, +1, 0}, {0, +1, -1}
    };

    // check each direction for potential captures
    for (int[] direction : directions) {
      int dirQ = direction[0];
      int dirR = direction[1];
      int dirS = direction[2];

      // asssign a current flip count to see if there is any to flip
      int flipCount = 0;

      // increment in the specific direction
      int currentQ = q + dirQ;
      int currentR = r + dirR;
      int currentS = s + dirS;

      if (!validateCoordinatesInBoard(currentQ, currentR, currentS)) {
        continue;
      }

      // check if the next cell in this direction belongs to the opponent/MT/current player
      while (validateCoordinatesInBoard(currentQ, currentR, currentS)) {
        ReversiPiece piece = getPieceAt(currentQ, currentR, currentS);

        if (piece == ReversiPiece.EMPTY || piece == null) {
          // found an empty cell before reaching the current player's piece
          break;
        }

        if (piece == currentPlayer) {
          // we found the current players piece, and its a legal move if we have flipped
          // opponent pieces in this direction
          if (flipCount > 0) {
            return true;
          } else {
            break;
          }
        } else {
          // this cell belongs to the opponent, so increment the flip count
          flipCount++;
        }

        // move to the next cell in the same direction
        currentQ += dirQ;
        currentR += dirR;
        currentS += dirS;
      }
    }

    return false; // The move is not legal in any direction
  }

  private List<Tile> getValidNeighbors(Tile dest) {
    List<Tile> allValidNeighbors = new ArrayList<>();
    for (Tile t : dest.getNeighbors()) {
      try {
        if (validateCoordinatesInBoard(t.getQ(), t.getR(), t.getS())) {
          allValidNeighbors.add(t);
        }
      } catch (IllegalArgumentException e) {
        // Don't do anything
      }
    }
    return allValidNeighbors;
  }

  private List<Tile> findNeighborsOccupiedByOpponent(List<Tile> allValidNeighbors) {
    List<Tile> neighborsOccupiedByOtherPlayer = new ArrayList<>();
    for (Tile neighbor : allValidNeighbors) {
      ReversiPiece neighborPiece = getPieceAt(neighbor);
      if (neighborPiece != ReversiPiece.EMPTY && neighborPiece != currentPlayer && neighborPiece != null) {
        neighborsOccupiedByOtherPlayer.add(neighbor);
      }
    }
    return neighborsOccupiedByOtherPlayer;
  }

  private void flipTiles(List<Tile> neighborsOccupiedByOtherPlayer, Tile dest) {
    for (Tile opp : neighborsOccupiedByOtherPlayer) {
      int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(), opp.getS() - dest.getS()};
      ArrayList<Tile> toBeFlipped = new ArrayList<>();
      toBeFlipped.add(opp);
      Tile nextTile = opp.addDirection(direction);

      while (validateCoordinatesInBoard(nextTile.getQ(), nextTile.getR(), nextTile.getS())) {
        ReversiPiece nextPiece = getPieceAt(nextTile);

        if (nextPiece == ReversiPiece.EMPTY) {
          throw new IllegalStateException("Cannot make this move");
        } else if (nextPiece != currentPlayer) {
          toBeFlipped.add(nextTile);
          nextTile = nextTile.addDirection(direction);
        } else if (nextPiece == currentPlayer) {
          for (Tile tile : toBeFlipped) {
            ReversiPiece flip = getPieceAt(tile);
            flip = (flip == ReversiPiece.BLACK) ? ReversiPiece.WHITE : ReversiPiece.BLACK;
            gameBoard.put(tile, flip);
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
}

