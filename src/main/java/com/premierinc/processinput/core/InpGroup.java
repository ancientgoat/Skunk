package com.premierinc.processinput.core;

import com.premierinc.processinput.base.InpNodeBase;

import java.util.List;

/**
 *
 */
public class InpGroup extends InpNodeBase {

  private String name;
  private List<InpNodeBase> decisions;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<InpNodeBase> getDecisions() {
    return decisions;
  }
}
