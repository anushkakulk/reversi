package cs3500.reversi.player;


/**
 * This interface is used to create union data. Represents the Player Moves that are
 * available in the reversi game.
 */
public interface IPlayerMove {

  /**
   * Notifies the given player of this IPlayerMove.
   */
  void notifyPlayer(ReversiPlayer p);
}
