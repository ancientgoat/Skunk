package com.premierinc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *
 */
public class JsonHelper {

  public static final String beanToJsonString(final Object inObject) {
    return _outputJson(inObject, getObjectMapper(null));
  }

  public static final String beanToYamlString(final Object inObject) {
    return _outputJson(inObject, getObjectMapper(new YAMLFactory()));
  }

  private static final String _outputJson(final Object inObject, final ObjectMapper inObjectMapper) {
    try {
      return inObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(inObject);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   *
   */
  private static final ObjectMapper getObjectMapper(final JsonFactory inJsonFactory) {
    final ObjectMapper objectMapper = new ObjectMapper(inJsonFactory);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper;
  }
}
