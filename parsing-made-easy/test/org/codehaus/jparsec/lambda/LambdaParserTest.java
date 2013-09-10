package org.codehaus.jparsec.lambda;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class LambdaParserTest {

  @Test
  public void canParseSingleVariable() throws Exception {
    LambdaParser parser = new LambdaParser();
    
    assertThat(parser.parse("x")).isEqualTo(new Var("x"));
    assertThat(parser.parse("y")).isEqualTo(new Var("y"));
    assertThat(parser.parse("foo")).isEqualTo(new Var("foo"));
  }
}
