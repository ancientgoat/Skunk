package com.premierinc.processinput.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.common.util.JsonHelper;
import com.premierinc.processinput.core.InpAndOr;
import com.premierinc.processinput.core.InpGroup;
import com.premierinc.processinput.core.InpLogic;

/**
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = InpLogic.class, name = "logic"),
    @JsonSubTypes.Type(value = InpAndOr.class, name = "andOr"),
    @JsonSubTypes.Type(value = InpGroup.class, name = "group")
})
public abstract class InpNodeBase {

  public final String dumpToJsonString() {
    return JsonHelper.beanToJsonString(this);
  }

  public final String dumpToYamlString() {
    return JsonHelper.beanToYamlString(this);
  }
}

//  private String description;
//  private DecisionIdentity identity;
//
//  public String getDescription() {
//    return description;
//  }
//
//  public DecisionIdentity getIdentity(){
//    return this.identity;
//  }
//
//  abstract public InpType getInpType();
//
//  public void whatAmI() {
//    System.out.println(this.getClass().getName());
//  }

