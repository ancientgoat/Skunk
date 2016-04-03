package com.premierinc.common.util;

/**
 *
 */
public class MyLogicExecuter {
	private MyLogicExecuter() {
	}

	/**
	 * true if input 'a GT b'.
   */
	public static final <T extends Comparable<T>> boolean gt(final T a, final T b) {
		return 0 < a.compareTo(b);
	}

	/**
	 * true if input 'a LT b'.
	 */
	public static final <T extends Comparable<T>> boolean lt(final T a, final T b) {
		return 0 > a.compareTo(b);
	}

	/**
	 * true if input 'a EQ b'.
	 */
	public static final <T extends Comparable<T>> boolean eq(final T a, final T b) {
		return 0 == a.compareTo(b);
	}
}
