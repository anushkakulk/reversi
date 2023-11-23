package cs3500.reversi.controller;

import java.util.Objects;

import cs3500.reversi.model.ModelStatusFeatures;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.Player;
import cs3500.reversi.view.PlayerActionFeatures;
import cs3500.reversi.view.ReversiView;

public class ReversiController implements IReversiController, PlayerActionFeatures, ModelStatusFeatures {
  private final Player player;
  private final ReversiModel model;
  private final ReversiView view;
  private boolean isMyTurn = false;

  public ReversiController(ReversiModel model, Player player, ReversiView view) {
    this.player = Objects.requireNonNull(player);
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.model.addModelStatusListener(this);
    this.view.addPlayerActionListener(this);
    this.player.addPlayerActionListener(this);

  }



//  @Override
//  public void run() {
//      if (isMyTurn) {
//        this.player.getPlayerDecision(model);
//        this.view.update();
//        // i want the player play their strategy. the issue is that for the human impl, their strategy
//        // doesn't do anything! they interact through the view, which the controller also listens to
//        // to know when the human player chose a move or pass via the gui.
//      }
//  }

  @Override
  public void handlePlayerChange(ReversiPiece currPlayer) {
    this.isMyTurn = currPlayer == this.player.getPiece();
    this.view.update();
  }


  @Override
  public void handleTileClicked(int xCoord, int rCoord, int sCoord) {
    // do nothing we don't really care if a tile was clicked
  }

  @Override
  public void handleMoveChosen(int xCoord, int rCoord, int sCoord) {
    if (isMyTurn) {
      try {
        model.move(xCoord, rCoord, sCoord);
      } catch (IllegalArgumentException | IllegalStateException e) {
        this.view.handleInvalidOperation(e.getMessage());
      }
    }
  }

  @Override
  public void handlePassChosen() {
    if (isMyTurn) {
      try {
        model.pass();
      } catch (IllegalArgumentException | IllegalStateException e) {
        this.view.handleInvalidOperation(e.getMessage());
      }
    }
  }
}
