package com.premierinc.processinput.core;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.common.enumeration.InpType;
import com.premierinc.common.exception.SkException;
import com.premierinc.common.util.MyLogicExecuter;
import com.premierinc.common.util.OperatorEnum;
import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processinput.coreinf.SkTestable;

import java.util.function.Predicate;

/**
 * A simple piece of boolean logic "LeftSide Operator RightSide", will return true or false
 * The Operator and RightSide are permanent and entered with the formula, the left side
 * is entered later - the logic executed.  And can be used over and over again, with a new
 * Left Side Variable entered each time.
 * <p>
 * ex.  X < 17,  X is the variable that gets filled in later and "< 17" is the permanent
 * operator and right side variable.
 */
public class InpLogic<T extends Comparable<T>> extends InpNodeBase
    implements SkTestable {

  Predicate<InpLogic> predicate;

  /**
   * Hopefully someone was smart enough to give a decent description of our Logic bit.
   */
  private String description;

  /**
   * This is the dynamic value entered on the fly, to use when executing this logic.
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  private T leftValue = null;

  /**
   * Operator we are to apply to our logic bit.
   */
  private OperatorEnum operator;

  /**
   * This is the right side of the logic operation, a permanent value entered
   * with the original formula.
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  private T rightValue = null;

  /**
   * The result of our logic when executed.
   *
   * @see #test
   */
  private boolean result;

  public InpType getInpType() {
    return InpType.LOGIC;
  }

  public T getLeftValue() {
    return leftValue;
  }

  public T getRightValue() {
    return rightValue;
  }

  public void setLeftValue(T leftValue) {
    this.leftValue = leftValue;
  }

  public boolean getResult() {
    return result;
  }

  public OperatorEnum getOperator() {
    return operator;
  }

  public Predicate<InpLogic> getPredicate() {
    return predicate;
  }

  public final boolean testFast(final T inValue) {
    this.leftValue = inValue;
    return this.test();
  }

  public final boolean test() {
    this.result = this.predicate.test(this);
    System.out.println(lastOutput());
    return this.result;
  }

  public final String lastOutput() {
    return String.format("%s %s %s  Result:%s", this.leftValue, this.operator, this.rightValue, this.result);
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   *
   */
  public static class Builder<T extends Comparable<T>> {

    private InpLogic inpLogic = new InpLogic<T>();

    public final Builder setValue(final int inValue) {
      this.inpLogic.rightValue = inValue;
      return this;
    }

    public final Builder setOperator(final OperatorEnum inOperator) {
      this.inpLogic.operator = inOperator;
      Predicate<InpLogic> predicate;
      switch (inOperator) {
        case LT:
          predicate = p -> MyLogicExecuter.lt((T) p.leftValue, (T) p.rightValue);
          break;
        case GT:
          predicate = p -> MyLogicExecuter.gt((T) p.leftValue, (T) p.rightValue);
          break;
        case EQ:
          predicate = p -> MyLogicExecuter.eq((T) p.leftValue, (T) p.rightValue);
          break;
        default:
          throw new IllegalStateException(String.format("No such Operator '%s'", inOperator));
      }
      this.inpLogic.predicate = predicate;
      return this;
    }

    /**
     *
     */
    private final void validateBuild() {
      if (null == this.inpLogic.operator) {
        throw new SkException(String.format("Missing operator.  Operator is required.\n%s",
            this.inpLogic.dumpToJsonString()));
      }
      if (null == this.inpLogic.rightValue) {
        throw new SkException(String.format("Missing value.  Value is required.\n%s",
            this.inpLogic.dumpToJsonString()));
      }
    }

    /**
     *
     */
    public final InpLogic<T> build() {
      validateBuild();
      return this.inpLogic;
    }
  }
}
