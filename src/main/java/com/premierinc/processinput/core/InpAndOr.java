package com.premierinc.processinput.core;

import com.premierinc.common.enumeration.AndOrOperatorEnum;
import com.premierinc.processinput.base.InpNodeBase;

/**
 *
 */
public class InpAndOr extends InpNodeBase {

  private AndOrOperatorEnum operator;

  public AndOrOperatorEnum getOperator() {
    return operator;
  }

  /**
   *
   */
  public static class Builder {

    private InpAndOr myLogic = new InpAndOr();

    public final Builder setOperator(final AndOrOperatorEnum inOperator) {
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
