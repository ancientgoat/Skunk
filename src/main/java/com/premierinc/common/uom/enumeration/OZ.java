package com.premierinc.common.uom.enumeration;

/**
 *
 */
public enum OZ {
  OZ(1), CUP(8), PINT(16), QUART(32), HALF_GALLON(64), GALLON(128);

  private final int numberOfOunces;

  OZ(final int inNumberOfOunces) {
    numberOfOunces = inNumberOfOunces;
  }

  public int getNumberOfOunces() {
    return numberOfOunces;
  }

  public <v extends Number, Comparable> V convertValue(final V inValue){
    V convertedValue = this.numberOfOunces * inValue;
    return convertedValue;
  }
}
