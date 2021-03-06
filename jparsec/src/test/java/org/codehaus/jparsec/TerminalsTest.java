package org.codehaus.jparsec;

import static java.util.Arrays.asList;
import static org.codehaus.jparsec.Asserts.assertFailure;
import static org.codehaus.jparsec.Asserts.assertParser;
import static org.codehaus.jparsec.Scanners.WHITESPACES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.codehaus.jparsec.Tokens.Tag;
import org.junit.Test;

/**
 * Unit test for {@link Terminals}.
 * 
 * @author Ben Yu
 */
public class TerminalsTest {

  @Test
  public void testSingleQuoteChar() {
    assertParser(Terminals.CharLiteral.SINGLE_QUOTE_TOKENIZER, "'a'", 'a');
    assertParser(Terminals.CharLiteral.SINGLE_QUOTE_TOKENIZER, "'\\''", '\'');
  }

  @Test
  public void testDoubleQuoteString() {
    assertParser(Terminals.StringLiteral.DOUBLE_QUOTE_TOKENIZER, "\"a\\r\\n\\t\"", "a\r\n\t");
    assertParser(Terminals.StringLiteral.DOUBLE_QUOTE_TOKENIZER, "\"\\\"\"", "\"");
  }

  @Test
  public void testSingleQuoteString() {
    assertParser(Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER, "'ab'", "ab");
    assertParser(Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER, "'a''b'", "a'b");
  }

  @Test
  public void testDecimalLiteralTokenizer() {
    assertParser(Terminals.DecimalLiteral.TOKENIZER, "1", Tokens.decimalLiteral("1"));
    assertParser(Terminals.DecimalLiteral.TOKENIZER, "01", Tokens.decimalLiteral("01"));
    assertParser(Terminals.DecimalLiteral.TOKENIZER, "09", Tokens.decimalLiteral("09"));
    assertParser(Terminals.DecimalLiteral.TOKENIZER, "12", Tokens.decimalLiteral("12"));
    assertParser(Terminals.DecimalLiteral.TOKENIZER, "12.3", Tokens.decimalLiteral("12.3"));
    assertParser(Terminals.DecimalLiteral.TOKENIZER, "0.1", Tokens.decimalLiteral("0.1"));
    assertParser(Terminals.DecimalLiteral.TOKENIZER, ".2", Tokens.decimalLiteral(".2"));
    assertFailure(Terminals.DecimalLiteral.TOKENIZER, "12x", 1, 3, "EOF expected, x encountered.");
  }

  @Test
  public void testIntegerLiteralTokenizer() {
    assertParser(Terminals.IntegerLiteral.TOKENIZER, "1", Tokens.fragment("1", Tag.INTEGER));
    assertParser(Terminals.IntegerLiteral.TOKENIZER, "12", Tokens.fragment("12", Tag.INTEGER));
    assertParser(Terminals.IntegerLiteral.TOKENIZER, "0", Tokens.fragment("0", Tag.INTEGER));
    assertParser(Terminals.IntegerLiteral.TOKENIZER, "01", Tokens.fragment("01", Tag.INTEGER));
    assertFailure(Terminals.IntegerLiteral.TOKENIZER, "12x", 1, 3, "EOF expected, x encountered.");
  }

  @Test
  public void testScientificNumberLiteralTokenizer() {
    assertParser(Terminals.ScientificNumberLiteral.TOKENIZER,
        "1E2", Tokens.scientificNotation("1", "2"));
    assertParser(Terminals.ScientificNumberLiteral.TOKENIZER,
        "1e+2", Tokens.scientificNotation("1", "2"));
    assertParser(Terminals.ScientificNumberLiteral.TOKENIZER,
        "10E-2", Tokens.scientificNotation("10", "-2"));
    assertFailure(Terminals.ScientificNumberLiteral.TOKENIZER,
        "1e2x", 1, 4, "EOF expected, x encountered.");
  }

  @Test
  public void testLongLiteralDecTokenizer() {
    assertParser(Terminals.LongLiteral.DEC_TOKENIZER, "1", 1L);
    assertParser(Terminals.LongLiteral.DEC_TOKENIZER, "10", 10L);
    assertFailure(Terminals.LongLiteral.DEC_TOKENIZER, "0", 1, 1);
    assertFailure(Terminals.LongLiteral.DEC_TOKENIZER, "12x", 1, 3, "EOF expected, x encountered.");
  }

  @Test
  public void testLongLiteralHexTokenizer() {
    assertParser(Terminals.LongLiteral.HEX_TOKENIZER, "0x0", 0L);
    assertParser(Terminals.LongLiteral.HEX_TOKENIZER, "0X10", 0X10L);
    assertParser(Terminals.LongLiteral.HEX_TOKENIZER, "0X1A", 0X1AL);
    assertParser(Terminals.LongLiteral.HEX_TOKENIZER, "0XFf", 0XFFL);
    assertFailure(Terminals.LongLiteral.HEX_TOKENIZER, "0", 1, 1);
    assertFailure(Terminals.LongLiteral.HEX_TOKENIZER, "1", 1, 1);
    assertFailure(Terminals.LongLiteral.HEX_TOKENIZER,
        "0x12x", 1, 5, "EOF expected, x encountered.");
  }

  @Test
  public void testTokenizeHexAsLong_throwsIfStringIsTooShort() {
    try {
      NumberLiteralsTranslator.tokenizeHexAsLong("0x");
      fail();
    } catch (IllegalStateException e) {}
  }

  @Test
  public void testLongLiteralOctTokenizer() {
    assertParser(Terminals.LongLiteral.OCT_TOKENIZER, "0", 0L);
    assertParser(Terminals.LongLiteral.OCT_TOKENIZER, "017", 15L);
    assertFailure(Terminals.LongLiteral.OCT_TOKENIZER, "1", 1, 1);
    assertFailure(Terminals.LongLiteral.OCT_TOKENIZER, "0X1", 1, 2);
    assertFailure(Terminals.LongLiteral.OCT_TOKENIZER, "08", 1, 2);
    assertFailure(Terminals.LongLiteral.OCT_TOKENIZER, "01x", 1, 3, "EOF expected, x encountered.");
  }

  @Test
  public void testLongLiteralTokenizer() {
    assertParser(Terminals.LongLiteral.TOKENIZER, "0", 0L);
    assertParser(Terminals.LongLiteral.TOKENIZER, "010", 8L);
    assertParser(Terminals.LongLiteral.TOKENIZER, "12", 12L);
    assertParser(Terminals.LongLiteral.TOKENIZER, "0X10", 16L);
    assertParser(Terminals.LongLiteral.TOKENIZER, "9", 9L);
    assertFailure(Terminals.LongLiteral.TOKENIZER, "1x", 1, 2, "EOF expected, x encountered.");
  }

  @Test
  public void testIdentifierTokenizer() {
    assertParser(Terminals.Identifier.TOKENIZER, "foo", Tokens.identifier("foo"));
    assertParser(Terminals.Identifier.TOKENIZER, "foo1", Tokens.identifier("foo1"));
    assertParser(Terminals.Identifier.TOKENIZER, "FOO", Tokens.identifier("FOO"));
    assertParser(Terminals.Identifier.TOKENIZER, "FOO_2", Tokens.identifier("FOO_2"));
    assertParser(Terminals.Identifier.TOKENIZER, "_foo", Tokens.identifier("_foo"));
    assertFailure(Terminals.Identifier.TOKENIZER, "1foo", 1, 1);
  }

  @Test
  public void testCharLiteralParser() {
    assertParser(
        Terminals.CharLiteral.PARSER.from(Terminals.CharLiteral.SINGLE_QUOTE_TOKENIZER, WHITESPACES),
        "'a'", 'a');
  }

  @Test
  public void testLongLiteralParser() {
    assertParser(Terminals.LongLiteral.PARSER.from(Terminals.LongLiteral.DEC_TOKENIZER, WHITESPACES),
        "1", 1L);
  }

  @Test
  public void testStringLiteralParser() {
    assertParser(
        Terminals.StringLiteral.PARSER.from(
            Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER, WHITESPACES),
        "'abc'", "abc");
  }

  @Test
  public void testIdentifierParser() {
    assertParser(Terminals.Identifier.PARSER.from(Terminals.Identifier.TOKENIZER, WHITESPACES),
        "foo", "foo");
  }

  @Test
  public void testIntegerLiteralParser() {
    assertParser(
        Terminals.IntegerLiteral.PARSER.from(Terminals.IntegerLiteral.TOKENIZER, WHITESPACES),
        "123", "123");
  }

  @Test
  public void testDecimalLiteralParser() {
    assertParser(
        Terminals.DecimalLiteral.PARSER.from(Terminals.DecimalLiteral.TOKENIZER, WHITESPACES),
        "1.23", "1.23");
  }

  @Test
  public void testFromFragment() {
    assertEquals("", Terminals.fromFragment().toString());
    assertEquals("1", Terminals.fromFragment(1).toString());
    TokenMap<String> fromToken = Terminals.fromFragment("foo", 1);
    assertEquals("[foo, 1]", fromToken.toString());
    assertEquals("test", fromToken.map(new Token(0, 3, Tokens.fragment("test", "foo"))));
    assertEquals("test", fromToken.map(new Token(0, 3, Tokens.fragment("test", 1))));
    assertNull(fromToken.map(new Token(0, 3, Tokens.fragment("test", "bar"))));
    assertNull(fromToken.map(new Token(0, 3, Tokens.fragment("test", 2))));
  }

  @Test
  public void testToken_noTokenName() {
    Terminals terminals = Terminals.operators("+", "-");
    Parser<Token> parser = terminals.token().from(terminals.tokenizer(), WHITESPACES);
    assertFailure(parser, "+", 1, 1);
  }

  @Test
  public void testToken_oneTokenNameOnly() {
    Terminals terminals = Terminals.operators("+", "-");
    Parser<Token> parser =
        terminals.token("+").from(terminals.tokenizer(), WHITESPACES);
    assertParser(parser, "+", new Token(0, 1, Tokens.reserved("+")));
    assertFailure(parser, "-", 1, 1, "+ expected, - encountered.");
  }

  @Test
  public void testToken_tokenNamesListed() {
    Terminals terminals = Terminals.operators("+", "-");
    Parser<Token> parser = terminals.token("+", "-").from(terminals.tokenizer(), WHITESPACES);
    assertParser(parser, "+", new Token(0, 1, Tokens.reserved("+")));
    assertParser(parser, "-", new Token(0, 1, Tokens.reserved("-")));
    assertFailure(parser, "*", 1, 1, "+ or - expected, * encountered.");
  }

  @Test
  public void testPhrase() {
    String[] keywords = { "hello", "world", "hell" };
    Terminals terminals = Terminals.operators().words(Scanners.IDENTIFIER).keywords(asList(keywords)).build();
    Parser<?> parser =
        terminals.phrase("hello", "world").from(terminals.tokenizer(), Scanners.WHITESPACES);
    parser.parse("hello   world");
    assertFailure(parser, "hello hell", 1, 7, "world");
    assertFailure(parser, "hell", 1, 1, "hello world");
    assertParser(parser.optional(), "hello hell", null, "hello hell");
  }

  @Test
  public void testCaseSensitive() {
    String[] keywords = { "foo", "bar", "baz" };
    Terminals terminals = Terminals.operators("+", "-").words(Scanners.IDENTIFIER).keywords(asList(keywords)).build();
    Parser<Token> parser =
        terminals.token("+", "-", "foo", "bar").from(terminals.tokenizer(), WHITESPACES);
    assertParser(parser, "+", new Token(0, 1, Tokens.reserved("+")));
    assertParser(parser, "-", new Token(0, 1, Tokens.reserved("-")));
    assertParser(parser, "foo", new Token(0, 3, Tokens.reserved("foo")));
    assertParser(parser, "bar", new Token(0, 3, Tokens.reserved("bar")));
    assertFailure(parser, "baz", 1, 1, "+, -, foo or bar expected, baz encountered.");
    assertFailure(parser, "Foo", 1, 1, "+, -, foo or bar expected, Foo encountered.");
    assertFailure(parser, "123", 1, 1, "+, -, foo or bar expected, 1 encountered.");
    assertParser(
        Terminals.Identifier.PARSER.from(terminals.tokenizer(), WHITESPACES), "FOO", "FOO");
  }

  @Test
  public void testCaseInsensitive() {
    String[] keywords = { "foo", "bar", "baz" };
    Terminals terminals =
        Terminals.operators("+", "-").words(Scanners.IDENTIFIER).caseInsensitiveKeywords(asList(keywords)).build();
    Parser<Token> parser =
        terminals.token("+", "-", "foo", "bar").from(terminals.tokenizer(), WHITESPACES);
    assertParser(parser, "+", new Token(0, 1, Tokens.reserved("+")));
    assertParser(parser, "-", new Token(0, 1, Tokens.reserved("-")));
    assertParser(parser, "foo", new Token(0, 3, Tokens.reserved("foo")));
    assertParser(parser, "Foo", new Token(0, 3, Tokens.reserved("foo")));
    assertParser(parser, "bar", new Token(0, 3, Tokens.reserved("bar")));
    assertFailure(parser, "baz", 1, 1, "+, -, foo or bar expected, baz encountered.");
    assertFailure(parser, "123", 1, 1, "+, -, foo or bar expected, 1 encountered.");
    assertParser(
        Terminals.Identifier.PARSER.from(terminals.tokenizer(), WHITESPACES), "xxx", "xxx");
  }

  @Test
  public void testCaseSensitive_withScanner() {
    Terminals terminals = Terminals
        .operators("+", "-")
        .words(Scanners.INTEGER)
        .keywords("12", "34")
        .build();
  Parser<Token> parser =
      terminals.token("+", "-", "12", "34").from(terminals.tokenizer(), WHITESPACES);
  assertParser(parser, "+", new Token(0, 1, Tokens.reserved("+")));
  assertParser(parser, "-", new Token(0, 1, Tokens.reserved("-")));
  assertParser(parser, "12", new Token(0, 2, Tokens.reserved("12")));
  assertParser(parser, "34", new Token(0, 2, Tokens.reserved("34")));
  assertFailure(parser, "foo", 1, 1, "+, -, 12 or 34 expected, f encountered.");
  assertFailure(parser, "123", 1, 1, "+, -, 12 or 34 expected, 123 encountered.");
  assertParser(
      Terminals.Identifier.PARSER.from(terminals.tokenizer(), WHITESPACES), "123", "123");
  }

  @Test
  public void testCaseInsensitive_withScanner() {
    Terminals terminals = Terminals
        .operators("+", "-")
        .words(Scanners.INTEGER)
        .caseInsensitiveKeywords("12", "34")
        .build();
  Parser<Token> parser =
      terminals.token("+", "-", "12", "34").from(terminals.tokenizer(), WHITESPACES);
  assertParser(parser, "+", new Token(0, 1, Tokens.reserved("+")));
  assertParser(parser, "-", new Token(0, 1, Tokens.reserved("-")));
  assertParser(parser, "12", new Token(0, 2, Tokens.reserved("12")));
  assertParser(parser, "34", new Token(0, 2, Tokens.reserved("34")));
  assertFailure(parser, "foo", 1, 1, "+, -, 12 or 34 expected, f encountered.");
  assertFailure(parser, "123", 1, 1, "+, -, 12 or 34 expected, 123 encountered.");
  assertParser(
      Terminals.Identifier.PARSER.from(terminals.tokenizer(), WHITESPACES), "123", "123");
  }

  @Test
  public void testCheckDup() {
    Terminals.checkDup(asList("a", "b"), asList("+", "-"));
    Terminals.checkDup(asList("a", "b"), asList("A", "B"));
    assertDup(asList("a", "b"), asList("x", "b"));
  }
  
  private static void assertDup(Iterable<String> a, Iterable<String> b) {
    try {
      Terminals.checkDup(a, b);
      fail();
    } catch (IllegalArgumentException e) {}
  }
}
