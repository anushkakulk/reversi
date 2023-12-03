package cs3500.reversi.model;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;
import java.util.Map;

import cs3500.reversi.adapterUtils;
import cs3500.reversi.controller.ModelStatusFeatures;
import cs3500.reversi.provider.controller.Listener;
import cs3500.reversi.provider.discs.Disc;
import cs3500.reversi.provider.discs.DiscColor;
import cs3500.reversi.provider.model.GameState;
import cs3500.reversi.provider.model.ReversiModel;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.player.PlayerTurn;

public class MergedReversiModel extends ReversiGameModel implements ReversiModel {

  @Override
  public void startGame(int boardSize) {
    super.startGame();
  }

  @Override
  public void makeMove(int x, int y) {
    Tile t = adapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength());
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

  public MergedReversiModel(int hexSideLength) {
    super(hexSideLength);
  }


  @Override
  public PlayerTurn currentTurn() {
    return super.getCurrentPlayer() == ReversiPiece.BLACK? PlayerTurn.PLAYER1 : PlayerTurn.PLAYER2;
  }

  @Override
  public Disc getDiscAt(int x, int y) {
    if (super.getPieceAt(adapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength())) == ReversiPiece.BLACK) {
      return new DiscImpl(DiscColor.BLACK);
    } else if (super.getPieceAt(adapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength())) == ReversiPiece.WHITE) {
      return new DiscImpl(DiscColor.WHITE);
    } else {
      return new DiscImpl(DiscColor.FACEDOWN);
    }
  }

  @Override
  public boolean isDiscFlipped(int x, int y) {
    return super.getPieceAt(adapterUtils.changeProviderCoordToTileCoord(x, y, super.getHexSideLength())) == ReversiPiece.EMPTY;
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
      Point2D p = adapterUtils.changeTileCoordToProviderCoord(t.getQ(), t.getR(), t.getS(), super.getHexSideLength());

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
      Tile t = adapterUtils.changeProviderCoordToTileCoord(x,y,this.getHexSideLength());
      try {
        super.validateCoordinatesInBoard(t.getQ(), t.getR(), t.getS());
      } catch (IllegalArgumentException e) {
        return false;
      }
      return true;
  }

  @Override
  public List<ReadOnlyReversiModel> getGameStates() {
    return null;
  }

  @Override
  public List<List<Integer>> getMoves() {
    return null;
  }


/*

[



 */

}
