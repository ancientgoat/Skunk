package com.premierinc.processtree.decisiontree;

import com.premierinc.common.exception.SkException;
import com.premierinc.common.util.LogicOperatorEnum;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.InpAndOr;
import com.premierinc.processinput.core.InpLogic;
import com.premierinc.processtree.decisioninf.SkAndOrInf;
import com.premierinc.processtree.decisioninf.SkLogicInf;
import com.premierinc.processtree.decisioninf.SkNodeInf;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SkAndOrNode implements SkAndOrInf {

  private final InpAndOr andOr;
  private List<SkNodeInf> nodeList = Collections.emptyList();

  public SkAndOrNode(final InpAndOr inInpAndOr) {
    if (null == inInpAndOr) {
      throw new SkException("SkAndOrNode input can not be null.");
    }
    this.andOr = inInpAndOr;
  }

  public SkAndOrInf add(final SkNodeInf inNode) {
    nodeList.add(inNode);
    return this;
  }

  @Override
  public LogicOperatorEnum getOperator() {
    return andOr.getOperator();
  }

  @Override
  public boolean test() {


    return false;
  }

}
