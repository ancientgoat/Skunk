package com.premierinc.processrun.runner;

import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.base.InpNodeBase;
import com.premierinc.processrun.organize.DecisionOrganizer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DecisionRunner {

  private final DecisionOrganizer organizer;
  private final Map<String, Comparable> valueMap = Collections.emptyMap();
  private final Map<String, List<InpNodeBase>> identityNameMap;//new HashMap<>();

  public DecisionRunner(final DecisionOrganizer inOrganizer) {
    this.organizer = inOrganizer;
    this.identityNameMap = this.organizer.getIdentityNameMap();
  }

  /**
   *
   */
  public <V extends Comparable> void addValue(final String inName, final V inValue) {

    if (!this.identityNameMap.containsKey(inName)) {
      throw new SkException(String.format("Identity name '%s' not found.", inName));
    }

    Comparable existingValue = this.valueMap.get(inName);
    if (null != existingValue) {
      throw new SkException(String.format("Identity name '%s' already has a value of '%s'.", inName, existingValue));
    }

    this.valueMap.put(inName, inValue);
  }

  /**
   *
   */
  public boolean test() {
    validateValueMap();

    return false;
  }

  /**
   *
   */
  private void validateValueMap() {
    // If not all value in identityNameMap are set, tell them which ones are missing.
    int diffSize = this.identityNameMap.size() - this.valueMap.size();

    if (0 != diffSize) {
      final StringBuilder sb = new StringBuilder(String.format("%s Identity names not set", diffSize));
      this.identityNameMap.keySet().stream()
          .filter(k -> !this.valueMap.containsKey(k))
          .forEach(k -> sb.append(String.format("\nValue not set for Identity name '%s'", k)))
      ;
      throw new SkException(sb.toString());
    }
  }
}