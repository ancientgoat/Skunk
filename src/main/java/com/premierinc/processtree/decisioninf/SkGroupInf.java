package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.InpNodeBase;

import java.util.List;

/**
 *
 */
public interface SkGroupInf extends SkNodeInf {
  String getName();

  List<InpNodeBase> getDecisions();
}
