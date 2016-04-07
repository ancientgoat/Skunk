package com.premierinc.processtree.decisiontree;

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
    return logicBit.test(this.leftSide);
  }
}
