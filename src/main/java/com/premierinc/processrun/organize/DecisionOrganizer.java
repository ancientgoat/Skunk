package com.premierinc.processrun.organize;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processinput.base.RuleIdentity;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processinput.coreinf.SkGroup;
import com.premierinc.processinput.coreinf.SkTestable;

import java.util.*;

/**
 * Organize Rules/Nodes by
 * - Validate that we have a tree
 * - Validate the decision tree is setup correctly.
 * - Build maps of Identities, to make sure we are able to fill all values and such.
 */
public class DecisionOrganizer {

  private DecisionChain decisionChain;
  private Map<String, List<InpNodeBase>> identityNameMap = new HashMap<>();

  public DecisionOrganizer(final DecisionChain inChain) {
    this.decisionChain = inChain;
    _initialize();
  }

  private void _initialize() {
    // Parse all given decisions, one level at a time.
    parseAllDecisions(this.decisionChain.getDecisions());
  }

  /**
   * @param inDecisions
   */
  private void parseAllDecisions(final List<InpNodeBase> inDecisions) {

    if (null == inDecisions) {
      throw new SkException("Input Rules are *null*.  Please fix.");
    } else if (0 == inDecisions.size()) {
      throw new SkException("Input Rules are empty, there are none.  Please fix.");
    }

    // Recursively process all Rules (or Decisions) one level at a time.
    Stack<InpNodeBase> stack = new Stack<>();
    stack.addAll(inDecisions);

    while (0 < stack.size()) {

      InpNodeBase base = stack.pop();

      // If group then add group's decision list to the stack for processing.
      if (base instanceof SkGroup) {
        stack.addAll(((SkGroup) base).getDecisions());
        continue;
      }

      // Testable
      if (base instanceof SkTestable) {
        RuleIdentity identity = ((SkTestable) base).getIdentity();
        final String name = identity.getName();
        List<InpNodeBase> baseList = this.identityNameMap.get(name);
        if (null == baseList) {
          baseList = new ArrayList<>();
        }
        baseList.add(base);
        this.identityNameMap.put(name, baseList);
      }
    }
  }

  /**
   *
   */
  public Map<String, List<InpNodeBase>> getIdentityNameMap() {
    return identityNameMap;
  }

  //  /**
//   * Build a single level of "process AndOr process".
//   *
//   * @param inDecisionList
//   */
//  private SkNodeInf _parseSingleLevelDecisionList(final List<InpNodeBase> inDecisionList) {
//
//    // Validate input list
//    validateSingleLevelSize(inDecisionList);
//
//    final List<SkNodeInf> topLevelAndOrList = new ArrayList();
//
//    while (0 < inDecisionList.size()) {
//
//      final InpNodeBase decisionA = inDecisionList.pop();
//
//      if (decisionA instanceof InpAndOr) {
//        throw new IllegalArgumentException(String.format("Should not have AndOr as first element : %s", decisionA.dumpToJsonString()));
//      }
//      if (!(decisionA instanceof ProcessResult)) {
//        throw new IllegalArgumentException(String.format("First element should be a 'ProcessResult' : %s",
//            decisionA.dumpToJsonString()));
//      }
//
//      final InpNodeBase decisionB = inDecisionList.pop();
//
//      if (decisionA instanceof InpAndOr) {
//        throw new IllegalArgumentException(String.format("Should not have AndOr as second element : %s",
//            decisionB.dumpToJsonString()));
//      }
//      if (!(decisionB instanceof ProcessResult)) {
//        throw new IllegalArgumentException(String.format("Second element should be a 'ProcessResult' : %s",
//            decisionB.dumpToJsonString()));
//      }
//
//      final InpNodeBase hopefullyAndOr = inDecisionList.pop();
//
//      if (!(hopefullyAndOr instanceof InpAndOr)) {
//        throw new IllegalArgumentException(String.format("SHOULD have AndOr as third element : %s",
//            hopefullyAndOr.dumpToJsonString()));
//      }
//
//      final InpAndOr logicAndOr = (InpAndOr) hopefullyAndOr;
//
//      final RunAndOr runAndOr = new RunAndOr.Builder()
//          .setProcessA((ProcessResult) decisionA)
//          .setProcessB((ProcessResult) decisionB)
//          .setOperator(logicAndOr.getOperator())
//          .build();
//
//      topLevelAndOrList.add(runAndOr);
//    }
//
//    // Maps
//    _buildMaps();
//
//    return topLevelAndOrList;
//  }
//
//  /**
//   * Our Decision list portion should have either one "Process" thingy,
//   * or a "ProcessThingy AndOr AnotherProcessThingy".  So it should have
//   * either 1 thing, or any odd number of things, with each even numbered thingy
//   * being an AndOr thingy.
//   *
//   * @param inDecisionList
//   */
//  private void validateSingleLevelSize(List<InpNodeBase> inDecisionList) {
//
//    int i = 0;
//
//    int listSize = inDecisionList.size();
//
//    if (0 == listSize) {
//      // Nothing to validate or process
//    }
//
//    for (final InpNodeBase base : inDecisionList) {
//
//    }
//
//  }
//
//  /**
//   *
//   */
//  private void _buildMaps() {
//
//    // FillIdentity Map
//    this.identityNameMap.clear();
//
//    for (final InpNodeBase base : this.decisionChain.getDecisions()) {
//      RuleIdentity identity = base.getIdentity();
//      if (null != identity) {
//        String identityName = identity.getName();
//        if (null != identityName && 0 < identityName.length()) {
//
//          List<InpNodeBase> baseList = this.identityNameMap.get(identityName);
//
//          if (null == baseList) {
//            baseList = new ArrayList();
//          }
//
//          baseList.add(base);
//          this.identityNameMap.put(identityName, baseList);
//        }
//      }
//    }
//
//    //
//    outputIdentityNameCounts();
//
//  }
//
//  /**
//   *
//   */
//  public void outputIdentityNameCounts() {
//    this.identityNameMap.keySet().stream().sorted()
//        .forEach(k -> System.out.println(String.format("IdentityName : %s  Count : %d",
//            k, this.identityNameMap.get(k).size())));
//  }
//
////  /**
////   * Identity name '%s'
////   * Add a value for an Identity Name type.
////   */
////  public <V extends Comparable> void addValue(final String inName, final V inValue) {
////    if (this.identityNameMap.containsKey(inName)) {
////      this.valueMap.put(inName, inValue);
////    } else {
////      throw new SkException(String.format("No such Identity name '%s'", inName));
////    }
////  }
}
