package com.premierinc.processinput.core;

import java.math.BigDecimal;

/**
 *
 */
public class LeftRightNumeric {

  private BigDecimal rightSide;
  private BigDecimal leftSide;

  public LeftRightNumeric(final BigDecimal inLeft, final BigDecimal inRight){
    this.leftSide = inLeft;
    this.rightSide = inRight;
  }

  public BigDecimal getLeftSide() {
    return leftSide;
  }

  public BigDecimal getRightSide() {
    return rightSide;
  }
}
