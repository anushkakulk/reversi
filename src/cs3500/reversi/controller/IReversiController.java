package cs3500.reversi.controller;

public interface IReversiController {

  /**
   * mediates between the player actions and model status and attempts to run the given action.
   * @param action a function object to run.
   */
  void handlePlayerAction(Runnable action);
}
