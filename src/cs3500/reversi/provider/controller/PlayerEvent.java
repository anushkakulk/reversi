package cs3500.reversi.provider.controller;

import cs3500.reversi.provider.player.PlayerTurn;

/**
 * A Class that represents a singular PlayerEvent.
 */
public class PlayerEvent implements Event {
  private final EventType playerEventType;
  private final String description;
  private final PlayerTurn executingPlayer;

  /**
   * A constructor for a PlayerEvent.
   *
   * @param playerEventType a playerEventType
   * @param description     a description
   * @param executingPlayer the executing player
   */
  public PlayerEvent(EventType playerEventType, String description,
                     PlayerTurn executingPlayer) {
    this.playerEventType = playerEventType;
    this.description = description;
    this.executingPlayer = executingPlayer;
  }

  /**
   * A getter that gets the description information of a player event.
   */
  public String getMessage() {
    return description;
  }

  /**
   * A getter that gets the exececuting player's player
   * type of player event.
   */
  public PlayerTurn getExecutingPlayer() {
    return executingPlayer;
  }

  @Override
  public EventType getEventType() {
    return this.playerEventType;
  }
}
