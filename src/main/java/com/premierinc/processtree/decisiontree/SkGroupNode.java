package com.premierinc.processtree.decisiontree;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processinput.core.InpGroup;
import com.premierinc.processinput.core.InpLogic;
import com.premierinc.processtree.decisioninf.SkGroupInf;
import com.premierinc.processtree.decisioninf.SkLogicInf;

import java.util.List;

/**
 *
 */
public class SkGroupNode implements SkGroupInf {

  private InpGroup group;

  public SkGroupNode(final InpGroup inGroup) {
    this.group = inGroup;
  }

  @Override
  public String getName() {
    return this.group.getName();
  }

  @Override
  public List<InpNodeBase> getDecisions() {
    return this.group.getDecisions();
  }

  @Override
  public boolean test() {
    return false;
  }
}
