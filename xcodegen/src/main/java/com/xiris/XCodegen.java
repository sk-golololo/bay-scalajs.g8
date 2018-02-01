package com.xiris;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.AbstractScalaCodegen;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;

public class XCodegen extends AbstractScalaCodegen implements CodegenConfig {

    private final static String SRC_MAIN_SCALA = "src/main/scala";
    private final static String apiVersion = "1.0.0";

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "XCodegen";
    }


    public String getHelp() {
        return "Generates a XCodegen client library.";
    }

    public XCodegen() {
        super();
        outputFolder = "generated-code/XCodegen";
        modelTemplateFiles.put("model.mustache", ".scala");
        apiTemplateFiles.put("api.mustache", ".scala");

        templateDir = "XCodegen";
        apiPackage = "controllers.swagger";
        modelPackage = "models.swagger";

        setReservedWordsLowerCase(
                Arrays.asList(
                        // local variable names used in API methods (endpoints)
                        "path", "contentTypes", "contentType", "queryParams", "headerParams",
                        "formParams", "postBody", "mp", "basePath", "apiInvoker",

                        // scala reserved words
                        "abstract", "case", "catch", "class", "def", "do", "else", "extends",
                        "false", "final", "finally", "for", "forSome", "if", "implicit",
                        "import", "lazy", "match", "new", "null", "object", "override", "package",
                        "private", "protected", "return", "sealed", "super", "this", "throw",
                        "trait", "try", "true", "type", "val", "var", "while", "with", "yield")
        );

        importMapping.remove("List");
        importMapping.remove("Set");
        importMapping.remove("Map");

        importMapping.put("ListBuffer", "scala.collection.mutable.ListBuffer");

        typeMapping = new HashMap<String, String>();
        typeMapping.put("Integer", "Int");
        typeMapping.put("enum", "NSString");
        typeMapping.put("array", "Seq");
        typeMapping.put("set", "Set");
        typeMapping.put("boolean", "Boolean");
        typeMapping.put("string", "String");
        typeMapping.put("int", "Int");
        typeMapping.put("long", "Long");
        typeMapping.put("float", "Float");
        typeMapping.put("byte", "Byte");
        typeMapping.put("short", "Short");
        typeMapping.put("char", "Char");
        typeMapping.put("double", "Double");
        typeMapping.put("object", "Any");
        typeMapping.put("file", "File");

        typeMapping.put("date", "LocalDate");
        typeMapping.put("DateTime", "LocalDateTime");


        this.importMapping.put("DateTime", "org.joda.time.*");
        this.importMapping.put("LocalDateTime", "org.joda.time.*");
        this.importMapping.put("LocalDate", "org.joda.time.*");
        this.importMapping.put("LocalTime", "org.joda.time.*");


        typeMapping.put("date", "LocalDate");
        typeMapping.put("LocalDate", "LocalDate");
        importMapping.put("date", "java.time.LocalDate");
        importMapping.put("LocalDate", "java.time.LocalDate");
        typeMapping.put("LocalDateTime", "LocalDateTime");
        importMapping.put("LocalDateTime", "java.time.LocalDateTime");
        typeMapping.put("DateTime", "LocalDateTime");
        importMapping.put("DateTime", "java.time.LocalDateTime");

        //TODO binary should be mapped to byte array
        // mapped to String as a workaround
        typeMapping.put("binary", "String");
        typeMapping.put("ByteArray", "String");

        instantiationTypes.put("array", "ListBuffer");
        instantiationTypes.put("map", "HashMap");

        additionalProperties.put("fnEnumEntry", new EnumEntryLambda());
    }

    @Override
    public void processOpts() {
        super.processOpts();
        //supportingFiles.add(new SupportingFile("dateTimeCodecs.mustache",  "/shared/" + SRC_MAIN_SCALA + "/" + modelPackage().replace('.', File.separatorChar), "DateTimeCodecs.scala"));
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/server/" + SRC_MAIN_SCALA + "/" + apiPackage().replace('.', File.separatorChar);
    }

    public String modelFileFolder() {
        return outputFolder + "/shared/" + SRC_MAIN_SCALA + "/" + modelPackage().replace('.', File.separatorChar);
    }

    private static abstract class CustomLambda implements Mustache.Lambda {
        @Override
        public void execute(Template.Fragment frag, Writer out) throws IOException {
            final StringWriter tempWriter = new StringWriter();
            frag.execute(tempWriter);
            out.write(formatFragment(tempWriter.toString()));
        }

        public abstract String formatFragment(String fragment);
    }

    private class EnumEntryLambda extends CustomLambda {
        @Override
        public String formatFragment(String fragment) {
            return formatIdentifier(fragment, true);
        }
    }

    @Override
    public String escapeQuotationMark(String input) {
        return input.replace("\"", "");
    }
}