package com.premierinc.core;

import com.premierinc.base.MyDecisionBase;
import com.premierinc.enumeration.LogicType;
import com.premierinc.util.OperatorEnum;

/**
 *
 */
public class MyLogicAndOr extends MyDecisionBase  {

  private OperatorEnum operator;

  public LogicType getLogicType() {
    return LogicType.ANDOR;
  }

  public OperatorEnum getOperator() {
    return operator;
  }

  /**
   *
   */
  public static class Builder<T extends Comparable<T>> {

    private MyLogicAndOr myLogic = new MyLogicAndOr();

    public final Builder setOperator(final OperatorEnum inOperator) {
      this.myLogic.operator = inOperator;
      return this;
    }

    private final void validateBuild() {
      if (null == this.myLogic.operator) {
        throw new IllegalArgumentException("Missing operator, this processing requires an 'operator'.");
      }
    }

    public final MyLogicAndOr build() {
      validateBuild();
      return this.myLogic;
    }
  }
}
