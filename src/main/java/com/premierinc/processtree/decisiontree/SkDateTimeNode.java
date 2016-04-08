package com.premierinc.processtree.decisiontree;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.InpDateTime;
import com.premierinc.processinput.core.LeftRightDateTime;
import com.premierinc.processtree.decisioninf.SkDateTimeInf;
import org.joda.time.DateTime;

/**
 *
 */
public class SkDateTimeNode implements SkDateTimeInf {

  private final InpDateTime logicBit;
  private DateTime leftSide;

  public SkDateTimeNode(final InpDateTime inInpDateTime) {
    this.logicBit = inInpDateTime;
  }

  @Override
  public void setLeftSide(Object inValue) {
    if (inValue instanceof DateTime) {
      setLeftSide((DateTime) inValue);
    } else {
      throw new SkException(String.format("Expected type of DateTime, got '%s'", leftSide.getClass().getName()));
    }
  }

  @Override
  public void addToLeftSideList(Object inValue) {
    throw new SkException("Not implemented.");
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
  public void setLeftSide(final DateTime inLeftSide) {
    this.leftSide = inLeftSide;
  }

  @Override
  public boolean test(LeftRightDateTime inLeftRightDateTime) {
    return this.logicBit.test(inLeftRightDateTime);
  }

  @Override
  public boolean test(DateTime inLeftSide) {
    return false;
  }

  @Override
  public boolean test() {
    if (null != this.leftSide) {
      return logicBit.test(this.leftSide);
    }
    throw new SkException(String.format("leftSide for Identity '%s' is null.", this.getIdentity().getName()));
  }
}
