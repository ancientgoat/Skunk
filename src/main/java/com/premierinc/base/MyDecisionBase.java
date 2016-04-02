package com.premierinc.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.core.MyLogicAndOr;
import com.premierinc.core.MyLogicOther;
import com.premierinc.core.MyModifier;
import com.premierinc.enumeration.LogicType;
import com.premierinc.util.JsonHelper;

/**
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = MyLogicOther.class, name = "logic"),
    @JsonSubTypes.Type(value = MyLogicAndOr.class, name = "andOr"),
    @JsonSubTypes.Type(value = MyModifier.class, name = "modify")
})
public abstract class MyDecisionBase {

  private String description;
  private String valueType;

  public String getDescription() {
    return description;
  }

  public String getValueType() {
    return valueType;
  }

  abstract public LogicType getLogicType();

  public final String dumpToString() {
    return JsonHelper.beanToJsonString(this);
  }
}
