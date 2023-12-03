package cs3500.reversi.player;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.reversi.controller.IEmitPlayerActions;
import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents a Player of Reversi Each Player plays a certain strategy and has a piece associated
 * with them on the board.
 */
public class Player implements ReversiPlayer, IEmitPlayerActions {
  private final Strategy strategy; // the strat to play.
  private final ReversiPiece piece; // this player's piece on the board.

  private final List<PlayerActionFeatures> listeners = new ArrayList<>();

  /**
   * Creates an instance of a player.
   *
   * @param strategy the strategy this player wishes to play with.
   * @param piece    this player's associated piece on the board.
   */
  public Player(Strategy strategy, ReversiPiece piece) {
    this.strategy = strategy;
    this.piece = piece;
  }

  /**
   * Returns this player's next move in the game.
   *
   * @param model the game model, from which the player is determining the next move to make.
   * @return an IPlayerMove (either a pass or move function object)
   */
  @Override
  public IPlayerMove getPlayerDecision(ReadOnlyReversiModel model) {
    IPlayerMove nextMove = this.strategy.chooseMove(model, this.piece);
    nextMove.notifyPlayer(this);
    return nextMove;
  }


  @Override
  public ReversiPiece getPiece() {
    return this.piece;
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
