package cs3500.reversi.model;

import java.util.Map;

/**
 * Represents a Mock for a ReversiGameModel that will return incorrect answers
 * for some of the ReversiModel methods to test that strategies will work as intended.
 */
public class MockFakeReversiGameModel implements ReversiModel {
  private final ReversiModel actualModel;
  private final int qToFake;
  private final int rToFake;
  private final int sToFake;


  /**
   * creates an instance of a FakeReversiGameModel.
   *
   * @param m an actual model that can validly be played
   * @param q the q coordinate for a tile that we want a strategy to pick.
   * @param r the r coordinate for a tile that we want a strategy to pick.
   * @param s the s coordinate for a tile that we want a strategy to pick.
   */
  public MockFakeReversiGameModel(ReversiModel m, int q, int r, int s) {
    this.actualModel = m;
    this.qToFake = q;
    this.rToFake = r;
    this.sToFake = s;
  }

  @Override
  public ReversiPiece getPieceAt(int q, int r, int s) throws IllegalArgumentException {

    return actualModel.getPieceAt(q, r, s);
  }

  @Override
  public ReversiPiece getPieceAt(Tile t) throws IllegalArgumentException {
    return actualModel.getPieceAt(t);
  }

  @Override
  public int numTilesGained(int q, int r, int s, ReversiPiece piece) {
    int flipped = actualModel.numTilesGained(q, r, s, piece);

    // lie to the strategy and pretend that moving to the given coord tile will return 100 flipped
    // tiles.
    if (q == qToFake && r == rToFake && s == sToFake) {
      flipped = 100;
    }

    return flipped;
  }

  @Override
  public int getHexSideLength() {
    return actualModel.getHexSideLength();
  }

  @Override
  public boolean isGameOver() {
    return actualModel.isGameOver();
  }

  @Override
  public ReversiPiece getWinner() throws IllegalStateException {
    return actualModel.getWinner();
  }

  @Override
  public ReversiPiece getCurrentPlayer() {
    return actualModel.getCurrentPlayer();
  }

  @Override
  public GameStatus getGameStatus() {
    return actualModel.getGameStatus();
  }

  @Override
  public int getScore(ReversiPiece player) {
    return actualModel.getScore(player);
  }

  @Override
  public boolean isValidMove(int q, int r, int s, ReversiPiece piece) {
    // lie to the strategy and pretend that moving to that corner is valid regardless.
    if (q == qToFake && r == rToFake && s == sToFake) {
      return true;
    }
    return actualModel.isValidMove(q, r, s, piece);
  }

  @Override
  public Map<Tile, ReversiPiece> getBoard() {
    return actualModel.getBoard();
  }

  @Override
  public void move(int q, int r, int s) throws IllegalStateException, IllegalArgumentException {
    actualModel.move(q, r, s);
  }

  @Override
  public void pass() {
    actualModel.pass();
  }

  @Override
  public void startGame() {
    actualModel.startGame();
  }

  @Override
  public void addModelStatusListener(ModelStatusFeatures listener) {

  }
}


