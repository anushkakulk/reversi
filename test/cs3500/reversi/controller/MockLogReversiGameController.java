package cs3500.reversi.controller;

import java.util.Objects;

import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.Player;
import cs3500.reversi.provider.controller.Event;
import cs3500.reversi.view.ReversiView;

/**
 * Represents a Mock for a ReversiController that will log input to any method calls
 * for some of the IReversiController, PlayerActionFeatures, ModelStatusFeatures
 * methods to test that the controller will work as intended.
 */
public class MockLogReversiGameController implements IReversiController, PlayerActionFeatures,
        ModelStatusFeatures {
  private final ReversiController actualController;
  private final StringBuilder log;
  private final ReversiPiece myPlayer;

  private int timesTurnChanged = 0;

  /**
   * Creates an instance of this mock type.
   *
   * @param log      the log that will contain a transcript of any inputs to any methods
   * @param c        an actual ReversiController that can be played
   * @param myPlayer the player that this mock is playing as
   */
  public MockLogReversiGameController(StringBuilder log, Player player, ReversiController c,
                                      ReversiPiece myPlayer, ReversiModel model, ReversiView view) {

    this.actualController = c;
    this.log = log;
    this.myPlayer = myPlayer;
    Player player1 = Objects.requireNonNull(player);
    ReversiModel model1 = Objects.requireNonNull(model);
    ReversiView view1 = Objects.requireNonNull(view);
    model1.addModelStatusListener(this);
    view1.addPlayerActionListener(this);
    player1.addPlayerActionListener(this);

  }

  @Override
  public void handlePlayerAction(Runnable action) {
    log.append("handlePlayerAction: ").append(action).append("\n");
    actualController.handlePlayerAction(action);
  }

  @Override
  public void handlePlayerChange(ReversiPiece currPlayer) {
    timesTurnChanged += 1;
    boolean isMyTurn = myPlayer == currPlayer;
    log.append("handlePlayerChange: ").append(currPlayer).append("\n");
    log.append("MyPlayer: ").append(myPlayer).append(", ").append("times" +
            "TurnChanged: ").append(timesTurnChanged).append(". isMy" +
            "Turn: ").append(isMyTurn).append("\n");
    actualController.handlePlayerChange(currPlayer);
  }

  @Override
  public void handleGameOver() {
    log.append("Notification from Model: Game Over.\n");
    actualController.handleGameOver();
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

  @Override
  public void update(Event e) {

  }
}