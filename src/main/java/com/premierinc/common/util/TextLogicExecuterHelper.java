package com.premierinc.common.util;

import com.premierinc.common.enumeration.TextOperatorEnum;
import com.premierinc.common.exception.SkException;
import com.premierinc.processinput.core.LeftRightText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 */
public class TextLogicExecuterHelper {

  private static final Map<TextOperatorEnum, Predicate<LeftRightText>> logicMap = new HashMap();

  static {
    logicMap.put(TextOperatorEnum.CONTAINS_ONE_OF, p -> containsOneOf(p.getLeftSide(), p.getRightList()));
    logicMap.put(TextOperatorEnum.LT, p -> lt(p.getLeftSide(), p.getRightSide()));
    logicMap.put(TextOperatorEnum.GT, p -> gt(p.getLeftSide(), p.getRightSide()));
    logicMap.put(TextOperatorEnum.EQ, p -> eq(p.getLeftSide(), p.getRightSide()));
  }

  private TextLogicExecuterHelper() {
  }

  public static Predicate buildPredicate(final TextOperatorEnum inOperator) {
    final Predicate<LeftRightText> predicate = logicMap.get(inOperator);

    if (null == predicate) {
      throw new SkException(String.format("Operator '%s' not implemented yet.", inOperator));
    }
    return predicate;
  }

  /**
   * true if input 'a GT b'.
   */
  //public static final <T extends String<T>> boolean gt(final T a, final T b) {
  public static final boolean gt(final String a, final String b) {
    return 0 < a.compareTo(b);
  }

  /**
   * true if input 'a LT b'.
   */
  //public static final <T extends String<T>> boolean lt(final String a, final String b) {
  public static final boolean lt(final String a, final String b) {
    return 0 > a.compareTo(b);
  }

  /**
   * true if input 'a EQ b'.
   */
  //public static final <T extends String<T>> boolean eq(final T a, final T b) {
  public static final boolean eq(final String a, final String b) {
    return 0 == a.compareTo(b);
  }

  /**
   * true if input containsOneOf many words.
   */
  public static final boolean containsOneOf(final String inTextBlock, final List<String> inWordList) {

    // Clean out double spaces
    final String textBlock = inTextBlock.replace("  ", " ");

    // Split the input text block into an UPPERCASE List
    final List<String> textParts = Arrays.asList(textBlock.toUpperCase().split(" "))
        .stream()
        .parallel()
        .map(w -> w.trim().toUpperCase())
        .filter(w -> 0 < w.length())
        .collect(Collectors.toList());
    ;

    // Clean the input Word List into an UPPERCASE List
    final List<String> wordList = inWordList.stream()
        .parallel()
        .map(w -> w.trim().toUpperCase())
        .filter(w -> 0 < w.length())
        .collect(Collectors.toList());

    boolean hasMatch = textParts.stream()
        .anyMatch(outerWord ->
            wordList.stream().anyMatch(innerWord -> innerWord.equals(outerWord))
        );

    return hasMatch;
  }
}
