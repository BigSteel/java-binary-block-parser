package com.igormaznitsa.jbbp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import com.igormaznitsa.jbbp.compiler.JBBPNamedFieldInfo;
import org.junit.jupiter.api.Test;

public class JBBPFieldStringTest {

  @Test
  public void testNameField() {
    final JBBPFieldString field =
        new JBBPFieldString(new JBBPNamedFieldInfo("test.field", "field", 123), "Huzzaa");
    final JBBPNamedFieldInfo namedField = field.getNameInfo();
    assertEquals("test.field", namedField.getFieldPath());
    assertEquals("field", namedField.getFieldName());
    assertEquals(123, namedField.getFieldOffsetInCompiledBlock());
  }

  @Test
  public void testReverseBits_Null() {
    assertNull(JBBPFieldString.reverseBits(null));
  }

  @Test
  public void testReverseBits_Text() {
    assertEquals("\u0C00谀䰀", JBBPFieldString.reverseBits("012"));
    assertEquals("012", JBBPFieldString.reverseBits(JBBPFieldString.reverseBits("012")));
  }

  @Test
  public void testGetAsString_NotNull() {
    final JBBPFieldString field =
        new JBBPFieldString(new JBBPNamedFieldInfo("test.field", "field", 123), "Huzzaa");
    assertEquals("Huzzaa", field.getAsString());
  }

  @Test
  public void testGetAsString_Null() {
    final JBBPFieldString field =
        new JBBPFieldString(new JBBPNamedFieldInfo("test.field", "field", 123), null);
    assertNull(field.getAsString());
  }

}