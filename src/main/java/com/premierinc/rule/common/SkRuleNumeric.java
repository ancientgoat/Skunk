package com.premierinc.rule.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.premierinc.common.enumeration.LogicOperatorEnum;
import com.premierinc.common.util.NumericLogicExecuterHelper;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.LeftRightNumeric;
import com.premierinc.rule.base.SkRuleBase;
import com.premierinc.rule.inf.SkRuleValueInf;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 *
 */
public class SkRuleNumeric extends SkRuleBase<BigDecimal> {

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

    /**
     *
     */
    public DecisionIdentity getIdentity() {
        return identity;
    }

    /**
     * This was made purposely as a thread safe method.
     */
    public final boolean test(final LeftRightNumeric inLeftRightNumeric) {
        boolean result = this.predicate.test(inLeftRightNumeric);
        return result;
    }

    /**
     * This was made purposely as a thread safe method.
     */
    public boolean test() {
        final LeftRightNumeric leftRightNumeric = new LeftRightNumeric(leftSide, this.rightNumericValue);
        return test(leftRightNumeric);
    }

    @JsonProperty
    public void setOperator(final LogicOperatorEnum inOperator) {
        this.operator = inOperator;
        this.predicate = NumericLogicExecuterHelper.buildPredicate(inOperator);
    }

    @Override
    public boolean evaluate() {
        return test();
    }

    @Override
    public void execute() throws Exception {
        System.out.println("SkRuleNumeric returns true!");
    }
}
