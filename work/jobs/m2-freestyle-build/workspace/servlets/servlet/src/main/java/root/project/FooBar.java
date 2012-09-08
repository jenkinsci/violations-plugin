package root.project;

public class FooBar {

  private boolean FALSE = false;

  public void doNothing() {
    if (true == FALSE) {
      System.exit(0);
    }
  }

  public void assertReality() {
    FALSE = false;
  }
}
