/*
 * Copyright 2017 Igor Maznitsa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igormaznitsa.jbbp.compiler.tokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


import com.igormaznitsa.jbbp.io.JBBPByteOrder;
import org.junit.jupiter.api.Test;

public class JBBPFieldTypeParameterContainerTest {

  @Test
  public void testConstructorAndGetters() {
    final String name = "name";
    final String extra = "extra";
    final JBBPFieldTypeParameterContainer params =
        new JBBPFieldTypeParameterContainer(JBBPByteOrder.BIG_ENDIAN, name, extra);
    assertSame(name, params.getTypeName());
    assertSame(extra, params.getExtraData());
    assertEquals(JBBPByteOrder.BIG_ENDIAN, params.getByteOrder());
  }

  @Test
  public void testToString() {
    assertEquals("int hello",
        new JBBPFieldTypeParameterContainer(JBBPByteOrder.BIG_ENDIAN, "int hello", null)
            .toString());
    assertEquals("<int hello",
        new JBBPFieldTypeParameterContainer(JBBPByteOrder.LITTLE_ENDIAN, "int hello", null)
            .toString());
    assertEquals("<bit:8 hello",
        new JBBPFieldTypeParameterContainer(JBBPByteOrder.LITTLE_ENDIAN, "bit hello", "8")
            .toString());
    assertEquals("<bit:8",
        new JBBPFieldTypeParameterContainer(JBBPByteOrder.LITTLE_ENDIAN, "bit", "8").toString());
  }

}
