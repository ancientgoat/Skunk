package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.LeftRight;

import java.math.BigDecimal;

/**
 *
 */
public interface SkLogicInf extends SkNodeInf {
  String getDescription();

  DecisionIdentity getIdentity();

  void setLeftSide(final BigDecimal leftSide);

  boolean test(final LeftRight inLeftRight);

  boolean test(final BigDecimal inLeftSide);
}
