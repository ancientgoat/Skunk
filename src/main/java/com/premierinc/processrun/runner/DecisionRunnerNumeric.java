package com.premierinc.processrun.runner;

import com.premierinc.common.exception.SkException;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processtree.decisioninf.SkNodeInf;
import com.premierinc.processtree.decisioninf.SkNumericInf;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 */
public class DecisionRunnerNumeric {

  //private static Logger logger = new Logger(DecisionRunnerNumeric.class);

  private final DecisionOrganizer organizer;
  private final Map<String, BigDecimal> valueMap = new HashMap<>();
  private Map<String, Set<SkNumericInf>> identityNameMap;

  private SkNodeInf topNode;

  public DecisionRunnerNumeric(final DecisionOrganizer inOrganizer) {
    this.organizer = inOrganizer;
    this.topNode = this.organizer.getTopNode();

    Map<String, Set<SkNodeInf>> inputMap = this.organizer.getIdentityNameMap();
    inputMap.keySet().stream()
        .forEach(key -> {
              Set<SkNodeInf> nodes = inputMap.get(key);
              Set<SkNumericInf> numericNodes = new HashSet<>();
              nodes.stream().forEach(n -> numericNodes.add((SkNumericInf) n));
              this.identityNameMap.put(key, numericNodes);
            }
        )
    ;
  }

  /**
   *
   */
  public void setValue(final String inName, final int inValue) {
    setValue(inName, new BigDecimal(inValue));
  }

  /**
   *
   */
  public void setValue(final String inName, final long inValue) {
    setValue(inName, new BigDecimal(inValue));
  }

  /**
   *
   */
  public void setValue(final String inName, final float inValue) {
    setValue(inName, new BigDecimal(inValue));
  }

  /**
   *
   */
  public void setValue(final String inName, final double inValue) {
    setValue(inName, new BigDecimal(inValue));
  }

  /**
   *
   */
  public void setValue(final String inName, final BigDecimal inValue) {

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
    fillDecisionTreeNumericValues();

    // Run the decision tree.
    boolean test = this.topNode.test();
    //System.out.println(String.format("And the final answer is '%s'", test));
    return test;
  }

  /**
   * In our local node tree, fill in all values that we have.
   */
  private void fillDecisionTreeNumericValues() {
    this.valueMap.keySet().forEach(
        key -> {
          final BigDecimal value = this.valueMap.get(key);
          final Set<SkNumericInf> nodeSet = (Set<SkNumericInf>) this.identityNameMap.get(key);
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