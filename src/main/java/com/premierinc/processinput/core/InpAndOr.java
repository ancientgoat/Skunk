package com.premierinc.processinput.core;

import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.common.enumeration.InpType;
import com.premierinc.common.util.OperatorEnum;

/**
 *
 */
public class InpAndOr extends InpNodeBase {

  private OperatorEnum operator;

  public InpType getLogicType() {
    return InpType.ANDOR;
  }

  public OperatorEnum getOperator() {
    return operator;
  }

  /**
   *
   */
  public static class Builder<T extends Comparable<T>> {

    private InpAndOr myLogic = new InpAndOr();

    public final Builder setOperator(final OperatorEnum inOperator) {
      this.myLogic.operator = inOperator;
      return this;
    }

    private final void validateBuild() {
      if (null == this.myLogic.operator) {
        throw new IllegalArgumentException("Missing operator, this processing requires an 'operator'.");
      }
    }

    public final InpAndOr build() {
      validateBuild();
      return this.myLogic;
    }
  }
}
