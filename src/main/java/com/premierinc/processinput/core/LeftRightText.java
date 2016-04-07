package com.premierinc.processinput.core;

import java.util.List;

/**
 *
 */
public class LeftRightText {

  private String rightSide;
  private String leftSide;
  private List<String> rightList;

  public LeftRightText(final String inLeft, final String inRight) {
    this.leftSide = inLeft;
    this.rightSide = inRight;
  }

  public LeftRightText(final String inLeft, final List<String> inRightList) {
    this.leftSide = inLeft;
    this.rightList = inRightList;
  }

  public String getLeftSide() {
    return leftSide;
  }

  public String getRightSide() {
    return rightSide;
  }

  public List<String> getRightList() {
    return rightList;
  }
}
