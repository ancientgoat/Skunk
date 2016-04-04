package com.premierinc.processrun.runenum;

/**
 *
 */
public enum EvenOdd {
  EVEN, ODD;

  public EvenOdd toggle() {
    if (this == EVEN) {
      return ODD;
    }
    return EVEN;
  }
}
