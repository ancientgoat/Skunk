package com.premierinc.processinput.core;

/**
 *
 */
public class LeftRight<V extends Comparable>  {

  private V rightSide;
  private V leftSide;

  public LeftRight(final V inLeft, final V inRight){
    this.leftSide = inLeft;
    this.rightSide = inRight;
  }

  public V getLeftSide() {
    return leftSide;
  }

  public V getRightSide() {
    return rightSide;
  }
}
