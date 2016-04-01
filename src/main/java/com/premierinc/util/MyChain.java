package com.premierinc.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.premierinc.base.MyDecisionBase;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
@JsonRootName(value="decisionList")
public class MyChain {

	private String name;
	private List<MyDecisionBase> rules;

	public void setName(String name) {
		this.name = name;
	}

	public void setRules(List<MyDecisionBase> rules) {
		this.rules = rules;
	}
}
