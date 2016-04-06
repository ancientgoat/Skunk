package com.premierinc.common.util;

import com.premierinc.common.enumeration.LogicOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.LeftRight;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 */
public class LogicExecuterHelper {

  private static final Map<LogicOperatorEnum, Predicate<LeftRight>> logicMap = new HashMap();

  static {
    logicMap.put(LogicOperatorEnum.LT, p -> LogicExecuterHelper.lt(p.getLeftSide(), p.getRightSide()));
    logicMap.put(LogicOperatorEnum.GT, p -> LogicExecuterHelper.gt(p.getLeftSide(), p.getRightSide()));
    logicMap.put(LogicOperatorEnum.EQ, p -> LogicExecuterHelper.eq(p.getLeftSide(), p.getRightSide()));
  }

  private LogicExecuterHelper() {
  }

  public static Predicate buildPredicate(final LogicOperatorEnum inOperator) {
    final Predicate<LeftRight> predicate = logicMap.get(inOperator);

    if (null == predicate) {
      throw new SkException(String.format("Operator '%s' not implemented yet.", inOperator));
    }
    return predicate;
  }

  /**
   * true if input 'a GT b'.
   */
  //public static final <T extends BigDecimal<T>> boolean gt(final T a, final T b) {
  public static final boolean gt(final BigDecimal a, final BigDecimal b) {
    return 0 < a.compareTo(b);
  }

  /**
   * true if input 'a LT b'.
   */
  //public static final <T extends BigDecimal<T>> boolean lt(final BigDecimal a, final BigDecimal b) {
  public static final boolean lt(final BigDecimal a, final BigDecimal b) {
    return 0 > a.compareTo(b);
  }

  /**
   * true if input 'a EQ b'.
   */
  //public static final <T extends BigDecimal<T>> boolean eq(final T a, final T b) {
  public static final boolean eq(final BigDecimal a, final BigDecimal b) {
    return 0 == a.compareTo(b);
  }
}
