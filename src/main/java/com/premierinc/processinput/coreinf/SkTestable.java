package com.premierinc.processinput.coreinf;

import com.premierinc.processinput.base.RuleIdentity;

/**
 *
 */
public interface SkTestable {
  String getDescription();
  RuleIdentity getIdentity();
  boolean test();
}
