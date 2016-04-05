package com.premierinc.processtree.decisioninf;

import com.premierinc.common.exception.SkException;

/**
 *
 */
public interface SkNodeInf {

  default boolean test() {
    throw new SkException(String.format("Please implement the method  'test()' in this class. (%s)",
        this.getClass().getName()));
  }
}
