package com.premierinc.processinput.coreinf;

import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processinput.base.RuleIdentity;

import java.util.List;

/**
 *
 */
public interface SkGroup {
  String getName();
  List<InpNodeBase> getDecisions();
}
