package com.premierinc.runner;

import com.premierinc.base.ProcessResult;
import com.premierinc.enumeration.LogicType;
import com.premierinc.util.OperatorEnum;
import javafx.util.Pair;

import java.util.function.Predicate;

/**
 *
 */
public class RunAndOr {

  Predicate<Pair<ProcessResult, ProcessResult>> predicate;

  private OperatorEnum operator;
  private boolean lastResult;
  private Pair<ProcessResult, ProcessResult> processPair = null;

  public LogicType getLogicType() {
    return LogicType.ANDOR;
  }

  public final boolean test() {
    this.lastResult = this.predicate.test(this.processPair);
    System.out.println(lastOutput());
    return this.lastResult;
  }

  public boolean getLastResult() {
    return lastResult;
  }

  public OperatorEnum getOperator() {
    return operator;
  }

  public Pair<ProcessResult, ProcessResult> getProcessPair() {
    return processPair;
  }

  public Predicate<Pair<ProcessResult, ProcessResult>> getPredicate() {
    return predicate;
  }

  public final String lastOutput() {
    return String.format("%s %s %s  Result:%s", this.processPair.getKey().getLastResult(), this.operator,
        this.processPair.getValue().getLastResult(), getLastResult());
  }

  /**
   *
   */
  public static class Builder {

    private RunAndOr myLogic = new RunAndOr();
    private ProcessResult processA;
    private ProcessResult processB;

    public final Builder setProcessA(final ProcessResult inProcess) {
      this.processA = inProcess;
      return this;
    }

    public final Builder setProcessB(final ProcessResult inProcess) {
      this.processB = inProcess;
      return this;
    }

    public final Builder setOperator(final OperatorEnum inOperator) {
      this.myLogic.operator = inOperator;
      return this;
    }

    private final void validateBuild() {
      if (null == this.processA) {
        throw new IllegalArgumentException("Missing processA, this processing requires ProcessA and processB.");
      }
      if (null == this.processB) {
        throw new IllegalArgumentException("Missing processB, this processing requires ProcessA and processB.");
      }
      if (null == this.myLogic.operator) {
        throw new IllegalArgumentException("Missing operator, this processing requires an 'operator'.");
      }
    }

    public final RunAndOr build() {
      validateBuild();
      this.myLogic.processPair = new Pair<ProcessResult, ProcessResult>(this.processA, this.processB);
      Predicate<Pair<ProcessResult, ProcessResult>> predicate;

      switch (this.myLogic.operator) {
        case OR:
          predicate = p -> p.getKey().getLastResult() || p.getValue().getLastResult();
          break;
        case AND:
          predicate = p -> p.getKey().getLastResult() && p.getValue().getLastResult();
          break;
        default:
          throw new IllegalStateException(String.format("Operator not implemented '%s'", this.myLogic.operator));
      }
      this.myLogic.predicate = predicate;
      return this.myLogic;
    }
  }
}
