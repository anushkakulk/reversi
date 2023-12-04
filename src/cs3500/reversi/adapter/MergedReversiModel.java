package cs3500.reversi.adapter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map;

import cs3500.reversi.controller.ModelStatusFeatures;
import cs3500.reversi.model.GameStatus;
import cs3500.reversi.model.ReversiGameModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.model.Tile;
import cs3500.reversi.provider.controller.Listener;
import cs3500.reversi.provider.discs.Disc;
import cs3500.reversi.provider.discs.DiscColor;
import cs3500.reversi.provider.model.GameState;
import cs3500.reversi.provider.model.ReversiModel;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.player.PlayerTurn;

public class MergedReversiModel extends ReversiGameModel implements ReversiModel {

  private List<ReadOnlyReversiModel> gameStates;
  private List<List<Integer>> moves;


  @Override
  public void startGame() {
    System.out.println("hey start game");
    super.startGame();
    this.gameStates.add(this);
    this.moves.add(new ArrayList<>());
  }

  @Override
  public void move(int q, int r, int s) {
    super.move(q, r, s);
    // added functionality for the provider's minimax strategy
    List<Integer> moveMade = new ArrayList<>();
    Point2D move = AdapterUtils.changeTileCoordToProviderCoord(q,r,s,this.getHexSideLength());
    moveMade.add((int) move.getX());
    moveMade.add((int) move.getY());
    this.moves.add(moveMade);
  }

  @Override
  public void startGame(int boardSize) {
    this.startGame();
  }

  @Override
  public void makeMove(int x, int y) {
    Tile t = AdapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength());
    super.move(t.getQ(), t.getR(), t.getS());
  }

  @Override
  public void addListener(Listener l) {
    super.addModelStatusListener((ModelStatusFeatures) l);
  }

  public static class DiscImpl implements Disc {
    private final DiscColor color;

    public DiscImpl(DiscColor c) {
      this.color = Objects.requireNonNull(c);
    }

    @Override
    public DiscColor getColor() {
      return color;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DiscImpl)) {
        return false;
      }
      DiscImpl tile = (DiscImpl) o;
      return (this.color.equals(tile.getColor()));
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.color);
    }
  }

  @Override
  protected void switchPlayer() {
    super.switchPlayer();

    this.gameStates.add(this);
  }

  public MergedReversiModel(int hexSideLength) {
    super(hexSideLength);
    this.gameStates  = new ArrayList<>();
    this.moves = new ArrayList<>();

    this.gameStates.add(this);
  }


  @Override
  public PlayerTurn currentTurn() {
    return super.getCurrentPlayer() == ReversiPiece.BLACK? PlayerTurn.PLAYER1 : PlayerTurn.PLAYER2;
  }

  @Override
  public Disc getDiscAt(int x, int y) {
    if (super.getPieceAt(AdapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength())) == ReversiPiece.BLACK) {
      return new DiscImpl(DiscColor.BLACK);
    } else if (super.getPieceAt(AdapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength())) == ReversiPiece.WHITE) {
      return new DiscImpl(DiscColor.WHITE);
    } else {
      return new DiscImpl(DiscColor.FACEDOWN);
    }
  }

  @Override
  public boolean isDiscFlipped(int x, int y) {
    return super.getPieceAt(AdapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength())) == ReversiPiece.EMPTY;
  }

  @Override
  public int getDimensions() {
    return this.getHexSideLength() + this.getHexSideLength() - 1;
  }

  @Override
  public GameState getCurrentGameState() {
    GameStatus rn = super.getGameStatus();
    switch (rn) {
      case PLAYING:
        return GameState.ONGOING;
      case STALEMATE:
        return GameState.STALEMATE;
      case WON:
        return super.getWinner() == ReversiPiece.BLACK ?
                GameState.PLAYER1WIN :  GameState.PLAYER2WIN;
    }
    return GameState.STALEMATE;
  }

  @Override
  public Disc[][] getCurrentBoardState() {
    Map<Tile, ReversiPiece> currState = super.getBoard();
    Disc[][] currBoard = new Disc[getDimensions()][getDimensions()];
    for (Tile t: currState.keySet()) {
      Point2D p = AdapterUtils.changeTileCoordToProviderCoord(t.getQ(), t.getR(), t.getS(), super.getHexSideLength());

      currBoard[(int)p.getY()]
               [(int)p.getX()] =
              getDiscAt((int)p.getX(),
                      (int) p.getY());
    }


    return currBoard;
  }

  @Override
  public boolean canPlayerPlay(int x, int y) {
    return false;
  }

  @Override
  public int getScore(PlayerTurn playerTurn) {
    return 0;
  }

  @Override
  public boolean doesPlayerHaveLegalMove() {
    return false;
  }

  @Override
  public String getType() {
    return "Normal";
  }

  @Override
  public PlayerTurn getOpponent(PlayerTurn player) {
    return player == PlayerTurn.PLAYER1 ? PlayerTurn.PLAYER2 : PlayerTurn.PLAYER1;
  }

  @Override
  public DiscColor getPlayerColor(PlayerTurn player) {
   return player == PlayerTurn.PLAYER1 ? DiscColor.BLACK : DiscColor.WHITE;
  }

  @Override
  public boolean checkValidCoordinates(int x, int y) {
      Tile t = AdapterUtils.changeProviderCoordToTileCoord(x,y,this.getHexSideLength());
      try {
        super.validateCoordinatesInBoard(t.getQ(), t.getR(), t.getS());
      } catch (IllegalArgumentException e) {
        return false;
      }
      return true;
  }

  @Override
  public List<ReadOnlyReversiModel> getGameStates() {
   List<ReadOnlyReversiModel> gameStatesCopy = new ArrayList<>();
   for (ReadOnlyReversiModel g : this.gameStates) {
     gameStatesCopy.add(g);
   }
   return  gameStatesCopy;
  }

  @Override
  public List<List<Integer>> getMoves() {
    List<List<Integer>> movesCopy = new ArrayList<>();
    for (List<Integer> innerList : this.moves) {
      List<Integer> innerListCopy = new ArrayList<>(innerList);
      movesCopy.add(innerListCopy);
    }
    return movesCopy;
  }
}
