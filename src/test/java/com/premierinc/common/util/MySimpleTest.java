package com.premierinc.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.InpLogic;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunner;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class MySimpleTest {

  public static final String FILE_ROOT_DIR = "C:\\work\\Skunk\\src\\test\\resources\\";
  public static final String ONE_RULE_TEST_FILE = String.format(FILE_ROOT_DIR, "OneRuleTest.json");
  public static final String TWO_RULES_TEST_FILE = String.format(FILE_ROOT_DIR, "TwoRulesTest.json");
  public static final String ONE_GROUP_TEST_FILE = String.format(FILE_ROOT_DIR, "OneGroupTest.json");

  @Test
  public void testReadFileTest() {
    try {
      ObjectMapper mapper = LogicMapperHelper.newInstance();

      // =======================================================================
      // mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      // mapper.getDeserializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // mapper.getSerializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // =======================================================================

      DecisionChain chain = mapper.readValue(new File(ONE_RULE_TEST_FILE), DecisionChain.class);

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
      DecisionChain chain = objectMapper.readValue(new File(ONE_RULE_TEST_FILE), DecisionChain.class);

      final DecisionOrganizer decisionOrganizer = new DecisionOrganizer(chain);
      final DecisionRunner decisionRunner = new DecisionRunner(decisionOrganizer);

      decisionRunner.addValue("MILK", 16.3);

      try {
        decisionRunner.addValue("MILKXX", 16.3);
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunner.test();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void spacer() {
    System.out.println("---------------------------------------");
  }

}
