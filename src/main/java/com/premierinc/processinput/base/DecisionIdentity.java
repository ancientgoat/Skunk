package com.premierinc.processinput.base;

import com.premierinc.common.uom.UomBase;

/**
 *
 */
public class DecisionIdentity {

  private String name;
  private String uom;
  private UomBase uomBase;

  public UomBase getUomBase() {
    return uomBase;
  }

  public String getName(){
    return this.name;
  }

  public String getUom(){
    return this.uom;
  }
}
