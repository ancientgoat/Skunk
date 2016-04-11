package com.premierinc.rule.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.processinput.core.*;
import com.premierinc.rule.common.SkRuleNumeric;
import com.premierinc.rule.inf.SkRuleValueInf;
import org.easyrules.api.Rule;
import org.easyrules.core.BasicRule;

/**
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SkRuleNumeric.class, name = "numeric"),
})
public abstract class SkRuleBase<T extends Comparable> extends BasicRule implements SkRuleValueInf { //implements Rule {

//    private String name;
//    private String description;
//    private int priority = 99;
    private boolean lastResult = false;

    protected T leftSide;

    public T getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(T leftSide) {
        this.leftSide = leftSide;
    }

    //    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public String getDescription() {
//        return null;
//    }
//
//    @Override
//    public int getPriority() {
//        return 0;
//    }

    protected void setLastResult(final boolean inResult){
        this.lastResult = inResult;
    }

    public boolean isLastResult() {
        return lastResult;
    }

    abstract public boolean evaluate();
//    @Override
//    public boolean evaluate();
//        return false;
//    }

    abstract public void execute() throws Exception;
//    @Override
//    public void execute() throws Exception {
//    }
}
