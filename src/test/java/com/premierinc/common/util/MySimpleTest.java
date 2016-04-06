package com.premierinc.common.util;

import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.constructor.SafeConstructor;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunner;
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
public class MySimpleTest {

  public static final String ONE_RULE_FILE_NAME = "OneRuleTest.json";
  public static final String TWO_RULES_FILE_NAME = "TwoRulesTest.json";
  public static final String ONE_GROUP_FILE_NAME = "OneGroupTest.json";

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
    try {
      ObjectMapper mapper = JsonMapperHelper.newInstanceJson();

      // =======================================================================
      // mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      // mapper.getDeserializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // mapper.getSerializationConfig().addMixInAnnotations(MyBase.class, InpNodeBase.class);
      // =======================================================================

      buildRunnerFromFile(ONE_RULE_TEST_FILE);

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
    timerStop("testOneRuleTest");
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
    timerStop("testTwoRuleTest");
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
    timerStop("testOneGroupRepeatTest");
    System.out.println(countMap);
  }

  private DecisionRunner buildRunnerFromFile(final String filePath) throws IOException {

    ObjectMapper objectMapper;
    DecisionChain chain;
    DecisionOrganizer decisionOrganizer;
    File file = new File(filePath);

    if (filePath.endsWith("yml") || filePath.endsWith("yaml")) {
      objectMapper = JsonMapperHelper.newInstanceYaml();


      Yaml yaml = new Yaml(new SafeConstructor());
      chain = (DecisionChain) yaml.load(new FileReader(file));
//      yaml.loadAll(input).forEach(System.out::println);
      decisionOrganizer = new DecisionOrganizer(chain);


    } else {
      objectMapper = JsonMapperHelper.newInstanceJson();
      chain = objectMapper.readValue(file, DecisionChain.class);
    }
    decisionOrganizer = new DecisionOrganizer(chain);

    return new DecisionRunner(decisionOrganizer);
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
}
