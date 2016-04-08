package com.premierinc.processinput.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.premierinc.common.enumeration.LogicOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.common.util.NumericLogicExecuterHelper;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.base.InpNodeBase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
public class InpNumeric extends InpNodeBase {

  private Predicate<LeftRightNumeric> predicate;

  /**
   * Hopefully someone was smart enough to give a decent description of our Logic bit.
   */
  private String description;

  /**
   * Operator we are to apply to our logic bit.
   */
  private LogicOperatorEnum operator;

  /**
   * This is the right side of the logic operation, a permanent value entered
   * with the original formula.
   */
  @JsonProperty("value")
  private BigDecimal rightNumericValue = null;

  /**
   *
   */
  private DecisionIdentity identity;

  @JsonProperty
  public void setOperator(final LogicOperatorEnum inOperator) {
    this.operator = inOperator;
    this.predicate = NumericLogicExecuterHelper.buildPredicate(inOperator);
  }

  public BigDecimal getRightNumericValue() {
    return rightNumericValue;
  }

  public LogicOperatorEnum getOperator() {
    return operator;
  }

  public Predicate<LeftRightNumeric> getPredicate() {
    return predicate;
  }

  /**
   * This was made purposely as a thread safe method.
   */
  public final boolean test(final LeftRightNumeric inLeftRightNumeric) {
    boolean result = this.predicate.test(inLeftRightNumeric);
    // System.out.println(lastOutput(inLeftRightNumeric, result));
    return result;
  }

  /**
   * This was made purposely as a thread safe method.
   */
  public boolean test(final BigDecimal inLeftValue) {
    final LeftRightNumeric leftRightNumeric = new LeftRightNumeric(inLeftValue, this.rightNumericValue);
    return test(leftRightNumeric);
    //boolean result = this.predicate.test(leftRightNumeric);
    //System.out.println(lastOutput(leftRightNumeric, result));
    //return result;
  }

  public final String lastOutput(final LeftRightNumeric inLeftRightNumeric, final boolean inResult) {
    return String.format("%s %s %s  Result: %s", inLeftRightNumeric.getLeftSide(), this.operator,
        inLeftRightNumeric.getRightSide(), inResult);
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
    if (null == this.rightNumericValue) {
      throw new SkException(String.format("Missing value.  Value is required.\n%s",
          this.dumpToJsonString()));
    }
  }
}
