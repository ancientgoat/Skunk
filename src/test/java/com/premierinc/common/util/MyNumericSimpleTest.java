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

  //  @Test
  //  public void testConvertAllToYamlTEst() {
  //    try {
  //      final String path = "C:/work/Skunk/src/test/resources";
  //      outputYaml(ONE_RULE_TEST_FILE, String.format("%s/%s", path, ONE_RULE_FILE_NAME));
  //      outputYaml(TWO_RULES_TEST_FILE, String.format("%s/%s", path, TWO_RULES_FILE_NAME));
  //      outputYaml(ONE_GROUP_TEST_FILE, String.format("%s/%s", path, ONE_GROUP_FILE_NAME));
  //
  //    } catch (Exception e) {
  //      throw new IllegalArgumentException(e);
  //    }
  //  }

  @Test
  public void testReadFileTest() {
    timerStart();
    DecisionRunnerGeneric decisionRunner;
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstanceJson();

      // =======================================================================
      // mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      // mapper.getDeserializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // mapper.getSerializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // =======================================================================

      decisionRunner = buildRunnerFromFile(ONE_RULE_TEST_FILE);

    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testReadFileTest");
  }

  /**
   *
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
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunnerNumeric.setValue("ORANGE_JUICE", 3.16);

      answer = decisionRunnerNumeric.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneGroupTest", answer);
  }

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

        countMap.put(trueOrFalse, 1 + countMap.get(trueOrFalse));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneGroupRepeatTest");
    System.out.println(countMap);
  }

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

  public void spacer() {
    System.out.println("---------------------------------------");
  }

  private void timerStart() {
    stopwatch = new StopWatch();
    stopwatch.start();
  }

  private void timerStop(final String inDescription) {
    stopwatch.stop();
    System.out.println(String.format("*** : %s : %s", inDescription, stopwatch.toString()));
  }

  private void timerStop(final String inDescription, final boolean inAnswer) {
    stopwatch.stop();
    System.out.println(String.format("*** : %s : %s: %s", inDescription, stopwatch.toString(), inAnswer));
  }
}
