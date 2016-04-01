package com.premierinc.util;

import org.testng.annotations.Test;

/**
 *
 */
public class MySimpleTest {

	@Test
	public void testTest(){

		final Integer firstValue = 1;

		MyBranch<Integer> branch_01 =
				new MyBranch.Builder<>()
						.setPermValue(firstValue)
						.setOperator(OperatorEnum.GT)
						.build();

		MyBranch<Integer> branch_02 =
				new MyBranch.Builder<>()
						.setPermValue(firstValue)
						.setOperator(OperatorEnum.LT)
						.build();

		MyBranch<Boolean> branch_03 =
				new MyBranch.Builder<>()
						.setPermValue(branch_01.firstValue)
						.setOperator(OperatorEnum.OR)
						.build();

		System.out.println(String.format("1 gt 1 : %s", branch_01.test(1)));
		System.out.println(String.format("1 gt 2 : %s", branch_01.test(2)));
		System.out.println(String.format("1 gt 3 : %s", branch_01.test(3)));

		System.out.println(String.format("1 lt 1 : %s", branch_02.test(1)));
		System.out.println(String.format("1 lt 2 : %s", branch_02.test(2)));
		System.out.println(String.format("1 lt 3 : %s", branch_02.test(3)));
	}
}
