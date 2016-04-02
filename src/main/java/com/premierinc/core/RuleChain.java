package com.premierinc.core;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.premierinc.base.MyDecisionBase;

import java.util.List;
import java.util.Stack;

/**
 *
 * --: Count Rules
 * --: Map Rule Value Types
 *
 */
@JsonRootName(value="decisionList")
public class RuleChain {

	private String name;
	private Stack<MyDecisionBase> rules;

	public void setName(String name) {
		this.name = name;
	}

	public void setRules(Stack<MyDecisionBase> rules) {
		this.rules = rules;
	}

	public String getName() {
		return name;
	}

	public Stack<MyDecisionBase> getRules() {
		return rules;
	}
}
