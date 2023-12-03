package cs3500.reversi.provider.controller;

/**
 * A ControllerListener represents an entity that listens from a controller for
 * the sake of other Controllers.
 */
public interface ControllerListener {

  /**
   * The Handle Event Method prepares each controller to sync.
   */
  void handleEvent();
}
