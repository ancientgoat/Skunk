package com.premierinc.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.premierinc.base.MyBase;
import com.premierinc.base.MyDecisionBase;
import java.util.function.Predicate;

/**
 *
 */
//public class MyLogic<T extends Comparable<T>> extends MyBase {
public class MyLogic<T extends Comparable<T>> extends MyDecisionBase {

	Predicate<MyLogic> predicate;

	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	private T permValue = null;

	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	private T tempValue = null;

	//@JsonProperty("operator")
	private OperatorEnum operator;
	private boolean lastResult;
	private MyLogic branchA = null;
	private MyLogic branchB = null;

	public T getPermValue() {
		return permValue;
	}

	public T getTempValue() {
		return tempValue;
	}

	public void setPermValue(T permValue) {
		this.permValue = permValue;
	}

	public void setOperator(OperatorEnum operator) {
		this.operator = operator;
	}

	public MyLogic<T> setTempValue(T tempValue) {
		this.tempValue = tempValue;
		return this;
	}

	public MyLogic<T> setBranchA(final MyLogic branchA) {
		this.branchA = branchA;
		return this;
	}

	public MyLogic<T> setBranchB(MyLogic branchB) {
		this.branchB = branchB;
		return this;
	}

	public final boolean testFast(final T inValue) {
		this.tempValue = inValue;
		return this.test();
	}

	public final boolean test() {
		if (haveValues()) {
			this.lastResult = this.predicate.test(this);
		} else if (haveBranches()) {
			this.branchA.test();
			this.branchB.test();
			this.lastResult = this.predicate.test(this);
		}
		System.out.println(lastOutput());
		return this.lastResult;
	}

	public boolean getLastResult() {
		return lastResult;
	}

	private final boolean haveValues() {
		return null != this.permValue && null != this.tempValue;
	}

	private final boolean haveBranches() {
		return null != this.branchA && null != this.branchB;
	}

	private final boolean havePermValue() {
		return null != this.permValue;
	}

	private final boolean haveBranchA() {
		return null != this.branchA;
	}

	public final String lastOutput() {
		if (haveValues()) {
			return String.format("%s %s %s  Result:%s", this.permValue, this.operator, this.tempValue, this.lastResult);
		} else if (haveBranches()) {
			return String.format("%s %s %s  Result:%s", this.branchA.getLastResult(), this.operator,
					this.branchB.getLastResult(), this.lastResult);
		}
		return "* Do not have values or branches *";
	}

	/**
	 *
	 */
	public static class Builder<T extends Comparable<T>> {

		private MyLogic myLogic = new MyLogic<T>();

		public final Builder setPermValue(final int inValue) {
			this.myLogic.permValue = inValue;
			return this;
		}

		public final Builder setTempValue(final int inValue) {
			this.myLogic.tempValue = inValue;
			return this;
		}

		public final Builder setBranchA(final MyLogic branchA) {
			this.myLogic.branchA = branchA;
			return this;
		}

		public Builder setBranchB(MyLogic branchB) {
			this.myLogic.branchB = branchB;
			return this;
		}

		public final Builder setOperator(final OperatorEnum inOperator) {
			this.myLogic.operator = inOperator;

			Predicate<MyLogic> predicate;

			switch (inOperator) {
			case LT:
				predicate = p -> MyLogicExecuter.lt((T) p.permValue, (T) p.tempValue);
				break;
			case GT:
				predicate = p -> MyLogicExecuter.gt((T) p.permValue, (T) p.tempValue);
				break;
			case EQ:
				predicate = p -> MyLogicExecuter.eq((T) p.permValue, (T) p.tempValue);
				break;
			case OR:
				predicate = p -> p.branchA.getLastResult() || p.branchB.getLastResult();
				break;
			case AND:
				predicate = p -> p.branchA.getLastResult() && p.branchB.getLastResult();
				break;
			default:
				throw new IllegalStateException(String.format("No such Operator '%s'", inOperator));
			}
			this.myLogic.predicate = predicate;
			return this;
		}

		private final void validateBuild() {
			switch (this.myLogic.operator) {
			case LT:
			case GT:
				if (!this.myLogic.havePermValue()) {
					throw new IllegalArgumentException(
							String.format("This operator '%s' requires a permValue.", this.myLogic.operator));
				}
				if (this.myLogic.haveBranchA()) {
					throw new IllegalArgumentException(
							String.format("This operator '%s' requires values, not " + "branches.",
									this.myLogic.operator));
				}
				break;

			case OR:
			case AND:
				if (!this.myLogic.haveBranchA()) {
					throw new IllegalArgumentException(
							String.format("This operator '%s' requires a permValue.", this.myLogic.operator));
				}
				if (this.myLogic.havePermValue()) {
					throw new IllegalArgumentException(
							String.format("This operator '%s' requires branches, not " + "values.",
									this.myLogic.operator));
				}
				break;

			default:
				throw new IllegalStateException(String.format("No such Operator '%s'", this.myLogic.operator));
			}
		}

		public final MyLogic<T> build() {
			validateBuild();
			return this.myLogic;
		}
	}
}
