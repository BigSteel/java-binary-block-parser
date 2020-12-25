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
import com.igormaznitsa.jbbp.compiler.JBBPNamedFieldInfo;
import com.igormaznitsa.jbbp.compiler.conversion.ExpressionEvaluatorVisitor;
import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.model.JBBPNumericField;

/**
 * Class implements an evaluator which works with only field.
 *
 * @since 1.0
 */
public final class JBBPOnlyFieldEvaluator implements JBBPIntegerValueEvaluator {
  private static final long serialVersionUID = -1031131501937541693L;

  /**
   * The Index in named field area for the field which is used by the evaluator.
   */
  private final int namedFieldIndex;
  /**
   * An External field name which value will be requested by the evaluator. It
   * can be null.
   */
  private final String externalFieldName;

  /**
   * The Constructor.
   *
   * @param externalFieldName the external field name, it can be null.
   * @param namedFieldIndex   the index of a named field in named field area.
   */
  public JBBPOnlyFieldEvaluator(final String externalFieldName, final int namedFieldIndex) {
    this.externalFieldName = externalFieldName;
    this.namedFieldIndex = namedFieldIndex;
  }

  @Override
  public int eval(final JBBPBitInputStream inStream, final int currentCompiledBlockOffset,
                  final JBBPCompiledBlock block, final JBBPNamedNumericFieldMap fieldMap) {
    final int result;
    if (this.externalFieldName == null) {
      final JBBPNamedFieldInfo namedField = block.getNamedFields()[this.namedFieldIndex];
      final JBBPNumericField numericField = fieldMap.get(namedField);
      if (numericField == null) {
        throw new java.lang.ArithmeticException(
            "Can't find field '" + namedField.getFieldName() + "' among numeric fields");
      } else {
        result = numericField.getAsInt();
      }
    } else {
      result = this.externalFieldName.equals("$")
          ? (int) inStream.getCounter()
          : fieldMap.getExternalFieldValue(this.externalFieldName, block, this);
    }
    return result;
  }

  @Override
  public String toString() {
    return this.externalFieldName == null ? "NamedFieldIndex=" + this.namedFieldIndex :
        this.externalFieldName;
  }

  @Override
  public void visitItems(final JBBPCompiledBlock block, final int currentCompiledBlockOffset,
                         final ExpressionEvaluatorVisitor visitor) {
    visitor.visitStart();

    if (this.externalFieldName == null) {
      visitor.visitField(block.getNamedFields()[this.namedFieldIndex], null);
    } else {
      if (this.externalFieldName.equals("$")) {
        visitor.visitSpecial(ExpressionEvaluatorVisitor.Special.STREAM_COUNTER);
      } else {
        visitor.visitField(null, this.externalFieldName);
      }
    }

    visitor.visitEnd();
  }
}
