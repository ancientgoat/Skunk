package com.premierinc.common.util;

import com.premierinc.common.enumeration.DateTimeOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.LeftRightDateTime;
import com.premierinc.processinput.core.LeftRightNumeric;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 */
public class DateTimeLogicExecuterHelper {

  private static final Map<DateTimeOperatorEnum, Predicate<LeftRightDateTime>> logicMap = new HashMap();

  static {
    logicMap.put(DateTimeOperatorEnum.LT, p -> lt(p.getLeftSide(), p.getRightSide()));
    logicMap.put(DateTimeOperatorEnum.LE, p -> le(p.getLeftSide(), p.getRightSide()));
    logicMap.put(DateTimeOperatorEnum.GT, p -> gt(p.getLeftSide(), p.getRightSide()));
    logicMap.put(DateTimeOperatorEnum.GE, p -> ge(p.getLeftSide(), p.getRightSide()));
    logicMap.put(DateTimeOperatorEnum.EQ, p -> eq(p.getLeftSide(), p.getRightSide()));
  }

  private DateTimeLogicExecuterHelper() {
  }

  public static Predicate buildPredicate(final DateTimeOperatorEnum inOperator, final DateAdjuster inDateAdjuster) {

    final Predicate<LeftRightDateTime> predicate = logicMap.get(inOperator);

    if (null == predicate) {
      throw new SkException(String.format("Operator '%s' not implemented yet.", inOperator));
    }
    return predicate;
  }

  /**
   * true if input 'a GT b'.
   */
  public static final boolean gt(final DateTime a, final DateTime b) {
    return 0 < a.compareTo(b);
  }

  /**
   * true if input 'a GE b'.
   */
  public static final boolean ge(final DateTime a, final DateTime b) {
    return 0 < a.compareTo(b);
  }

  /**
   * true if input 'a LT b'.
   */
  public static final boolean lt(final DateTime a, final DateTime b) {
    return 0 > a.compareTo(b);
  }

  /**
   * true if input 'a LE b'.
   */
  public static final boolean le(final DateTime a, final DateTime b) {
    return 0 > a.compareTo(b);
  }

  /**
   * true if input 'a EQ b'.
   */
  public static final boolean eq(final DateTime a, final DateTime b) {
    return 0 == a.compareTo(b);
  }
}
