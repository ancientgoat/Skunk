package com.premierinc.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.premierinc.base.MyDecisionBase;
import com.premierinc.common.CamelCaseNamingStrategy;

import javax.annotation.PostConstruct;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;

/**
 *
 */
public class LogicMapperHelper {
  private LogicMapperHelper() {
  }

  public static final ObjectMapper newInstance() {
    ObjectMapper mapper = new CustomMapper();
    mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
    mapper.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
    mapper.getDeserializationConfig().findMixInClassFor(MyDecisionBase.class);
    return mapper;
  }

  private static class CustomMapper extends ObjectMapper {
    @PostConstruct
    public void customConfiguration() {
      // Uses Enum.toString() for serialization of an Enum
      this.enable(WRITE_ENUMS_USING_TO_STRING);
      // Uses Enum.toString() for deserialization of an Enum
      this.enable(READ_ENUMS_USING_TO_STRING);

      // this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
      // this.setPropertyNamingStrategy(new CamelCaseNamingStrategy());
      // this.getDeserializationConfig().findMixInClassFor(MyDecisionBase.class);
      // this.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
  }
}
