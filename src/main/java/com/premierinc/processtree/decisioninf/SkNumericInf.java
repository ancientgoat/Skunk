package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.LeftRightNumeric;

import java.math.BigDecimal;

/**
 *
 */
public interface SkNumericInf extends SkNodeInf {
  String getDescription();

  DecisionIdentity getIdentity();

  void setLeftSide(final BigDecimal leftSide);

  boolean test(final LeftRightNumeric inLeftRightNumeric);

  boolean test(final BigDecimal inLeftSide);
}
