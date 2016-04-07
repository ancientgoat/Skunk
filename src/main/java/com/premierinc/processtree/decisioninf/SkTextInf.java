package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.LeftRightNumeric;
import com.premierinc.processinput.core.LeftRightText;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public interface SkTextInf extends SkNodeInf {
  String getDescription();

  DecisionIdentity getIdentity();

  void setLeftSide(final String leftSide);

  void addToLeftSideList(final String inValue);

  boolean test(final LeftRightText inLeftRightText);

  boolean test(final String inLeftSide);

  boolean test(final List<String> inLeftSide);
}
