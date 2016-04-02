package com.premierinc.runner;

import com.premierinc.base.MyDecisionBase;
import com.premierinc.base.ProcessResult;
import com.premierinc.core.MyLogicAndOr;
import com.premierinc.core.RuleChain;

import java.util.List;
import java.util.Stack;

/**
 *
 */
public class RuleOrganizer {

  private RuleChain ruleChain;
  private Stack<MyDecisionBase> decisionStack;
  private List<RunAndOr> andOrList;

  public RuleOrganizer(final RuleChain inChain) {
    this.ruleChain = inChain;
    this.decisionStack = this.ruleChain.getRules();
    _initialize();
  }

  private void _initialize() {

    while (0 < this.decisionStack.size()) {

      final MyDecisionBase decisionA = this.decisionStack.pop();

      if (decisionA instanceof MyLogicAndOr) {
        throw new IllegalArgumentException(String.format("Should not have AndOr as first element : %s", decisionA.dumpToString()));
      }
      if (!(decisionA instanceof ProcessResult)) {
        throw new IllegalArgumentException(String.format("First element should be a 'ProcessResult' : %s",
            decisionA.dumpToString()));
      }

      final MyDecisionBase decisionB = this.decisionStack.pop();

      if (decisionA instanceof MyLogicAndOr) {
        throw new IllegalArgumentException(String.format("Should not have AndOr as second element : %s",
            decisionB.dumpToString()));
      }
      if (!(decisionB instanceof ProcessResult)) {
        throw new IllegalArgumentException(String.format("Second element should be a 'ProcessResult' : %s",
            decisionB.dumpToString()));
      }

      final MyDecisionBase hopefullyAndOr = this.decisionStack.pop();

      if (!(hopefullyAndOr instanceof MyLogicAndOr)) {
        throw new IllegalArgumentException(String.format("SHOULD have AndOr as third element : %s",
            hopefullyAndOr.dumpToString()));
      }

      final RunAndOr runAndOr = new RunAndOr.Builder()
          .setProcessA((ProcessResult) decisionA)
          .setProcessB((ProcessResult) decisionB)
          .setOperator( hopefullyAndOr.getOperator())
          .build();

      this.andOrList.add(runAndOr);
    }
  }
}
