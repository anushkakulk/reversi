package cs3500.reversi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.model.Tile;
import cs3500.reversi.player.IPlayerMove;
import cs3500.reversi.player.Move;
import cs3500.reversi.player.Pass;
import cs3500.reversi.player.ReversiPlayer;
import cs3500.reversi.player.ReversiPosn;
import cs3500.reversi.provider.discs.DiscColor;
import cs3500.reversi.provider.player.Player;
import cs3500.reversi.provider.player.PlayerTurn;
import cs3500.reversi.provider.strategy.IStrategy;

public class AdaptedPlayer extends Player implements ReversiPlayer {

  private final List<PlayerActionFeatures> listeners = new ArrayList<>();

  public AdaptedPlayer(PlayerTurn playerTurn, IStrategy strategy) {
    super(playerTurn, strategy);
  }

  @Override
  public IPlayerMove getPlayerDecision(ReadOnlyReversiModel model) {
    List<Integer> move = super.getIStrategy().executeStrategy();
    Tile t = AdapterUtils.changeProviderCoordToTileCoord(move.get(0), move.get(1), model.getHexSideLength());
    IPlayerMove nextMove;
    boolean valid = false;
    try {
      valid = model.isValidMove(t.getQ(), t.getR(), t.getS(), this.getPiece());
    } catch (IllegalArgumentException | IllegalStateException e) {
      // do nothing
    }

    if (valid) {
      nextMove = new Move(new ReversiPosn(t.getQ(), t.getR(), t.getS()));
    } else {
      nextMove = new Pass();
    }

    nextMove.notifyPlayer(this);
    return nextMove;
  }

  @Override
  public ReversiPiece getPiece() {
    return this.getPlayerColor() == DiscColor.BLACK ? ReversiPiece.BLACK : ReversiPiece.WHITE;
  }

  @Override
  public void addPlayerActionListener(PlayerActionFeatures listener) {
    this.listeners.add(Objects.requireNonNull(listener));
  }

  @Override
  public void notifyMoveChosen(int q, int r, int s) {
    for (PlayerActionFeatures e : listeners) {
      e.handleMoveChosen(q, r, s);
    }
  }

  @Override
  public void notifyPassChosen() {
    for (PlayerActionFeatures e : listeners) {
      e.handlePassChosen();
    }
  }
}

