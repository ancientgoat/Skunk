package com.premierinc.processtree.decisiontree;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.InpNumeric;
import com.premierinc.processinput.core.LeftRightNumeric;
import com.premierinc.processtree.decisioninf.SkNumericInf;

import java.math.BigDecimal;

/**
 *
 */
public class SkNumericNode implements SkNumericInf {

  private final InpNumeric logicBit;
  private BigDecimal leftSide;

  public SkNumericNode(final InpNumeric inInpNumeric) {
    this.logicBit = inInpNumeric;
  }

  @Override
  public void setLeftSide(final BigDecimal leftSide) {
    this.leftSide = leftSide;
  }

  @Override
  public String getDescription() {
    return this.logicBit.getDescription();
  }

  @Override
  public DecisionIdentity getIdentity() {
    return this.logicBit.getIdentity();
  }


  @Override
  public boolean test(LeftRightNumeric inLeftRightNumeric) {
    return logicBit.test(inLeftRightNumeric);
  }

  @Override
  public boolean test(BigDecimal inLeftSide) {
    return logicBit.test(inLeftSide);
  }

  @Override
  public boolean test() {
    return logicBit.test(this.leftSide);
  }
}
