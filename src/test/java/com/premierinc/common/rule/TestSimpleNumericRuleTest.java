package com.premierinc.common.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.common.util.JsonMapperHelper;
import com.premierinc.processinput.core.DecisionChain;
import com.premierinc.processrun.organize.DecisionOrganizer;
import com.premierinc.processrun.runner.DecisionRunnerGeneric;
import com.premierinc.rule.base.SkRuleBase;
import com.premierinc.rule.common.SkRuleNumeric;
import com.premierinc.rule.inf.SkRuleValueInf;
import org.easyrules.api.Rule;
import org.easyrules.api.RulesEngine;
import org.easyrules.core.CompositeRule;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

/**
 *
 */
public class TestSimpleNumericRuleTest {

    public static final String ONE_RULE_FILE_NAME = "SimpleNumericRule_01.json";

    public static final String FILE_ROOT_DIR = "C:/work/Skunk/src/test/resources/easyrules";
    public static final String ONE_RULE_TEST_FILE = String.format("%s/%s", FILE_ROOT_DIR, ONE_RULE_FILE_NAME);

    @Test
    public void testSimpleRuleTest() {

        final RulesEngine rulesEngine = aNewRulesEngine()
                .withSkipOnFirstAppliedRule(false) // OR
                .withSkipOnFirstFailedRule(true)   // AND
                .build();

        SkRuleBase rule = buildRunnerFromFile(ONE_RULE_TEST_FILE);

        CompositeRule myCompositeRule =
                new CompositeRule("myCompositeRule", "a composite rule");
        myCompositeRule.addRule(rule);

        rulesEngine.registerRule(myCompositeRule);

        rule.setLeftSide(new BigDecimal(12));

        rulesEngine.fireRules();


    }

    /**
     * Read an input file, and return a DecisionRunner.
     */
    private SkRuleBase buildRunnerFromFile(final String filePath) {

        try {
            ObjectMapper objectMapper;
            File file = new File(filePath);
            objectMapper = JsonMapperHelper.newInstanceJson();
            SkRuleBase rule = objectMapper.readValue(file, SkRuleBase.class);
            return rule;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
