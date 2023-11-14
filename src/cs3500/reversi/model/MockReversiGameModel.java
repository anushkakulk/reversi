package cs3500.reversi.model;

import java.util.Map;

import cs3500.reversi.Reversi;

public class MockReversiGameModel implements  ReversiModel {
  private ReversiModel actualModel;
  private final StringBuilder log;

  // TODO so, this is going to be our mock!

  // essentially, have this class implement all the methods of the reversiModel.
  // inside each method, append whatever the input is to the log and then call the actualModel's version
  // of that method (the actual model is a delegate, so just log the inputs and then delegate!)
  public MockReversiGameModel(ReversiModel m, StringBuilder log) {
    this.actualModel = m;
    this.log = log;
  }

  @Override
  public ReversiPiece getPieceAt(int q, int r, int s) throws IllegalArgumentException {
    log.append("getPieceAt: ").append(q).append(", ").append(r).append(", ").append(s);
    return actualModel.getPieceAt(q, r, s);
  }

  @Override
  public ReversiPiece getPieceAt(Tile t) throws IllegalArgumentException {
    log.append("getPieceAt: ").append(t);
    return actualModel.getPieceAt(t);
  }

  @Override
  public int numTilesFlipped(int q, int r, int s, ReversiPiece piece) {
    int flipped = actualModel.numTilesFlipped(q, r, s, piece);
    log.append("numTilesFlipped: ").append(flipped).append(". ");
    return flipped;
  }

  @Override
  public int getHexSideLength() {
    return actualModel.getHexSideLength();
  }

  @Override
  public boolean isGameOver() {
    log.append("isGameOver");
    return actualModel.isGameOver();
  }

  @Override
  public ReversiPiece getWinner() throws IllegalStateException {
    log.append("getWinner");
    return actualModel.getWinner();
  }

  @Override
  public ReversiPiece getCurrentPlayer() {
    log.append("getCurrentPlayer");
    return actualModel.getCurrentPlayer();
  }

  @Override
  public GameStatus getGameStatus() {
    log.append("getGameStatus");
    return actualModel.getGameStatus();
  }

  @Override
  public int getScore(ReversiPiece player) {
    log.append("getScore: ").append(player);
    return actualModel.getScore(player);
  }

  @Override
  public boolean isValidMove(int q, int r, int s, ReversiPiece piece) {
    log.append("isValidMove: ").append(q).append(", ").append(r).append(", ").append(s).append(", ")
        .append(piece).append(". ");
    return actualModel.isValidMove(q, r, s, piece);
  }

  @Override
  public Map<Tile, ReversiPiece> getBoard() {
    log.append("getBoard");
    return actualModel.getBoard();
  }

  @Override
  public void move(int q, int r, int s) throws IllegalStateException, IllegalArgumentException {
    log.append("move: ").append(q).append(", ").append(r).append(", ").append(s);
    actualModel.move(q, r, s);
  }

  @Override
  public void pass() {
    log.append("pass");
    actualModel.pass();
  }
}


