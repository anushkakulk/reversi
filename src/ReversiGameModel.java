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
  // TODO THE MATH FOR NUM TILES IN A BOARD WITH hexSideLength  = 3 * hexSideLength * (hexSideLength - 1) + 1
  private int numTiles;

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
    this.numTiles = board.size();

    this.currentPlayer = ReversiPiece.BLACK;
  }

  // TODO implement the whole moving functionality
  // TODO how to do the whole 'turn' functionality?

  @Override
  public void movePiece(int q, int r, int s) throws IllegalStateException, IllegalArgumentException {
    checkHasGameStarted();
    validateCoordinatesInBoard(q, r, s);

    // check if it's the correct player's turn
    if (currentPlayer != ReversiPiece.BLACK && currentPlayer != ReversiPiece.WHITE) {
      throw new IllegalArgumentException("Invalid player");
    }

    // check if the cell is mt
    ReversiPiece piece = getPieceAt(q, r, s);
    if (piece != ReversiPiece.EMPTY) {
      throw new IllegalArgumentException("Selected cell is not empty.");
    }

    // check if the move is legal by verifying adjacent opponent discs and capturing
    boolean legalMove = isLegalMove(q, r, s, currentPlayer);

    if (legalMove) {
      // update the game board to flip captured discs
      // TODO need to implement the logic to flip the discs in all directions between two discs of the current player
      //updateBoard(q, r, s, currentPlayer);
      //gameBoard.put(new Tile(currentQ, currentR, currentS), currentPlayer);
    }
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


  // helper method that throws the given coordinates are for a tile outside of the game board
  //!!changed this to return a boolean
  private boolean validateCoordinatesInBoard(int q, int r, int s) {
    if (q >= this.hexSideLength || r >= this.hexSideLength || s >= this.hexSideLength) {
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

      // check if the next cell in this direction belongs to the opponent/MT/current player
      while (validateCoordinatesInBoard(currentQ, currentR, currentS)) {
        ReversiPiece piece = getPieceAt(currentQ, currentR, currentS);

        if (piece == ReversiPiece.EMPTY) {
          // found an empty cell before reaching the current player's piece
          break;
        }

        if (piece == currentPlayer) {
          // we found the current players piece, and its a legal move if we have flipped
          // opponent pieces in this direction
          if (flipCount > 0) {
            return true;
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

  public void updateBoard() {

  }

  @Override
  public void move(int q, int r, int s) {
    validateCoordinatesInBoard(q, r, s);
    // get valid neighbors

    Tile dest = new Tile(q, r, s);
    List<Tile> allValidNeighbors = new ArrayList<>();
    for (Tile t : dest.getNeighbors()) {
      try {
        if (validateCoordinatesInBoard(t.getQ(), t.getR(), t.getS())) {
          allValidNeighbors.add(t);
        }
      } catch (IllegalArgumentException e) {
        // dont do anything
      }
    }

    List<Tile> neighborsOccupiedByOtherPlayer = new ArrayList<>();
    // TODO iterate through allValidNeighbors and check if its the opposite player, if it is, add it
    // to the list

    for (Tile neighbor : allValidNeighbors) {
      ReversiPiece neighborPiece = getPieceAt(neighbor);

      if (neighborPiece != ReversiPiece.EMPTY && neighborPiece != currentPlayer) {
        // the neighbor is occupied by the opposite player
        neighborsOccupiedByOtherPlayer.add(neighbor);
      }
//********************* NEW
      for (Tile opp : neighborsOccupiedByOtherPlayer) {
        // iterate through neighbors occupied by the opponent
        int[] direction = {opp.getQ() - dest.getQ(), opp.getR() - dest.getR(),
            opp.getS() - dest.getS()};
        // calculate the direction vector from the destination tile to the neighbor

        Tile nextTile = opp.addDirection(direction);
        // calculate the next tile to check in the same direction

        ArrayList<Tile> toBeFlipped = new ArrayList<>();
        // create a list to store tiles that need to be flipped

        while (validateCoordinatesInBoard(nextTile.getQ(), nextTile.getR(), nextTile.getS())) {
          // continue while the next tile is within the game board
          ReversiPiece nextPiece = getPieceAt(nextTile);
          // get the piece at the next tile

          if (nextPiece == ReversiPiece.EMPTY) {
            // ff the next tile is empty, it's an invalid move
            throw new IllegalArgumentException("Cannot make this move");
          } else if (nextPiece != currentPlayer) {
            toBeFlipped.add(nextTile);
            nextTile.addDirection(direction);
            // if the next tile is occupied by the opponent
            // add the tile to the list of tiles to be flipped and move to the next tile in the same direction
          } else if (nextPiece == currentPlayer) {
            for (Tile tile : toBeFlipped) {
              // if the next tile is occupied by the current player
              // Iterate through the tiles to be flipped
              ReversiPiece flip = getPieceAt(tile);
              // Get the piece at the tile to be flipped
              // flip the piece from opponent to current player (not usre if im doing this right)
              flip = (flip == ReversiPiece.BLACK)
                  ? ReversiPiece.WHITE : ReversiPiece.BLACK;
            }
          }
        }
      }
    }

    switchPlayer();
  }


  @Override
  public void switchPlayer() {
    this.currentPlayer = this.currentPlayer == ReversiPiece.BLACK ? ReversiPiece.WHITE : ReversiPiece.BLACK;
  }
}
