package com.premierinc.util;

/**
 *
 */
public class MyLogic {
	private MyLogic() {
	}

	public static final <T extends Comparable<T>> boolean gt(final T a, final T b) {
		return a.compareTo(b) > 0;
	}

	public static final <T extends Comparable<T>> boolean lt(final T a, final T b) {
		return a.compareTo(b) < 0;
	}
}
