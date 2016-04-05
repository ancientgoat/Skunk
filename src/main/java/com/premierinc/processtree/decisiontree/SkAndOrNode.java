package com.premierinc.processtree.decisiontree;

import com.premierinc.common.enumeration.AndOrOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.InpAndOr;
import com.premierinc.processtree.decisioninf.SkAndOrInf;
import com.premierinc.processtree.decisioninf.SkNodeInf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SkAndOrNode implements SkAndOrInf {

  /**
   * The input AndOr, describes exactly what this type of AndOr bit of logic.
   * This is the top node of this portion of the decision tree, this node alone should
   * return a 'true' or 'false' which represents the value for this node, and all nodes below.
   */
  private final InpAndOr andOr;

  /**
   * All sub Nodes that are separated by this AndOr.  Lists belong to a
   * single type only.  So if this is an 'And' node then every node in this list
   * was separated by an 'And' (and not an 'Or' os something else)
   */
  private List<SkNodeInf> nodeList = new ArrayList<>();

  public SkAndOrNode(final InpAndOr inInpAndOr) {
    if (null == inInpAndOr) {
      throw new SkException("SkAndOrNode input can not be null.");
    }
    this.andOr = inInpAndOr;
  }

  public SkAndOrInf add(final SkNodeInf inNode) {
    nodeList.add(inNode);
    return this;
  }

  @Override
  public AndOrOperatorEnum getOperator() {
    return andOr.getOperator();
  }

  @Override
  public boolean test() {

    boolean testResult = false;
    final AndOrOperatorEnum andOrOperator = andOr.getOperator();

    switch (andOrOperator) {
      case AND:
        testResult = executeAnd(this.nodeList);
        break;

      case OR:
        testResult = executeOr(this.nodeList);
        break;

      default:
        throw new SkException(String.format("The Operator '%s' is not implemented at this time.", andOrOperator));
    }

    return testResult;
  }

  /**
   *
   */
  private boolean executeAnd(final List<SkNodeInf> inNodeList) {

    boolean boolResult = false;

    for (final SkNodeInf node : inNodeList) {
      boolResult = node.test();
      if (!boolResult) {
        // Since this is an 'AND' then any failure, makes the whole thing fail.
        break;
      }
    }
    return boolResult;
  }

  /**
   *
   */
  private boolean executeOr(final List<SkNodeInf> inNodeList) {

    boolean boolResult = false;

    for (final SkNodeInf node : inNodeList) {
      boolResult = node.test();
      if (boolResult) {
        // Since this is an 'OR' then any successful test, makes the whole thing successful.
          break;
      }
    }
    return boolResult;
  }
}
