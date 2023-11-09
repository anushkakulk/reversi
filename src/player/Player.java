package player;

import model.ReadOnlyReversiModel;
import model.ReversiModel;
import model.ReversiPiece;

public class Player implements ReversiPlayer{
  private final Strategy strategy;
  private final ReversiPiece piece;

  public Player(Strategy strategy, ReversiPiece piece) {
    this.strategy = strategy;
    this.piece = piece;
  }

  @Override
  public IPlayerMove getPlayerDecision(ReadOnlyReversiModel model) {
    return this.strategy.chooseMove(model, this.piece);
  }
}
