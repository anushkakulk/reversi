package cs3500.reversi.controller;

import java.util.Objects;

import cs3500.reversi.adapter.AdapterUtils;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.model.Tile;
import cs3500.reversi.player.ReversiPlayer;
import cs3500.reversi.provider.controller.Event;
import cs3500.reversi.provider.controller.EventType;
import cs3500.reversi.view.ReversiView;

/**
 * Represents a controller for a Reversi game.
 */
public class ReversiController implements IReversiController, PlayerActionFeatures,
        ModelStatusFeatures {
  private final ReversiPlayer player;
  private final ReversiModel model;
  private final ReversiView view;
  private boolean isMyTurn = false;

  /**
   * Creates an instance of a Reversi Controller that handles interaction and updates between the
   * model and the view for the given player.
   *
   * @param model  the model that this controller is listening to for notifications.
   * @param player the player this controller is for/listening to for notifications.
   * @param view   the view this controller is listening to for notifications .
   */
  public ReversiController(ReversiModel model, ReversiPlayer player, ReversiView view) {
    this.player = Objects.requireNonNull(player);
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.addListeners(model, player, view);
  }

  /**
   * Adds the listeners for this controller to the given model, player, and view.
   */
  public void addListeners(ReversiModel model, ReversiPlayer player, ReversiView view) {
    model.addModelStatusListener(this);
    view.addPlayerActionListener(this);
    player.addPlayerActionListener(this);
  }

  @Override
  public void handlePlayerChange(ReversiPiece currPlayer) {
    this.isMyTurn = currPlayer == this.player.getPiece();
    this.view.update();
    updateViewTitle();
    if (isMyTurn) {
      this.player.getPlayerDecision(this.model);
    }
  }

  @Override
  public void handleGameOver() {
    String message = "Game Over! Outcome of game: " + model.getGameStatus();
    if (model.getWinner() == this.player.getPiece()) {
      message += ". Congratulations on Winning!";
    } else {
      message += ". Better luck next time!";
    }
    this.view.displayMessage(message);
  }

  @Override
  public void handleTileClicked(int xCoord, int rCoord, int sCoord) {
    int numFlipped = model.numTilesGained(xCoord, rCoord, sCoord, this.player.getPiece());
    System.out.println(numFlipped);
    this.view.handleHintOn(numFlipped);
  }

  @Override
  public void handleMoveChosen(int xCoord, int rCoord, int sCoord) {
    handlePlayerAction(() -> model.move(xCoord, rCoord, sCoord));
  }

  @Override
  public void handlePassChosen() {
    handlePlayerAction(model::pass);
  }

  @Override
  public void handlePlayerAction(Runnable action) {
    if (isMyTurn && !model.isGameOver()) {
      try {
        action.run();
      } catch (IllegalArgumentException | IllegalStateException e) {
        this.view.displayMessage(e.getMessage());
      }
    }
  }

  private void updateViewTitle() {
    ReversiPiece me = this.player.getPiece();
    this.view.displayTitle(me + "'s Board. Score = " + model.getScore(me) + ". Turn: "
            + model.getCurrentPlayer());
  }

  @Override
  public void update(Event e) {
    if (e.getEventType() == EventType.MOVE) {
      String[] strArray = e.getMessage().split(" ");
      Tile moveChosen = AdapterUtils.changeProviderCoordToTileCoord(
              Integer.parseInt(strArray[0]),
              Integer.parseInt(strArray[1]),
              this.model.getHexSideLength()
      );
      this.handleMoveChosen(moveChosen.getQ(), moveChosen.getR(), moveChosen.getS());
    } else if (e.getEventType() == EventType.PASS) {
      this.handlePassChosen();
    }

  }
}