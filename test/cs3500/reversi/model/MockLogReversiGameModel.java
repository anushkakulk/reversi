package cs3500.reversi.model;

import java.util.Map;

import cs3500.reversi.controller.ModelStatusFeatures;

/**
 * Represents a Mock for a ReversiGameModel that will log input to any method calls
 * for some of the ReversiModel methods to test that strategies will work as intended.
 */
public class MockLogReversiGameModel implements  ReversiModel {
  private ReversiModel actualModel;
  private final StringBuilder log;


  /**
   * Creates an instance of this mock type.
   * @param m an actual ReversiModel that can be played
   * @param log the log that will contain a transcript of any inputs to any methods
   */
  public MockLogReversiGameModel(ReversiModel m, StringBuilder log) {
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
  public int numTilesGained(int q, int r, int s, ReversiPiece piece) {
    int flipped = actualModel.numTilesGained(q, r, s, piece);
    log.append("numTilesGained: ").append(flipped).append(".\n");
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
        .append(piece).append(".\n");
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

  @Override
  public void startGame() {
    actualModel.startGame();
  }

  @Override
  public void addModelStatusListener(ModelStatusFeatures listener) {
    this.actualModel.addModelStatusListener(listener);
  }
}


