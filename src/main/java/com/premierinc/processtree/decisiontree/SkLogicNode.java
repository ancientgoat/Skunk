package com.premierinc.processtree.decisiontree;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.InpLogic;
import com.premierinc.processtree.decisioninf.SkLogicInf;

/**
 *
 */
public class SkLogicNode implements SkLogicInf {

  private final InpLogic logicBit;

  public SkLogicNode(final InpLogic inInpLogic) {
    this.logicBit = inInpLogic;
  }

  @Override
  public boolean test() {

    final String name = this.logicBit.getIdentity().getName();


    return false;
  }

  @Override
  public String getDescription() {
    return this.logicBit.getDescription();
  }

  @Override
  public DecisionIdentity getIdentity() {
    return this.logicBit.getIdentity();
  }
}
