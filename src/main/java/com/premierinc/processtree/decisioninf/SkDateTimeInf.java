package com.premierinc.processtree.decisioninf;

import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.core.LeftRightDateTime;
import com.premierinc.processinput.core.LeftRightText;
import org.joda.time.DateTime;

import java.util.List;

/**
 *
 */
public interface SkDateTimeInf extends SkValueInf {

  String getDescription();

  DecisionIdentity getIdentity();

  void setLeftSide(final DateTime leftSide);

  boolean test(final LeftRightDateTime inLeftRightDateTime);

  boolean test(final DateTime inLeftSide);
}
