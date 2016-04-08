package com.premierinc.processinput.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.premierinc.common.enumeration.TextOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.common.util.TextLogicExecuterHelper;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.base.InpNodeBase;

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
public class InpText extends InpNodeBase {

  private Predicate<LeftRightText> predicate;

  /**
   * Hopefully someone was smart enough to give a decent description of our Logic bit.
   */
  private String description;

  /**
   * Operator we are to apply to our logic bit.
   */
  private TextOperatorEnum operator;

  /**
   * This is the right side of the logic operation, a permanent value entered
   * with the original formula.
   */
  @JsonProperty("value")
  private String rightTextValue = null;

  @JsonProperty("list")
  private List<String> rightTextList = null;

  /**
   *
   */
  private DecisionIdentity identity;

  @JsonProperty
  public void setOperator(final TextOperatorEnum inOperator) {
    this.operator = inOperator;
    this.predicate = TextLogicExecuterHelper.buildPredicate(inOperator);
  }

  public List<String> getRightTextList() {
    return rightTextList;
  }

  public String getRightTextValue() {
    return rightTextValue;
  }

  public TextOperatorEnum getOperator() {
    return operator;
  }

  public Predicate<LeftRightText> getPredicate() {
    return predicate;
  }

  /**
   * This was made purposely as a thread safe method.
   */
  public final boolean test(final LeftRightText inLeftRightText) {
    boolean result = this.predicate.test(inLeftRightText);
    // System.out.println(lastOutput(inLeftRightText, result));
    return result;
  }

  /**
   * This was made purposely as a thread safe method.
   */
  public boolean test(final String inLeftValue) {
    LeftRightText leftRight = null;
    if (null == this.rightTextValue) {
      leftRight = new LeftRightText(inLeftValue, this.rightTextList);
    } else {
      leftRight = new LeftRightText(inLeftValue, this.rightTextValue);
    }
    return test(leftRight);
  }

  public final String lastOutput(final LeftRightText inLeftRightText, final boolean inResult) {
    return String.format("%s %s %s  Result: %s", inLeftRightText.getLeftSide(), this.operator,
        inLeftRightText.getRightSide(), inResult);
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
    if (null == this.rightTextValue) {
      throw new SkException(String.format("Missing value.  Value is required.\n%s",
          this.dumpToJsonString()));
    }
  }
}
