package com.igormaznitsa.jbbp.plugin.common.converters;

import static com.igormaznitsa.jbbp.utils.JBBPUtils.ARRAY_STRING_EMPTY;


import com.igormaznitsa.jbbp.JBBPParser;
import com.igormaznitsa.jbbp.compiler.conversion.JBBPToJavaConverter;
import com.igormaznitsa.jbbp.io.JBBPBitOrder;
import com.igormaznitsa.jbbp.plugin.common.utils.CommonUtils;
import com.igormaznitsa.meta.common.utils.Assertions;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class JavaConverter implements JBBPScriptTranslator {
  @Override
  @Nonnull
  public Set<File> translate(@Nonnull final Parameters parameters, final boolean dryRun)
      throws IOException {
    final String text;
    final String rawFileName;
    if (parameters.getScriptFile() == null) {
      rawFileName =
          parameters.getDestFileName() == null ? "JbbpNoName" : parameters.getDestFileName();
      text = Assertions
          .assertNotNull("Script file is null, expected script text", parameters.getScriptText());
    } else {
      final File scriptToProcess = parameters.getScriptFile();
      rawFileName = FilenameUtils.getBaseName(scriptToProcess.getName());
      text = FileUtils.readFileToString(scriptToProcess, parameters.getEncodingIn());
    }
    final String className = CommonUtils.extractClassName(rawFileName);
    final String packageName =
        parameters.getPackageName() == null ? CommonUtils.extractPackageName(rawFileName) :
            parameters.getPackageName();

    final Set<File> resultFiles = Collections.singleton(CommonUtils
        .scriptFileToJavaFile(parameters.getOutputDir(), parameters.getPackageName(),
            parameters.getScriptFile()));
    if (!dryRun) {

      final File resultJavaFile = resultFiles.iterator().next();

      final JBBPParser parser = JBBPParser
          .prepare(text, JBBPBitOrder.LSB0, parameters.customFieldTypeProcessor,
              parameters.getParserFlags());

      final String[] implementsSorted = parameters.getClassImplements().toArray(ARRAY_STRING_EMPTY);
      Arrays.sort(implementsSorted);

      final JBBPToJavaConverter.Builder builder = JBBPToJavaConverter.makeBuilder(parser)
          .setMapSubClassesInterfaces(parameters.getSubClassInterfaces())
          .setMapSubClassesSuperclasses(parameters.getSubClassSuperclasses())
          .setMainClassName(className)
          .setHeadComment(parameters.getHeadComment())
          .setMainClassPackage(packageName)
          .setMainClassCustomText(parameters.getCustomText())
          .setAddGettersSetters(parameters.isAddGettersSetters())
          .setDoMainClassAbstract(parameters.isDoAbstract())
          .setMainClassImplements(implementsSorted)
          .setParserFlags(parameters.getParserFlags())
          .setSuperClass(parameters.getSuperClass());

      if (parameters.isAddBinAnnotations()) {
        builder.addBinAnnotations();
      }

      if (parameters.isAddNewInstanceMethods()) {
        builder.addNewInstanceMethods();
      }

      if (parameters.isDoInternalClassesNonStatic()) {
        builder.doInternalClassesNonStatic();
      }

      if (parameters.isDisableGenerateFields()) {
        builder.disableGenerateFields();
      }

      FileUtils.write(resultJavaFile, builder.build().convert(), parameters.getEncodingOut());
    }
    return resultFiles;
  }
}


