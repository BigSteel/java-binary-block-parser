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

package com.igormaznitsa.jbbp.compiler.varlen;

import com.igormaznitsa.jbbp.JBBPNamedNumericFieldMap;
import com.igormaznitsa.jbbp.compiler.JBBPCompiledBlock;
import com.igormaznitsa.jbbp.compiler.conversion.ExpressionEvaluatorVisitor;
import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import java.io.Serializable;

/**
 * The Interface describes a class which can evaluate and provide an integer value.
 *
 * @since 1.0
 */
public interface JBBPIntegerValueEvaluator extends Serializable {
  /**
   * Calculate an integer value.
   *
   * @param inStream                   a bit input stream
   * @param currentCompiledBlockOffset the current offset in the compiled block
   * @param block                      a compiled script block
   * @param fieldMap                   a named numeric field map
   * @return calculated value as integer
   */
  int eval(JBBPBitInputStream inStream, int currentCompiledBlockOffset, JBBPCompiledBlock block,
           JBBPNamedNumericFieldMap fieldMap);

  /**
   * Visit all expression items
   *
   * @param block                      the compiled block contains value, must not be null
   * @param currentCompiledBlockOffset the offset of the data in the compiled block
   * @param visitor                    the visitor to visit items, must not be null
   * @since 1.3.0
   */
  void visitItems(JBBPCompiledBlock block, int currentCompiledBlockOffset,
                  ExpressionEvaluatorVisitor visitor);
}
