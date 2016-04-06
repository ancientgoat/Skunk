package com.premierinc.processtree.decisiontree;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.InpLogic;
import com.premierinc.processinput.core.LeftRight;
import com.premierinc.processtree.decisioninf.SkLogicInf;

import java.math.BigDecimal;

/**
 *
 */
public class SkLogicNode implements SkLogicInf {

  private final InpLogic logicBit;
  private BigDecimal leftSide;

  public SkLogicNode(final InpLogic inInpLogic) {
    this.logicBit = inInpLogic;
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
  public boolean test(LeftRight inLeftRight) {
    return logicBit.test(inLeftRight);
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
