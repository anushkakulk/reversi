package cs3500.reversi.provider.strategy;

import java.util.List;

/**
 * Represents a General Interface for a Strategy Implementation.
 */
public interface IStrategy {

  /**
   * execute a strategy based on the strategy implementation.
   *
   * @return a position as a List for the model to execute.
   */
  List<Integer> executeStrategy();

  /**
   * Gets a strategies' implementation type.
   */
  StrategyType getStrategyType();

}
