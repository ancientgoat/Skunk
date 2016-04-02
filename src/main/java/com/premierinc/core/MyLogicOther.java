package com.premierinc.core;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.base.MyDecisionBase;
import com.premierinc.base.ProcessResult;
import com.premierinc.enumeration.LogicType;
import com.premierinc.util.MyLogicExecuter;
import com.premierinc.util.OperatorEnum;

import java.util.function.Predicate;

/**
 *
 */
public class MyLogicOther<T extends Comparable<T>> extends MyDecisionBase implements ProcessResult {

  Predicate<MyLogicOther> predicate;

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  private T permValue = null;

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  private T tempValue = null;

  private OperatorEnum operator;
  private boolean lastResult;
  private MyLogicOther branchA = null;
  private MyLogicOther branchB = null;

  public LogicType getLogicType() {
    return LogicType.OTHER;
  }

  public T getPermValue() {
    return permValue;
  }

  public T getTempValue() {
    return tempValue;
  }

  public void setPermValue(T permValue) {
    this.permValue = permValue;
  }

  public void setOperator(OperatorEnum operator) {
    this.operator = operator;
  }

  public MyLogicOther<T> setTempValue(T tempValue) {
    this.tempValue = tempValue;
    return this;
  }

  public MyLogicOther<T> setBranchA(final MyLogicOther branchA) {
    this.branchA = branchA;
    return this;
  }

  public MyLogicOther<T> setBranchB(MyLogicOther branchB) {
    this.branchB = branchB;
    return this;
  }

  public final boolean testFast(final T inValue) {
    this.tempValue = inValue;
    return this.test();
  }

  public final boolean test() {
    if (haveValues()) {
      this.lastResult = this.predicate.test(this);
    } else if (haveBranches()) {
      this.branchA.test();
      this.branchB.test();
      this.lastResult = this.predicate.test(this);
    }
    System.out.println(lastOutput());
    return this.lastResult;
  }

  public boolean getLastResult() {
    return lastResult;
  }

  public OperatorEnum getOperator() {
    return operator;
  }

  public MyLogicOther getBranchA() {
    return branchA;
  }

  public MyLogicOther getBranchB() {
    return branchB;
  }

  public boolean isLastResult() {
    return lastResult;
  }

  public Predicate<MyLogicOther> getPredicate() {
    return predicate;
  }

  private final boolean haveValues() {
    return null != this.permValue && null != this.tempValue;
  }

  private final boolean haveBranches() {
    return null != this.branchA && null != this.branchB;
  }

  private final boolean havePermValue() {
    return null != this.permValue;
  }

  private final boolean haveBranchA() {
    return null != this.branchA;
  }

  public final String lastOutput() {
    if (haveValues()) {
      return String.format("%s %s %s  Result:%s", this.permValue, this.operator, this.tempValue, this.lastResult);
    } else if (haveBranches()) {
      return String.format("%s %s %s  Result:%s", this.branchA.getLastResult(), this.operator,
          this.branchB.getLastResult(), this.lastResult);
    }
    return "* Do not have values or branches *";
  }

  /**
   *
   */
  public static class Builder<T extends Comparable<T>> {

    private MyLogicOther myLogicOther = new MyLogicOther<T>();

    public final Builder setPermValue(final int inValue) {
      this.myLogicOther.permValue = inValue;
      return this;
    }

    public final Builder setTempValue(final int inValue) {
      this.myLogicOther.tempValue = inValue;
      return this;
    }

    public final Builder setBranchA(final MyLogicOther branchA) {
      this.myLogicOther.branchA = branchA;
      return this;
    }

    public Builder setBranchB(MyLogicOther branchB) {
      this.myLogicOther.branchB = branchB;
      return this;
    }

    public final Builder setOperator(final OperatorEnum inOperator) {
      this.myLogicOther.operator = inOperator;

      Predicate<MyLogicOther> predicate;

      switch (inOperator) {
        case LT:
          predicate = p -> MyLogicExecuter.lt((T) p.permValue, (T) p.tempValue);
          break;
        case GT:
          predicate = p -> MyLogicExecuter.gt((T) p.permValue, (T) p.tempValue);
          break;
        case EQ:
          predicate = p -> MyLogicExecuter.eq((T) p.permValue, (T) p.tempValue);
          break;
        case OR:
          predicate = p -> p.branchA.getLastResult() || p.branchB.getLastResult();
          break;
        case AND:
          predicate = p -> p.branchA.getLastResult() && p.branchB.getLastResult();
          break;
        default:
          throw new IllegalStateException(String.format("No such Operator '%s'", inOperator));
      }
      this.myLogicOther.predicate = predicate;
      return this;
    }

    private final void validateBuild() {
      switch (this.myLogicOther.operator) {
        case LT:
        case GT:
          if (!this.myLogicOther.havePermValue()) {
            throw new IllegalArgumentException(
                String.format("This operator '%s' requires a permValue.", this.myLogicOther.operator));
          }
          if (this.myLogicOther.haveBranchA()) {
            throw new IllegalArgumentException(
                String.format("This operator '%s' requires values, not " + "branches.",
                    this.myLogicOther.operator));
          }
          break;

        case OR:
        case AND:
          if (!this.myLogicOther.haveBranchA()) {
            throw new IllegalArgumentException(
                String.format("This operator '%s' requires a permValue.", this.myLogicOther.operator));
          }
          if (this.myLogicOther.havePermValue()) {
            throw new IllegalArgumentException(
                String.format("This operator '%s' requires branches, not " + "values.",
                    this.myLogicOther.operator));
          }
          break;

        default:
          throw new IllegalStateException(String.format("No such Operator '%s'", this.myLogicOther.operator));
      }
    }

    public final MyLogicOther<T> build() {
      validateBuild();
      return this.myLogicOther;
    }
  }
}
