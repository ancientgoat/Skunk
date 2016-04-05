package com.premierinc.processinput.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.common.enumeration.InpType;
import com.premierinc.common.enumeration.LogicOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.common.util.LogicExecuterHelper;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.base.InpNodeBase;

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
public class InpLogic<T extends Comparable<T>> extends InpNodeBase {

  private Predicate<LeftRight> predicate;

  /**
   * Hopefully someone was smart enough to give a decent description of our Logic bit.
   */
  private String description;

  //  /**
  //   * This is the dynamic value entered on the fly, to use when executing this logic.
  //   */
  //  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  //  private T leftValue = null;
  //
  /**
   * Operator we are to apply to our logic bit.
   */
  private LogicOperatorEnum operator;

  /**
   * This is the right side of the logic operation, a permanent value entered
   * with the original formula.
   */
  @JsonProperty("value")
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  private T rightValue = null;

  /**
   *
   */
  private DecisionIdentity identity;

  @JsonProperty
  public void setOperator(final LogicOperatorEnum inOperator) {
    this.operator = inOperator;
    this.predicate = LogicExecuterHelper.buildPredicate(inOperator);
  }


  public InpType getInpType() {
    return InpType.LOGIC;
  }

  public T getRightValue() {
    return rightValue;
  }

  public LogicOperatorEnum getOperator() {
    return operator;
  }

  public Predicate<LeftRight> getPredicate() {
    return predicate;
  }

  /**
   * This was made purposely as a thread safe method.
   */
  public final boolean test(final LeftRight inLeftRight) {


    boolean result = this.predicate.test(inLeftRight);
    System.out.println(lastOutput(inLeftRight, result));
    return result;
  }

  /**
   * This was made purposely as a thread safe method.
   */
  public boolean test(final Comparable inLeftValue) {


    final LeftRight leftRight = new LeftRight(inLeftValue, this.rightValue);
    boolean result = this.predicate.test(leftRight);
    System.out.println(lastOutput(leftRight, result));
    return result;
  }

  public final String lastOutput(final LeftRight inLeftRight, final boolean inResult) {
    return String.format("%s %s %s  Result:%s", inLeftRight.getLeftSide(), this.operator,
        inLeftRight.getRightSide(), inResult);
  }

  public String getDescription() {
    return this.description;
  }

  public DecisionIdentity getIdentity() {
    return this.identity;
  }

  /**
   *
   */
  private final void validateBuild() {
    if (null == this.operator) {
      throw new SkException(String.format("Missing operator.  Operator is required.\n%s",
          this.dumpToJsonString()));
    }
    if (null == this.rightValue) {
      throw new SkException(String.format("Missing value.  Value is required.\n%s",
          this.dumpToJsonString()));
    }
  }
}
