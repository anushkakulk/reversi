package cs3500.reversi.view;

import cs3500.reversi.controller.SimpleReversiView;

public interface ReversiView extends SimpleReversiView {
  /**
   * adds a listener for any player actions to the view.
   *
   * @param listener a handler that will listen and handle any player actions.
   */
  void addPlayerActionListener(PlayerActionFeatures listener);

  /**
   * Updates the view based on the state of the model.
   */
  void update();

  /**
   * prints out a message about the game state for the view.
   * @param message some message about the gamestate
   */
  void displayMessage(String message);

  /**
   * prints out a message for the title of  the view.
   * @param titleMessage some message about the title
   */
  void displayTitle(String titleMessage);
}
