package com.premierinc.core;

import com.premierinc.base.MyDecisionBase;
import com.premierinc.enumeration.LogicType;
import com.premierinc.util.MyLogicExecuter;
import com.premierinc.util.OperatorEnum;

import java.util.function.Predicate;

/**
 *
 */
public class MyModifier<T extends Comparable<T>> extends MyDecisionBase {

	Predicate<MyModifier> predicate;
	private T permValue = null;
	private T tempValue = null;
	private OperatorEnum operator;
	private boolean lastResult;
	private MyModifier branchA = null;
	private MyModifier branchB = null;

	public LogicType getLogicType() {
		return LogicType.MODIFY;
	}

	public T getPermValue() {
		return permValue;
	}

	public T getTempValue() {
		return tempValue;
	}

	public MyModifier<T> setTempValue(T tempValue) {
		this.tempValue = tempValue;
		return this;
	}

	public MyModifier<T> setBranchA(final MyModifier branchA) {
		this.branchA = branchA;
		return this;
	}

	public MyModifier<T> setBranchB(MyModifier branchB) {
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

		private MyModifier myLogic = new MyModifier<T>();

		public final Builder setPermValue(final int inValue) {
			this.myLogic.permValue = inValue;
			return this;
		}

		public final Builder setTempValue(final int inValue) {
			this.myLogic.tempValue = inValue;
			return this;
		}

		public final Builder setBranchA(final MyModifier branchA) {
			this.myLogic.branchA = branchA;
			return this;
		}

		public Builder setBranchB(MyModifier branchB) {
			this.myLogic.branchB = branchB;
			return this;
		}

		public final Builder setOperator(final OperatorEnum inOperator) {
			this.myLogic.operator = inOperator;

			Predicate<MyModifier> predicate;

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

		public final MyModifier<T> build() {
			validateBuild();
			return this.myLogic;
		}
	}
}
