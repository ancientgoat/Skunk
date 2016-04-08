package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;

/**
 *
 */
public interface SkValueInf extends SkNodeInf {

  void setLeftSide(final Object inValue);
  void addToLeftSideList(final Object inValue);
  DecisionIdentity getIdentity();
  String getDescription();

}
