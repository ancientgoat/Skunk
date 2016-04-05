package com.premierinc.processtree.decisioninf;

import com.premierinc.common.enumeration.AndOrOperatorEnum;

/**
 *
 */
public interface SkAndOrInf extends SkNodeInf {
  SkAndOrInf add(final SkNodeInf inNode);
  AndOrOperatorEnum getOperator();
  boolean test();
}
