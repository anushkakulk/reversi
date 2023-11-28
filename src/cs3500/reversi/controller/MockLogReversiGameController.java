package cs3500.reversi.controller;

import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.ReversiView;

import java.util.Objects;

/**
 * Represents a Mock for a ReversiController that will log input to any method calls
 * for some of the IReversiController, PlayerActionFeatures, ModelStatusFeatures
 * methods to test that the controller will work as intended.
 */
public class MockLogReversiGameController implements IReversiController, PlayerActionFeatures, ModelStatusFeatures {
  private ReversiController actualController;
  private final StringBuilder log;

  private final ReversiPiece myPlayer;

  private final ReversiModel model;
  private final ReversiView view;

  private final Player player;
  /**
   * Creates an instance of this mock type.
   *
   * @param log       the log that will contain a transcript of any inputs to any methods
   * @param c         an actual ReversiController that can be played
   * @param myPlayer  the player that this mock is playing as
   */
  public MockLogReversiGameController(StringBuilder log, Player player, ReversiController c, ReversiPiece myPlayer,
      ReversiModel model, ReversiView view) {

    this.actualController = c;
    this.log = log;
    this.myPlayer = myPlayer;
    this.player = Objects.requireNonNull(player);
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.model.addModelStatusListener(this);
    this.view.addPlayerActionListener(this);
    this.player.addPlayerActionListener(this);

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
    log.append("MyPlayer: ").append(myPlayer).append(", ").append("Move Chosen: ")
        .append(xCoord).append(", ").append(rCoord).append(", ").append(sCoord)
        .append("\n");
        actualController.handleMoveChosen(xCoord, rCoord, sCoord);
  }

  @Override
  public void handlePassChosen() {
    log.append("MyPlayer: ").append(myPlayer).append(", ").append("Pass Chosen")
        .append("\n");
    actualController.handlePassChosen();
  }
}