package com.premierinc.processrun.runner;

import com.premierinc.common.exception.SkException;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processtree.decisioninf.SkLogicInf;
import com.premierinc.processtree.decisioninf.SkNodeInf;
import org.slf4j.Logger;

import java.util.*;

/**
 *
 */
public class DecisionRunner {

  //private static Logger logger = new Logger(DecisionRunner.class);

  private final DecisionOrganizer organizer;
  private final Map<String, Comparable> valueMap = new HashMap<>();
  private final Map<String, Set<SkLogicInf>> identityNameMap;
  private SkNodeInf topNode;

  public DecisionRunner(final DecisionOrganizer inOrganizer) {
    this.organizer = inOrganizer;
    this.identityNameMap = this.organizer.getIdentityNameMap();
    this.topNode = this.organizer.getTopNode();
  }

  /**
   *
   */
  public <V extends Comparable> void addValue(final String inName, final V inValue) {

    if (!this.identityNameMap.containsKey(inName)) {
      throw new SkException(String.format("Identity name '%s' not found.", inName));
    }

    // TODO: output a warning?  yes or no?
    //Comparable existingValue = this.valueMap.get(inName);
    //if (null != existingValue) {
    //  throw new SkException(String.format("Identity name '%s' already has a value of '%s'.", inName, existingValue));
    //}

    this.valueMap.put(inName, inValue);
  }

  /**
   *
   */
  public boolean execute() {
    // First, validate that we have the correct number of values
    //  to input into this decision tree.
    validateValueMap();

    // Next fill in the values.
    fillDecisionTreeValues();

    // Run the decision tree.
    boolean test = this.topNode.test();
    //System.out.println(String.format("And the final answer is '%s'", test));
    return test;
  }

  /**
   * In our local node tree, fill in all values that we have.
   */
  private void fillDecisionTreeValues() {
    this.valueMap.keySet().forEach(
        key -> {
          final Comparable value = this.valueMap.get(key);
          final Set<SkLogicInf> nodeSet = this.identityNameMap.get(key);
          nodeSet.stream().parallel().forEach(
              n -> n.setLeftSide(value)
          );
        });
  }

  /**
   *
   */

  private void validateValueMap() {
    // If not all value in identityNameMap are set, tell them which ones are missing.
    int diffSize = this.identityNameMap.size() - this.valueMap.size();

    if (0 != diffSize) {
      final StringBuilder sb = new StringBuilder(String.format("%s Identity names not set", diffSize));
      this.identityNameMap.keySet().stream()
          .filter(k -> !this.valueMap.containsKey(k))
          .forEach(k -> sb.append(String.format("\nValue not set for Identity name '%s'", k)))
      ;
      throw new SkException(sb.toString());
    }
  }
}