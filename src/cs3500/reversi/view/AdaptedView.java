package cs3500.reversi.view;

import cs3500.reversi.controller.PlayerActionFeatures;
import cs3500.reversi.model.ReadOnlyReversiModel;
import cs3500.reversi.provider.controller.Listener;

public class AdaptedView extends ReversiGUIView implements cs3500.reversi.provider.view.ReversiView {
  private final cs3500.reversi.provider.view.ReversiView delegate;

  public AdaptedView(cs3500.reversi.provider.view.ReversiView delegate, ReadOnlyReversiModel m) {
    super(m);
    this.delegate = delegate;
  }

  public void addListener( Listener listener) {
    delegate.addListener(listener);
  }

  @Override
  public void addPlayerActionListener(PlayerActionFeatures listener) {
    this.addListener(listener);
  }

  @Override
  public void update() {
    this.render();
  }

  @Override
  public void displayMessage(String message) {
    delegate.showPopup(message);
  }

  @Override
  public void displayTitle(String titleMessage) {

  }

  @Override
  public void render() {
    delegate.render();
  }

  @Override
  public void showPopup(String message) {
    delegate.showPopup(message);
  }
}
