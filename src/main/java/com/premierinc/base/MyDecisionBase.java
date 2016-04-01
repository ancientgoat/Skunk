package com.premierinc.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.util.MyLogic;
import com.premierinc.util.MyModifier;

/**
 *
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
//@JsonSubTypes({ @Type(value = Lion.class, name = "lion"), @Type(value = Elephant.class, name = "elephant") })
//public abstract class Animal {
//	@JsonProperty("name")
//	String name;
//	@JsonProperty("sound")
//	String sound;
//	@JsonProperty("type")
//	String type;
//	@JsonProperty("endangered")
//	boolean endangered;
//
//}

// MyLogic
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = MyLogic.class, name = "logic"),
		@JsonSubTypes.Type(value = MyModifier.class, name = "modify")})
public abstract class MyDecisionBase {
	private String description;

	public String getDescription() {
		return description;
	}

}
