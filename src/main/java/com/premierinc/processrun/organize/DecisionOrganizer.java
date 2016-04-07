package com.premierinc.processrun.organize;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processinput.core.*;
import com.premierinc.processtree.decisioninf.SkAndOrInf;
import com.premierinc.processtree.decisioninf.SkNodeInf;
import com.premierinc.processtree.decisioninf.SkNumericInf;
import com.premierinc.processtree.decisioninf.SkTextInf;
import com.premierinc.processtree.decisiontree.SkAndOrNode;
import com.premierinc.processtree.decisiontree.SkGroupNode;
import com.premierinc.processtree.decisiontree.SkNumericNode;
import com.premierinc.processtree.decisiontree.SkTextNode;

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
  private Map<String, Set<SkNodeInf>> identityNameMap = new HashMap<>();
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
   *
   */
  public SkNodeInf getTopNode() {
    return topNode;
  }

  /**
   * @param inDecisions
   */
  private void parseAllDecisions(final List<InpNodeBase> inDecisions) {

    if (null == inDecisions) {
      throw new SkException("Input Rules are *null*.  Please fix.");
    }

    validateSingleLevelSize(inDecisions);
    this.topNode = buildSingleLevelOfTheDecisionTree(inDecisions);
  }

  /**
   * 1) Validate Decision list is in the proper order.
   * 2) Build an testable (executable) node tree.
   */
  private SkNodeInf buildSingleLevelOfTheDecisionTree(final List<InpNodeBase> inDecisions) {

    SkNodeInf localTopNode = null;

    // Process just one level of the decision tree.
    final List<InpNodeBase> localDecisions = inDecisions;
    final int decisionListSize = localDecisions.size();

    validateSingleLevelSize(localDecisions);

    if (1 == decisionListSize) {
      localTopNode = getLogicOrGroup(identityNameMap, localDecisions.get(0));
    } else {
      localTopNode = processMoreThanOneDecision(identityNameMap, localDecisions);
    }
    return localTopNode;
  }

  /**
   * The goal of this method is to ...:
   * 1) Find and set the top level decision node for this list of Decisions.
   * 2) Organize the ndoe into a proper (suggested) execution order.
   */
  private SkNodeInf processMoreThanOneDecision(final Map<String, Set<SkNodeInf>> identityNameMap,
                                               final List<InpNodeBase> inDecisions) {
    SkNodeInf localTopNode = null;
    final Stack<SkAndOrInf> andOrStack = new Stack<>();

    while (0 < inDecisions.size()) {

      final InpNodeBase base01 = inDecisions.remove(0);
      final SkNodeInf node = getLogicOrGroup(identityNameMap, base01);
      final SkAndOrInf prevAndOr = (0 < andOrStack.size() ? andOrStack.peek() : null);

      if (0 == inDecisions.size()) {
        // Only one decision left (found just above), we assume it's NOT an AndOr.
        processLastDecisionInList(node, prevAndOr);
      } else {
        // Every even numbered decision in the list had better be of an AndOr type.
        final InpNodeBase base02 = inDecisions.remove(0);
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
  private void populateIdentityWithNode(final Map<String, Set<SkNodeInf>> inIdentityNameMap, final SkNodeInf inNode) {

    if (inNode instanceof SkTextInf) {

      final String identityName = ((SkTextInf) inNode).getIdentity().getName();

      Set<SkNodeInf> nodeSet = inIdentityNameMap.get(identityName);
      if (null == nodeSet) {
        nodeSet = new HashSet<>();
      }

      if (inNode instanceof SkTextNode) {
        nodeSet.add((SkTextNode) inNode);
      } else {
        throw new SkException(String.format("Expected 'SkTextNode' but found '%s'",
            inNode.getClass().getName()));
      }

      inIdentityNameMap.put(identityName, nodeSet);
    }
  }

  /**
   *
   */
  private void processLastDecisionInList(SkNodeInf node, SkAndOrInf prevAndOr) {
    if (null != prevAndOr) {
      prevAndOr.add(node);
    } else {
      throw new SkException("How come we don't have a previous AndOr?");
    }
  }

  /**
   *
   */
  private SkNodeInf getLogicOrGroup(Map<String, Set<SkNodeInf>> inIdentityNameMap,
                                    final InpNodeBase inNode) {
    if (inNode instanceof InpNumeric) {

      SkNodeInf skNode = translateNode(inNode);
      populateIdentityWithNode(inIdentityNameMap, skNode);
      return skNode;

    } else if (inNode instanceof InpText) {

      SkNodeInf skNode = translateNode(inNode);
      populateIdentityWithNode(inIdentityNameMap, skNode);
      return skNode;

    } else if (inNode instanceof InpGroup) {

      final List<InpNodeBase> localDecisions = ((InpGroup) inNode).getDecisions();
      final SkNodeInf localNode = processMoreThanOneDecision(inIdentityNameMap, localDecisions);
      populateIdentityWithNode(inIdentityNameMap, localNode);
      return localNode;

    } else {
      throw new SkException(String.format("Expected decision type of 'numeric', 'text', or 'group', but found '%s'",
          inNode.getClass().getName()));
    }
  }

  /**
   *
   */
  private SkNodeInf translateNode(final InpNodeBase inNode) {
    if (inNode instanceof InpNumeric) {

      return new SkNumericNode((InpNumeric) inNode);

    } else if (inNode instanceof InpText) {

      return new SkTextNode((InpText) inNode);

    } else if (inNode instanceof InpGroup) {

      return new SkGroupNode((InpGroup) inNode);

    } else if (inNode instanceof InpAndOr) {

      return new SkAndOrNode((InpAndOr) inNode);

    } else {
      throw new SkException(String.format("Node instance '%s' type not implemented yet.", inNode.getClass().getName()));
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
  public Map<String, Set<SkNodeInf>> getIdentityNameMap() {
    return this.identityNameMap;
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
      throw new SkException("Input decision list size is zero.  Please fill and try again.");
    }

    if (1 != (listSize % 2)) {
      throw new SkException(String.format(
          "Input decision list size should be an odd number, but is '%s'.", listSize));
    }
  }
}
