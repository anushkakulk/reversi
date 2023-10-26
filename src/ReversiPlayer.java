/**
 * Represents the interface for a Player of Reversi.
 */
public interface ReversiPlayer {

  // ----  NOTE ABOUT THE INTERFACE ----
  // basically, a player in the game needs to make a game play decision (which as of now means
  // they can either move one of their piece to a certain position or they can pass their turn)
  // so a player needs will make a decision, and the result of that decision will be
  // a function object, either a Pass or a MovePiece. That object contains information about the
  // player's decision and will be given to the model, to actually play the game! A player will
  // also have a unique ReversiPiece that corresponds to themselves, so the model knows which
  // whose turn it is and what piece to put down depending on what "piece"'s turn it is.

  /**
   * This method is where the player decides what the next move for their gameplay is, where they
   * can either Pass or MovePiece.
   *
   * @return PlayerMove, which is either a Pass or a MovePiece.
   */
  PlayerMove makeAGamePlayDecision(); // unsure if model should be passed in to this method or not

}
