package com.premierinc.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.base.MyBase;
import com.premierinc.base.MyDecisionBase;
import com.premierinc.common.CamelCaseNamingStrategy;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.testng.annotations.Test;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;

/**
 *
 */
public class MySimpleTest {


	@Test
	public void testReadFileTest() {
		try {
			ObjectMapper mapper = new CustomMapper();
			mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
			mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
			mapper.getDeserializationConfig().findMixInClassFor(MyDecisionBase.class);
			//mapper.getDeserializationConfig().addMixInAnnotations(MyBase.class, MyDecisionBase.class);
			//		mapper.getSerializationConfig().addMixInAnnotations(MyBase.class, MyDecisionBase.class);

			MyChain chain = mapper.readValue(new File("C:\\work\\Skunk\\src\\test\\resources\\SimpleTest.json"),
					MyChain.class);

			System.out.println("******************************************************");
			System.out.println(mapper.writeValueAsString(chain));
			System.out.println("******************************************************");

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	//@Test
	public void testTest() {

		final Integer firstValue = 1;

		MyLogic<Integer> branch_01 = new MyLogic.Builder<>().setPermValue(firstValue).setOperator(OperatorEnum.GT)
				.build();

		MyLogic<Integer> branch_02 = new MyLogic.Builder<>().setPermValue(firstValue).setOperator(OperatorEnum.LT)
				.build();

		MyLogic<Boolean> branch_03 = new MyLogic.Builder<>().setBranchA(branch_01).setBranchB(branch_02).setOperator(
				OperatorEnum.OR).build();

		System.out.println(String.format("1 gt 1 : %s", branch_01.testFast(1)));
		spacer();
		System.out.println(String.format("1 gt 2 : %s", branch_01.testFast(2)));
		spacer();
		System.out.println(String.format("1 gt 3 : %s", branch_01.testFast(3)));
		spacer();

		System.out.println(String.format("1 lt 1 : %s", branch_02.testFast(1)));
		spacer();
		System.out.println(String.format("1 lt 2 : %s", branch_02.testFast(2)));
		spacer();
		System.out.println(String.format("1 lt 3 : %s", branch_02.testFast(3)));
		spacer();

		branch_01.setTempValue(4);
		branch_02.setTempValue(14);
		System.out.println(String.format("1,2,3: %s", branch_03.test()));
		spacer();

	}

	public void spacer() {
		System.out.println("---------------------------------------");
	}

	public static class CustomMapper extends ObjectMapper {
		@PostConstruct
		public void customConfiguration() {
			// Uses Enum.toString() for serialization of an Enum
			this.enable(WRITE_ENUMS_USING_TO_STRING);
			// Uses Enum.toString() for deserialization of an Enum
			this.enable(READ_ENUMS_USING_TO_STRING);
		}
	}
}
