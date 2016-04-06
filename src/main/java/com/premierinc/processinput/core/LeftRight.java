package com.premierinc.processinput.core;

import org.springframework.util.comparator.ComparableComparator;

/**
 *
 */
//public class LeftRight<V extends Comparable>  {
public class LeftRight  {

  private Comparable rightSide;
  private Comparable leftSide;

  public LeftRight(final Comparable inLeft, final Comparable inRight){
    this.leftSide = inLeft;
    this.rightSide = inRight;
  }

  public Comparable getLeftSide() {
    return leftSide;
  }

  public Comparable getRightSide() {
    return rightSide;
  }
}
