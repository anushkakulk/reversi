package cs3500.reversi.provider.controller;

import cs3500.reversi.provider.player.PlayerTurn;

/**
 * A Class that represents a singular ModelEvent.
 */
public class ModelEvent implements Event {
  private final EventType modelEventType;
  private final String message;
  private final PlayerTurn executingPlayer;

  /**
   * A constructor for a ModelEvent.
   *
   * @param modelEventType a ModelEventType
   * @param message        a Message
   */
  public ModelEvent(EventType modelEventType, String message,
                    PlayerTurn executingPlayer) {
    this.modelEventType = modelEventType;
    this.message = message;
    this.executingPlayer = executingPlayer;
  }

  /**
   * A getter that gets the message information of a model event.
   */
  public String getMessage() {
    return this.message;
  }

  @Override
  public PlayerTurn getExecutingPlayer() {
    return this.executingPlayer;
  }

  @Override
  public EventType getEventType() {
    return this.modelEventType;
  }

}
