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
public abstract class MyBase {

	private String description;

	public String getDescription() {
		return description;
	}
}
