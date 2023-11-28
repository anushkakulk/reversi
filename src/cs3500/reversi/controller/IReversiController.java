package cs3500.reversi.controller;

/**
 * Represents the basic functionality a controller should do, involving handling player action
 * and passing on/handling information to/from the model for updates.
 */
public interface IReversiController {

  /**
   * mediates between the player actions and model status and attempts to run the given action.
   * @param action a function object to run.
   */
  void handlePlayerAction(Runnable action);
}
