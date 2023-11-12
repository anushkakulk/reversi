package cs3500.reversi.model;

import java.util.Map;

import cs3500.reversi.Reversi;

public class MockReversiGameModel implements  ReversiModel{
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
    // so you'd do something like. TODO do this kinda thing for all the methods in this
    log.append("getPieceAt: ").append(q).append(", ").append(r).append(", ").append(s);
    return actualModel.getPieceAt(q, r, s);
  }

  @Override
  public ReversiPiece getPieceAt(Tile t) throws IllegalArgumentException {
    return null;
  }

  @Override
  public int getHexSideLength() {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public ReversiPiece getWinner() throws IllegalStateException {
    return null;
  }

  @Override
  public ReversiPiece getCurrentPlayer() {
    return null;
  }

  @Override
  public GameStatus getGameStatus() {
    return null;
  }

  @Override
  public int getScore(ReversiPiece player) {
    return 0;
  }

  @Override
  public boolean isValidMove(int q, int r, int s, ReversiPiece piece) {
    return false;
  }

  @Override
  public int numTilesFlipped(int q, int r, int s, ReversiPiece piece) {

  }

  @Override
  public Map<Tile, ReversiPiece> getBoard() {
    return null;
  }

  @Override
  public void move(int q, int r, int s) throws IllegalStateException, IllegalArgumentException {

  }

  @Override
  public void pass() {

  }






}
