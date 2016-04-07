package com.premierinc.common.util;

import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.constructor.SafeConstructor;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunnerText;
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
public class MyTextSimpleTest {

  public static final String ONE_RULE_FILE_NAME = "TextOneRuleTest.json";
  public static final String TWO_RULES_FILE_NAME = "TextTwoRulesTest.json";
  public static final String ONE_GROUP_FILE_NAME = "TextOneGroupTest.json";

  public static final String FILE_ROOT_DIR = "C:/work/Skunk/src/test/resources/";
  public static final String ONE_RULE_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_RULE_FILE_NAME);
  public static final String TWO_RULES_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, TWO_RULES_FILE_NAME);
  public static final String ONE_GROUP_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_GROUP_FILE_NAME);

  private StopWatch stopwatch;

  @Test
  public void testReadFileTest() {
    timerStart();
    DecisionRunnerText decisionRunnerText = null;
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstanceJson();
      decisionRunnerText = buildRunnerFromFile(ONE_RULE_TEST_FILE);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testReadFileTest");
  }


  @Test
  public void testOneRuleTest() {
    timerStart();
    try {
      final DecisionRunnerText decisionRunnerText = buildRunnerFromFile(ONE_RULE_TEST_FILE);

      decisionRunnerText.setValue("PARAGRAPH_MORE_OR_LESS_ABOUT_MILK",
          "This is a paragraph that may or may not have some words about milk.  But I do know this, and that is\n"
              + " the world is ending.  Cats and Dogs are living in sin.  Grown men are running screaming through the\n"
              + " streets.  Even small children have started in their backyards digging bunkers.\n"
      );

      try {
        decisionRunnerText.addValueToList("MILKXX", "16.3");
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      boolean answer = decisionRunnerText.execute();

      System.out.println(String.format("And the anwser is '%s'.", answer));

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneRuleTest");
  }

  @Test
  public void testTwoRuleTest() {
    timerStart();
    try {
      final DecisionRunnerText decisionRunnerText = buildRunnerFromFile(TWO_RULES_TEST_FILE);

      decisionRunnerText.addValueToList("MILK", "16.3");

      try {
        decisionRunnerText.execute();
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunnerText.addValueToList("ORANGE_JUICE", "3.16");

      decisionRunnerText.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testTwoRuleTest");
  }


  @Test
  public void testOneGroupTest() {
    stopwatch.start();

    try {
      final DecisionRunnerText decisionRunnerText = buildRunnerFromFile(ONE_GROUP_TEST_FILE);

      decisionRunnerText.addValueToList("MILK", "16.3");

      try {
        decisionRunnerText.execute();
        Assert.fail("We were suppose to get an error here????");
      } catch (SkException e) {
        int i = 0;
      }
      decisionRunnerText.addValueToList("ORANGE_JUICE", "3.16");

      decisionRunnerText.execute();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneGroupTest");
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
      final DecisionRunnerText decisionRunnerText = buildRunnerFromFile(ONE_GROUP_TEST_FILE);

      for (int i = maxStart; i < maxTests; i++) {
        decisionRunnerText.addValueToList("MILK", "" + (i + 0.3));
        decisionRunnerText.addValueToList("ORANGE_JUICE", "" + (i + 0.16));
        Boolean trueOrFalse = decisionRunnerText.execute();

        countMap.put(trueOrFalse, 1 + countMap.get(trueOrFalse));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    timerStop("testOneGroupRepeatTest");
    System.out.println(countMap);
  }

  private DecisionRunnerText buildRunnerFromFile(final String filePath) throws IOException {

    File file = new File(filePath);
    ObjectMapper objectMapper = JsonMapperHelper.newInstanceJson();
    DecisionChain chain = objectMapper.readValue(file, DecisionChain.class);
    DecisionOrganizer decisionOrganizer = new DecisionOrganizer(chain);

    return new DecisionRunnerText(decisionOrganizer);
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
}
