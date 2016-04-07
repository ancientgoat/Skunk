package com.premierinc.processrun.runner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.premierinc.common.exception.SkException;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processtree.decisioninf.SkNodeInf;
import com.premierinc.processtree.decisioninf.SkTextInf;

import java.util.*;

/**
 *
 */
public class DecisionRunnerText {

  //private static Logger logger = new Logger(DecisionRunnerNumeric.class);

  private final DecisionOrganizer organizer;
  private final Map<String, String> valueMap = new HashMap<>();
  private final Map<String, List<String>> valueMapList = new HashMap<>();
  private Map<String, Set<SkTextInf>> identityNameMap = Maps.newHashMap();

  private SkNodeInf topNode;

  public DecisionRunnerText(final DecisionOrganizer inOrganizer) {
    this.organizer = inOrganizer;
    this.topNode = this.organizer.getTopNode();

    Map<String, Set<SkNodeInf>> inputMap = this.organizer.getIdentityNameMap();
    inputMap.keySet().stream()
        .forEach(key -> {
              Set<SkNodeInf> nodes = inputMap.get(key);
              Set<SkTextInf> textNodes = new HashSet<>();
              nodes.stream().forEach(n -> textNodes.add((SkTextInf) n));
              this.identityNameMap.put(key, textNodes);
            }
        )
    ;
  }

  /**
   *
   */
  public void setValue(final String inName, final String inValue) {

    if (!this.identityNameMap.containsKey(inName)) {
      throw new SkException(String.format("Identity name '%s' not found.", inName));
    }

    this.valueMap.put(inName, inValue);
  }

  /**
   *
   */
  public void addValueToList(final String inName, final String inValue) {

    if (!this.identityNameMap.containsKey(inName)) {
      throw new SkException(String.format("Identity name '%s' not found.", inName));
    }
    List<String> valueList = this.valueMapList.get(inName);
    if (null == valueList) {
      valueList = Lists.newArrayList();
    }
    valueList.add(inValue);
    this.valueMapList.put(inName, valueList);
  }

  /**
   *
   */
  public boolean execute() {
    // First, validate that we have the correct number of values
    //  to input into this decision tree.
    validateValueMaps();

    // Next fill in the values.
    fillDecisionTreeTextValues();

    // Run the decision tree.
    boolean test = this.topNode.test();
    // System.out.println(String.format("And the final answer is '%s'", test));
    return test;
  }

  /**
   * In our local node tree, fill in all values that we have.
   */
  private void fillDecisionTreeTextValues() {

    this.valueMap.keySet().forEach(
        key -> {
          final String value = this.valueMap.get(key);
          final Set<SkTextInf> nodeSet = (Set<SkTextInf>) this.identityNameMap.get(key);
          nodeSet.stream().parallel().forEach(
              n -> n.setLeftSide(value)
          );
        });

    this.valueMapList.keySet().forEach(
        key -> {
          final String value = this.valueMap.get(key);
          final Set<SkTextInf> nodeSet = (Set<SkTextInf>) this.identityNameMap.get(key);
          nodeSet.stream().parallel().forEach(
              n -> n.addToLeftSideList(value)
          );
        });
  }

  /**
   *
   */

  private void validateValueMaps() {
    // If not all value in identityNameMap are set, tell them which ones are missing.
    int diffSize = this.identityNameMap.size() - this.valueMap.size() - this.valueMapList.size();

    if (0 != diffSize) {
      final StringBuilder sb = new StringBuilder(String.format("%s Identity names not set", diffSize));
      this.identityNameMap.keySet().stream()
          .filter(k -> !this.valueMap.containsKey(k))
          .forEach(k -> sb.append(String.format("\nValue not set for Identity name '%s'", k)))
      ;
      throw new SkException(sb.toString());
    }

    // Make sure the valueMap and valueMapList DO NOT have the same key.  So that
    //  we insure that each and every one of our Identity Names have values.

    // First check all keys in valueMap
    this.valueMap.keySet().stream().forEach(key -> {
          if (this.valueMapList.containsKey(key)) {
            throw new SkException(String.format("Both 'valueMap' and 'valueMapList' have the same key of '%s'.", key));
          }
        }
    );
    // Next check all keys in valueMapList
    this.valueMapList.keySet().stream().forEach(key -> {
          if (this.valueMap.containsKey(key)) {
            throw new SkException(String.format("Both 'valueMapList' and 'valueMap' have the same key of '%s'.", key));
          }
        }
    );
  }
}