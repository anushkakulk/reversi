/**
 * Represents the interface for a Player of Reversi.
 */
public interface ReversiPlayer {

  /**
   * This method is where the player decides what the next move for their gameplay is, where they
   * can either Pass or MovePiece.
   *
   * @return PlayerMove, which is either a Pass or a MovePiece.
   */
  PlayerMove makeAGamePlayDecision(); // unsure if model should be passed in to this method or not

}
