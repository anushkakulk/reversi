package cs3500.reversi.provider.controller;

import cs3500.reversi.provider.player.PlayerTurn;

/**
 * Represents an event that occurs in a Reversi game.
 */
public interface Event {

  String getMessage();

  PlayerTurn getExecutingPlayer();

  EventType getEventType();

}
