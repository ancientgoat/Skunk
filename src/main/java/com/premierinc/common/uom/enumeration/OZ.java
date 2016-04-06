package com.premierinc.common.uom.enumeration;

import java.math.BigDecimal;

/**
 *
 */
public enum OZ {
  OZ(1), CUP(8), PINT(16), QUART(32), HALF_GALLON(64), GALLON(128);

  private final BigDecimal numberOfOunces;

  OZ(final int inNumberOfOunces) {
    numberOfOunces = new BigDecimal(inNumberOfOunces);
  }

  public BigDecimal getNumberOfOunces() {
    return numberOfOunces;
  }

  public BigDecimal convertValue(final BigDecimal inValue){
    BigDecimal convertedValue = this.numberOfOunces.multiply(inValue);
    return convertedValue;
  }
}
