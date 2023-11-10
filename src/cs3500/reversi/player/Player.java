package cs3500.reversi.player;


import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.model.ReversiPiece;

/**
 * Represents a Player of Reversi Each Player plays a certain strategy and has a piece associated
 * with them on the board.
 */
public class Player implements ReversiPlayer {
  private final Strategy strategy; // the strat to play.
  private final ReversiPiece piece; // this player's piece on the board.


  /**
   * Creates an instance of a player/
   * @param strategy the strategy thi player wishes to play with.
   * @param piece this player's associated piece on the board.
   */
  public Player(Strategy strategy, ReversiPiece piece) {
    this.strategy = strategy;
    this.piece = piece;
  }

  /**
   * Returns this player's next move in the game.
   * @param model the game .model, from which the player is determining the next move to make.
   * @return an IPlayerMove (either a pass or move function object)
   */
  @Override
  public IPlayerMove getPlayerDecision(ReadOnlyReversiModel model) {
    return this.strategy.chooseMove(model, this.piece);
  }
}
