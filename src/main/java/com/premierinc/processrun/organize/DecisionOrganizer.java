package com.premierinc.processrun.organize;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.base.DecisionIdentity;
import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processinput.core.InpAndOr;
import com.premierinc.processinput.core.InpGroup;
import com.premierinc.processinput.core.InpLogic;
import com.premierinc.processrun.runenum.EvenOdd;
import com.premierinc.processtree.decisioninf.SkAndOrInf;
import com.premierinc.processtree.decisioninf.SkGroupInf;
import com.premierinc.processtree.decisioninf.SkNodeInf;
import com.premierinc.processtree.decisioninf.SkLogicInf;
import com.premierinc.processtree.decisiontree.SkAndOrNode;
import com.premierinc.processtree.decisiontree.SkGroupNode;
import com.premierinc.processtree.decisiontree.SkLogicNode;

import java.util.*;

/**
 * Organize Rules/Nodes by
 * - Validate that we have a tree
 * - Validate the decision tree is setup correctly.
 * - Build maps of Identities, to make sure we are able to fill all values and such.
 */
public class DecisionOrganizer {

  private final DecisionChain decisionChain;
  private final List<InpNodeBase> decisions;
  private Map<String, List<InpNodeBase>> identityNameMap = Collections.emptyMap();
  private SkNodeInf topNode;

  public DecisionOrganizer(final DecisionChain inChain) {
    this.decisionChain = inChain;
    this.decisions = this.decisionChain.getDecisions();
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
    }

    validateSingleLevelSize(inDecisions);
    buildIdentityNameMap();
    this.topNode = buildTestableDecisionTree(inDecisions);
  }

  /**
   * 1) Validate Decision list is in the proper order.
   * 2) Build an testable (executable) node tree.
   */
  private SkNodeInf buildTestableDecisionTree(final List<InpNodeBase> inDecisions) {

    SkNodeInf localTopNode = null;

    // Process just the top level of the decision tree.
    boolean shouldBelogicNode = true;

    final List<InpNodeBase> localDecisions = inDecisions;
    EvenOdd evenOdd = EvenOdd.EVEN; // 0 (zero) is even

    int decisionListSize = localDecisions.size();

    validateSingleLevelSize(localDecisions);

    if (1 == decisionListSize) {
      localTopNode = processSingleNode(localDecisions.get(0));
    } else {
      localTopNode = parseMoreThanOneDecision(localDecisions);
    }

    return localTopNode;
  }

  /**
   *
   */
  private SkNodeInf parseMoreThanOneDecision(List<InpNodeBase> localDecisions) {

    SkNodeInf localTopNode = null;
    final Stack<SkAndOrInf> andOrStack = new Stack<>();

    while (0 < localDecisions.size()) {

      final InpNodeBase base01 = localDecisions.remove(0);
      final SkNodeInf node = getLogicOrGroup(base01);
      final SkAndOrInf prevAndOr = (0 < andOrStack.size() ? andOrStack.peek() : null);

      if (0 == localDecisions.size()) {
        // Only one decision left, we assume it's NOT an AndOr.
        if (null != prevAndOr) {
          prevAndOr.add(node);
        } else {
          throw new SkException("How come we don't have a previous AndOr?");
        }
      } else {
        final InpNodeBase base02 = localDecisions.remove(0);
        SkAndOrInf andOrNode = getAndOr(base02);

        if (null != prevAndOr) {
          if (prevAndOr.getOperator() == andOrNode.getOperator()) {
            andOrNode = prevAndOr;
          } else {
            andOrStack.push(andOrNode);
          }
        } else {
          // This is the first AndOr.
          // Set our top node to the first andOrNode, since we have more than
          //  one decision.
          localTopNode = andOrNode;
          andOrStack.push(andOrNode);
        }
        andOrNode.add(node);
      }
    }
    return localTopNode;
  }

  /**
   *
   */
  private SkNodeInf processSingleNode(final InpNodeBase inNode) {
    return getLogicOrGroup(inNode);
  }

  /**
   *
   */
  private SkNodeInf getLogicOrGroup(final InpNodeBase inNode) {
    if (inNode instanceof InpLogic) {
      return new SkLogicNode((InpLogic) inNode);
    } else if (inNode instanceof InpGroup) {
      return new SkGroupNode((InpGroup) inNode);
    } else {
      throw new SkException(String.format("Expected decision type of Logic or Group, but found '%s'",
          inNode.getClass().getName()));
    }
  }

  /**
   *
   */
  private SkAndOrInf getAndOr(final InpNodeBase inNode) {
    if (inNode instanceof InpAndOr) {
      return new SkAndOrNode((InpAndOr) inNode);
    } else {
      throw new SkException(String.format("Expected decision type of AndOr, but found '%s'",
          inNode.getClass().getName()));
    }
  }

  /**
   *
   */
  private void buildIdentityNameMap() {

    // Recursively process all Rules (or Decisions) one level at a time.
    final List<InpNodeBase> decisions = this.decisionChain.getDecisions();
    Stack<InpNodeBase> stack = new Stack<>();
    stack.addAll(decisions);

    while (0 < stack.size()) {

      InpNodeBase base = stack.pop();

      // If group then add group's decision list to the stack for processing.
      if (base instanceof SkGroupInf) {
        stack.addAll(((SkGroupInf) base).getDecisions());
        continue;
      }

      // Testable
      if (base instanceof SkLogicInf) {
        DecisionIdentity identity = ((SkLogicInf) base).getIdentity();
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


  /**
   * Our Decision list portion should have either one "Process" thingy,
   * or a "ProcessThingy AndOr AnotherProcessThingy".  So it should have
   * either 1 thing, or any odd number of things, with each even numbered thingy
   * being an AndOr thingy.
   *
   * @param inDecisions
   */
  private void validateSingleLevelSize(List<InpNodeBase> inDecisions) {

    int listSize = inDecisions.size();

    if (0 == listSize) {
      // Nothing to validate or process
      throw new SkException("Input decision list size is zero.  Pleaes fill and try again.");
    }

    if (1 != (listSize % 2)) {
      throw new SkException(String.format(
          "Input decision list size should be an odd number, but is '%s'.", listSize));
    }
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
//  /**
//   *
//   */
//  private void _buildMaps() {
//
//    // FillIdentity Map
//    this.identityNameMap.clear();
//
//    for (final InpNodeBase base : this.decisionChain.getDecisions()) {
//      DecisionIdentity identity = base.getIdentity();
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
