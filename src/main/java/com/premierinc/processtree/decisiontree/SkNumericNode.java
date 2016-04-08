package com.premierinc.processtree.decisiontree;

import com.premierinc.common.exception.SkException;
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
  public void addToLeftSideList(Object inValue) {

  }

  @Override
  public void setLeftSide(final Object leftSide) {
    try {
      setLeftSide(new BigDecimal(leftSide.toString()));
    }catch(Exception e){
      throw new SkException(String.format("Expected numeric type of some kind, but got '%s'", leftSide.getClass().getName()));
    }
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
    if (null != this.leftSide) {
      return logicBit.test(this.leftSide);
    }
    throw new SkException(String.format("leftSide for Identity '%s' is null.", this.getIdentity().getName()));

  }

}
