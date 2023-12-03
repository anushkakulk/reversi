package cs3500.reversi.provider.controller;

import cs3500.reversi.provider.player.PlayerTurn;

public interface Event {

  String getMessage();
  PlayerTurn getExecutingPlayer();
  EventType getEventType();

}
