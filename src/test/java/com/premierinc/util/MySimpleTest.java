package com.premierinc.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.base.MyDecisionBase;
import com.premierinc.common.CamelCaseNamingStrategy;

import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;

import com.premierinc.core.MyLogicOther;
import com.premierinc.core.RuleChain;
import com.premierinc.runner.RuleOrganizer;
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
      ObjectMapper mapper = LogicMapperHelper.newInstance();

      // =======================================================================
      // mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      // mapper.getDeserializationConfig().addMixInAnnotations(MyBase.class, MyDecisionBase.class);
      // mapper.getSerializationConfig().addMixInAnnotations(MyBase.class, MyDecisionBase.class);
      // =======================================================================

      RuleChain chain = mapper.readValue(new File("C:\\work\\Skunk\\src\\test\\resources\\SimpleTest.json"),
          RuleChain.class);

      System.out.println("******************************************************");
      System.out.println(JsonHelper.beanToJsonString(chain));
      System.out.println("******************************************************");
      System.out.println(JsonHelper.beanToYamlString(chain));
      System.out.println("******************************************************");

    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Test
  public void testRuleOrganizerTest() {
    try {
      ObjectMapper objectMapper = LogicMapperHelper.newInstance();
      RuleChain chain = objectMapper.readValue(new File("C:\\work\\Skunk\\src\\test\\resources\\SimpleTest.json"),
          RuleChain.class);

    final RuleOrganizer ruleOrganizer = new RuleOrganizer(chain);

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  //@Test
  public void testTest() {

    final Integer firstValue = 1;

    MyLogicOther<Integer> branch_01 = new MyLogicOther.Builder<>().setPermValue(firstValue).setOperator(OperatorEnum.GT)
        .build();

    MyLogicOther<Integer> branch_02 = new MyLogicOther.Builder<>().setPermValue(firstValue).setOperator(OperatorEnum.LT)
        .build();

    MyLogicOther<Boolean> branch_03 = new MyLogicOther.Builder<>().setBranchA(branch_01).setBranchB(branch_02).setOperator(
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

}
