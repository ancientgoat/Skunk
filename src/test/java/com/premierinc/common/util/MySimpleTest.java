package com.premierinc.common.util;

import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunner;
import org.springframework.util.StopWatch;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class MySimpleTest {

  public static final String ONE_RULE_FILE_NAME = "OneRuleTest.json";
  public static final String TWO_RULES_FILE_NAME = "TwoRulesTest.json";
  public static final String ONE_GROUP_FILE_NAME = "OneGroupTest.json";

  public static final String FILE_ROOT_DIR = "C:/work/Skunk/src/test/resources/";
  public static final String ONE_RULE_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_RULE_FILE_NAME);
  public static final String TWO_RULES_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, TWO_RULES_FILE_NAME);
  public static final String ONE_GROUP_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_GROUP_FILE_NAME);

  private StopWatch stopwatch = new StopWatch();

  @Test
  public void testReadFileTest() {
    timerStart();
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

      final String path = "C:/work/Skunk/src/test/resources";
      outputYaml(ONE_RULE_TEST_FILE, String.format("%s/%s", path, ONE_RULE_FILE_NAME));
      outputYaml(TWO_RULES_TEST_FILE, String.format("%s/%s", path, TWO_RULES_FILE_NAME));
      outputYaml(ONE_GROUP_TEST_FILE, String.format("%s/%s", path, ONE_GROUP_FILE_NAME));

    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    timerStop();
  }

  /**
   *
   */
  private void outputYaml(final String fileName, final String outputFileName) {
    timerStart();
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstance();
      File file = new File(fileName);
      String yamlName = outputFileName.replace(".json", ".yaml");
      File outputFile = new File(yamlName);
      FileWriter writer = new FileWriter(outputFile);

      DecisionChain chain = mapper.readValue(file, DecisionChain.class);

      writer.write(JsonHelper.beanToYamlString(chain));
      writer.flush();
      writer.close();
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    timerStop();
  }

  @Test
  public void testOneRuleTest() {
    timerStart();
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
    timerStop();
  }

  @Test
  public void testTwoRuleTest() {
    timerStart();
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
    timerStop();
  }


  @Test
  public void testOneGroupTest() {
    stopwatch.start();

    try {
      final DecisionRunner decisionRunner = buildRunnerFromFile(ONE_GROUP_TEST_FILE);

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
    timerStop();
  }


  @Test
  public void testOneGroupRepeatTest() {
    stopwatch.start();

    final int maxStart = -1000000;
    final int maxTests = maxStart + 2000000;

    Map<Boolean, Integer> countMap = Maps.newHashMap();
    countMap.put(Boolean.FALSE, 0);
    countMap.put(Boolean.TRUE, 0);

    try {
      final DecisionRunner decisionRunner = buildRunnerFromFile(ONE_GROUP_TEST_FILE);

      for (int i = maxStart; i < maxTests; i++) {
        decisionRunner.addValue("MILK", i + 0.3);
        decisionRunner.addValue("ORANGE_JUICE", i + 0.16);
        Boolean trueOrFalse = decisionRunner.execute();

        countMap.put(trueOrFalse, 1 + countMap.get(trueOrFalse));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop();
    System.out.println(countMap);
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

  private void timerStart() {
    stopwatch.start();
  }

  private void timerStop() {
    stopwatch.stop();
    System.out.println(String.format("********* : %s", stopwatch.toString()));
  }
}
