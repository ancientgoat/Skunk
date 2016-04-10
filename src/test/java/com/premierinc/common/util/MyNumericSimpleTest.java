package com.premierinc.common.util;

import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.constructor.SafeConstructor;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunnerGeneric;
import com.premierinc.processrun.runner.DecisionRunnerGeneric;
import org.springframework.util.StopWatch;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class MyNumericSimpleTest {

  public static final String ONE_RULE_FILE_NAME = "NumericOneRuleTest.json";
  public static final String TWO_RULES_FILE_NAME = "NumericTwoRulesTest.json";
  public static final String ONE_GROUP_FILE_NAME = "NumericOneGroupTest.json";

  public static final String FILE_ROOT_DIR = "C:/work/Skunk/src/test/resources/";
  public static final String ONE_RULE_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_RULE_FILE_NAME);
  public static final String TWO_RULES_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, TWO_RULES_FILE_NAME);
  public static final String ONE_GROUP_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_GROUP_FILE_NAME);

  private StopWatch stopwatch;

  /**
   * Simple test that just reads in a Rule, that's all.
   */
  @Test
  public void testReadFileTest() {
    timerStart();
    DecisionRunnerGeneric decisionRunner;
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstanceJson();
      buildRunnerFromFile(ONE_RULE_TEST_FILE);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testReadFileTest");
  }

  /**
   * Read and execute(test) a simple rule.
   */
  @Test
  public void testOneRuleTest() {
    timerStart();
    boolean answer = false;
    try {
      final DecisionRunnerGeneric decisionRunner = buildRunnerFromFile(ONE_RULE_TEST_FILE);

      decisionRunner.setValue("MILK", 16.3);

      try {
        decisionRunner.setValue("MILKXX", 16.3);
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      answer = decisionRunner.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneRuleTest", answer);
  }

  /**
   * Read and execute(test) a two simple rule, separated by an And or Or..
   */
  @Test
  public void testTwoRuleTest() {
    timerStart();
    boolean answer = false;
    try {
      final DecisionRunnerGeneric decisionRunnerNumeric = buildRunnerFromFile(TWO_RULES_TEST_FILE);

      decisionRunnerNumeric.setValue("MILK", 16.3);

      try {
        decisionRunnerNumeric.execute();
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunnerNumeric.setValue("ORANGE_JUICE", 3.16);

      answer = decisionRunnerNumeric.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testTwoRuleTest", answer);
  }

  /**
   * Read and execute(test) a two simple rules, the second rule being a group of two rules.
   */
  @Test
  public void testOneGroupTest() {
    timerStart();
    boolean answer = false;

    try {
      final DecisionRunnerGeneric decisionRunnerNumeric = buildRunnerFromFile(ONE_GROUP_TEST_FILE);
      decisionRunnerNumeric.setValue("MILK", 16.3);

      try {
        decisionRunnerNumeric.execute();
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException skE) {
        ; // We are suppose to get here, because all values in the Rules/Decision are not set.
      }
      decisionRunnerNumeric.setValue("ORANGE_JUICE", 3.16);
      answer = decisionRunnerNumeric.execute();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneGroupTest", answer);
  }

  /**
   * Read and execute(test) a two simple rules, the second rule being a group of two rules.
   * Do this many, many times.
   */
  @Test
  public void testOneGroupRepeatTest() {

    timerStart();
    final int maxStart = -1000000;
    final int maxTests = maxStart + 2000000;

    Map<Boolean, Integer> countMap = Maps.newHashMap();
    countMap.put(Boolean.FALSE, 0);
    countMap.put(Boolean.TRUE, 0);

    try {
      final DecisionRunnerGeneric decisionRunnerNumeric = buildRunnerFromFile(ONE_GROUP_TEST_FILE);

      for (int i = maxStart; i < maxTests; i++) {
        decisionRunnerNumeric.setValue("MILK", i + 0.3);
        decisionRunnerNumeric.setValue("ORANGE_JUICE", i + 0.16);
        Boolean trueOrFalse = decisionRunnerNumeric.execute();
        // Keep track of how many tests return 'true' and 'false'.
        countMap.put(trueOrFalse, 1 + countMap.get(trueOrFalse));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }

    timerStop("testOneGroupRepeatTest");
    spacer();
    System.out.println(countMap);
    spacer();
  }

  /**
   * Read an input file, and return a DecisionRunner.
   */
  private DecisionRunnerGeneric buildRunnerFromFile(final String filePath) throws IOException {

    ObjectMapper objectMapper;
    DecisionChain chain;
    DecisionOrganizer decisionOrganizer;
    File file = new File(filePath);

    objectMapper = JsonMapperHelper.newInstanceJson();
    chain = objectMapper.readValue(file, DecisionChain.class);
    decisionOrganizer = new DecisionOrganizer(chain);

    return new DecisionRunnerGeneric(decisionOrganizer);
  }

  /**
   * Print a line spacer to STDOUT.
   */
  public void spacer() {
    System.out.println("---------------------------------------");
  }

  /**
   * Start a timer, we'd like to time each test.
   */
  private void timerStart() {
    stopwatch = new StopWatch();
    stopwatch.start();
  }

  /**
   * Stop the timer.
   */
  private void timerStop(final String inDescription) {
    stopwatch.stop();
    System.out.println(String.format("*** : %s : %s", inDescription, stopwatch.toString()));
  }

  /**
   * Stop the timer, and show the 'true' or 'false' result of a test.
   */
  private void timerStop(final String inDescription, final boolean inAnswer) {
    stopwatch.stop();
    System.out.println(String.format("*** : %s : %s: %s", inDescription, stopwatch.toString(), inAnswer));
  }

  /**
   * Not used.  Read a json file and output a yaml file.
   */
  private void outputYaml(final String fileName, final String outputFileName) {
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstanceJson();
      File file = new File(fileName);
      String yamlName = outputFileName.replace(".json", ".yaml");
      File outputFile = new File(yamlName);
      FileWriter writer = new FileWriter(outputFile);

      DecisionChain chain = mapper.readValue(file, DecisionChain.class);

      Map<String, DecisionChain> map = Maps.newHashMap();
      map.put("decisionList", chain);

      writer.write(JsonHelper.beanToYamlString(map));
      writer.flush();
      writer.close();
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}
