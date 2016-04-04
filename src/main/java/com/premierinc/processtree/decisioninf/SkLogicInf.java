package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;

/**
 *
 */
public interface SkLogicInf extends SkNodeInf {
  String getDescription();

  DecisionIdentity getIdentity();
}
