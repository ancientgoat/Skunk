package com.premierinc.util;

import java.util.function.Predicate;

/**
 *
 */
public class MyBranch<T extends Comparable<T>> {

	Predicate<MyBranch> predicate;
	private T permValue;
	private T tempValue;
	private OperatorEnum operator;
	private boolean lastResult;

	public T getPermValue() {
		return permValue;
	}

	public T getTempValue() {
		return tempValue;
	}

	public final boolean test(final T inValue) {
		this.tempValue = inValue;
		this.lastResult = this.predicate.test(this);
		return this.lastResult;
	}

	/**
	 *
	 */
	public static class Builder<T extends Comparable<T>> {

		private MyBranch myBranch = new MyBranch<T>();

		public final Builder setPermValue(final int inValue) {
			this.myBranch.permValue = inValue;
			return this;
		}

		public final Builder setOperator(final OperatorEnum inOperator) {
			this.myBranch.operator = inOperator;

			Predicate<MyBranch> predicate;

			switch (inOperator) {
			case LT:
				predicate = p -> MyLogic.lt((T) p.permValue, (T) p.tempValue);
				break;
			case GT:
				predicate = p -> MyLogic.gt((T) p.permValue, (T) p.tempValue);
				break;
			case OR:
				predicate = p -> p.lastResult || (boolean) p.tempValue;
				break;
			case AND:
				predicate = p -> p.lastResult && (boolean) p.tempValue;
				break;
			default:
				throw new IllegalStateException(String.format("No such Operator '%s'", inOperator));
			}
			this.myBranch.predicate = predicate;
			return this;
		}

		public final MyBranch<T> build() {
			return this.myBranch;
		}
	}
}
