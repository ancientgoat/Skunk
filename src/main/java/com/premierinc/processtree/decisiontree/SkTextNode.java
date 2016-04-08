package com.premierinc.processtree.decisiontree;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.InpText;
import com.premierinc.processinput.core.LeftRightText;
import com.premierinc.processtree.decisioninf.SkTextInf;

import java.util.List;

/**
 *
 */
public class SkTextNode implements SkTextInf {

  private final InpText logicBit;
  private String leftSide;
  private List<String> leftSideList;

  public SkTextNode(final InpText inInpText) {
    this.logicBit = inInpText;
  }

  @Override
  public void setLeftSide(Object inValue) {
    if (inValue instanceof String) {
      setLeftSide((String) inValue);
    } else {
      throw new SkException(String.format("Expected type of String, got '%s'", leftSide.getClass().getName()));
    }
  }

  @Override
  public void addToLeftSideList(Object inValue) {
    if (inValue instanceof String) {
      addToLeftSideList((String) inValue);
    } else {
      throw new SkException(String.format("Expected type of String, got '%s'", leftSide.getClass().getName()));
    }
  }

  @Override
  public String getDescription() {
    return this.logicBit.getDescription();
  }

  @Override
  public DecisionIdentity getIdentity() {
    return this.logicBit.getIdentity();
  }

  @Override
  public void setLeftSide(final String inLeftSide) {
    this.leftSide = inLeftSide;
  }

  @Override
  public void addToLeftSideList(final String inValue) {
    this.leftSideList.add(inValue);
  }

  @Override
  public boolean test(String inLeftSide) {
    return false;
  }

  @Override
  public boolean test(List<String> inLeftSide) {
    return false;
  }


  @Override
  public boolean test(LeftRightText inLeftRightText) {
    return logicBit.test(inLeftRightText);
  }

  @Override
  public boolean test() {
    if (null != this.leftSide) {
      return logicBit.test(this.leftSide);
    }
    throw new SkException(String.format("leftSide for Identity '%s' is null.", this.getIdentity().getName()));
  }
}
