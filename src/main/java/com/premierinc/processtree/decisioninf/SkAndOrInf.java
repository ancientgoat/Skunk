package com.premierinc.processtree.decisioninf;

import com.premierinc.common.util.LogicOperatorEnum;

/**
 *
 */
public interface SkAndOrInf extends SkNodeInf {
  SkAndOrInf add(final SkNodeInf inNode);
  LogicOperatorEnum getOperator();
}
