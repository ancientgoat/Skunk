package com.premierinc.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunner;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class MySimpleTest {

  public static final String FILE_ROOT_DIR = "C:/work/Skunk/src/test/resources/";
  public static final String ONE_RULE_TEST_FILE = String.format("%s/OneRuleTest.json", FILE_ROOT_DIR);
  public static final String TWO_RULES_TEST_FILE = String.format("%s/TwoRulesTest.json", FILE_ROOT_DIR);
  public static final String ONE_GROUP_TEST_FILE = String.format("%s/OneGroupTest.json", FILE_ROOT_DIR);

  @Test
  public void testReadFileTest() {
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstance();

      // =======================================================================
      // mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      // mapper.getDeserializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // mapper.getSerializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // =======================================================================

      File file = new File(ONE_RULE_TEST_FILE);
      DecisionChain chain = mapper.readValue(file, DecisionChain.class);

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
  public void testOneRuleTest() {
    try {
      final DecisionRunner decisionRunner = buildRunnerFromFile(ONE_RULE_TEST_FILE);

      decisionRunner.addValue("MILK", 16.3);

      try {
        decisionRunner.addValue("MILKXX", 16.3);
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunner.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Test
  public void testTwoRuleTest() {
    try {
      final DecisionRunner decisionRunner = buildRunnerFromFile(TWO_RULES_TEST_FILE);

      decisionRunner.addValue("MILK", 16.3);

      try {
        decisionRunner.execute();
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunner.addValue("ORANGE_JUICE", 3.16);

      decisionRunner.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private DecisionRunner buildRunnerFromFile(final String filePath) throws IOException {
    final ObjectMapper objectMapper = JsonMapperHelper.newInstance();
    final DecisionChain chain = objectMapper.readValue(new File(filePath), DecisionChain.class);
    final DecisionOrganizer decisionOrganizer = new DecisionOrganizer(chain);
    return new DecisionRunner(decisionOrganizer);
  }

  public void spacer() {
    System.out.println("---------------------------------------");
  }
}
