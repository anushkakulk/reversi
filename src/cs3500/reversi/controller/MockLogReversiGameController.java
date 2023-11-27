package cs3500.reversi.controller;

import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents a Mock for a ReversiController that will log input to any method calls
 * for some of the IReversiController, PlayerActionFeatures, ModelStatusFeatures
 * methods to test that the controller will work as intended.
 */
public class MockLogReversiGameController implements IReversiController, PlayerActionFeatures, ModelStatusFeatures {
  private ReversiController actualController;
  private final StringBuilder log;

  /**
   * Creates an instance of this mock type.
   * @param c an actual ReversiController that can be played
   * @param log the log that will contain a transcript of any inputs to any methods
   */
  public MockLogReversiGameController(StringBuilder log, ReversiController c) {
    this.actualController = c;
    this.log = log;
  }

  @Override
  public void handlePlayerAction(Runnable action) {
    log.append("handlePlayerAction: ").append(action).append("\n");
    actualController.handlePlayerAction(action);
  }

  @Override
  public void handlePlayerChange(ReversiPiece currPlayer) {
 log.append("handlePlayerChange: ").append(currPlayer).append("\n");
    actualController.handlePlayerChange(currPlayer);
  }

  @Override
  public void handleGameOver() {

  }

  @Override
  public void handleTileClicked(int xCoord, int rCoord, int sCoord) {
 log.append("handleTileClicked: ").append(xCoord).append(", ").append(rCoord).append(", ").
     append(sCoord).append("\n");
    actualController.handleTileClicked(xCoord, rCoord, sCoord);
  }

  @Override
  public void handleMoveChosen(int xCoord, int rCoord, int sCoord) {
    log.append("handleMoveChosen: ").append(xCoord).append(", ").append(rCoord).append(", ").
        append(sCoord).append("\n");
        actualController.handleMoveChosen(xCoord, rCoord, sCoord);
  }

  @Override
  public void handlePassChosen() {

  }
}