package de.jpx3.intave.share;

public class Input {
  private boolean forward;
  private boolean backward;
  private boolean left;
  private boolean right;
  private boolean jump;
  private boolean shift;
  private boolean sprint;

  public int forwardKey() {
    return forward ? 1 : backward ? -1 : 0;
  }

  public int sidewaysKey() {
    return left ? 1 : right ? -1 : 0;
  }

  public boolean forward() {
    return forward;
  }

  public boolean backward() {
    return backward;
  }

  public boolean left() {
    return left;
  }

  public boolean right() {
    return right;
  }

  public boolean jump() {
    return jump;
  }

  public boolean shift() {
    return shift;
  }

  public boolean sprint() {
    return sprint;
  }

  public void setForward(boolean forward) {
    this.forward = forward;
  }

  public void setBackward(boolean backward) {
    this.backward = backward;
  }

  public void setLeft(boolean left) {
    this.left = left;
  }

  public void setRight(boolean right) {
    this.right = right;
  }

  public void setJump(boolean jump) {
    this.jump = jump;
  }

  public void setShift(boolean shift) {
    this.shift = shift;
  }

  public void setSprint(boolean sprint) {
    this.sprint = sprint;
  }
}
