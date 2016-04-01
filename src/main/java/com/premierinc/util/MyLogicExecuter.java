package com.premierinc.util;

/**
 *
 */
public class MyLogicExecuter {
	private MyLogicExecuter() {
	}

	public static final <T extends Comparable<T>> boolean gt(final T a, final T b) {
		return 0 < a.compareTo(b);
	}

	public static final <T extends Comparable<T>> boolean lt(final T a, final T b) {
		return 0 > a.compareTo(b);
	}

	public static final <T extends Comparable<T>> boolean eq(final T a, final T b) {
		return 0 == a.compareTo(b);
	}
}
