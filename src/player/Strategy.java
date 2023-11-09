package player;

import java.util.Optional;

import model.ReadOnlyReversiModel;
import model.ReversiModel;
import model.ReversiPiece;

public class Strategy {
IPlayerMoveStrategy attempt;

  public Strategy(IPlayerMoveStrategy stratToPlay) {
    this.attempt = stratToPlay;
  }

  IPlayerMove chooseMove(ReadOnlyReversiModel board, ReversiPiece who) {
    Optional<ReversiPosn> guess = this.attempt.playStrategy(board, who);
    if (guess.isPresent()) { return new Move(guess.get()); }
    return new Pass();
  }
}
