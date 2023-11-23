package cs3500.reversi.view;

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
   * manages an invalid operation for the view.
   * @param message
   */
  void handleInvalidOperation(String message);

}
