package cs3500.reversi;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.model.ReversiPiece;
import cs3500.reversi.player.AvoidNextToCornersStrategy;
import cs3500.reversi.player.CaptureMostStrategy;
import cs3500.reversi.player.HumanStrategy;
import cs3500.reversi.player.IPlayerMoveStrategy;
import cs3500.reversi.player.ManyStrategy;
import cs3500.reversi.player.PlayCornersStrategy;
import cs3500.reversi.player.Player;
import cs3500.reversi.player.Strategy;

public class ReversiArgParser {
  private int argIndex;
  private Player player1;
  private Player player2;

  public ReversiArgParser(String [] args) {
    this.argIndex = 0;
    IPlayerMoveStrategy strat1 = getStrategy(args);
    this.argIndex += 1;
    IPlayerMoveStrategy strat2 = getStrategy(args);
    this.player1 = new Player(new Strategy(strat1), ReversiPiece.BLACK);
    this.player2 = new Player(new Strategy(strat2), ReversiPiece.WHITE);
  }

  private IPlayerMoveStrategy getStrategy (String[] args) {
    if (this.argIndex >= args.length) {
      throw new IllegalArgumentException("Index out of bounds");
    }
    String input = args[this.argIndex].toUpperCase();
    switch (input) {
      case "HUMAN":
        return new HumanStrategy();
      case "STRATEGY1":
        return new CaptureMostStrategy();
      case "STRATEGY2":
        return new AvoidNextToCornersStrategy();
      case "STRATEGY3":
        return new PlayCornersStrategy();
      case "MANYSTRATEGY":
        this.argIndex +=1;
        int numStrategies = Integer.parseInt(args[this.argIndex]);
        List<IPlayerMoveStrategy> all = new ArrayList<>();
        for (int i = this.argIndex; i < numStrategies; i++) {
          all.add(getStrategy(args));
          this.argIndex+=1;
        }
        return new ManyStrategy(all);
      default:
        throw new IllegalArgumentException("Invalid Player Type");
    }
  }

  public Player getPlayer1(){
    return this.player1;
  }

  public Player getPlayer2(){
    return this.player2;
  }
}
