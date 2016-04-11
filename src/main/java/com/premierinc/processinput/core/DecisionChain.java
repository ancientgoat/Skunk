package com.premierinc.processinput.core;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.premierinc.processinput.base.InpNodeBase;

import java.util.List;

/**
 *
 * --: Count Rules
 * --: Map rule Value Types
 *
 */
@JsonRootName(value="decisionList")
public class DecisionChain {

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
