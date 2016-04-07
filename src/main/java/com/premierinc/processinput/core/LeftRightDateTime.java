package com.premierinc.processinput.core;

import com.premierinc.common.enumeration.LogicOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.common.util.DateAdjuster;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 *
 */
public class LeftRightDateTime {

  private DateTime leftSide;
  private DateAdjuster dateAdjuster;
  private Integer adjusterValue;

  public LeftRightDateTime(final DateTime inLeft, final DateAdjuster inDateAdjuster,
                           final Integer inAdjusterValue) {
    this.leftSide = inLeft;
    this.dateAdjuster = inDateAdjuster;
    this.adjusterValue = inAdjusterValue;
  }

  public DateTime getLeftSide() {
    return leftSide;
  }

  public DateTime getRightSide() {

    DateTime rightSide;

    switch (dateAdjuster) {
      case DAYS_AGO:
        rightSide = DateTime.now().minusDays(adjusterValue);
        break;

      default:
        throw new SkException(String.format("DateAdjuster '%s' not implemented", dateAdjuster));
    }

    return rightSide;
  }
}
