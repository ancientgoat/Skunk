package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.LeftRight;

/**
 *
 */
public interface SkLogicInf<V extends Comparable> extends SkNodeInf {
  String getDescription();

  DecisionIdentity getIdentity();

  void setLeftSide(final V leftSide);

  boolean test(final LeftRight inLeftRight);

  boolean test(final V inLeftSide);
}
