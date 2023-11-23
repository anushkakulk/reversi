package cs3500.reversi.player;

import cs3500.reversi.model.ReversiModel;

public class HumanChoice implements IPlayerMove {

  public HumanChoice() {
    // represents a human move;
  }


  @Override
  public void run(ReversiModel model) {
    // do nothing as of now
  }

  @Override
  public void notifyPlayer(Player p) {
  // do nothing since, the player shouldn't notify the controller, the view should notify
  }
}
